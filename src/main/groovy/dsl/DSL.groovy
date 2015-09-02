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
        //println  Processor.process(description, true)
        //println new PegDownProcessor().markdownToHtml(description)
    }

    def features(String clsName, Closure closure){
        clsId = slg.slugify(clsName)

        requestLst = [:]
        argLst     = [:]
        featureLst = []
        closure()

        //println 'Features Lst'
        //println featureLst

        requestLst['production_unit_types'] = ['rdfs:subClassOf', clsName]
        featureLst.each{
            requestLst['widgets'] = []
            requestLst['widgets'] << it.request
            it.args.each{ id, arg ->
                arg['id'] = clsId+'_'+arg.id
                argLst['widgets'] = []
                argLst['widgets'] << ['widget': id, 'args': arg]
            }
        }

        //argLst[id] = clsId+'_'+id

        //println 'Request Lst'
        //println requestLst
        //println 'Arg Lst:'
        //println argLst

        toolIndexStack.push(['widget': 'selectEntity', 'request': ['production_units': ['a', 'dbp:Farm']], 'args': [:]])
        toolIndexStack.push(['widget': 'createEntity', 'request': requestLst, 'args': argLst])
        //dsl.toolIndexStack.push(['widget': 'createEntity', 'args': ['microregions': data[0][2], 'technologies': data[1][2], 'production_unit_types': data[2][2]]])

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
        contentLst = [:]
        id = slg.slugify(clsName)
        contentLst['id'] = id
        if(textMap.label)
            contentLst['label'] = textMap.label

        args = [:]
        args['instance'] = contentLst

        featureLst << ['request': ['instance': ['a', clsName]], 'args': args]
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



