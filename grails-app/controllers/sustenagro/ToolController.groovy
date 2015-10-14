package sustenagro

import com.github.slugify.Slugify
import rdfSlurper.DataReader
import utils.Uri

import static rdfSlurper.RDFSlurper.N

class ToolController {
    def slp
    def dsl

    def index() {
        def existOnt = false
        def result = slp.query("?o rdf:type owl:Ontology")

        if (result){
            if (result[0].o == "http://bio.icmc.usp.br/sustenagro#")
                existOnt = true
            println existOnt
            println result[0].o

        }

        def queryLabelDescription = { query ->
            def uri = '<'+slp.toURI(query[1])+'>'
            //println uri
            result = slp.query("$uri rdfs:label ?label. optional {$uri dc:description ?description}")

            if (!result) {
                try{
                    result = slp.query("?id rdfs:label ?label. FILTER (STR(?label)='${query[1]}')", '')
                    if (result){
                        uri = "<${result[0].id}>"
                    }
                }
                catch(RuntimeException e){
                        println new RuntimeException('Unknown label: '+query[1])
                }//result = slp.query("$uri rdfs:label ?label. optional {$uri dc:description ?description}")

            }
            return slp.query("?id ${query[0]} $uri; rdfs:label ?label. optional {?id dc:description ?description}. FILTER ( ?id != $uri )")
        }

        dsl.toolIndexStack.each{ command ->
            if(command.request){
                command.request.each{ key, query ->
                    if(key!='widgets'){
                        command.args[key] = queryLabelDescription(query)
                        //println "Key: $key, Query: $query"
                    }
                    else if(key=='widgets'){
                        query.each{ subKey, subQuery ->
                            command.args.widgets[subKey]['args']['data'] = queryLabelDescription(subQuery)
                            //println "Key: $subKey, Query: $subQuery"
                        }
                    }
                }
            }
        }

        /*dsl.toolIndexStack.each{ command ->
            if(command.args.widgets) {
                command.args.widgets.each { key, query ->
                    println '\nKey:'
                    println key
                    println 'Query:'
                    println query
                    println ''
                }
            }
        }*/

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

        def production_unit_id = new Slugify().slugify(params['productionunit_name'])

        slp.addNode(
            N(':'+production_unit_id,
            'rdf:type': slp.v(params['productionunit_types']),
            'rdfs:label': params['productionunit_name'],
            //'dbp:Microregion': slp.v(params['production_unit_microregion']),
            //'sa:culture': slp.v(params['production_unit_culture']),
            //':AgriculturalEfficiency': slp.v(params['production_unit_technology'])
        ))

        if(params['microregion'])
            slp.g.addEdge(slp.v(':' + production_unit_id), slp.v(params['microregion']), 'dbp:Microregion')

        if(params['agriculturalefficiency'])
            slp.g.addEdge(slp.v(':' + production_unit_id), slp.v(params['agriculturalefficiency']), slp.toURI(':AgriculturalEfficiency'))

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
            slp.select('distinct ?id ?title ?class ?valueType')
               .query( '?a  rdfs:subClassOf '+it+''' .
                        ?id rdfs:subClassOf ?a; rdfs:label ?title.
                        ?id rdfs:subClassOf ?y.
                        ?y  owl:onClass ?class.
                        ?class rdfs:subClassOf ?valueType.
                          FILTER( ?valueType = <http://bio.icmc.usp.br/sustenagro#Categorical> || ?valueType = <http://bio.icmc.usp.br/sustenagro#Real> )''')
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

        println environmental_indicators
        //println economic_indicators
        //println social_indicators

        categ(environmental_indicators)
        categ(economic_indicators)
        categ(social_indicators)

        categorical.each{ k, v ->
            //println k
            slp.query("?id a <$k>; rdfs:label ?title.").each{
                //it.id = slp.fromURI(it.id)
                v.push(it)
            }
        }

        def name = slp.":$params.id".'$rdfs:label'

        def values = [:]
        def report
        dsl._cleanProgram()
        def evaluationID = params['evaluation']

        if (evaluationID != null) {

            slp.select('?cls ?value')
            .query("?id dc:isPartOf :${evaluationID}. ?id a ?cls. ?id :value ?value.").each{
                values[it.cls] = it.value
            }

            println values

            //println 'Evaluation'
            //println slp.toURI(':'+params['evaluation'])

            def data = new DataReader(slp, slp.toURI(':'+evaluationID))

            dsl.data = data
            dsl.program()
            report = dsl.report

            def page = g.render(template: 'report', model: [report: report])

            def file = new File("reports/${evaluationID}.html")
            file.write(page.toString())
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
            indicators += slp.select("?id ?class")
                             .query("?a rdfs:subClassOf $it. ?id rdfs:subClassOf ?a. ?id rdfs:subClassOf ?y. ?y owl:onClass ?class.")
        }

        def value = ''
        indicators.each{
            if(params[it.id]){
                println "indicator: $it.id, value: " + params[it.id]

                if(it.class == "http://bio.icmc.usp.br/sustenagro#Real"){
                    value = params[it.id]
                }
                else{
                    value = slp.v(params[it.id])
                }

                slp.addNode(
                    N(it.id+'-'+evaluation_name,
                        'rdf:type': slp.v(it.id),
                        'dc:isPartOf': slp.v(':'+evaluation_name),
                        ':value': value)
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