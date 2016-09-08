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

/**
 * EvaluationObject
 *
 * @author Dilvan Moreira.
 * @author John Garavito.
 */
class EvaluationObject {
    def _id
    def _ctx
    def model
    def widgets
    def k
    def gui

    EvaluationObject(String id, ApplicationContext applicationContext){
        _id = id
        _ctx = applicationContext
        k = _ctx.getBean('k')
        gui = _ctx.getBean('gui')
        model = []
        widgets = []
    }

    def instance(Map attrs = [:], String id){
        def uri = k.toURI(id)
        def range = (id != 'rdfs:subClassOf')? k[uri].range : _id
        def dataType = (range)? range : k.toURI('xsd:string')
        def widget = (attrs['widget'])? attrs['widget'] : gui['dataTypeToWidget'].find { k.toURI(it.key) == dataType }.value
        def request = []
        def prop

        attrs['id'] = uri
        attrs['required'] = (attrs['required'])? attrs['required'] : (k[_id].getRestriction(uri).size > 0)

        if(id == 'rdfs:subClassOf'){
            prop = id
            request = [prop, dataType]
            attrs['required'] = true
        }
        else if(k[uri].type.contains(k.toURI('owl:ObjectProperty'))){
            prop = 'rdf:type'
            request = [prop, dataType]
        }

        widget = (widget)? widget : 'textForm'

        if(!k[uri].isFunctional() && attrs['multipleSelection'])
            widget = 'multipleCategoryForm'

        if(widget == 'categoryForm')
            attrs['selectType'] = (attrs['multipleSelection'])? 'checkbox' : 'radio'

        model << [id: uri,
                  range: range,
                  dataType: dataType,
                  prop: prop,
                  attrs: attrs]

        widgets << [ id: uri,
                     widget: widget,
                     request: request,
                     attrs: attrs]
    }

    def reload(){
        /*def widgetsTmp = widgets
        widgets = []

        println 'widgets'
        widgets.each{
            println it
        }

        println 'widgetsTmp'
        widgetsTmp.each{
            println it
        }*/
    }

    def type(Map attrs = [:], String id='rdfs:subClassOf'){
        instance(attrs, id)
    }

    def getURI(){
        return k.toURI(_id)
    }

    def getWidgets(Locale locale){
        def widgetsTmp = []
        def widgetTmp
        def i18nParams = ['label', 'header', 'placeholder']

        widgets.each{ widget ->
            widgetTmp = [:]
            widget.each{ key, value ->
                if(key != 'attrs')
                    widgetTmp[key] = value
                else{
                    widgetTmp['attrs'] = [:]

                    value.each{
                        if(i18nParams.contains(it.key) && it.value.getClass() == LinkedHashMap){
                            widgetTmp['attrs'][it.key] = it.value[locale.language]
                        }
                        else {
                            widgetTmp['attrs'][it.key] = it.value
                        }
                    }
                }
            }
            widgetsTmp.push(widgetTmp)
        }

        return widgetsTmp
    }
}
