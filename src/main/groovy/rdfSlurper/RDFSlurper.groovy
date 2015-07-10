package rdfSlurper

import com.tinkerpop.blueprints.impls.sail.SailGraph
import com.tinkerpop.blueprints.impls.sail.SailTokens
import com.tinkerpop.blueprints.impls.sail.impls.MemoryStoreSailGraph
import com.tinkerpop.blueprints.impls.sail.impls.SparqlRepositorySailGraph
import com.tinkerpop.gremlin.groovy.Gremlin

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

    //def getAt(int ind) {
    //    int i =0
    //    node().value.find{i++ == ind}
    //}

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
        slurp.g.addEdge(subj, make(obj2), slurp.toURI(propName))
    }

    def make(node) {

        def addProps = { uri, map ->
            def v2 = slurp.g.addVertex(slurp.toURI(uri))
            map.each {key, value ->
                slurp.g.addEdge(v2, make(value), slurp.toURI(key))
            }
            v2
        }
        switch (node) {

            case List:
                return addProps(node[0], node[1])
            case Map:
                return addProps(
                        //'_:Z' + ((node.size() * 100000000 + 7652526535345544) * Math.random()).toLong(),
                        //'http://blankNode.org/blank/B' + ((node.size() * 100000000 + 7652526535345544) * Math.random()).toLong(),
                        null,
                        node)
            case String:
                return slurp.g.addVertex('"' + node + '"@' + slurp.lang)
            case int|Integer|long|Long|BigInteger:
                return slurp.g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#integer>')
            case float|Float|double|Double|BigDecimal:
                return slurp.g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#double>')
            case boolean:
                return slurp.g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#boolean>')
        }
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
        slurp.g.addEdge(subj, make(obj2), propName)
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
    private Map<String, String> _prefixes = [:]
    String lang = 'en'

    RDFSlurper(){
        g = new MemoryStoreSailGraph()
    }

    RDFSlurper(String endpoint, String update) {
        g = new SparqlRepositorySailGraph(endpoint, update)
        //"http://localhost:8000/sparql/", "http://localhost:8000/update/")
    }

    RDFSlurper(String file){
        g = new MemoryStoreSailGraph()

        //addDefaultNamespaces()
        addNamespace('sa','http://biomac.icmc.usp.br/sustenagro#')
        addNamespace('dbp','http://dbpedia.org/ontology/')

        g.loadRDF(new FileInputStream(file), 'http://biomac.icmc.usp.br/sustenagro#', 'rdf-xml', null)
    }

    def sparql(String q) {
        println prefixes + '\n' + q
        g.executeSparql(prefixes + '\n' + q)
    }

    def addDefaultNamespaces() { //throw new RuntimeException('Method not working.')}

        addNamespace(SailTokens.RDF_PREFIX, SailTokens.RDF_NS);
        addNamespace(SailTokens.RDFS_PREFIX, SailTokens.RDFS_NS);
        addNamespace(SailTokens.OWL_PREFIX, SailTokens.OWL_NS);
        addNamespace(SailTokens.XSD_PREFIX, SailTokens.XSD_NS);
        addNamespace(SailTokens.FOAF_PREFIX, SailTokens.FOAF_NS);
        addNamespace('dc','http://purl.org/dc/terms#')
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
}

