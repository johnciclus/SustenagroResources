package dsl

import org.springframework.context.ApplicationContext
/**
 * Created by john on 27/02/16.
 */
class Feature {
    private def _uri
    private def _ctx
    private def _k
    private def _model = [:]

    Feature(String id, ApplicationContext applicationContext){
        def grandChildren
        def category
        _ctx = applicationContext
        _k = _ctx.getBean('k')
        _uri = _k.toURI(id)

        _model = ['label': _k[_uri].label, 'subClass': [:]]
        grandChildren = _k[_uri].getGrandchildren('?id ?label ?subClass ?relevance ?category ?weight')

        _k[_uri].getSubClass('?label').each{ subClass ->
            _model['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
            grandChildren.each{
                if(it.subClass == subClass.subClass) {
                    _model['subClass'][subClass.subClass]['subClass'][it.id] = it
                    _model['subClass'][subClass.subClass]['subClass'][it.id]['valueTypes'] = _k[it.category].getSuperClass()
                    _model['subClass'][subClass.subClass]['subClass'][it.id]['categoryIndividuals'] = _k[it.category].getIndividualsIdLabel()
                    if(it.weight){
                        _model['subClass'][subClass.subClass]['subClass'][it.id]['weightId'] = it.id + '-' + it.weight.substring(it.weight.lastIndexOf('#')+1)
                        _model['subClass'][subClass.subClass]['subClass'][it.id]['weightIndividuals'] = _k[it.weight].getIndividualsIdLabel()
                    }
                }
            }
        }
        //def categoriesTmp = grandChildren.categoryList()
        //def method = 'getIndividualsIdLabel'

        //categoriesTmp.each{ key, value ->
        //    categories[key] = _k[key]."${method}"()
        //}

        //categories[_k.toURI(':ProductionEnvironmentAlignmentCategory')] = []
        //categories[_k.toURI(':SugarcaneProcessingOptimizationCategory')] = []

        /*
        categories += grandChildren.categoryList()

        categories[_k.toURI(':ProductionEnvironmentAlignmentCategory')] = []
        categories[_k.toURI(':SugarcaneProcessingOptimizationCategory')] = []

        def method = 'getIndividualsIdLabel'

        categories.each { key, value ->
            value = _k[key]."${method}"().each{            //getIndividualsIdLabel().each {
                value.push(it)
            }
        }
        */
        //def attrs = [:]
        /*
        dsl.dimensionsMap.each{ feature ->
            _model[feature.key] = ['subClass': [:]]
            grandChildren = _k[feature.key].getGrandchildren('?id ?label ?subClass ?category ?valueType ?weight')
            _k[feature.key].getSubClass('?label').each{ subClass ->
                _model[feature.key]['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
                grandChildren.each{
                    if(it.subClass == subClass.subClass) {
                        _model[feature.key]['subClass'][subClass.subClass]['subClass'][it.id] = it
                    }
                }
            }
            categories += grandChildren.categoryList()
        }

        dsl.featureMap.each{ feature ->
            _model[feature.key] = ['subClass': [:]]
            grandChildren = _k[feature.key].getGrandchildren('?id ?label ?subClass ?category ?valueType')
            _k[feature.key].getSubClass('?label').each{ subClass ->
                _model[feature.key]['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
                grandChildren.each{
                    if(it.subClass == subClass.subClass) {
                        _model[feature.key]['subClass'][subClass.subClass]['subClass'][it.id] = it
                    }
                }
            }
            categories += grandChildren.categoryList()
        }

        categories[_k.toURI(':ProductionEnvironmentAlignmentCategory')] = []
        categories[_k.toURI(':SugarcaneProcessingOptimizationCategory')] = []

        def method = 'getIndividualsIdLabel'

        categories.each { key, v ->
            _k[key]."${method}"().each{            //getIndividualsIdLabel().each {
                v.push(it)
            }
        }

        /*
        println "* Tree *"
        Uri.printTree(_model)

        println "Categories"
        categories.each{ category ->
            println category.key
            category.value.each{
                println "\t "+it
            }
        }

        features: _model,
        categories: categories,
        technologyTypes: technologyTypes
        */

    }
    def getUri(){
        return _uri
    }

    def getModel(){
        return _model
    }

    def conditional(String arg, String type, Closure closure = {}){
        //_model << [conditional: [objectType: _k.toURI(arg), isType: _k.toURI(type), then: closure()]]
    }

    def include(String arg){
        [include: _k.toURI(arg)]
    }

    def exclude(String arg){

    }

    def evalObject(String arg){
        /*
        def asserts = []
        _model.each{
            def types = _k[_k.toURI(arg)].getType()
            if(it['conditional'])
                if(types.contains(it['conditional']['objectType']))
                    if(types.contains(it['conditional']['isType']))
                        if(it['conditional']['then']){
                            asserts << it['conditional']['then']['include']
                        }
        }
        return asserts
        */
    }

    def getIndividuals(){
        def individuals = [:]
        _model.subClass.each{ subClassKey, subClass ->
            subClass.subClass.each{
                individuals[it.key] = it.value
            }
        }
        return individuals
    }
    def getIndividualKeys(){
        def individuals = []
        _model.subClass.each{ subClassKey, subClass ->
            subClass.subClass.each{
                individuals.push(it.key)
            }
        }
        return individuals
    }

    def getWeightedIndividualKeys(){
        def individuals = []
        _model.subClass.each{ subClassKey, subClass ->
            subClass.subClass.each{
                if(it.value.weightId)
                    individuals.push(it.value.weightId)
            }
        }
        return individuals
    }
}
