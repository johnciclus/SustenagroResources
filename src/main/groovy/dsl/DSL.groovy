package dsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.kohsuke.groovy.sandbox.SandboxTransformer
import org.springframework.context.ApplicationContext
import org.springframework.core.io.Resource
import semantics.DataReader
import utils.Uri

/**
 * Created by dilvan on 7/14/15.
 */

class DSL {
    private _ctx
    private _k
    private _gui
    private static _md

    private _sandbox
    private _script
    private _program
    private _shell

    private _data
    private _msg
    private _props = [:]
    private _featureMap = [:]
    private _scenarioMap = [:]
    private _reportView = []
    private _evaluationObjectInstance

    DSL(String filename, ApplicationContext applicationContext){
        // Create CompilerConfiguration and assign
        // the DelegatingScript class as the base script class.
        _ctx = applicationContext
        _k = _ctx.getBean('k')
        _gui = _ctx.getBean('gui')
        _md = _ctx.getBean('md')
        _msg = _ctx.getBean('messageSource')

        def _cc = new CompilerConfiguration()
        _cc.addCompilationCustomizers(new SandboxTransformer())
        _cc.setScriptBaseClass(DelegatingScript.class.getName())

        _shell = new GroovyShell(new Binding(), _cc)
        _sandbox = new DSLSandbox()
        _sandbox.register()

        // Configure the GroovyShell and pass the compiler configuration.
        //_shell = new GroovyShell(this.class.classLoader, binding, cc)
        //println _ctx.getResource(filename).getFile().text

        //_script = (DelegatingScript) _shell.parse(new File(filename).text)
        //println _ctx.getBean('path')
        //println new File(_ctx.getBean('path')+filename).toString()

        _script = (DelegatingScript) _shell.parse(new File(_ctx.getBean('path')+filename))
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
        _data = null
        _props = [:]
        _featureMap = [:]
        _reportView = []
        _evaluationObjectInstance = null

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

        _evaluationObjectInstance = object
    }

    def evaluationObjectInfo(){
        def analyseURI = _props[_data].id
        def evalObjURI = _k[analyseURI].getAttr('appliedTo')
        def data = []
        _k[evalObjURI].getDataProperties().each{
            data.push([label: it.dataPropertyLabel.capitalize(), value: it.value])
        }
        _k[evalObjURI].getObjectProperties().each{
            data.push([label: it.objectPropertyLabel.capitalize(), value: it.valueLabel])
        }
        _reportView.push(['widget': 'evaluationObjectInfo', 'attrs': [data: data]])
    }

    def getEvaluationObject(){
        _evaluationObjectInstance
    }

    def feature(Map attrs, String id, Closure closure = {}){
        String uri = _k.toURI(id)
        def feature = new Feature(uri, attrs, _ctx)

        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = feature
        closure()

        _featureMap[uri] = feature
    }

    def feature(String id, Closure closure = {}){
        feature([:], id, closure)
    }

    def getFeatureMap(){
        return _featureMap
    }

    def getScenarioMap(){
        _scenarioMap
    }

    def report(Closure c){
        _program = c
    }

    def runReport(){
        _program()
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
                if(it.relevance){
                    weight = (it.relevance.getClass() == Boolean ? (it.relevance ? 1 : -1) : it.relevance)
                }
                else if(it.weight){
                    weight = it.weight
                }
                else{
                    weight = 1.0
                }
                val += (float) value * weight
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

    def getVariables(){
        def result = [:]
        _props.each{ key, value ->
            if(value.getClass() != DataReader)
                result[key] = value
        }
        return result
    }

    def getReportView(){
        return _reportView
    }

    def data(String str){
        _data = str
        //_props[str]
    }

    def setData(obj){
        _props[_data]= obj
    }

    def getData(String key){
        _props[key]
    }

    def printData(){
        println _props
    }

    def propertyMissing(String key) {
        //println "propertyMissing: key "+key
        getData(key)
        //new Node(_k, _k.toURI(props[key]))
    }

    def propertyMissing(String key, arg) {
        //println "propertyMissing: key, arg "+key+"->"+arg
        _props[key] = arg
    }

    def methodMissing(){
        println "methodMissing: key "
    }

    def methodMissing(String key) {
        println "methodMissing: key "+key
        //new Node(_k, _k.toURI(props[key]))
    }

    def methodMissing(String key, attrs){
        //println "DSL methodMissing: "+ key
        def attrsTmp = attrs.clone()
        def tmp
        if(attrs.getClass() == Object[]){
            def container = []
            def element = null
            def locale = Locale.getDefault()
            def i18nParams = ['label', 'label_x', 'label_y', 'legend']

            if(_gui.getWidgetsNames().contains(key)){
                /*if(key=='text'){
                    println key
                    println attrs
                    println attrs.size()
                    println attrs[0].getClass()
                }*/
                if(attrs.size()==1 && attrs[0].getClass() == String){
                    if(_reportView){
                        container = _reportView
                        element = ['widget': key, 'attrs': ['text': _toHTML(attrs[0])]]
                    }
                    else{
                        _props[key] =_toHTML(attrs[0])
                    }
                }
                else if(attrs.size()==1 && attrs[0].getClass() == LinkedHashMap){
                    if(_reportView && key == 'text'){
                        container = _reportView
                        if(attrs[0].getClass() == LinkedHashMap){
                            tmp = attrs[0][locale.language]
                        }
                        else{
                            tmp = attrs[0]
                        }
                        element = ['widget': key, 'attrs': ['text': _toHTML(tmp)]]
                    }
                    else{
                        container = _reportView
                        i18nParams.each{ param ->
                            if(attrsTmp[0][param] && (attrs[0][param].getClass() == LinkedHashMap)){
                                attrsTmp[0][param] = attrs[0][param][locale.language]
                            }
                            else if(attrsTmp[0][param] && (attrs[0][param].getClass() == ArrayList)){
                                tmp = []
                                attrs[0][param].each{
                                    if(it.getClass() == LinkedHashMap)
                                        tmp.push(it[locale.language])
                                    else if(it.getClass() == String)
                                        tmp.push(it)
                                }
                                attrsTmp[0][param] = tmp
                            }
                        }
                        element = ['widget': key, 'attrs': attrsTmp[0]]
                    }
                }
                else if(attrs.size()==2 && attrs[0].getClass() == LinkedHashMap && attrs[1].getClass() == ArrayList){
                    if(attrs[0].text){
                        container = attrs[1]
                        element = ['widget': key, 'attrs': ['text': _toHTML(attrs[0].text)]]
                    }
                    else{
                        container = attrs[1]
                        element = ['widget': key, 'attrs': attrs[0]]
                    }
                }
                if(element)
                    container.push(element)
            }
            else if(attrs.size()==1 && attrs[0].getClass() == String){
                _props[key] = _toHTML(attrs[0])
            }
            else{
                println 'Unknown method: '+ key
                attrs.eachWithIndex{ it, int i ->
                    println "Attrs ["+i+"]: ("+it+")"
                    Uri.printTree(it)
                }
            }
        }
    }

    def clean(String controller, String action){
        if(controller?.trim() && action?.trim()){
            _gui.viewsMap[controller][action] = []
            if(action == 'analysis'){
               _reportView = []
            }
        }
    }

    def message(String code){
        _msg.getMessage(code, null, java.util.Locale.getDefault())
    }

    static _toHTML(String txt) {_md.markdownToHtml(txt)}
    /*
    def _evalIndividuals(String id){
        def uri = _k.toURI(':'+id)
        def types = _k[uri].getType()

        _analysisMap.each{ analyseKey, analyse ->
            _props.each{ key, value ->
                types.each{
                    if(it == value){
                        _props[key] = uri
                        analyse.closure()
                        _props[key] = value
                    }
                }
            }
            //_gui.viewsMap['tool']['analysis'] = analyse.object.widgets
        }
        println "Run Analyse"

        println "Analizes map size "+_analysisMap.size()
        //println _props
    }
    */
}

