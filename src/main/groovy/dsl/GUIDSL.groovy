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
    def controller
    def action

    static _md

    def viewsMap
    def dataTypeToWidget

    def props = [:]

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
        viewsMap['tool']['analysis'] = []
        viewsMap['tool']['scenario'] = []

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
        viewsMap['tool']['analysis'] = []
        viewsMap['tool']['scenario'] = []

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

        viewsMap[controller][action].push(['widget': 'selectEvaluationObject', 'request': request, args: args])
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

        viewsMap[controller][action].push(['widget': 'createEvaluationObject', 'request': requestLst, 'args': args])
    }

    def paragraph(Map args = [:]){
        viewsMap[controller][action].push(['widget': 'paragraph', 'args': [text: _toHTML(args['text'])]])
    }

    def paragraph(String txt){
        //report << ['paragraph', _toHTML(txt)]
        viewsMap[controller][action].push(['widget': 'paragraph', 'args': [text: _toHTML(txt)]])
    }

    def linebreak(){
        //report << ['linebreak']
        viewsMap[controller][action].push(['widget': 'linebreak'])
    }

    def recommendation(String txt){
        //report << ['recommendation', _toHTML(txt)]
        viewsMap[controller][action].push(['widget': 'paragraph', 'args': [text: _toHTML('Recomendação: '+ txt)]])

    }

    def recommendation(boolean c, String txt){
        //if (c) report << ['recommendation', _toHTML(txt)]
    }

    def recommendation(Map map){
        //if (map.if) report << ['recommendation', _toHTML(map.show)]
    }

    def recommendation(Map map, String txt){
        //if (map['if']) report << ['recommendation', _toHTML(txt)]
    }

    def table(ArrayList list, Map headers = [:]){
        //report << ['table', list, headers]
        viewsMap[controller][action].push(['widget': 'tableReport', 'args': [header: headers, data: list]])
    }

    def map(String url){
        //report << ['map', url]
        viewsMap[controller][action].push(['widget': 'map', 'args': [map_url: url]])
    }

    def matrix(Map map){
        //report << ['matrix', map.x, map.y, map.labelX, map.labelY, map.rangeX, map.rangeY, map.quadrants, map.recomendations]
        viewsMap[controller][action].push(['widget': 'matrix', 'args': [x: map.x, y: map.y, label_x: map.labelX, label_y: map.labelY, range_x: map.rangeX, range_y: map.rangeY, quadrants: map.quadrants, recomendations: map.recomendations]])
    }

    def tabs(Map extArgs = [:], Map data = [:], String evaluationObjectId){
        def args = [:]
        def tab_prefix = 'tab_'
        def uri = _k.toURI(evaluationObjectId)

        args['evalObjInstance']  = uri

        args['id'] = 'analysis'
        args['tabs'] = [:]
        args['widgets'] = [:]

        data.individuals.eachWithIndex{ it, int i ->
            args['tabs'][tab_prefix+i] = ['widget': 'tab', args: [id: tab_prefix+i, label: it.value.label]]
            args['widgets'][tab_prefix+i] = [args: [id: tab_prefix+i, label: it.value.label, data: it.value.subClass, values: data.values, weights: data.weights]]
        }

        args['tabs'][tab_prefix+'0'].args['widgetClass'] = 'active'
        args['widgets'][tab_prefix+'0'].args['widgetClass'] = 'active'
        args['widgets'][tab_prefix+(data.individuals.size()-1)].args['submitLabel'] = extArgs['submitLabel']

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

        Uri.printTree(data)

        /*
        extArgs['tabs'].eachWithIndex{ it, int i ->
            args['tabs'][tab_prefix+i] = ['widget': 'tab', args: [id: tab_prefix+i, label: it.label]]
            args['widgets'][tab_prefix+i] = ['widget': it.widget, args: [id: tab_prefix+i, label: it.label]]
        }
        */

        /*data.each{ tab ->
            tab.value.each { key, value ->
                args['widgets'][tab.key].args[key] = value
            }
        }*/



        viewsMap[controller][action].push(['widget': 'tabs', 'args': args])

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

    def individual(String key, String uri){
        props[key]= _k.toURI(uri)
    }

    def methodMissing(String key, args){
        //println "methodMissing"
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

    def _setView(String controllerName, String actionName){
        this.controller = controllerName
        this.action = actionName
    }

    static _toHTML(String txt) {_md.markdownToHtml(txt)}

}
