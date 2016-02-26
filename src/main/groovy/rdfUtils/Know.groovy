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

    def addDefaultNamespaces(){
        addNamespace('rdf','http://www.w3.org/1999/02/22-rdf-syntax-ns#')
        addNamespace('rdfs','http://www.w3.org/2000/01/rdf-schema#')
        addNamespace('owl','http://www.w3.org/2002/07/owl#')
        addNamespace('xsd','http://www.w3.org/2001/XMLSchema#')
        addNamespace('foaf','http://xmlns.com/foaf/0.1/')
        addNamespace('dc','http://purl.org/dc/terms/')
        addNamespace('dbp','http://dbpedia.org/ontology/')
        addNamespace('dbpr','http://dbpedia.org/resource/')
        addNamespace('','http://semantic.icmc.usp.br/sustenagro#')
    }

    def addNamespace(String prefix, String namespace){
        _prefixes.put(prefix, namespace)
    }

    def propertyMissing(String name){
        getAt(name)
    }

    def getAt(String name){
        findNode(name)
    }

    def findNode(String name){
        new Node(this, toURI(name))
    }

    def select(str){
        select = str
        this
    }

    def query(String q, String order = '', String lang = this.lang){
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

    def toURI(String id){
        if (id==null || id == '' ) return null
        if (id.contains(' ')) return null
        if (id.startsWith('_:')) return id
        if (id.startsWith(':')) return _prefixes['']+id.substring(1)
        if (id.startsWith('http:')) return id
        if (id.startsWith('urn:')) return id
        //println '!id.contains(:) '+id + ' : '
        if (!id.contains(':')) return searchPrefix(id).uri+id

        // slp.query("?id rdfs:label ?label. FILTER (STR(?label)='$cls')", '', '')

        println 'prexixes analyse'
        def pre = _prefixes[id.split(':')[0]]
        if (pre==null) return id
        return pre+id.substring(id.indexOf(':')+1)
    }

    def fromURI(String uidri){
        if (id==null) return null
        if (id.startsWith('_:')) return id
        if (!id.startsWith('http:')) return id
        def v = _prefixes.find { key, obj ->
            id.startsWith(obj)
        }
        v.key + ':' + id.substring(v.value.size())
    }

    def shortURI(String id){
        if (id==null || id == '' ) return null
        if (id.contains(' ')) return null
        if (id.startsWith('_:')) return id.substring(2)
        if (id.startsWith(':')) return '-'+id.substring(1)
        if (!id.contains(':')) return searchPrefix(id).alias+'-'+id

    }

    def shortToURI(String id){
        if (id==null || id == '' ) return null
        if (id.contains(' ')) return null
        if (id.contains('-')) return _prefixes[id.split('-')[0]]+id.substring(1)
        if (!id.contains('-'))  return '_:'+id
    }

    def existOntology(String uri){
        def existOnt = false
        def result = query("?o rdf:type owl:Ontology")

        println "******** Result ********"
        println result

        result.each{
            if(it.o == uri)
                existOnt = true
        }

        existOnt
    }

    def searchPrefix(String name){
        def query
        def result = []
        _prefixes.find {alias, uri ->
            query = this.query("<"+uri+name+"> a ?class")
            if(query.size()>0){
                result = ['alias' : alias, 'uri': uri]
                return true
            }
            return false
        }
        return result
    }

    def getPrefixes(){
        def str = ''
        _prefixes.each {key, obj -> str += "PREFIX $key: <$obj>\n"}
        str
    }

    def getPrefixesMap(){
        return _prefixes
    }

    def getBasePrefix(){
        return _prefixes['']
    }

    def setLang(String lg){
        lang = lg
    }
}
