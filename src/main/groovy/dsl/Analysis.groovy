package dsl

import org.springframework.context.ApplicationContext
import utils.Uri

/**
 * Created by john on 27/02/16.
 */
class Analysis {
    def _id
    def _ctx
    def k
    //def gui
    def model
    def widgets
    def tabs
    def ref
    def dsl

    static md

    Analysis(String id, ApplicationContext applicationContext){
        _id = id
        _ctx = applicationContext
        k = _ctx.getBean('k')
        //gui = _ctx.getBean('gui')
        md = _ctx.getBean('md')
        model = []
        widgets = []
        tabs = []
        ref = widgets
    }

    def paragraph(String arg){
        println 'Paragraph test'
        widgets << ['widget': 'paragraph', 'args': ['text': toHTML(arg)]]
        println widgets
    }

    def tabs(Map args, String id, Closure closure = {}){
        def request = []
        //dsl = _ctx.getBean('dsl')
        args['id'] = id
        args['widgets'] = [:]

        println id
        println closure.owner
        println closure.delegate
        println closure.thisObject
        println ''

        def refTmp = ref
        ref = args['widgets']
        closure()               //closure(args['widgets'])
        println args
        ref = refTmp



        /*
        tabs.each{ tab ->
            args['widgets'][tab.id] = tab.widget
        }

        for(int i=0; i < tabs.size(); i++){
            if(i == 0 ){
                args['widgets'][tabs[i].id].args['next'] = tabs[i+1].id
                args['widgets'][tabs[i].id].args['nextLabel'] = args['nextLabel']
            }
            else if(i == (tabs.size()-1)){
                args['widgets'][tabs[i].id].args['previous'] = tabs[i-1].id
                args['widgets'][tabs[i].id].args['previousLabel'] = args['previousLabel']
            }
            else{
                args['widgets'][tabs[i].id].args['previous'] = tabs[i-1].id
                args['widgets'][tabs[i].id].args['next'] = tabs[i+1].id
                args['widgets'][tabs[i].id].args['previousLabel'] = args['previousLabel']
                args['widgets'][tabs[i].id].args['nextLabel'] = args['nextLabel']
            }
        }

        model << [  id: _id]
        */

        widgets <<   [widget: 'tabs',
                      request: request,
                      args: args ]

        //<input type="hidden" name="production_unit_id" value="${evaluationObject.id}">
        //<g:render template="/widgets/tabs" model="${model}" />
    }

    def tabs(String id, Closure closure = {}){
        tabs([:], id, closure)
    }

    def tab(Map args = [:], String id='', Closure closure = {}){
        args['id'] = id
        args['widgets'] = [:]

        println id
        println closure.owner
        println closure.delegate
        println closure.thisObject
        println ''

        def refTmp = ref
        ref = args['widgets']
        closure()
        println args
        ref = refTmp


        /*
        widgets.eachWithIndex{ widget, index ->
            args['widgets'][widget.id] = widget.widget
        }
        */

        ref[id] = [widget: 'tab',
                   request: [],
                   args: args]

        /*tabs <<  [id: id, widget:  [widget: 'tab',
                            request: [],
                            args: args ]]
        */
}

def indicatorList(String id){
    println "Indicator list "
    ref[id] = [widget: 'paragraph',
               request: [],
               args: [text: "Indicator list "]]
}

static toHTML(String txt) {md.markdownToHtml(txt)}

}
