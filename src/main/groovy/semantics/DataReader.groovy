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

package semantics

/**
 * DataReader
 *
 * @author Dilvan Moreira.
 * @author John Garavito.
 */
class DataReader {

    Know k
    String id

    DataReader(k, id){
        this.k = k
        this.id = id
    }

    def findNode(String name){
        def uri = k.toURI(name)
        def res

        if(!uri){
            res = k[id].findURI(name)
            if( res.size()>0 )
                uri = res[0].uri
            else{
                throw new RuntimeException("Unknown value: $name")
            }
        }

        def classList = k[uri].getSuperClass()

        /*
        println name
        println uri
        println classList
        */

        if(classList.contains(k.toURI(':TechnologicalEfficiencyFeature'))){
            try{
                res = k[uri].getChildrenIndividuals(id, '?ind ?label ?valueTypeLabel ?value ?weightTypeLabel ?weight ?justification')
                //res = k[uri].getIndividualsFeatureValueWeight(id, '?ind ?label ?valueTypeLabel ?value ?weightType ?weightTypeLabel ?weight')
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(k.toURI('ui:Feature'))){
            try{
                res = k[uri].getGrandChildrenIndividuals(id, '?ind ?label ?valueTypeLabel ?value ?relevance ?justification')
                k[uri].getChildrenExtraIndividuals(id, '?ind ?name ?justification ?valueTypeLabel ?value ?relevance').each{
                    res.push([ind: it.ind, label: it.name, justification: it.justification, valueTypeLabel: it.valueTypeLabel, value: it.value, totalValue: it.value*it.relevance, relevance: it.relevance])
                }
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(k.toURI('dbp:Region'))){
            try {
                res = k[id].getMap('?map ?label')
            }
            catch (e) {
                res = [:]
            }
        }

        //println uri
        //println classList
        //println res

        return res
    }

    def propertyMissing(String name) {
        getAt(name)
    }

    def getAt(String name) {
        findNode(name)
    }
}