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

package utils

/**
 * Uri
 *
 * @author Dilvan Moreira.
 * @author John Garavito.
 */
class Uri {
    def static simpleDomain(ArrayList list, String dom, String prefix=":"){
        list.each{ el ->
            el.each{
                if(it.value instanceof String)
                    it.value = it.value.replace(dom,prefix)
            }
        }
        return list
    }

    def static fullDomain(ArrayList list, String dom){
        list.each{ el ->
            el.each{
                it.value = it.value.replace(":", dom)
            }
        }
        return list
    }

    def static printTree(Object object, int level=-1){
        if(object.getClass() == LinkedHashMap){
            level++
            object.each{
                print "\t"*level + it.key + " : "
                if((it.value.getClass() != LinkedHashMap) && (it.value.getClass() != ArrayList)) {
                    println it.value
                }
                else{
                    println ""
                    printTree(it.value, level)
                }
            }
        }
        else if(object.getClass() == ArrayList){
            level++
            object.eachWithIndex{ it, index ->
                print "\t"*level + '['+index+"] : "
                if((it.getClass() != LinkedHashMap) && (it.getClass() != ArrayList)) {
                    println it
                }
                else{
                    println ""
                    printTree(it, level)
                }
            }
        }
    }
}
