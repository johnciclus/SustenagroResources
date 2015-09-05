package dsl

import com.github.rjeschke.txtmark.Processor
import org.codehaus.groovy.control.CompilerConfiguration
import org.pegdown.PegDownProcessor
import rdfSlurper.RDFSlurper
import com.github.slugify.Slugify

/**
 * Created by dilvan on 7/14/15.
 */
class DSL {
    def featureLst = []
    def dimensions = []
    def report = []
    Closure program
    def data
    def props = [:]

    def slg = new Slugify()

    def toolIndexStack = []

    private def _nameFile = ''
    private Script _script
    private GroovyShell _shell
    private static md = new PegDownProcessor()

    DSL(String file){
        // Create CompilerConfiguration and assign
        // the DelegatingScript class as the base script class.
        def compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.scriptBaseClass = DelegatingScript.class.name

        // Configure the GroovyShell and pass the compiler configuration.
        _shell = new GroovyShell(this.class.classLoader, new Binding(), compilerConfiguration)

        _nameFile = file

        _script = _shell.parse(new File(_nameFile).text)
        _script.setDelegate(this)

        // Run DSL script.
        _script.run()
    }

    def reLoad(){
        toolIndexStack = []

        _script = _shell.parse(new File(_nameFile).text)
        _script.setDelegate(this)

        // Run DSL script.
        _script.run()
    }

    def title(String str){
        toolIndexStack.push(['widget': 'title', 'args': ['title': str]])
        //println 'toolIndexStack'
        //println toolIndexStack
        //println ''
    }

    def data(String str){
        data = str
        props[str]
    }

    def setData(obj){
        props[data]= obj
    }

    def description(String str){
        toolIndexStack.push(['widget': 'description', 'args': ['description': toHTML(str)]])
        //println 'toolIndexStack'
        //println toolIndexStack
        //println ''
        //println  Processor.process(description, true)
        //println new PegDownProcessor().markdownToHtml(description)
    }

    def features(String clsName, Closure closure){
        clsId = slg.slugify(clsName)

        def requestLst          = [:]
        requestLst['widgets']   = [:]
        def argLst              = [:]
        argLst['widgets']       = [:]
        featureLst              = []
        closure()

        //println 'Features Lst'
        //println featureLst

        requestLst[clsId+'_types'] = ['rdfs:subClassOf', clsName]
        featureLst.each{
            requestLst['widgets'][it.id] = it.request
            argLst['widgets'][it.id] = ['widget': it.widget, 'args': it.args]
        }

        //println 'requestLst'
        //println requestLst
        //println 'argLst'
        //println argLst

        //argLst[id] = clsId+'_'+id

        //println 'Request Lst'
        /*requestLst.widgets.each{ key, value ->
            println 'widget'
            println key
            println value
        }*/
        //println 'Arg Lst:'
        //println argLst

        //println 'toolIndexStack'
        //println toolIndexStack
        //println ''

        toolIndexStack.push(['widget': 'selectEntity', 'request': ['production_units': ['a', 'dbp:Farm']], 'args': [:]])
        toolIndexStack.push(['widget': 'createEntity', 'request': requestLst, 'args': argLst])
        //dsl.toolIndexStack.push(['widget': 'createEntity', 'args': ['microregions': data[0][2], 'technologies': data[1][2], 'production_unit_types': data[2][2]]])
        //println 'toolIndexStack'
        //println toolIndexStack
        //println ''
    }

//    def recommendation(Map map, String txt){
//        recommendations << [map['if'],txt]
//    }

    static toHTML(String txt) {md.markdownToHtml(txt)}

    def show(String txt){
        report << ['show', toHTML(txt)]
    }

    def recommendation(String txt){
        report << ['recommendation', toHTML(txt)]
    }

    def recommendation(boolean c, String txt){
        if (c) report << ['recommendation', toHTML(txt)]
    }

    def recommendation(Map map){
        if (map.if) report << ['recommendation', toHTML(map.show)]
    }

    def recommendation(Map map, String txt){
        if (map['if']) report << ['recommendation', toHTML(txt)]
    }

    def instance(Map textMap = [:], String clsName){
        def argLst = [:]
        id = slg.slugify(clsName)
        argLst['id'] = id
        if(textMap.label)
            argLst['label'] = textMap.label

        featureLst << ['id': id, 'widget': 'instance', 'request': ['a', clsName], 'args': argLst]
    }

    def matrix(Map map){
        report << ['matrix', map.x, map.y, map.labelX, map.labelY, map.rangeX, map.rangeY]
    }

    def map(String url){
        report << ['map', url]
    }

    def subclass(String str){
        featureLst << ['subclass': ['rdfs:subClassOf', str]]
    }

    def dimension(String cls) {
        dimensions << cls
    }

    def prog(Closure c){
        program = c
    }

    def _cleanProgram(){
        report = []
    }

    def propertyMissing(String str, arg) {
        props[str] = arg
    }

    def propertyMissing(String str) {
        props[str]
    }
}



