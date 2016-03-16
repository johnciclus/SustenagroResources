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
    def contanst

    static _md

    def viewsMap
    def dataTypeToWidget

    def GUIDSL(String file, ApplicationContext applicationContext){

        _ctx = applicationContext
        _k = _ctx.getBean('k')
        _md = _ctx.getBean('md')

        dataTypeToWidget = [:]
        widgetAttrs = [:]
        contanst = [:]

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

    def contanst(Object arg, String id){
        contanst[id] = arg
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

    def createEvaluationObject(Map args = [:], ArrayList widgets = [], String evaluationObjectId){
        def uri = _k.toURI(evaluationObjectId)
        def requestLst              = [:]
        requestLst['widgets']       = [:]
        args['widgets']             = [:]
        args['evalObjType']  = uri

        widgets.each{
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

    def tabs(Map extArgs = [:], Map data = [:], String evaluationObjectId){
        def args = [:]
        def tab_prefix = 'tab_'
        def uri = _k.toURI(evaluationObjectId)

        args['evalObjInstance']  = uri

        args['id'] = 'assessment'
        args['tabs'] = [:]
        args['widgets'] = [:]

        extArgs['tabs'].eachWithIndex{ it, int i ->
            args['tabs'][tab_prefix+i] = ['widget': 'tab', args: [id: tab_prefix+i, label: it.label]]
            args['widgets'][tab_prefix+i] = ['widget': it.widget, args: [id: tab_prefix+i, label: it.label]]
        }
        args['tabs'][tab_prefix+'0'].args['widgetClass'] = 'active'
        args['widgets'][tab_prefix+'0'].args['widgetClass'] = 'active'

        args['widgets'].eachWithIndex{ widget, int i ->
            if(i > 0 ){
                widget.value.args['previous'] = tab_prefix+(i-1)
                widget.value.args['previousLabel'] = extArgs['previousLabel']
            }
            if(i < (args['tabs'].size()-1)){
                widget.value.args['next'] = tab_prefix+(i+1)
                widget.value.args['nextLabel'] = extArgs['nextLabel']
            }
        }

        data.each{ tab ->
            tab.value.each { key, value ->
                args['widgets'][tab.key].args[key] = value
            }
        }

        viewsMap['tool']['assessment'].push(['widget': 'tabs', 'args': args])

        /*

        <g:render template="/widgets/tabs" model="${['id': 'indicators',
                                             'tabs': [[label: '1. Ambientais'],
                                                      [label: '2. Econômicos'],
                                                      [label: '3. Sociais']] ]}" />

        <ul id="indicator_tabs" class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"> <a href="#environmental_indicators" aria-controls="environmental_indicators" role="tab" data-toggle="tab">  1. Ambientais  </a></li>
            <li role="presentation">                <a href="#economic_indicators" aria-controls="economic_indicators" role="tab" data-toggle="tab">            2. Econômicos  </a></li>
            <li role="presentation">                <a href="#social_indicators" aria-controls="social_indicators" role="tab" data-toggle="tab">                3. Sociais     </a></li>
        </ul>

        <div id="indicator_content" class="tab-content">
            <div role="tabpanel" class="tab-pane" >

            </div>
        </div>



         */

    }

    def renderReport(ArrayList report){
        report.each{
            switch(it[0]){
                case 'paragraph':
                    viewsMap['tool']['assessment'].push(['widget': 'paragraph', 'args': [text: _toHTML(it[1])]])
                    break
                case 'linebreak':
                    viewsMap['tool']['assessment'].push(['widget': 'linebreak'])
                    break
                case 'recommendation':
                    println it
                    viewsMap['tool']['assessment'].push(['widget': 'paragraph', 'args': [text: _toHTML('Recomendação: '+ it[1])]])
                    break
                case 'table':
                    viewsMap['tool']['assessment'].push(['widget': 'tableReport', 'args': [header: it[2], data: it[1]]])
                    break
                case 'map':
                    viewsMap['tool']['assessment'].push(['widget': 'map', 'args': [map_url: it[1]]])
                    break
                case 'matrix':
                    viewsMap['tool']['assessment'].push(['widget': 'matrix', 'args': [x: it[1], y: it[2], label_x: it[3], label_y: it[4], range_x: it[5], range_y: it[6], quadrants: it[7], recomendations: []]])
                    break

            }
        }
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
