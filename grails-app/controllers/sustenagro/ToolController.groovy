package sustenagro

import com.github.slugify.Slugify
import com.github.rjeschke.txtmark.Processor
import org.pegdown.PegDownProcessor
import rdfSlurper.RDFSlurper
import utils.Uri

import static rdfSlurper.RDFSlurper.N

class ToolController {
    def slp
    def dsl

    def index() {

        def inputs = []
        def query

        dsl.featureLst.each{
            def uri = '<'+slp.toURI(it[1])+'>'
            query = slp.query("$uri rdfs:label ?label. optional {$uri dc:description ?description}")
            if (query.empty) {
                query = slp.query("?id rdfs:label ?label. FILTER (STR(?label)='${it[1]}')", '')
                if (query.empty)
                    throw new RuntimeException('Unknown label: '+it[1])
                uri = "<${query[0].id}>"
                query = slp.query("$uri rdfs:label ?label. optional {$uri dc:description ?description}")
            }
            def lst = slp.query("?id ${it[0]} $uri ; rdfs:label ?name. optional {?id dc:description ?description}")
            inputs << [query[0].label, query[0].description, lst]
        }

        //println 'inp: '
        //inputs.each{
        //  println it
        //}
        //println Processor.process("This is ***TXTMARK***");
        //String html = new Markdown4jProcessor().process("This is a **bold** text");

        render(view: 'index',
               model:    [
                       name: dsl.name,
                       //description:   Processor.process(dsl.description),
                       description:   new PegDownProcessor().markdownToHtml(dsl.description),
                       microregions: inputs[0][2],
                       technologies: inputs[1][2],
                       production_unit_types: inputs[2][2],
                       production_units: inputs[3][2]])
    }

    def createProductionUnit() {
        Slugify slug = new Slugify()

        def production_unit_id = slug.slugify(params['production_unit_name'])

        slp.addNode(
            N(':'+production_unit_id,
                'rdf:type': slp.v(params['production_unit_type']),
                'rdfs:label': params['production_unit_name'],
                'dbp:Microregion': slp.v(params['production_unit_microregion']),
                //'sa:culture': slp.v(params['production_unit_culture']),
                ':AgriculturalEfficiency': slp.v(params['production_unit_technology'])
            ))

        slp.g.commit()
        //        g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
        redirect(action: 'assessment', id: production_unit_id)
    }

    def selectProductionUnit(){
        def production_unit_id = Uri.removeDomain(params['production_unit_id'], 'http://bio.icmc.usp.br/sustenagro#')
        redirect(action: 'assessment', id: production_unit_id)
    }

    def assessment() {

        def indicators = {cls ->
            slp.query('?a  rdfs:subClassOf '+cls+''' .
                   ?id rdfs:subClassOf ?a; rdfs:label ?title.
                   ?id rdfs:subClassOf ?y.
                   ?y  owl:onClass ?class.
                   ?class rdfs:subClassOf ?valueType.''')
        }

        def environmental_indicators = indicators(':EnvironmentalIndicator')
        def economic_indicators = indicators(':EconomicIndicator')
        def social_indicators = indicators(':SocialIndicator')

        def removeDomain = { lst ->
            lst.each{ ind ->
                ind['id_name'] = Uri.removeDomain(ind.id, 'http://bio.icmc.usp.br/sustenagro#')
            }
        }

        removeDomain(environmental_indicators)
        removeDomain(economic_indicators)
        removeDomain(social_indicators)

        def categorical = [:]
        def categ = { ind ->
            ind.each{
                slp.query("<$it.id> rdfs:subClassOf ?a. ?a owl:onClass ?id").each{
                   categorical[it.id] = []
                }
            }
        }

        categ(environmental_indicators)
        categ(economic_indicators)
        categ(social_indicators)

        categorical.each{ k, v ->
            slp.query("?id a <$k>; rdfs:label ?title.").each{
                v.push(it)
            }
        }

        String name = slp.":$params.id".'$rdfs:label'

        render(view: 'assessment',
               model: [sustenagro: 'http://bio.icmc.usp.br/sustenagro#',
                       production_unit_id: params.id,
                       production_unit_name: name,
                       environmental_indicators: environmental_indicators,
                       economic_indicators: economic_indicators,
                       social_indicators: social_indicators,
                       categorical: categorical ])
    }

    def assessmentReport() {
        def production_unit_id = params['production_unit_id']

        def indicators = []
        dsl.dimensions.each{
            indicators += slp.query("?a rdfs:subClassOf $it. ?id rdfs:subClassOf ?a.")
        }

        def indicators_names = []

        indicators.each{
            indicators_names.push(Uri.removeDomain(it.id, 'http://bio.icmc.usp.br/sustenagro#'))
        }

        def filled_ind = []

        indicators_names.each{ ind ->
            if(params[ind] != '' && params[ind] != null ){
                filled_ind.push(indicator: ind, value: params[ind])
            }
        }

        filled_ind.each{
            println it
        }

        redirect(action: 'assessment', id: params['production_unit_id'])
    }

}