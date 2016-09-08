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

import spock.lang.Shared
import spock.lang.Specification

//class DSLSpec extends Specification {
//   // @Shared def dsl = new DSL('src/main/groovy/dsl/Dsl.groovy')
//
//    def setup() {
////        InputStream inp = this.class.getResourceAsStream('test.ntriple')
////        InputStream inp = new FileInputStream('src/test/resources/test.ntriple')
////        s.g.loadRDF(inp, 'http://tinkerpop.com#', 'n-triples', null)
////        s.addDefaultNamespaces()
////        s.addNamespace('', 'http://tinkerpop.com#')
//    }
//
////    def "name description"(){
////        expect:
////        dsl.name == 'Avaliação da sustentabilidade em agricultura'
////        dsl.description == '''
////          O processo de avaliação da sustentabilidade é composto pelas seguintes etapas:
////        '''
////    }
//
//    def "features"(){
//        expect:
//        dsl.featureLst == [['a', 'Microregion'],
//                           ['a', ':AgriculturalEfficiency'],
//                           ['rdfs:subClassOf', ':ProductionUnit']]
//    }
//
//    def "dimensions"(){
//        expect:
//        dsl.dimensions == [':EnvironmentalIndicator',
//                           ':EconomicIndicator',
//                           ':SocialIndicator']
//    }
//
//    def "program"(){
//        when:
//        dsl.indicator = [:]
//        dsl.indicator['co2 emission'] = 1.0d
//        dsl.indicator['soil'] = 1.0d
//        dsl.indicator['landBurn'] = 1.0d
//        dsl.indicator['road length'] = 1.0d
//        dsl.indicator['distância'] = 1.0d
//        dsl.indicator['landBurn'] = 1.0d
//
//
//        dsl.program()
//
//        then:
//        dsl.soil == 1007.1
//        dsl.indice == 1.0
//        dsl.transport == 10.0
//    }
//}
//
//
