package dsl

import org.springframework.context.ApplicationContext
/**
 * Created by john on 27/02/16.
 */
class Feature {
    def _id
    def _ctx
    def k
    def gui
    def model = []
    def features = [:]
    def categories = [:]
    def technologyTypes = []

    Feature(String id, ApplicationContext applicationContext){
        _id = id
        _ctx = applicationContext
        k = _ctx.getBean('k')
        gui = _ctx.getBean('gui')

        def uri = k.toURI(id)
        def grandChildren

        features[uri] = ['subClass': [:]]
        grandChildren = k[uri].getGrandchildren('?id ?label ?subClass ?category ?valueType')
        k[uri].getSubClass('?label').each{ subClass ->
            features[uri]['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
            grandChildren.each{
                if(it.subClass == subClass.subClass) {
                    features[uri]['subClass'][subClass.subClass]['subClass'][it.id] = it
                }
            }
        }
        categories += grandChildren.categoryList()

        //def args = [:]
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

        features: features,
        categories: categories,
        technologyTypes: technologyTypes
        */

    }

    def conditional(String arg, String type, Closure closure = {}){
        model << [conditional: [objectType: k.toURI(arg), isType: k.toURI(type), then: closure()]]
    }

    def include(String arg){
        [include: k.toURI(arg)]
    }

    def exclude(String arg){

    }

    def evalObject(String arg){
        def asserts = []
        model.each{
            def types = k[k.toURI(arg)].getType()
            if(it['conditional'])
                if(types.contains(it['conditional']['objectType']))
                    if(types.contains(it['conditional']['isType']))
                        if(it['conditional']['then']){
                            asserts << it['conditional']['then']['include']
                        }
        }
        return asserts
    }

    def updateCategories(){
        categories[k.toURI(':ProductionEnvironmentAlignmentCategory')] = []
        categories[k.toURI(':SugarcaneProcessingOptimizationCategory')] = []

        def method = 'getIndividualsIdLabel'

        categories.each { key, v ->
            k[key]."${method}"().each{            //getIndividualsIdLabel().each {
                v.push(it)
            }
        }
    }
}
