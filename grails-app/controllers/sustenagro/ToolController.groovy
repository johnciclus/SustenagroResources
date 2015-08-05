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
            def lst = slp.query("?id ${it[0]} $uri ; rdfs:label ?label. optional {?id dc:description ?description}")
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
        def production_unit_alias = Uri.removeDomain(params['production_unit_id'], 'http://bio.icmc.usp.br/sustenagro#')
        def evaluation_alias = Uri.removeDomain(params['evaluation'], 'http://bio.icmc.usp.br/sustenagro#')

        def indicators = slp.query("?id :compose :$evaluation_alias. ?id :value ?value")

        //println "indicators"
        //println indicators

        redirect(   action: 'assessment',
                    id: production_unit_alias,
                    model:    [indicators: indicators])
    }

    def selectEvaluations(){
        def production_unit_id = Uri.removeDomain(params['production_unit_id'], 'http://bio.icmc.usp.br/sustenagro#')
        def evaluations = slp.query("?id :appliedTo :$production_unit_id. ?id rdfs:label ?label")

        render( template: 'list_evaluations',
                model:    [evaluations: evaluations]);
    }

    def assessment() {

        def indicators = {
            slp.select('?id ?title ?class ?valueType')
               .query( '?a  rdfs:subClassOf '+it+''' .
                        ?id rdfs:subClassOf ?a; rdfs:label ?title.
                        ?id rdfs:subClassOf ?y.
                        ?y  owl:onClass ?class.
                        ?class rdfs:subClassOf ?valueType.''')
        }

        def categorical = [:]

        def categ = {
            it.each{
                slp.query("<$it.id> rdfs:subClassOf ?a. ?a owl:onClass ?id").each{
                    categorical[it.id] = []
                }
            }
        }

        def reduceURI = {
            it.each{
                //ind['id_name'] = Uri.removeDomain(ind.id, 'http://bio.icmc.usp.br/sustenagro#')
                //ind['id_name'] = URLEncoder.encode(ind.id)
                //ind['id_name'] = ind.id
                //it.id = slp.fromURI(it.id)
                //println 'ind1- '+ slp.fromURI(ind.id)
            }
        }

        def environmental_indicators = indicators(':EnvironmentalIndicator')
        def economic_indicators = indicators(':EconomicIndicator')
        def social_indicators = indicators(':SocialIndicator')

        categ(environmental_indicators)
        categ(economic_indicators)
        categ(social_indicators)

        reduceURI(environmental_indicators)
        reduceURI(economic_indicators)
        reduceURI(social_indicators)

        categorical.each{ k, v ->
            slp.query("?id a <$k>; rdfs:label ?title.").each{
                //it.id = slp.fromURI(it.id)
                v.push(it)
            }
        }

        //println categorical

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

        def num = slp.query('?id a <http://bio.icmc.usp.br/sustenagro#Evaluation>. ?id <http://bio.icmc.usp.br/sustenagro#appliedTo> <http://bio.icmc.usp.br/sustenagro#' + production_unit_id +'>' ).size() + 1
        def evaluation_name = production_unit_id+"-evaluation-"+num

        slp.addNode(
            N(':'+evaluation_name,
                    'rdf:type': slp.v('http://bio.icmc.usp.br/sustenagro#Evaluation'),
                    ':appliedTo': slp.v(':'+production_unit_id) ,
                    'rdfs:label': 'Avaliação '+ num)
        )

        slp.g.commit()

        def indicators = []
        def inputs = [:]

        dsl.dimensions.each{
            indicators += slp.query("?a rdfs:subClassOf $it. ?id rdfs:subClassOf ?a.")
        }

        indicators.each{
            def name = slp.fromURI2(it.id)
            def ind_value

            if(params[name] != '' && params[name] != null ){
                //println "indicator: $it.id, value: " + slp.toURI2(params[name])
                ind_value = slp.toURI2(params[name])
                inputs[it.id] = ind_value

                slp.addNode(
                    N(':'+name+'-'+evaluation_name,
                      'rdf:type': slp.v(it.id),
                      ':compose': slp.v(':'+evaluation_name),
                      ':value': slp.v(ind_value))
                )

                slp.g.commit()

                println "Indicator-> " + it.id +' val:'+ ind_value
            }
        }

        redirect(action: 'assessment', id: params['production_unit_id'])
    }
}