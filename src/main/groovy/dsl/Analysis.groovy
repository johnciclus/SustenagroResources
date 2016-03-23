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

    def text(String arg){
        //println 'Paragraph test'
        widgets << ['widget': 'text', 'attrs': ['text': toHTML(arg)]]
        //println widgets
    }

    def tabs(Map attrs, String id, Closure closure = {}){
        def request = []
        //dsl = _ctx.getBean('dsl')
        attrs['id'] = id
        attrs['widgets'] = [:]

        println id
        println closure.owner
        println closure.delegate
        println closure.thisObject
        println ''

        def refTmp = ref
        ref = attrs['widgets']
        closure()               //closure(attrs['widgets'])
        println attrs
        ref = refTmp



        /*
        tabs.each{ tab ->
            attrs['widgets'][tab.id] = tab.widget
        }

        for(int i=0; i < tabs.size(); i++){
            if(i == 0 ){
                attrs['widgets'][tabs[i].id].attrs['next'] = tabs[i+1].id
                attrs['widgets'][tabs[i].id].attrs['nextLabel'] = attrs['nextLabel']
            }
            else if(i == (tabs.size()-1)){
                attrs['widgets'][tabs[i].id].attrs['previous'] = tabs[i-1].id
                attrs['widgets'][tabs[i].id].attrs['previousLabel'] = attrs['previousLabel']
            }
            else{
                attrs['widgets'][tabs[i].id].attrs['previous'] = tabs[i-1].id
                attrs['widgets'][tabs[i].id].attrs['next'] = tabs[i+1].id
                attrs['widgets'][tabs[i].id].attrs['previousLabel'] = attrs['previousLabel']
                attrs['widgets'][tabs[i].id].attrs['nextLabel'] = attrs['nextLabel']
            }
        }

        model << [  id: _id]
        */

        widgets <<   [widget: 'tabs',
                      request: request,
                      attrs: attrs ]

        //<input type="hidden" name="production_unit_id" value="${evaluationObject.id}">
        //<g:render template="/widgets/tabs" model="${model}" />
    }

    def tabs(String id, Closure closure = {}){
        tabs([:], id, closure)
    }

    def tab(Map attrs = [:], String id='', Closure closure = {}){
        attrs['id'] = id
        attrs['widgets'] = [:]

        println id
        println closure.owner
        println closure.delegate
        println closure.thisObject
        println ''

        def refTmp = ref
        ref = attrs['widgets']
        closure()
        println attrs
        ref = refTmp


        /*
        widgets.eachWithIndex{ widget, index ->
            attrs['widgets'][widget.id] = widget.widget
        }
        */

        ref[id] = [widget: 'tab',
                   request: [],
                   attrs: attrs]

        /*tabs <<  [id: id, widget:  [widget: 'tab',
                            request: [],
                            attrs: attrs ]]
        */
}

def indicatorList(String id){
    println "Indicator list "
    ref[id] = [widget: 'text',
               request: [],
               attrs: [text: "Indicator list "]]
}

static toHTML(String txt) {md.markdownToHtml(txt)}

}
