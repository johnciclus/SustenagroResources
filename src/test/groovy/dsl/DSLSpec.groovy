package dsl

import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by dilvan on 5/30/15.
 */
class DSLSpec extends Specification {
    @Shared def dsl = new DSL('src/test/resources/testDsl.groovy')

    def setupSpec() {
//        //InputStream inp = this.class.getResourceAsStream('test.ntriple')
//        InputStream inp = new FileInputStream('src/test/resources/test.ntriple')
//        s.g.loadRDF(inp, 'http://tinkerpop.com#', 'n-triples', null)
//        s.addDefaultNamespaces()
//        s.addNamespace('', 'http://tinkerpop.com#')
    }

//    def "name description"(){
//        expect:
//        dsl.name == 'Avaliação da sustentabilidade em agricultura'
//        dsl.description == '''
//O processo de avaliação da sustentabilidade é composto pelas seguintes etapas:
// '''
//    }

    def "features"(){
        expect:
        dsl.featureLst == [['a', 'Microregion'],
                           ['a', ':AgriculturalEfficiency'],
                           ['rdfs:subClassOf', ':ProductionUnit']]
    }

    def "dimensions"(){
        expect:
        dsl.dimensions == [':EnvironmentalIndicator',
                           ':EconomicIndicator',
                           ':SocialIndicator']
    }

    def "program"(){
        when:
        dsl.indicator = [:]
        dsl.indicator['co2 emission'] = 1.0d
        dsl.indicator['soil'] = 1.0d
        dsl.indicator['landBurn'] = 1.0d
        dsl.indicator['road length'] = 1.0d
        dsl.indicator['distância'] = 1.0d
        dsl.indicator['landBurn'] = 1.0d


        dsl.program()

        then:
        dsl.soil == 1007.1
        dsl.indice == 1.0
        dsl.transport == 10.0
    }
}


