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
    def gui
    def model
    def widgets
    def dsl
    def tabs
    static md

    Analysis(String id, ApplicationContext applicationContext){
        _id = id
        _ctx = applicationContext
        k = _ctx.getBean('k')
        gui = _ctx.getBean('gui')
        md = _ctx.getBean('md')
        model = []
        widgets = []
        tabs = []
    }

    def paragraph(String arg){
        widgets << ['widget': 'paragraph', 'args': ['text': toHTML(arg)]]
    }

    def tabs(Map args = [:], String id, Closure closure = {}){
        def features = [:]
        def categories = [:]
        def technologyTypes = []
        def grandChildren
        //def args = [:]
        dsl = _ctx.getBean('dsl')

        /*
        dsl.dimensionsMap.each{ feature ->
            features[feature.key] = ['subClass': [:]]
            grandChildren = k[feature.key].getGrandchildren('?id ?label ?subClass ?category ?valueType ?weight')
            k[feature.key].getSubClass('?label').each{ subClass ->
                features[feature.key]['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
                grandChildren.each{
                    if(it.subClass == subClass.subClass) {
                        features[feature.key]['subClass'][subClass.subClass]['subClass'][it.id] = it
                    }
                }
            }
            categories += grandChildren.categoryList()
        }

        dsl.featureMap.each{ feature ->
            features[feature.key] = ['subClass': [:]]
            grandChildren = k[feature.key].getGrandchildren('?id ?label ?subClass ?category ?valueType')
            k[feature.key].getSubClass('?label').each{ subClass ->
                features[feature.key]['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
                grandChildren.each{
                    if(it.subClass == subClass.subClass) {
                        features[feature.key]['subClass'][subClass.subClass]['subClass'][it.id] = it
                    }
                }
            }
            categories += grandChildren.categoryList()
        }

        categories[k.toURI(':ProductionEnvironmentAlignmentCategory')] = []
        categories[k.toURI(':SugarcaneProcessingOptimizationCategory')] = []

        def method = 'getIndividualsIdLabel'

        categories.each { key, v ->
            k[key]."${method}"().each{            //getIndividualsIdLabel().each {
                v.push(it)
            }
        }

        /*
        println "* Tree *"
        Uri.printTree(features)

        println "Categories"
        categories.each{ category ->
            println category.key
            category.value.each{
                println "\t "+it
            }
        }
        */


        def request = []
        args['id'] = id
        args['widgets'] = [:]
        closure()

        tabs.eachWithIndex{ tab, index ->
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

        model << [  id: _id,
                    features: features,
                    categories: categories,
                    technologyTypes: technologyTypes]

        widgets << [widget: 'tabs',
                    request: request,
                    args: args ]

        //<input type="hidden" name="production_unit_id" value="${evaluationObject.id}">
        //<g:render template="/widgets/tabs" model="${model}" />

    }

    def tab(Map args = [:], String id='', Closure closure = {}){
        args['id'] = id

        tabs << [id: id, widget: [widget: 'tab',
                                 request: [],
                                 args: args]]
    }

    static toHTML(String txt) {md.markdownToHtml(txt)}

}
