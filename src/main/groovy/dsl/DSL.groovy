package dsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.kohsuke.groovy.sandbox.SandboxTransformer
import org.springframework.context.ApplicationContext
import semantics.Node

/**
 * Created by dilvan on 7/14/15.
 */

class DSL {
    def viewsMap = [:]
    def evaluationObjectMap = [:]
    def featureMap = [:]
    //def dimensionsMap = [:]
    def analyzesMap = [:]
    def report = []

    def data
    def props = [:]
    Closure program

    GroovyShell _shell
    def _sandbox
    def _script
    def _ctx
    def _k
    static _md

    DSL(String file, ApplicationContext applicationContext){
        // Create CompilerConfiguration and assign
        // the DelegatingScript class as the base script class.
        _ctx = applicationContext
        _k = _ctx.getBean('k')
        _md = _ctx.getBean('md')

        viewsMap['tool'] = [:]
        viewsMap['tool']['index'] = []
        viewsMap['tool']['assessment'] = []

        def _cc = new CompilerConfiguration()
        _cc.addCompilationCustomizers(new SandboxTransformer())
        _cc.setScriptBaseClass(DelegatingScript.class.getName())

        _shell = new GroovyShell(new Binding(), _cc)
        _sandbox = new DSLSandbox()
        _sandbox.register()

        // Configure the GroovyShell and pass the compiler configuration.
        //_shell = new GroovyShell(this.class.classLoader, binding, cc)

        _script = (DelegatingScript) _shell.parse(new File(file).text)
        _script.setDelegate(this)

        // Run DSL script.
        try {
            _script.run()
        }
        finally {
            _sandbox.unregister()
        }
    }

    def reload(String code){
        viewsMap = [:]
        viewsMap['tool'] = [:]
        viewsMap['tool']['index'] = []
        viewsMap['tool']['assessment'] = []

        evaluationObjectMap = [:]
        featureMap = [:]
        dimensionsMap = [:]
        analyzesMap = [:]
        report = []

        _sandbox.register()

        //def stack = code.tokenize("\n")

        //for (c in stack){
        //    println c + "\n"
        //}

        _script = (DelegatingScript) _shell.parse(code)
        _shell
        _script.setDelegate(this)

        def response  = [:]

        // Run DSL script.
        try {
            _script.run()
            response.status = 'ok'
        }
        catch(Exception e){
            response.error = [:]
            for (StackTraceElement el : e.getStackTrace()) {
                if(el.getMethodName() == 'run' && el.getFileName() ==~ /Script.+\.groovy/) {
                    response.error.line = el.getLineNumber()
                    response.error.message = e.getMessage()
                    response.error.filename = el.getFileName()
                }
            }
            response.status = 'error'
        }
        finally {
            _sandbox.unregister()
        }
        return response
    }

    def title(String arg){
        viewsMap['tool']['index'].push(['widget': 'title', 'args': ['title': arg]])
    }

    def description(String arg){
        viewsMap['tool']['index'].push(['widget': 'description', 'args': ['description': toHTML(arg)]])

        //println  Processor.process(description, true)
        //println new PegDownProcessor().markdownToHtml(description)
    }

    def paragraph(String arg){
        viewsMap['tool']['assessment'].push(['widget': 'paragraph', 'args': ['text': arg]])
    }

    def evaluationObject(String id, Closure closure){
        String uri = _k.toURI(id)
        def object = new EvaluationObject(uri, _ctx)

        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = object
        closure()

        evaluationObjectMap[uri] = object
    }

    def selectEvaluationObject(Map args = [:], String id){
        def uri = _k.toURI(id)
        def request = ['unities': ['a', uri]]
        def shortId = _k.shortURI(uri)
        args['evaluationObject']= uri

        //println uri
        //println shortId

        viewsMap['tool']['index'].push(['widget': 'selectEvaluationObject', 'request': request, args: args])
    }

    def createEvaluationObject(Map args = [:], String id){
        def uri = _k.toURI(id)
        def requestLst        = [:]
        requestLst['widgets'] = [:]
        args['widgets']       = [:]
        args['evaluationObject']         = uri

        evaluationObjectMap[uri].widgets.each{
            if(it.request) {
                requestLst['widgets'][it.id] = it.request
            }
            args['widgets'][it.id] = ['widget': it.widget, 'args': it.args]
        }

        viewsMap['tool']['index'].push(['widget': 'createEvaluationObject', 'request': requestLst, 'args': args])
    }

    /*def dimension(String id, Closure closure = {}) {
        String uri = _k.toURI(id)
        def feature = new Feature(uri, _ctx)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = feature
        closure()

        dimensionsMap[uri] = feature
    }

    def productionFeature(String id, Closure closure = {}){
        String uri = _k.toURI(id)
        def feature = new Feature(uri, _ctx)

        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = feature
        closure()

        featureMap[uri] = feature
    }
    */

    def indicators(String id, Closure closure = {}){
        String uri = _k.toURI(id)
        def feature = new Feature(uri, _ctx)

        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = feature
        closure()

        featureMap[uri] = feature
    }


//    def recommendation(Map map, String txt){
//        recommendations << [map['if'],txt]
//    }

    def data(String str){
        data = str
        props[str]
    }

    def setData(obj){
        props[data]= obj
    }

    def individual(String key, String uri){
        props[key]= _k.toURI(uri)
    }

    /*
    def propertyMissing(String str, arg) {
        props[str] = arg
    }

    def propertyMissing(String arg) {
        props[arg]
    }
    */

    def propertyMissing(String str, arg) {
        props[str] = arg
    }

    def propertyMissing(String key) {
        //props[str]
        new Node(_k, _k.toURI(props[key]))
    }

    def printData(){
        println 'props'
        println props
    }

    def assessment(String id, Closure closure){
        String uri = _k.toURI(id)

        def requestLst        = [:]
        requestLst['widgets'] = [:]
        def args              = [:]
        args['widgets']       = [:]

        def object = new Analysis(uri, _ctx)

        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = object

        analyzesMap[uri] = [object: object,
                            closure: closure]
    }

    static toHTML(String txt) {_md.markdownToHtml(txt)}

    def show(String txt){
        report << ['show', toHTML(txt)]

    }

    def linebreak(){
        report << ['linebreak']
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

    def table(ArrayList list, Map headers = [:]){
        report << ['table', list, headers]
    }

    def matrix(Map map){
        report << ['matrix', map.x, map.y, map.labelX, map.labelY, map.rangeX, map.rangeY, map.quadrants, map.recomendations]
    }

    def map(String url){
        report << ['map', url]
    }

    def prog(Closure c){
        program = c
    }

    def _evalIndividuals(String id){
        def uri = _k.toURI(':'+id)
        def types = _k[uri].getType()

        analyzesMap.each{ analyseKey, analyse ->
            props.each{ key, value ->
                types.each{
                    if(it == value){
                        props[key] = uri
                        analyse.closure()
                        props[key] = value
                    }
                }
            }
            viewsMap['tool']['assessment'] = analyse.object.widgets
        }
        println "Run Analyse"

        println "Analizes map size "+analyzesMap.size()
        //println props
    }

    def _cleanView(String controller, String action){

        if(controller?.trim() && controller?.trim()){
            println "clean "+ controller + " - " + action
            //viewsMap[controller][action] = []
        }
        report = []

        analyzesMap.each{ analyseKey, analyse ->
            analyse.object.widgets = []
            analyse.object.model = []
        }
    }

    def sum(obj){
        float val = 0
        float value
        //println obj

        if(obj instanceof ArrayList) {
            obj.each {
                value = (it.value.getClass() == Boolean ? (it.value ? 1 : -1) : it.value)
                val += (float) value
            }
            return val
        }
        else if(obj instanceof Boolean){
            return (obj ? 1.0 : -1.0)
        }
        else if(obj instanceof Double || obj instanceof Integer){
            return obj
        }
    }

    def weightedSum(obj){
        float val = 0
        float value
        float weight

        if(obj instanceof ArrayList) {
            obj.each {
                value = (it.value.getClass() == Boolean ? (it.value ? 1 : -1) : it.value)
                weight = (it.weight.getClass() == Boolean ? (it.weight ? 1 : -1) : it.weight)
                val += (float) value*weight
            }
        }

        return val
    }

    def average(obj){
        float val = 0
        float num
        //println obj

        if(obj instanceof ArrayList){
            obj.each{
                num = (it.getClass() == Boolean ? (it ? 1 : -1) : it)
                val += (float) num
            }
            return val/obj.size()
        }
        else if(obj instanceof Boolean){
            return (obj ? 1.0 : -1.0)
        }
        else if(obj instanceof Double || obj instanceof Integer){
            return obj
        }
    }
}