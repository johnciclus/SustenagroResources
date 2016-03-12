package dsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.kohsuke.groovy.sandbox.SandboxTransformer
import org.springframework.context.ApplicationContext
import utils.Uri

/**
 * Created by john on 26/02/16.
 */
class GUIDSL {
    def _shell
    def _sandbox
    def _script
    def _ctx
    def _k
    def widgetAttrs
    static _md

    def viewsMap
    def dataTypeToWidget

    def GUIDSL(String file, ApplicationContext applicationContext){

        _ctx = applicationContext
        _k = _ctx.getBean('k')
        _md = _ctx.getBean('md')

        dataTypeToWidget = [:]
        widgetAttrs = [:]

        viewsMap = [:]
        viewsMap['tool'] = [:]
        viewsMap['tool']['index'] = []
        viewsMap['tool']['assessment'] = []

        // Create CompilerConfiguration and assign
        // the DelegatingScript class as the base script class.
        def _cc = new CompilerConfiguration()
        _cc.addCompilationCustomizers(new SandboxTransformer())
        _cc.setScriptBaseClass(DelegatingScript.class.getName())

        _shell = new GroovyShell(new Binding(), _cc)
        _sandbox = new DSLSandbox()
        _sandbox.register()

        // Configure the GroovyShell and pass the compiler configuration.
        //_shell = new GroovyShell(this.class.classLoader, binding, cc)
        _ctx = applicationContext

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

        dataTypeToWidget = [:]

        viewsMap = [:]
        viewsMap['tool'] = [:]
        viewsMap['tool']['index'] = []
        viewsMap['tool']['assessment'] = []

        _sandbox.register()

        //def stack = code.tokenize("\n")

        //for (c in stack){
        //    println c + "\n"
        //}

        _script = (DelegatingScript) _shell.parse(code)
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

    def dataType(Map args = [:], String id){
        def k = _ctx.getBean('k')
        dataTypeToWidget[k.toURI(id)] = args['widget']
    }

    def widgetAttributes(Map args = [:], String id){
        widgetAttrs[id] = args
    }

    def selectEvaluationObject(Map args = [:], String id){
        def uri = _k.toURI(id)
        def request = ['evaluationObjects': ['a', uri]]
        def shortId = _k.shortURI(uri)
        args['evaluationObject']= uri

        //println uri
        //println shortId

        viewsMap['tool']['index'].push(['widget': 'selectEvaluationObject', 'request': request, args: args])
    }

    def createEvaluationObject(Map args = [:], String id){
        def uri = _k.toURI(id)
        def requestLst              = [:]
        requestLst['widgets']       = [:]
        args['widgets']             = [:]
        args['evaluationObject']    = uri

        def dsl = _ctx.getBean('dsl')

        dsl.evaluationObjectInstance.widgets.each{
            if(it.request) {
                requestLst['widgets'][it.id] = it.request
            }
            args['widgets'][it.id] = ['widget': it.widget, 'args': it.args]
        }

        viewsMap['tool']['index'].push(['widget': 'createEvaluationObject', 'request': requestLst, 'args': args])
    }

    def paragraph(Map args = [:]){
        viewsMap['tool']['assessment'].push(['widget': 'paragraph', 'args': [text: _toHTML(args['text'])]])
    }

    def tabs(Map extArgs = [:], String id){
        def dsl = _ctx.getBean('dsl')
        def args = [:]
        args['id'] = id
        args['tabs'] = [:]
        args['widgets'] = [:]

        extArgs['tabs'].eachWithIndex{ it, int i ->
            args['tabs']['tab_'+i] = ['widget': 'tab', args: [id: 'tab_'+i, label: it.label]]
            args['widgets']['tab_'+i] = ['widget': it.widget, args: [id: 'tab_'+i, label: it.label]]
        }
        args['tabs']['tab_0'].args['widgetClass'] = 'active'
        args['widgets']['tab_0'].args['widgetClass'] = 'active'

        args['widgets'].eachWithIndex{ widget, int i ->
            if(i > 0 ){
                widget.value.args['previous'] = 'tab_'+(i-1)
                widget.value.args['previousLabel'] = extArgs['previousLabel']
            }
            if(i < (args['tabs'].size()-1)){
                widget.value.args['next'] = 'tab_'+(i+1)
                widget.value.args['nextLabel'] = extArgs['nextLabel']
            }
        }

        def indicators = [:]
        def categories = [:]

        dsl.featureMap.each{ key, feature ->
            feature.features.each{
                indicators[it.key] = it.value
            }
            categories += feature.updateCategories()
            feature.cleanCategories()
            //Uri.printTree(feature.features)
        }



        args['widgets']['tab_0'].args['indicators'] = indicators
        args['widgets']['tab_0'].args['categories'] = categories
        args['widgets']['tab_0'].args['values'] = [:]

        viewsMap['tool']['assessment'].push(['widget': 'tabs', 'args': args])
    }

    def _requestData(String controllerName, String actionName){
        viewsMap[controllerName][actionName].each{ command ->
            if(command.request){
                command.request.each{ key, args ->
                    if(key!='widgets'){
                        command.args[key] = _k[args[1]].getLabelDescription(args[0].toString())
                    }
                    else if(key=='widgets'){
                        args.each{ subKey, subArgs ->
                            //command.args.widgets[subKey]['args']['data'] = getLabelDescription(subArgs[1], subArgs[0])
                            command.args.widgets[subKey]['args']['data'] = _k[subArgs[1]].getLabelDescription(subArgs[0].toString())
                        }
                    }
                }
            }
        }
    }

    static _toHTML(String txt) {_md.markdownToHtml(txt)}

}
