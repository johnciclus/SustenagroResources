/*
 * Copyright (c) 2015-2016 Dilvan Moreira.
 * Copyright (c) 2015-2016 John Garavito.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package dsl

import org.springframework.context.ApplicationContext
import utils.Uri

/**
 * Feature
 *
 * @author Dilvan Moreira.
 * @author John Garavito.
 */
class Feature {
    private def _uri
    private def _name
    private def _ctx
    private def _k
    private def _model = [:]
    private def _conditional = []
    private def _attrs

    Feature(String id, Map attrs, ApplicationContext applicationContext){
        def grandChildren
        _ctx  = applicationContext
        _k    = _ctx.getBean('k')
        _uri  = _k.toURI(id)
        _name = _uri.substring(_uri.lastIndexOf('#')+1)
        _attrs = attrs

        _model = [label: _k[_uri].label, subClass: [:], superClass: _k[_uri].getSuperClass()]
        grandChildren = _k[_uri].getGrandchildren('?id ?label ?subClass ?relevance ?category ?weight ?weightLabel')

        _k[_uri].getSubClass('?label').each{ subClass ->
            _model['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
            grandChildren.each{
                if(it.subClass == subClass.subClass) {
                    _model['subClass'][subClass.subClass]['subClass'][it.id] = it
                    _model['subClass'][subClass.subClass]['subClass'][it.id]['valueTypes'] = _k[it.id].getCollectionIndividualsTypes()
                    _model['subClass'][subClass.subClass]['subClass'][it.id]['categoryIndividuals'] = _k[it.id].getCollectionIndividuals().capitalizeLabels()

                    if(it.weight){
                        _model['subClass'][subClass.subClass]['subClass'][it.id]['weightIndividuals'] = _k[it.id].getWeightIndividuals().capitalizeLabels()
                    }
                }
            }
        }
    }

    def reload(){
        def grandChildren
        _model = [:]
        _model = [label: _k[_uri].label, subClass: [:], superClass: _k[_uri].getSuperClass()]
        grandChildren = _k[_uri].getGrandchildren('?id ?label ?subClass ?relevance ?category ?weight ?weightLabel')

        _k[_uri].getSubClass('?label').each{ subClass ->
            _model['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
            grandChildren.each{
                if(it.subClass == subClass.subClass) {
                    _model['subClass'][subClass.subClass]['subClass'][it.id] = it
                    _model['subClass'][subClass.subClass]['subClass'][it.id]['valueTypes'] = _k[it.id].getCollectionIndividualsTypes()
                    _model['subClass'][subClass.subClass]['subClass'][it.id]['categoryIndividuals'] = _k[it.id].getCollectionIndividuals().capitalizeLabels()

                    if(it.weight){
                        _model['subClass'][subClass.subClass]['subClass'][it.id]['weightIndividuals'] = _k[it.id].getWeightIndividuals().capitalizeLabels()
                    }
                }
            }
        }
    }

    def getUri(){
        return _uri
    }

    def getName(){
        return _name
    }

    def getModel(String evalObjId = null){
        if(evalObjId && _conditional.size() > 0){
            def uri = _k.toURI('inds:'+evalObjId)
            def model = [:]
            def evalObjTypes

            _model.each{ key, value ->
                if(key != 'subClass')
                    model[key] = value
            }
            model['subClass'] = [:]

            _conditional.each{
                evalObjTypes = _k[uri].getType()
                if(evalObjTypes.contains(it.objectType)){
                    if(evalObjTypes.contains(it.isType)){
                        def includes = it.then.include
                        if(includes.getClass() == ArrayList){
                            includes.each{ include ->
                                model['subClass'][include] = _model['subClass'][include]
                            }
                        }
                        else{
                            model['subClass'][includes] = _model['subClass'][includes]
                        }
                    }
                }
            }
            return model
        }
        else
            return _model


    }

    def getAttrs(){
        return _attrs
    }

    def conditional(String arg, String type, Closure closure = {}){
        _conditional << [objectType: _k.toURI(arg), isType: _k.toURI(type), then: closure()]
    }

    def include(String arg){
        [include: _k.toURI(arg)]
    }

    def include(String[] args){
        def includes = []
        args.each{
            includes.push(_k.toURI(it))
        }
        [include: includes]
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

        //Uri.printTree(_conditional)

        _model.subClass.each{ subClassKey, subClass ->
            subClass.subClass.each{
                individuals[it.key] = it.value
            }
        }
        return individuals
    }

    def getValueIndividuals(){
        def individuals = [:]
        _model.subClass.each{ subClassKey, subClass ->
            subClass.subClass.each{ indKey, indValue ->
                if(indValue.categoryIndividuals) {
                    indValue.categoryIndividuals.each {
                        individuals[it.id] = it
                    }
                }
            }
        }
        return individuals
    }

    def getWeightIndividuals(){
        def individuals = [:]
        _model.subClass.each{ subClassKey, subClass ->
            subClass.subClass.each{ indKey, indValue ->
                if(indValue.weightIndividuals){
                    indValue.weightIndividuals.each{
                        individuals[it.id] = it
                    }
                }
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
