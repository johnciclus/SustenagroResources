package dsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.kohsuke.groovy.sandbox.SandboxTransformer
import org.springframework.context.ApplicationContext
import semantics.Node

/**
 * Created by dilvan on 7/14/15.
 */

class DSL {
    def featureMap = [:]
    def analyzesMap = [:]
    def report = []

    def evaluationObjectInstance

    def data
    def props = [:]
    Closure program

    GroovyShell _shell
    def _sandbox
    def _script
    def _ctx
    def _k
    def _gui
    static _md

    DSL(String file, ApplicationContext applicationContext){
        // Create CompilerConfiguration and assign
        // the DelegatingScript class as the base script class.
        _ctx = applicationContext
        _k = _ctx.getBean('k')
        _gui = _ctx.getBean('gui')
        _md = _ctx.getBean('md')

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
        featureMap = [:]
        analyzesMap = [:]
        report = []

        evaluationObjectInstance = null

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

    def evaluationObject(String id, Closure closure){
        String uri = _k.toURI(id)
        def object = new EvaluationObject(uri, _ctx)

        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = object
        closure()

        evaluationObjectInstance = object
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

    def data(String str){
        data = str
        props[str]
    }

    def setData(obj){
        props[data]= obj
    }

    def setData(String str, obj){
        props[str]= obj
    }

    def getData(String str){
        props[str]
    }

    def individual(String key, String uri){
        props[key]= _k.toURI(uri)
    }

    def propertyMissing(String key, arg) {
        props[key] = arg
    }

    def propertyMissing(String key) {
        props[key]
        //new Node(_k, _k.toURI(props[key]))
    }

    def methodMissing(String key, args){
        if(args.getClass() == Object[]){
            if(args.size()==1){
                if(args[0].getClass() == String)
                    props[key] = _toHTML(args[0])
                else
                    props[key] = args[0]
            }
            else
                props[key] = args
        }
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

    static _toHTML(String txt) {_md.markdownToHtml(txt)}

    def show(String txt){
        report << ['show', _toHTML(txt)]

    }

    def linebreak(){
        report << ['linebreak']
    }

    def recommendation(String txt){
        report << ['recommendation', _toHTML(txt)]
    }

    def recommendation(boolean c, String txt){
        if (c) report << ['recommendation', _toHTML(txt)]
    }

    def recommendation(Map map){
        if (map.if) report << ['recommendation', _toHTML(map.show)]
    }

    def recommendation(Map map, String txt){
        if (map['if']) report << ['recommendation', _toHTML(txt)]
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

    def _clean(String controller, String action){

        if(controller?.trim() && controller?.trim()){
            _gui.viewsMap[controller][action] = []
        }

        /*
        report = []

        analyzesMap.each{ analyseKey, analyse ->
            analyse.object.widgets = []
            analyse.object.model = []
        }
        */
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
            //_gui.viewsMap['tool']['assessment'] = analyse.object.widgets
        }
        println "Run Analyse"

        println "Analizes map size "+analyzesMap.size()
        //println props
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

    /*
       def title(String arg) {
           setData('title', arg)
       }

       def description(String arg){
           setData('description', _toHTML(arg))
           //def gui = _ctx.getBean('gui')
           //gui.viewsMap['tool']['index'].push(['widget': 'description', 'args': ['description': _toHTML(arg)]])

           //println  Processor.process(description, true)
           //println new PegDownProcessor().markdownToHtml(description)
       }


       def paragraph(String arg){
           //def gui = _ctx.getBean('gui')
           //gui.viewsMap['tool']['assessment'].push(['widget': 'paragraph', 'args': ['text': arg]])
       }

       def recommendation(Map map, String txt){
           recommendations << [map['if'],txt]
       }
       */

}

