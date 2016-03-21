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

    def dataType(Map attrs = [:], String id){
        def k = _ctx.getBean('k')
        dataTypeToWidget[k.toURI(id)] = attrs['widget']
    }

    def widgetAttributes(Map attrs = [:], String id){
        widgetAttrs[id] = attrs
    }

    def contanst(Object arg, String id){
        contanst[id] = arg
    }

    def selectEvaluationObject(Map attrs = [:], String id){
        def uri = _k.toURI(id)
        def request = ['evaluationObjects': ['a', uri]]
        def shortId = _k.shortURI(uri)
        attrs['evaluationObject']= uri

        //println uri
        //println shortId

        viewsMap[controller][action].push(['widget': 'selectEvaluationObject', 'request': request, attrs: attrs])
    }

    def createEvaluationObject(Map attrs = [:], ArrayList widgets = [], String evaluationObjectId){
        def uri = _k.toURI(evaluationObjectId)
        def requestLst              = [:]
        requestLst['widgets']       = [:]
        attrs['widgets']             = [:]
        attrs['evalObjType']  = uri

        widgets.each{
            if(it.request) {
                requestLst['widgets'][it.id] = it.request
            }
            attrs['widgets'][it.id] = ['widget': it.widget, 'attrs': it.attrs]
        }

        viewsMap[controller][action].push(['widget': 'createEvaluationObject', 'request': requestLst, 'attrs': attrs])
    }

    def paragraph(Map attrs = [:]){
        viewsMap[controller][action].push(['widget': 'paragraph', 'attrs': [text: _toHTML(attrs['text'])]])
    }

    def paragraph(String txt){
        //report << ['paragraph', _toHTML(txt)]
        viewsMap[controller][action].push(['widget': 'paragraph', 'attrs': [text: _toHTML(txt)]])
    }

    def linebreak(){
        //report << ['linebreak']
        viewsMap[controller][action].push(['widget': 'linebreak'])
    }

    def recommendation(String txt){
        //report << ['recommendation', _toHTML(txt)]
        viewsMap[controller][action].push(['widget': 'paragraph', 'attrs': [text: _toHTML('Recomendação: '+ txt)]])

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
        viewsMap[controller][action].push(['widget': 'tableReport', 'attrs': [header: headers, data: list]])
    }

    def map(String url){
        //report << ['map', url]
        viewsMap[controller][action].push(['widget': 'map', 'attrs': [map_url: url]])
    }

    def matrix(Map map){
        //report << ['matrix', map.x, map.y, map.labelX, map.labelY, map.rangeX, map.rangeY, map.quadrants, map.recomendations]
        viewsMap[controller][action].push(['widget': 'matrix', 'attrs': [x: map.x, y: map.y, label_x: map.label_x, label_y: map.label_y, range_x: map.range_x, range_y: map.range_y, quadrants: map.quadrants, recomendations: map.recomendations]])
    }

    def tabs(Map extAttrs = [:], Map widgets = [:], String evaluationObjectId){
        def attrs = [:]
        def tab_prefix = 'tab_'
        def uri = _k.toURI(evaluationObjectId)

        attrs['evalObjInstance']  = uri

        attrs['id'] = 'analysis'
        attrs['tabs'] = [:]
        attrs['tabpanels'] = [:]

        widgets.eachWithIndex{ it, int i ->
            attrs['tabs'][tab_prefix+i] = ['widget': 'tab', attrs: [id: tab_prefix+i, label: extAttrs.labels[tab_prefix+i]]]
            attrs['tabpanels'][tab_prefix+i] = it.value //[attrs: it.value.attrs]
        }

        attrs['tabs'][tab_prefix+'0'].attrs['widgetClass'] = 'active'
        attrs['tabs'][tab_prefix+(widgets.size()-1)].attrs['submitLabel'] = extAttrs['submitLabel']

        attrs['tabs'].eachWithIndex{ tab, int i ->
            if(i > 0 ){
                tab.value.attrs['previous'] = tab_prefix+(i-1)
                tab.value.attrs['previousLabel'] = extAttrs['previousLabel']
            }
            if(i < (attrs['tabs'].size()-1)){
                tab.value.attrs['next'] = tab_prefix+(i+1)
                tab.value.attrs['nextLabel'] = extAttrs['nextLabel']
            }
        }

        Uri.printTree(attrs)

        /*
        extAttrs['tabs'].eachWithIndex{ it, int i ->
            attrs['tabs'][tab_prefix+i] = ['widget': 'tab', attrs: [id: tab_prefix+i, label: it.label]]
            attrs['widgets'][tab_prefix+i] = ['widget': it.widget, attrs: [id: tab_prefix+i, label: it.label]]
        }
        */

        /*data.each{ tab ->
            tab.value.each { key, value ->
                attrs['widgets'][tab.key].attrs[key] = value
            }
        }*/


        //
        viewsMap[controller][action].push(['widget': 'tabs', 'attrs': attrs])

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

    def methodMissing(String key, attrs){
        //println "methodMissing"
        if(attrs.getClass() == Object[]){
            if(attrs.size()==1){
                if(attrs[0].getClass() == String)
                    props[key] = _toHTML(attrs[0])
                else
                    props[key] = attrs[0]
            }
            else
                props[key] = attrs
        }
    }

    def setData(String str, obj){
        _props[str]= obj
    }

    def requestData(String controllerName, String actionName){
        viewsMap[controllerName][actionName].each{ command ->
            if(command.request){
                command.request.each{ key, attrs ->
                    if(key!='widgets'){
                        command.attrs[key] = _k[attrs[1]].getLabelDescription(attrs[0].toString())
                    }
                    else if(key=='widgets'){
                        attrs.each{ subKey, subArgs ->
                            //command.attrs.widgets[subKey]['attrs']['data'] = getLabelDescription(subArgs[1], subArgs[0])
                            command.attrs.widgets[subKey]['attrs']['data'] = _k[subArgs[1]].getLabelDescription(subArgs[0].toString())
                        }
                    }
                }
            }
        }
    }

    def setView(String controllerName, String actionName){
        this.controller = controllerName
        this.action = actionName
    }

    static _toHTML(String txt) {_md.markdownToHtml(txt)}

    /*
       def title(String arg) {
           setData('title', arg)
       }

       def description(String arg){
           setData('description', _toHTML(arg))
           //def gui = _ctx.getBean('gui')
           //gui.viewsMap['tool']['index'].push(['widget': 'description', 'attrs': ['description': _toHTML(arg)]])

           //println  Processor.process(description, true)
           //println new PegDownProcessor().markdownToHtml(description)
       }


       def paragraph(String arg){
           //def gui = _ctx.getBean('gui')
           //gui.viewsMap['tool']['analysis'].push(['widget': 'paragraph', 'attrs': ['text': arg]])
       }

       def recommendation(Map map, String txt){
           recommendations << [map['if'],txt]
       }
   */

}
