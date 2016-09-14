/*
 * Copyright (c) 2015-2016 Dilvan Moreira.
 * Copyright (c) 2015-2016 John Garavito.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package semantics

import groovySparql.SparqlBase

import org.apache.jena.update.UpdateExecutionFactory
import org.apache.jena.update.UpdateFactory
import org.apache.jena.update.UpdateProcessor
import org.apache.jena.update.UpdateRequest
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.sparql.modify.request.UpdateLoad

/**
 * Class RDFSlurper
 *
 * @author Dilvan Moreira.
 * @author John Garavito.
 */

/*
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

    String lang = 'en'
    //Logger log = Logger.getLogger(RDFSlurper.class);

    private Map<String, String> _prefixes = [:]
    private SparqlBase sparql
    private String select = '*'

    RDFSlurper(){
    }

    RDFSlurper(String endpoint, String update) {
        //"http://localhost:8000/sparql/", "http://localhost:8000/update/")
        // SPARQL 1.0 or 1.1 endpoint
        sparql = new SparqlBase(endpoint: endpoint)
    }

    RDFSlurper(String url){
        //String url = 'http://bio.icmc.usp.br:9999/bigdata/namespace/sustenagro/sparql'
        //"http://localhost:8000/sparql/", "http://localhost:8000/update/")

        // SPARQL 1.0 or 1.1 endpoint
        sparql = new SparqlBase(endpoint: url)

        addDefaultNamespaces()

        setLang('pt')
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

    /*
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
    */


    def loadRDF(InputStream is){
        Model m = ModelFactory.createDefaultModel()

        m.read(is, "http://bio.icmc.usp.br/sustenagro#")
        UpdateRequest request = UpdateFactory.create()
        request.add(new UpdateLoad("http://java.icmc.usp.br/sustenagro/SustenAgroOntology.rdf", "http://bio.icmc.usp.br/sustenagro#"))
        UpdateProcessor processor = UpdateExecutionFactory.createRemoteForm(request, 'http://java.icmc.usp.br:9999/bigdata/namespace/kb/sparql')
        processor.execute()
    }

    def addDefaultNamespaces() {
        addNamespace('rdf','http://www.w3.org/1999/02/22-rdf-syntax-ns#')
        addNamespace('rdfs','http://www.w3.org/2000/01/rdf-schema#')
        addNamespace('owl','http://www.w3.org/2002/07/owl#')
        addNamespace('xsd','http://www.w3.org/2001/XMLSchema#')
        addNamespace('foaf','http://xmlns.com/foaf/0.1/')
        addNamespace('dc','http://purl.org/dc/terms/')
        addNamespace('dbp','http://dbpedia.org/ontology/')
        addNamespace('','http://bio.icmc.usp.br/sustenagro#')
    }

    def addNamespace(String prefix, String namespace){
        _prefixes.put(prefix, namespace)
        // Method not working with SparqlRepositorySailGraph
        //g.addNamespace(prefix, namespace)
    }

    def toURI(String uri){
        if (uri==null || uri == '' ) return null
        if (uri.contains(' ')) return null
        if (uri.startsWith('_:')) return uri
        if (uri.startsWith(':')) return _prefixes['']+uri.substring(1)
        if (uri.startsWith('http:')) return uri
        if (uri.startsWith('urn:')) return uri
        //println '!uri.contains(:) '+uri + ' : '
        if (!uri.contains(':')) return appendPrefixe(uri)

        // slp.query("?id rdfs:label ?label. FILTER (STR(?label)='$cls')", '', '')

        println 'prexixes analyse'
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

    def appendPrefixe(String name){
        def result = null
        def query

        _prefixes.each {alias, uri ->
            //println uri+name
            query = this.query("<"+uri+name+"> a ?class")
            //println query
            if(query.size()>0){
                result = uri+name
            }
        }
        return result
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

    /*def propertyMissing(String name) {
        getAt(name)
    }
    */

    def v(String name){
        g.v(toURI(name))
    }

    /*
    def getAt(String name) {
        def node = g.v(toURI(name))

        //println "uri:"+toURI(name)

        if (node.both.count()==0) {
            g.removeVertex(node)
            node = findVertex(name)
        }
        new GNode(this, node._())
    }
    */

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
            case int: case Integer: case long: case Long: case BigInteger:
                return g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#integer>')
            case float: case Float: case double: case Double: case BigDecimal:
                return g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#double>')
            case boolean:
                return g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#boolean>')
        }
    }

    def dataSchema(value){
        def result = null
        switch (value) {
            case String:
                result = '"' + node + '"@' + lang
                break
            case int: case Integer: case long: case Long: case BigInteger:
                result = '"' + node + '"^^<http://www.w3.org/2001/XMLSchema#integer>'
                break
            case float: case Float: case double: case Double: case BigDecimal:
                result = '"' + node + '"^^<http://www.w3.org/2001/XMLSchema#double>'
                break
            case boolean:
                result = '"' + node + '"^^<http://www.w3.org/2001/XMLSchema#boolean>'
                break
        }
        return result
    }
}
