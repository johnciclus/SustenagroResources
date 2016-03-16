package dsl

import org.springframework.context.ApplicationContext
/**
 * Created by john on 27/02/16.
 */
class Feature {
    def _id
    def _ctx
    def k
    def model = []
    def features = [:]
    def categories = [:]

    Feature(String id, ApplicationContext applicationContext){
        _id = id
        _ctx = applicationContext
        k = _ctx.getBean('k')

        def uri = k.toURI(id)
        def grandChildren

        features[uri] = ['label': k[uri].label, 'subClass': [:]]
        grandChildren = k[uri].getGrandchildren('?id ?label ?subClass ?category ?valueType')            //'?id ?label ?subClass ?category ?valueType ?weight'
        k[uri].getSubClass('?label').each{ subClass ->
            features[uri]['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
            grandChildren.each{
                if(it.subClass == subClass.subClass) {
                    features[uri]['subClass'][subClass.subClass]['subClass'][it.id] = it
                }
            }
        }
        def categoriesTmp = grandChildren.categoryList()
        def method = 'getIndividualsIdLabel'

        categoriesTmp.each{ key, value ->
            categories[key] = k[key]."${method}"()
        }

        //categories[k.toURI(':ProductionEnvironmentAlignmentCategory')] = []
        //categories[k.toURI(':SugarcaneProcessingOptimizationCategory')] = []

        /*
        categories += grandChildren.categoryList()

        categories[k.toURI(':ProductionEnvironmentAlignmentCategory')] = []
        categories[k.toURI(':SugarcaneProcessingOptimizationCategory')] = []

        def method = 'getIndividualsIdLabel'

        categories.each { key, value ->
            value = k[key]."${method}"().each{            //getIndividualsIdLabel().each {
                value.push(it)
            }
        }
        */
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

    def getIndividuals(){
        def individuals = [:]
        features.each{ key, feautre ->
            feautre.subClass.each{ subClassKey, subClass ->
                subClass.subClass.each{
                    individuals[it.key] = it.value
                }
            }
        }
        return individuals
    }
    def getIndividualKeys(){
        def individuals = []
        features.each{ key, feature ->
            feature.subClass.each{ subClassKey, subClass ->
                subClass.subClass.each{
                    individuals.push(it.key)
                }
            }
        }
        return individuals
    }
}
