package rdfUtils

import groovySparql.Sparql

/**
 * Created by john on 12/2/15.
 */

class Know {

    private Map<String, String> _prefixes = [:]
    private Sparql sparql
    private String select = '*'
    private String lang = 'en'

    public Know(String url){
        sparql = new Sparql(endpoint: url)
        addDefaultNamespaces()
        setLang('pt')
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

    def propertyMissing(String name) {
        getAt(name)
    }

    def getAt(String name) {
        findNode(name)
    }

    def findNode(String name) {
        //println name
        //println toURI(name)
        new Node(this, toURI(name))
    }

    def select(str){
        select = str
        this
    }

    def query(String q, String order = '', String lang = this.lang) {
        def f = "$prefixes \nselect $select where {$q} ${order}"
        select = '*'
        sparql.query(f, lang)
    }

    def insert(String q, String lang = this.lang){
        def f = "$prefixes \nINSERT DATA {$q}"
        sparql.update(f)
    }

    def delete(String q){
        def f = "$prefixes \n DELETE WHERE {$q}"
        sparql.update(f)
    }

    def update(String q){
        def f = "$prefixes \n $q"
        sparql.update(f)
    }

    def removeAll(String data){
        delete("?s ?p ?o")
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

    def existOntology(String uri){
        def existOnt = false
        def result = query("?o rdf:type owl:Ontology")

        result.each{
            if(it.o == uri)
                existOnt = true
        }

        existOnt
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

    def setLang(String lg){
        lang = lg
    }

}
