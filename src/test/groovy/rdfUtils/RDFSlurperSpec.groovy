package rdfUtils

import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by dilvan on 5/30/15.
 */
class RDFSlurperSpec extends Specification {
    @Shared def s = new RDFSlurper()

    def setupSpec() {
        //InputStream inp = this.class.getResourceAsStream('test.ntriple')
        InputStream inp = new FileInputStream('src/test/resources/test.ntriple')
        s.g.loadRDF(inp, 'http://tinkerpop.com#', 'n-triples', null)
        s.addDefaultNamespaces()
        s.addNamespace('', 'http://tinkerpop.com#')
    }

    def "name spaces"(){
        when:
        def pref =
                'PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n'+
                'PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n'+
                'PREFIX owl: <http://www.w3.org/2002/07/owl#>\n'+
                'PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n'+
                'PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n'+
                'PREFIX dc: <http://purl.org/dc/terms/>\n' +
                'PREFIX : <http://tinkerpop.com#>\n'
//        def pref2 =
//                'PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n' +
//                'PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n' +
//                'PREFIX owl: <http://www.w3.org/2002/07/owl#>\n' +
//                'PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n' +
//                'PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n' +
//                'PREFIX : <http://tinkerpop.com#>'
        then:
        s.prefixes == pref
    }

    def "reading values"(){
        expect:
        s.'1'.node().id.find{true} == 'http://tinkerpop.com#1'
        s.'1'.node().kind.find{true} == 'uri'
        s.':1'.name.node().id.find{true} == '"marko"^^<http://www.w3.org/2001/XMLSchema#string>'
        s.':1'.name.node().kind.find{true} == 'literal'
        s.':1'.name.node().type.find{true} == 'http://www.w3.org/2001/XMLSchema#string'
        s.':1'.name.node().value.find{true} == 'marko'

        s.'1'.knows.$name == 'vadas'
        s.'1'.Knows.$age == 27

        s.':1'.Knows.':name'.collect().find{true} == 'vadas'
        s.'1'.knows.name.collect().find{true} == 'vadas'
        s.'1'.knows.name.toList()[0] == 'vadas'
    }

    def "assigning using <<"(){
        when:
        s.'1'.knows.name << 'Kolajj'
        s.g.commit()

        then:
        s.'1'.knows.$name == 'Kolajj'
        s.'1'.knows.name.collect().find{true} == 'Kolajj'
        s.':1'.knows.name.node().lang.find{true} == 'en'
    }

    def "assignment using []"(){
        when:
        s.'1'.knows[':bank'] =
                RDFSlurper.N('bank1',
                   name:    'Barclays',
                   country: RDFSlurper.N(name:    'United Kingdom',
                              language:'English'))
        s.g.commit()

        then:
        s.'1'.knows.bank.$name == 'Barclays'
        s.'1'.knows.bank.country.$name == 'United Kingdom'
        s.'1'.knows.bank.country.$language == 'English'
    }

    def "assignment using complex graph"(){
        when:
        s.'1'.knows[':car'] =
                ['car1',
                        ['rdfs:label':    'Eclipse',
                         maker: [name:    'Mitsubishi',
                                 country: 'Japan']]]
        s.g.commit()

        then:
        s.'1'.knows.car.'$rdfs:label' == 'Eclipse'
        s.'1'.knows.car.maker.$name == 'Mitsubishi'
        s.'1'.knows.car.maker.country.collect().find{true} == 'Japan'
    }

    def "assignment using complex graph using <<"(){
        when:
        s.'1'.knows.car <<
                ['car2',
                    ['rdfs:label':    'Voyage',
                    maker: [name:    'Volkswagen',
                           country: 'Brazil']]]
        s.g.commit()

        then:
        s.'1'.knows.car.'$rdfs:label' == 'Voyage'
        s.'1'.knows.car.maker.$name == 'Volkswagen'
        s.'1'.knows.car.maker.country.collect().find{true} == 'Brazil'
    }

//    def "read using node names"(){
//        when:
//        s.'1'[':hasValue'] =
//                ['val3',
//                 ['rdfs:label':    'Variable',
//                  'dc:hasPart': ['rdf:type':    ':CO2Emission',
//                                 ':value': [':dataValue': 5.0]]]]
//        s.'1'[':hasClass'] = [':CO2Emission', ['rdfs:label': 'CO2 Emission']]
//        s.g.commit()
//        def d = new DataReader(s, s.toURI(':car3'))
//
//        then:
//        d.':CO2Emission' == 5.0
//        d.'CO2 Emission' == 5.0
//    }
}


