package dsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.kohsuke.groovy.sandbox.SandboxTransformer
import org.springframework.context.ApplicationContext
import semantics.DataReader
import semantics.Node

/**
 * Created by dilvan on 7/14/15.
 */

class DSL {
    def featureMap = [:]
    def analyzesMap = [:]

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

        evaluationObjectInstance = null

        _sandbox.register()

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

    def feature(String id, Closure closure = {}){
        String uri = _k.toURI(id)
        def feature = new Feature(uri, _ctx)

        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = feature
        closure()

        featureMap[uri] = feature
    }

    def prog(Closure c){
        program = c
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
        float val = 0.0
        float value
        float weight

        if(obj instanceof ArrayList) {
            obj.each {
                value = (it.value.getClass() == Boolean ? (it.value ? 1 : -1) : it.value)
                //weight = (it.weight.getClass() == Boolean ? (it.weight ? 1 : -1) : it.weight)
                //val += (float) value*weight
                val += value
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


    def data(String str){
        data = str
        //props[str]
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

    def propertyMissing(String key, arg) {
        //println "propertyMissing: key, arg "+key+"->"+arg
        props[key] = arg
    }

    def propertyMissing(String key) {
        props[key]
        //new Node(_k, _k.toURI(props[key]))
    }

    def printData(){
        println 'props'
        println props
    }

    def getScenario(){
        def result = [:]
        props.each{ key, value ->
            if(value.getClass() != DataReader)
                result[key] = value
        }
        return result
    }

    def analysis(String id, Closure closure){
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

    def _clean(String controller, String action){
        if(controller?.trim() && action?.trim()){
            _gui.viewsMap[controller][action] = []
        }

        /*
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
            //_gui.viewsMap['tool']['analysis'] = analyse.object.widgets
        }
        println "Run Analyse"

        println "Analizes map size "+analyzesMap.size()
        //println props
    }
}

