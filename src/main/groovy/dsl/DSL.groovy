package dsl

import com.github.rjeschke.txtmark.Processor
import org.codehaus.groovy.control.CompilerConfiguration
import org.pegdown.PegDownProcessor
import com.github.slugify.Slugify
import org.kohsuke.groovy.sandbox.SandboxTransformer

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
    def toolEvaluationStack = []

    def _nameFile = ''
    def _cc
    def _shell

    def _sandbox
    def _script
    static md = new PegDownProcessor()

    DSL(String file){
        // Create CompilerConfiguration and assign
        // the DelegatingScript class as the base script class.
        _cc = new CompilerConfiguration()
        _cc.addCompilationCustomizers(new SandboxTransformer())
        _cc.setScriptBaseClass(DelegatingScript.class.getName())

        _shell = new GroovyShell(new Binding(), _cc)
        _sandbox = new DSLSandbox()
        _sandbox.register()

        // Configure the GroovyShell and pass the compiler configuration.
        //_shell = new GroovyShell(this.class.classLoader, binding, cc)

        _nameFile = file

        _script = (DelegatingScript) _shell.parse(new File(_nameFile).text)
        _script.setDelegate(this)

        // Run DSL script.
        try {
            _script.run()
        } finally {
            _sandbox.unregister()
        }
    }

    def reLoad(){
        toolIndexStack = []
        toolEvaluationStack = []

        _sandbox.register()

        _script = (DelegatingScript) _shell.parse(new File(_nameFile).text)
        _script.setDelegate(this)

        // Run DSL script.
        try {
            _script.run()
        } finally {
            _sandbox.unregister()
        }
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
        def clsId = slg.slugify(clsName)

        def requestLst          = [:]
        requestLst['widgets']   = [:]
        def argLst              = [:]
        argLst['widgets']       = [:]
        featureLst              = []

        featureLst.push(['id': clsId+'_name', 'widget': 'name', 'args': ['id': clsId+'_name', 'label': 'Nome da unidade produtiva']])
        featureLst.push(['id': clsId+'_types', 'widget': 'instance', 'request': ['rdfs:subClassOf', clsName], 'args': ['id': clsId+'_types', 'label': 'Caracterização dos sistemas produtivos no Centro-Sul']])
        closure()

        featureLst.each{
            if(it.request) {
                requestLst['widgets'][it.id] = it.request
            }
            argLst['widgets'][it.id] = ['widget': it.widget, 'args': it.args]
        }

        toolIndexStack.push(['widget': 'selectEntity', 'request': ['production_units': ['a', 'dbp:Farm']], 'args': [:]])
        toolIndexStack.push(['widget': 'createEntity', 'request': requestLst, 'args': argLst])
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
        def id = slg.slugify(clsName)
        def argLst = [:]
        argLst['id'] = id
        if(textMap.label)
            argLst['label'] = textMap.label

        featureLst << ['id': id, 'widget': 'instance', 'request': ['a', clsName], 'args': argLst]
    }

    def matrix(Map map){
        report << ['matrix', map.x, map.y, map.labelX, map.labelY, map.rangeX, map.rangeY, map.quadrants, map.recomendations]
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


