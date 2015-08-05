package rdfSlurper

import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.QuerySolution
import com.hp.hpl.jena.query.Syntax
import com.hp.hpl.jena.rdf.model.RDFNode
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.sail.SailGraph
import com.tinkerpop.blueprints.impls.sail.SailTokens
import com.tinkerpop.blueprints.impls.sail.impls.MemoryStoreSailGraph
import com.tinkerpop.blueprints.impls.sail.impls.SparqlRepositorySailGraph
import com.tinkerpop.gremlin.groovy.Gremlin

import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.query.QueryExecution
import groovy.sparql.Sparql

import com.hp.hpl.jena.query.QuerySolutionMap
import com.hp.hpl.jena.query.ResultSet

/*
new MarkupBuilder().root {
   a( a1:'one' ) {
     b { mkp.yield( '3 < 5' ) }
     c( a2:'two', 'blah' )
   }
 }
Will print the following to System.out:
<root>
   <a a1='one'>
     <b>3 &lt; 5</b>
     <c a2='two'>blah</c>
   </a>
 </root>
 */

class Sparql2 extends Sparql {

    def query(String sparql, String lang) {
        Query query = QueryFactory.create(sparql, Syntax.syntaxARQ)
        QueryExecution qe = null

        /**
         * Some explanation here - ARQ can provide a QE based on a pure
         * SPARQL service endpoint, or a Jena model, plus you can still
         * do remote queries with the model using the in-SPARQL "service"
         * keyword.
         */
        if (model) {
            qe = QueryExecutionFactory.create(query, model)
        } else {
            if (!endpoint) {
                return
            }
            qe = QueryExecutionFactory.sparqlService(endpoint, query)
            if (config.timeout) {
                ((QueryEngineHTTP) qe).addParam(timeoutParam, config.timeout as String)
            }
            if (user) {
                ((QueryEngineHTTP) qe).setBasicAuthentication(user, pass?.toCharArray())
            }
        }

        def res = []
        try {

            for (ResultSet rs = qe.execSelect(); rs.hasNext();) {
                QuerySolution sol = rs.nextSolution()

                Map<String, Object> row = [:]
                boolean add = true
                for (Iterator<String> varNames = sol.varNames(); varNames.hasNext();) {
                    String varName = varNames.next()
                    RDFNode varNode = sol.get(varName)
                    row.put(varName, (varNode.isLiteral() ? varNode.asLiteral().value : varNode.toString()))
                    if (lang!='' &&
                            varNode.isLiteral() &&
                            varNode.asLiteral().language!=null &&
                            varNode.asLiteral().language.size()>1 &&
                            varNode.asLiteral().language!=lang) add= false
                }
                //println 'row: '+row
                if (add)
                    res.push(row)
                //closure.delegate = row
                //closure.call()
            }
        } finally {
            qe.close()
        }
        return res
    }
}

abstract class GGraph {
    RDFSlurper slurp
    def pipe

    GGraph(RDFSlurper slurp, pipe) {
        this.slurp = slurp
        this.pipe = pipe
    }

    def propertyMissing(String name) {
        getAt(name)
    }

    abstract def node()

    def collect(){
        node().value
    }

    def toList(){
        def lst = []
        node().value.fill(lst)
        lst
    }

    def findVertex(String name){
        def id = node().id.find{true}

        def q = slurp.prefixes +//"${g.namespaces['']}> \n" +
                "SELECT ?obj WHERE {<$id> <${slurp.toURI(name)}> ?obj.}"

        //println q

        def res = slurp.g.executeSparql(q)
        //println res
        if (!res.empty) return slurp.g.v(id).outE(slurp.toURI(name))

        q = slurp.prefixes +//<${g.namespaces['']}> \n" +
            "SELECT ?pred WHERE {<$id> ?pred ?z. ?pred rdfs:label \"$name\"@$slurp.lang}"

        res = slurp.g.executeSparql(q)
        if (res.empty)
            throw new RuntimeException("Unknown field: $name.")
        slurp.g.v(id).outE(res[0].pred.id)
    }

    def getAt(String name) {
        if (name[0]=='$') {
            def vertice = findVertex(slurp.toURI(name.substring(1)))
            def lst = []
            vertice.inV.fill(lst)
            def ret= lst.find{
                it.value.class != String || it.lang == slurp.lang
            }

            if (ret==null) throw new RuntimeException("No value for $name for language $slurp.lang")
            return ret.value
        }
        def edge1 = findVertex(name)
        new GEdge(slurp, edge1._())
    }

    abstract def getTriple(String propName)

    def putAt(String propName, obj2) {
        def (subj, pred, obj1) = getTriple(propName)

        if (pred!=null) {
            slurp.g.removeEdge(pred)
            if (obj1.outE().count() == 0)
                slurp.g.removeVertex(obj1)
        }
        slurp.g.addEdge(subj, slurp.addNode(obj2), slurp.toURI(propName))
    }
}

class GNode extends GGraph {

    def node() {pipe}

    GNode(RDFSlurper slurp, ver){
        super(slurp, ver)
    }

    def getTriple(String propName){
        def subj = node().find{true}
        def pred = subj.outE(slurp.toURI(propName)).find{true}
        def obj = pred?.outV.find{true}
        [subj, pred, obj]
    }
}

class GEdge extends GGraph {

    def getEdge() {pipe}

    GEdge(RDFSlurper slurp, edge){
        super(slurp, edge)
    }

    def node() {
        edge.inV
    }

    def getTriple(String propName){
        def subj = edge.inV.find{true}
        def pred = subj.outE(slurp.toURI(propName)).find{true}
        def obj = pred?.inV.find{true}
        [subj, pred, obj]
    }

    def leftShift(obj2) {
        def pred = edge.find{true}
        def subj = pred.outV.find{true}
        def obj1 = pred.inV.find{true}

        def propName = pred.label
        //println 'prop: '+propName

        slurp.g.removeEdge(pred)
        if (obj1.outE().count()==0)
            slurp.g.removeVertex(obj1)
        slurp.g.addEdge(subj, slurp.addNode(obj2), propName)
    }
}

/**
 * Class RDFSlurper
 *
 * From: https://github.com/tinkerpop/blueprints/wiki/Sail-Implementation
 *
 * Each vertex is represented by an id: the string representation of the RDF
 * value being represented. For instance:
 *
 *  URI:  http://markorodriguez.com
 *  Blank Node:  _:A12345
 *  Literal:  "hello"^^<http://www.w3.org/2001/XMLSchema#string>
 *
 * They also have a kind property (kind) that is either uri, literal, or bnode.
 *
 * Only literal-based vertices have three other vertex properties.
 * Only two of which can be set.
 * These properties are the language (lang), the datatype (type), and the
 * typecasted value (value) of the literal label.
 * Note that in RDF, a literal can have either a language, a datatype, or neither.
 * Never can a literal have both a language and a datatype.
 * The typecasted value is the object created by the casting of the label of the
 * literal by its datatype.
 * The value of "6"^^<http://www.w3.org/2001/XMLSchema#int> is the integer 6.
 *
 * An edge represents an RDF statement. An edge id the string representation of
 * the RDF statement. For instance:
 *
 * Statement
 * (http://tinkerpop.com#marko, http://someontology.com#age, "30"^^<http://www.w3.org/2001/XMLSchema#int>)
 * (http://tinkerpop.com#marko, http://www.w3.org/2002/07/owl#sameAs, http://markorodrigue.com#marko) [http://tinkerpop.com#graph]
 *
 * Statements can only have a single property: the named graph property (ng).
 *
 */
//@CompileStatic
class RDFSlurper {

    static {
        Gremlin.load()
    }

    SailGraph g
    String lang = 'en'

    private Map<String, String> _prefixes = [:]
    private Sparql2 sparql2
    private String select = '*'

    RDFSlurper(){
        g = new MemoryStoreSailGraph()
    }

    RDFSlurper(String endpoint, String update) {
        g = new SparqlRepositorySailGraph(endpoint, update)
        //"http://localhost:8000/sparql/", "http://localhost:8000/update/")
        // SPARQL 1.0 or 1.1 endpoint
        sparql2 = new Sparql2(endpoint: endpoint)
    }

    RDFSlurper(String file){
        //g = new MemoryStoreSailGraph()
        String url = 'http://localhost:9999/bigdata/sparql'
        //String url = 'http://bio.icmc.usp.br:9999/bigdata/namespace/sustenagro/sparql'
        //"http://localhost:8000/sparql/", "http://localhost:8000/update/")

        g = new SparqlRepositorySailGraph(url, url)

        addDefaultNamespaces()
        addNamespace('','http://bio.icmc.usp.br/sustenagro#')
        addNamespace('dbp','http://dbpedia.org/ontology/')

        setLang('pt')

        // SPARQL 1.0 or 1.1 endpoint
        sparql2 = new Sparql2(endpoint: url)

        //removeAll()
        //g.loadRDF(new FileInputStream(file), 'http://biomac.icmc.usp.br/sustenagro#', 'rdf-xml', null)
    }

    def removeAll(){
         g.E.each{
            g.removeEdge(it)
        }
    }

//    def sparql(String q) {
//        def ret = []
//        def f = prefixes + '\n' + q
//        println f
//        g.executeSparql(f).each{
//            def map = [:]
//            def add = true
//            it.each {key, val->
//                map[key] = val.value ? val.value : val.id
//                if (val.lang != null && val.lang!=lang) add= false
//            }
//            if (add) ret.add(map)
//        }
//        ret
//    }


    def query1(String q) {
        def ret = []
        def f = prefixes + '\n select * where {' + q +'}'
        g.executeSparql(f).each{
            def map = [:]
            def add = true
            it.each {key, val->
                map[key] = val.value ? val.value : val.id
                if (val.lang != null && val.lang!=lang) add= false
            }
            if (add) ret.add(map)
        }
        ret
    }

    def select(str){
        select = str
        this
    }

    def query(String q, String lang = this.lang) {
        def f = "$prefixes \n select $select where {$q}"
        select = '*'
        sparql2.query(f, lang)
    }

    def addDefaultNamespaces() {
        addNamespace(SailTokens.RDF_PREFIX, SailTokens.RDF_NS);
        addNamespace(SailTokens.RDFS_PREFIX, SailTokens.RDFS_NS);
        addNamespace(SailTokens.OWL_PREFIX, SailTokens.OWL_NS);
        addNamespace(SailTokens.XSD_PREFIX, SailTokens.XSD_NS);
        addNamespace(SailTokens.FOAF_PREFIX, SailTokens.FOAF_NS);
        addNamespace('dc','http://purl.org/dc/terms/')
    }

    def addNamespace(String prefix, String namespace){
        _prefixes.put(prefix, namespace)
        // Method not working with SparqlRepositorySailGraph
        //g.addNamespace(prefix, namespace)
    }

    def toURI(String uri){
        if (uri==null) return null
        if (uri.startsWith('_:')) return uri
        if (uri[0]==':') return _prefixes['']+uri.substring(1)
        if (uri.startsWith('http:')) return uri
        if (uri.startsWith('urn:')) return uri
        if (!uri.contains(':')) return _prefixes['']+ uri
        def pre = _prefixes[uri.tokenize(':')[0]]
        if (pre==null) return uri
        return pre+uri.substring(uri.indexOf(':')+1)
    }

    def fromURI(String uri){
        if (uri==null) return null
        if (uri.startsWith('_:')) return uri
        if (!uri.startsWith('http:')) return uri
        def v = _prefixes.find { key, obj ->
            uri.startsWith(obj)
        }
        v.key + ':' + uri.substring(v.value.size())
    }

    def getPrefixes(){
        def str = ''
        _prefixes.each {key, obj -> str += "PREFIX $key: <$obj>\n"}
        str
    }

    def findVertex(String name){

        def q = prefixes + "SELECT ?subj WHERE {?subj rdfs:label \"$name\"@$lang}"

        //println q
        def res = g.executeSparql(q)
        if (res.empty)
            throw new RuntimeException("Unknown Element: $name.")
        //println res[0].subj.id
        g.v(res[0].subj.id)
    }

    def propertyMissing(String name) {
        getAt(name)
    }

    def v(String name){
        g.v(toURI(name))
    }

    def getAt(String name) {
        def node = g.v(toURI(name))

        //println "uri:"+toURI(name)

        if (node.both.count()==0) {
            g.removeVertex(node)
            node = findVertex(name)
        }
        new GNode(this, node._())
    }

    static N(Map node) {node}
    static N(Map node, String uri) {
        [uri, node]
    }

    def addNode(node) {

        def addProps = { uri, map ->
            def v2 = g.addVertex(toURI(uri))
            map.each {key, value ->
                g.addEdge(v2, addNode(value), toURI(key))
            }
            v2
        }
        switch (node) {
            case Vertex:
                return node
            case List:
                return addProps(node[0], node[1])
            case Map:
                return addProps(
                        //'_:Z' + ((node.size() * 100000000 + 7652526535345544) * Math.random()).toLong(),
                        //'http://blankNode.org/blank/B' + ((node.size() * 100000000 + 7652526535345544) * Math.random()).toLong(),
                        null,
                        node)
            case String:
                return g.addVertex('"' + node + '"@' + lang)
            case int|Integer|long|Long|BigInteger:
                return g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#integer>')
            case float|Float|double|Double|BigDecimal:
                return g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#double>')
            case boolean:
                return g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#boolean>')
        }
    }
}

class DataReader {

    RDFSlurper slp
    String uri

    def findNode(String name){

        def q = slp.query("<$uri> rdfs:label '$name'; :value ?v. ")

        //println res[0].subj.id
        g.v(res[0].subj.id)
    }

    def propertyMissing(String name) {
        getAt(name)
    }

    def getAt(String name) {
        def node = g.v(toURI(name))

        //println "uri:"+toURI(name)

        if (node.both.count()==0) {
            g.removeVertex(node)
            node = findVertex(name)
        }
        new GNode(this, node._())
    }
}

