package sustenagro

/**
 * Created by dilvan on 7/2/15.
 */

import com.tinkerpop.blueprints.impls.sail.impls.MemoryStoreSailGraph
import com.tinkerpop.blueprints.impls.sail.impls.SparqlRepositorySailGraph

import com.tinkerpop.gremlin.groovy.Gremlin

import static SlurpRDF.*

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
    MemoryStoreSailGraph g
    def pipe

    GGraph(MemoryStoreSailGraph g, pipe) {
        this.g = g
        this.pipe = pipe
    }

    def propertyMissing(String name) {
        getAt(name)
    }

    abstract def getVertex()

    def getValue(){
        vertex.value
    }

    def findVertex(String name){
        def id = vertex.id.find{true}
        def q = "SELECT ?obj WHERE {<$id> ${toURI(name)} ?obj.}"

        def res = g.executeSparql(q)
        if (!res.empty) return g.v(id).outE(toURI(name))

        q = "SELECT ?pred WHERE {<$id> ?pred ?z. ?pred rdfs:label \"$name\"@$SlurpRDF.lang}"

        res = g.executeSparql(q)
        if (res.empty)
            throw new RuntimeException("Unknown field: $name.")
        g.v(id).outE(res[0].pred.id)
    }

    def getAt(String name) {
        if (name[0]=='$') {
            def vertice = findVertex(toURI(name.substring(1)))
            def lst = []
            vertice.inV.fill(lst)
            def ret= lst.find{
                it.value.class != String || it.lang == SlurpRDF.lang
            }
            if (ret==null) throw new RuntimeException("No value for $name for language $SlurpRDF.lang")
            return ret.value
        }
        def edge1 = findVertex(name)
        new GEdge(g, edge1._())
    }

    abstract def getTriple(String propName)

    def putAt(String propName, obj2) {
        def (subj, pred, obj1) = getTriple(propName)

        g.removeEdge(pred)
        if (obj1.outE().count()==0)
            g.removeVertex(obj1)
        g.addEdge(subj, make(obj2), propName)
    }

    def make(node) {

        def addProps = { uri, map ->
            def v2 = g.addVertex(uri)
            map.each {key, value ->
                g.addEdge(v2, make(value), key)
            }
            v2
        }
        switch (node) {
            case List:
                return addProps(node[0], node[1])
            case Map:
                return addProps(
                        '_:Z' + ((node.size() * 100000000 + 7652526535345544) * Math.random()).toLong(),
                        node)
            case String:
                return g.addVertex('"' + node + '"@' + SlurpRDF.lang)
            case int|Integer|long|Long|BigInteger:
                return g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#integer>')
            case float|Float|double|Double|BigDecimal:
                return g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#double>')
            case boolean:
                return g.addVertex('"' + node + '"^^<http://www.w3.org/2001/XMLSchema#boolean>')
        }
    }

    static toURI(String uri){
        (uri.contains(':')) ? uri : ':'+uri
    }
}

class GNode extends GGraph {

    def getVertex() {pipe}

    GNode(MemoryStoreSailGraph g, ver){
        super(g, ver)
    }

    def getTriple(String propName){
        def subj = vertex.find{true}
        def pred = subj.outE(toURI(prop)).find{true}
        def obj = pred.outV.find{true}
        [subj, pred, obj]
    }
}

class GEdge extends GGraph {

    def getEdge() {pipe}

    GEdge(MemoryStoreSailGraph g, edge){
        super(g, edge)
    }

    def getVertex() {
        edge.inV
    }

    def getTriple(String propName){
        def subj = edge.inV.find{true}
        def pred = subj.outE(toURI(propName)).find{true}
        def obj = pred.inV.find{true}
        [subj, pred, obj]
    }

    def leftShift(obj2) {
        def pred = edge.find{true}
        def subj = pred.outV.find{true}
        def obj1 = pred.inV.find{true}

        def propName = pred.label

        g.removeEdge(pred)
        if (obj1.outE().count()==0)
            g.removeVertex(obj1)
        g.addEdge(subj, make(obj2), propName)
    }
}

class SlurpRDF {
    MemoryStoreSailGraph g
    static String lang = 'en'

    SlurpRDF(MemoryStoreSailGraph g){
        this.g = g
    }

    def findVertex(String name){

        def q = "SELECT ?subj WHERE {?subj rdfs:label \"$name\"@$SlurpRDF.lang}"

        def res = g.executeSparql(q)
        if (res.empty)
            throw new RuntimeException("Unknown Element: $name.")
        //println res[0].subj.id
        g.v(res[0].subj.id)
    }

    def propertyMissing(String name) {
        def node = g.v(GNode.toURI(name))

        if (node.both.count()==0) {
            g.removeVertex(node)
            node = findVertex(name)
        }
        new GNode(g, node._())
    }

    static N(Map node) {node}
    static N(String uri, Map node) {
        [uri, node]
    }
}

/**
 * Created by dilvan on 5/29/15.
 */
//@CompileStatic
class GraphTest {
    static {
        Gremlin.load()
    }

    def mod1(){
        def g = new MemoryStoreSailGraph()

        g.addNamespace('', 'http://tinkerpop.com#')

        g.addDefaultNamespaces()

        //g.addNamespace('xsd', 'http://www.w3.org/2001/XMLSchema#')
        //g.addNamespace('foaf','http://xmlns.com/foaf/0.1/')
        //g.addNamespace('owl','http://www.w3.org/2002/07/owl#')


        g.loadRDF(new FileInputStream(
                //'/Applications/Bigdata/BIGDATA_RELEASE_1_5_0/ant-build/gremlin-groovy-2.5.0/data/graph-example-1.ntriple'),
                'lixo.ntriple'),
                'http://tinkerpop.com#', 'n-triples', null)
        def results = []

        def s = new SlurpRDF(g)

        println 'a= '

        println s.Tinker22.$age //.ver.each{println it}

        println s.'1'.knows.$name //.ver.each{println it}
        println s.'1'.Knows.$age //.ver.each{println it}

        println 'b= '
        //g.v(':1').out(':knows').out(':name').value.each{println it}
        s.'1'.knows.name.value.each{println it}
//        s.'1'.knows['name'] = 'Lolu'//.set('name', 'Kola')
        //s.'1'.knows['name'] = 'Kola'
        println 'b1= '
        s.'1'.knows.name.value.each{println it}

        //s.'1'.knows[':name'] = 'Kolajj'
        s.'1'.knows.name << 'Kolajj'

        println 'result: '

        s.':1'.Knows.name.value.each{println it}

        s.'1'.knows[':name'] =
                N(':kjkjkk',
                        [':name': 'kjljlk',
                         ':bhhbhhb': N(':name':"klklk kkkk")])

        s.'1'.knows.name.value.each{println it}

//        g =loll( ui: lakll( k: 'lkj',
//                            lk: 'ghf',
//                            gh:  B( mk: 'loiiu',
//                                    lk: 'lkkk'),
//                            loi: 'lkjjh'),
//                 yuy: B( k:'lkj',
//                         lk: 'ghf',
//                         mk: 'loiiu',
//                         loi: 'lkjjh'))
//
//        s.with {
//            N('loll',
//                    ui: N('lokll',
//                            k: 'lkj',
//                            lk: 'ghf',
//                            gh: N(mk: 'loiiu',
//                                    lk: 'lkkk'),
//                            loi: 'lkjjh'),
//                    yuy: N(k: 'lkj',
//                            lk: 'ghf',
//                            mk: 'loiiu',
//                            loi: 'lkjjh'))
//        }
//
//        s.node(
//           [ _id: 'loll',
//              ui: [ uri: 'lokll',
//                    k: 'lkj',
//                    lk: 'ghf',
//                    gh: [ mk: 'loiiu',
//                          lk: 'lkkk'],
//                    loi: 'lkjjh'],
//              yuy: [ k:'lkj',
//                     lk: 'ghf',
//                     mk: 'loiiu',
//                     loi: 'lkjjh']])
//
//        s.node(
//          [ 'loll', [
//              ui: [ 'lokll', [
//                    k: 'lkj',
//                    lk: 'ghf',
//                    gh: [ mk: 'loiiu',
//                          lk: 'lkkk'],
//                    loi: 'lkjjh']],
//              yuy: [ k:'lkj',
//                     lk: 'ghf',
//                     mk: 'loiiu',
//                     loi: 'lkjjh']]])
//

        g.saveRDF(new FileOutputStream('lixo2.ntriple'), 'n-triples')

        return results
    }

    def mod3(){
        def g = new SparqlRepositorySailGraph("http://localhost:8000/sparql/", "http://localhost:8000/update/")



        g.addNamespace('tg','http://tinkerpop.com#')
        g.addNamespace('tst', 'http://kjhkjh.com/')

        println(' 1 -> ')
        g.v('http://tinkerpop.com#1').outE.id.each{println it}

        def v = g.addVertex('"lixoLIXO"^^<http://www.w3.org/2001/XMLSchema#string>');
        g.addEdge(g.v('http://tinkerpop.com#1'), v, 'http://kjhkjh.com/newG')

        println(' 1 -> ')
        g.v('http://tinkerpop.com#1').outE.id.each{println it}

//        g.v('http://tinkerpop.com#1').out.map.each{println it}

        println(' 2 -> ')
        g.v('http://tinkerpop.com#1').out('http://kjhkjh.com/jhgjh').kind.each{println it}
        g.v('http://tinkerpop.com#1').out('http://kjhkjh.com/jhgjh').id.each{println it}


        println 'done'
    }

    def mod2(){
        def g = new SparqlRepositorySailGraph("http://dbpedia.org/sparql")
        def v = g.v('http://dbpedia.org/resource/Grateful_Dead')
        v.outE.label.dedup.each{println it}

    }

//    def met() {
//        def g = new BigdataGraphClient("http://localhost:9999/bigdata")
//        //g = new MemoryStoreSailGraph()
//        g.loadGraphML("/Applications/Bigdata/BIGDATA_RELEASE_1_5_0/ant-build/gremlin-groovy-2.5.0/data/graph-example-1.xml")
//        //Graph g = TinkerGraphFactory.createTinkerGraph()
//        //g.addVertex(label:'software',name:'blueprints')
//        //g.addNamespace('tg','http://tinkerpop.com#')
//        //g.loadRDF(new FileInputStream('/Applications/Bigdata/BIGDATA_RELEASE_1_5_0/ant-build/gremlin-groovy-2.5.0/data/graph-example-1.ntriple'), 'http://tinkerpop.com#', 'n-triples', null)
//
//        def results = []
//        //g.V.fill(results)
//        //g.E.fill(results)
//        def v = g.v(1)
//        v.outE.filter{it.weight<1.0f}.inV.fill(results)
//
//        return results
//    }

//    def exampleMethod() {
//            Graph g = TinkerGraphFactory.createTinkerGraph()
//        def results = []
//        //g.v(1).out('knows').fill(results)
//        //g.E.fill(results)
//        def v = g.v(1)
//        v.outE('knows').filter{it.weight<1.0f}.inV.fill(results)
//        return results
//    }

    static void main(String... args){
        def t = new GraphTest()
        t.mod1()
    }
}
