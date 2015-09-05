package sustenagro

import com.github.slugify.Slugify
import com.github.rjeschke.txtmark.Processor
import dsl.DataReader

//import org.pegdown.PegDownProcessor
import rdfSlurper.DataReader
import rdfSlurper.RDFSlurper
import utils.Uri

import static rdfSlurper.RDFSlurper.N

class ToolController {
    def slp
    def dsl

    def index() {


        def queryLabelDescription = { query ->
            def uri = '<'+slp.toURI(query[1])+'>'
            //println uri
            /*def result = slp.query("$uri rdfs:label ?label. optional {$uri dc:description ?description}")
            if (!result) {
                result = slp.query("?id rdfs:label ?label. FILTER (STR(?label)='${query[1]}')", '')
                if (!result){
                    //throw new RuntimeException('Unknown label: '+query[1])
                }
                else{
                    uri = "<${result[0].id}>"
                    return result = slp.query("$uri rdfs:label ?label. optional {$uri dc:description ?description}")
                }
            }*/
            def result = slp.query("?id ${query[0]} $uri; rdfs:label ?label. optional {?id dc:description ?description}")
            return result
        }
        println 'inputs:\n'

        dsl.toolIndexStack.each{ command ->
            if(command.request){
                println "command.request"
                println command.request
                command.request.each{ key, query ->
                    if(key!='widgets'){
                        println key
                        println query
                        println '\n'
                        command.args[key] = queryLabelDescription(query)
                    }
                    else{
                        println 'Key: '+key
                        println '\n'
                        query.each{ subKey, subQuery ->
                            println 'subKey: '+subKey
                            println 'subQuery: '+subQuery

                            println 'command.args.widgets'
                            println command.args.widgets
                            println '\n'
                            println command.args.widgets[subKey]['args']
                            command.args.widgets[subKey]['args']['data']= queryLabelDescription(subQuery)
                        }
                    }
                }
            }
        }

        //println Processor.process("This is ***TXTMARK***");
        //String html = new Markdown4jProcessor().process("This is a **bold** text");

        render(view: 'index',
               model: [inputs: dsl.toolIndexStack,
                       //name: dsl.name,
                       //description:   Processor.process(dsl.description),
                       //description:   dsl.description,
                       //microregions: inputs[0][2],
                       //technologies: inputs[1][2],
                       //production_unit_types: inputs[2][2],
                       //production_units: inputs[3][2]
                       ])
    }

    def createProductionUnit() {

        def production_unit_id = new Slugify().slugify(params['production_unit_name'])

        slp.addNode(
            N(':'+production_unit_id,
            'rdf:type': slp.v(params['production_unit_type']),
            'rdfs:label': params['production_unit_name'],
            //'dbp:Microregion': slp.v(params['production_unit_microregion']),
            //'sa:culture': slp.v(params['production_unit_culture']),
            //':AgriculturalEfficiency': slp.v(params['production_unit_technology'])
        ))

        if(params['production_unit_microregion'])
            slp.g.addEdge(slp.v(':' + production_unit_id), slp.v(params['production_unit_microregion']), 'dbp:Microregion')

        if(params['production_unit_technology'])
            slp.g.addEdge(slp.v(':' + production_unit_id), slp.v(params['production_unit_technology']), ':AgriculturalEfficiency')

        slp.g.commit()
        //slp.g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
        redirect(action: 'assessment', id: production_unit_id)
    }

    def selectProductionUnit(){
        def production_unit_id = Uri.removeDomain(params['production_unit_id'], 'http://bio.icmc.usp.br/sustenagro#')

        redirect(   action: 'assessment',
                    id: production_unit_id)
    }

    def selectEvaluation(){
        def production_unit_alias = Uri.removeDomain(params['production_unit_id'], 'http://bio.icmc.usp.br/sustenagro#')
        def evaluation_name = Uri.removeDomain(params['evaluation'], 'http://bio.icmc.usp.br/sustenagro#')

        //def indicators = slp.query("?id :compose :$evaluation_alias. ?id :value ?value")

        //println "indicators"
        //println indicators

        redirect(   action: 'assessment',
                    id: production_unit_alias,
                    params: [evaluation: evaluation_name])
    }

    def evaluations(){
        def production_unit_id = Uri.removeDomain(params['production_unit_id'], 'http://bio.icmc.usp.br/sustenagro#')
        def evaluations = slp.query("?id :appliedTo :$production_unit_id. ?id rdfs:label ?label")

        render( template: 'evaluations',
                model:    [evaluations: evaluations,
                           production_unit_id: production_unit_id]);
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

        def environmental_indicators = indicators(':EnvironmentalIndicator')
        def economic_indicators = indicators(':EconomicIndicator')
        def social_indicators = indicators(':SocialIndicator')

        categ(environmental_indicators)
        categ(economic_indicators)
        categ(social_indicators)

        categorical.each{ k, v ->
            slp.query("?id a <$k>; rdfs:label ?title.").each{
                //it.id = slp.fromURI(it.id)
                v.push(it)
            }
        }

        String name = slp.":$params.id".'$rdfs:label'

        def values = [:]
        def report
        dsl._cleanProgram()

        if (params['evaluation'] != null) {

            slp.select('?cls ?value')
            .query("?id dc:isPartOf :${params['evaluation']}. ?id a ?cls. ?id :value ?value.").each{
                values[it.cls] = it.value
            }

            def data = new DataReader(slp, slp.toURI(':'+params['evaluation']))

            dsl.data = data
            dsl.program()
            report = dsl.report
        }

        render(view: 'assessment',
               model: [sustenagro: 'http://bio.icmc.usp.br/sustenagro#',
                       production_unit: [id: params.id, name: name],
                       indicators: [environmental: environmental_indicators, economic: economic_indicators, social: social_indicators],
                       categorical: categorical,
                       values: values,
                       report: report])
    }

    def report() {
        def production_unit_id = params['production_unit_id']

        def num = slp.query('?id a :Evaluation. ?id :appliedTo :' + production_unit_id).size() + 1
        def evaluation_name = production_unit_id+"-evaluation-"+num

        slp.addNode(
            N(':'+evaluation_name,
                    'rdf:type': slp.v(':Evaluation'),
                    ':appliedTo': slp.v(':'+production_unit_id),
                    'rdfs:label': 'Avaliação '+ num)
        )

        slp.g.commit()

        def indicators = []

        dsl.dimensions.each{
            indicators += slp.query("?a rdfs:subClassOf $it. ?id rdfs:subClassOf ?a.")
        }

        indicators.each{
            if(params[it.id]){
                //println "indicator: $it.id, value: " + slp.toURI2(params[name])

                slp.addNode(
                        N(it.id+'-'+evaluation_name,
                            'rdf:type': slp.v(it.id),
                            'dc:isPartOf': slp.v(':'+evaluation_name),
                            ':value': slp.v(params[it.id]))
                )
                slp.g.addEdge(slp.v(':'+evaluation_name), slp.v(it.id+'-'+evaluation_name), 'http://purl.org/dc/terms/hasPart')
            }
        }
        
        slp.g.commit()

        //println slp.toURI(':'+evaluation_name)

        //Save evaluation data

        //def recommendations = []
        //dsl.recommendations.each{if (it[0]()) recommendations << new PegDownProcessor().markdownToHtml(it[1])}

        //println 'Recom1: '
        //recommendations.each{println it}

        redirect(action: 'assessment',
                id: params['production_unit_id'],
                params: [evaluation: evaluation_name])

    }
}