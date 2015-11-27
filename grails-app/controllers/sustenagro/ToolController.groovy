package sustenagro

import com.github.slugify.Slugify
import rdfUtils.DataReader
import utils.Uri

class ToolController {
    def slp
    def dsl

    def index() {
        if(slp.existOntology("http://bio.icmc.usp.br/sustenagro#")){
            def queryLabelDescription = { query ->
                def uri = '<'+slp.toURI(query[1])+'>'
                def result = slp.query("$uri rdfs:label ?label. optional {$uri dc:description ?description}")

                if (result.size == 0) {
                    try{
                        result = slp.query("?id rdfs:label ?label. FILTER (STR(?label)='${query[1]}')", '', '')
                        if (result.size > 0){
                            uri = "<${result[0].id}>"
                        }
                    }
                    catch(RuntimeException e){
                        new RuntimeException('Unknown label: '+query[1])
                    }
                }
                return slp.query("?id ${query[0]} $uri; rdfs:label ?label. optional {?id dc:description ?description}. FILTER ( ?id != $uri )")
            }

            dsl.toolIndexStack.each{ command ->
                if(command.request){
                    command.request.each{ key, query ->
                        if(key!='widgets'){
                            command.args[key] = queryLabelDescription(query)
                        }
                        else if(key=='widgets'){
                            query.each{ subKey, subQuery ->
                                command.args.widgets[subKey]['args']['data'] = queryLabelDescription(subQuery)
                            }
                        }
                    }
                }
            }

            //println Processor.process("This is ***TXTMARK***");
            //String html = new Markdown4jProcessor().process("This is a **bold** text");

            render(view: 'index', model: [inputs: dsl.toolIndexStack])
        }
        else
            redirect(uri: "/")
    }

    def createProductionUnit() {

        def production_unit_id = new Slugify().slugify(params.productionunit_name)

        String sparql = ":" + production_unit_id +
                        " rdf:type <"+    params.productionunit_types+">;"+
                        " rdfs:label '"+  params.productionunit_name+"'@pt"

        if(params.microregion)
            sparql += "; dbp:Microregion <" + params.microregion + ">"

        if(params.agriculturalefficiency)
            sparql += "; :AgriculturalEfficiency <" + params.agriculturalefficiency + ">"

        sparql += "."

        slp.insert(sparql)

        //"dbp:Microregion <http://pt.dbpedia.org/resource/Microrregião_de_São_Carlos>;"
        //":AgriculturalEfficiency :HighAgriculturalEfficiency.")


        /*slp.addNode(
            N(':'+production_unit_id,
            'rdf:type': slp.v(params['productionunit_types']),
            'rdfs:label': params['productionunit_name']
            //'dbp:Microregion': slp.v(params['production_unit_microregion']),
            //'sa:culture': slp.v(params['production_unit_culture']),
            //':AgriculturalEfficiency': slp.v(params['production_unit_technology'])
        ))



        if(params['agriculturalefficiency'])
            slp.g.addEdge(slp.v(':' + production_unit_id), slp.v(params['agriculturalefficiency']), slp.toURI(':AgriculturalEfficiency'))

        slp.g.commit()*/
        //slp.g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
        redirect(action: 'assessment', id: production_unit_id)
    }

    def selectProductionUnit(){
        def production_unit_id = Uri.removeDomain(params.production_unit_id, 'http://bio.icmc.usp.br/sustenagro#')

        redirect(   action: 'assessment',
                    id: production_unit_id)
    }

    def selectEvaluation(){
        def production_unit_alias = Uri.removeDomain(params.production_unit_id, 'http://bio.icmc.usp.br/sustenagro#')
        def evaluation_name = Uri.removeDomain(params.evaluation, 'http://bio.icmc.usp.br/sustenagro#')

        //def indicators = slp.query("?id :compose :$evaluation_alias. ?id :value ?value")

        //println "indicators"
        //println indicators

        redirect(   action: 'assessment',
                    id: production_unit_alias,
                    params: [evaluation: evaluation_name])
    }

    def evaluations(){
        def production_unit_id = Uri.removeDomain(params.production_unit_id, 'http://bio.icmc.usp.br/sustenagro#')
        def evaluations = slp.query("?id :appliedTo :$production_unit_id. ?id rdfs:label ?label")

        render( template: 'evaluations',
                model:    [evaluations: evaluations,
                           production_unit_id: production_unit_id]);
    }

    def assessment() {
        def indicators = [:]
        def indCategorical = [:]
        def effCategorical = [:]

        def categ = {
            it.each{
                indCategorical[it.category] = []
            }
        }

        indicators['environmental'] = getGrandchildren(':EnvironmentalIndicator')
        indicators['economic'] = getGrandchildren(':EconomicIndicator')
        indicators['social'] = getGrandchildren(':SocialIndicator')

        categ(indicators['environmental'])
        categ(indicators['economic'])
        categ(indicators['social'])

        indCategorical.each{ k, v ->
            //println k
            slp.query("?id a <$k>; rdfs:label ?label.").each{
                //it.id = slp.fromURI(it.id)
                v.push(it)
            }
        }

        def efficiencyFeatures = getGrandchildren(':ProductionEfficiencyFeature')

        efficiencyFeatures.each{ feature ->
            effCategorical[feature.category] = []
            slp.query("?id a <$feature.category>; rdfs:label ?label.").each{
                //it.id = slp.fromURI(it.id)
                effCategorical[feature.category].push(it)
            }
        }

        println effCategorical

        def name = slp.query(":$params.id rdfs:label ?label")[0].label

        def values = [:]
        def report
        dsl._cleanProgram()
        def evaluationID = params.evaluation

        if (evaluationID != null) {

            slp.select('?cls ?value').query("?id dc:isPartOf :$evaluationID. ?id a ?cls. ?id :value ?value.").each{
                values[it.cls] = it.value
            }
            //println "values"
            //println values

            //println 'Evaluation'
            //println slp.toURI(':'+params['evaluation'])

            def data = new DataReader(slp, slp.toURI(':'+evaluationID))

            dsl.data = data
            dsl.program()
            report = dsl.report

            //def page = g.render(template: 'report', model: [report: report])
            //lack generate the report file

            //def file = new File("reports/${evaluationID}.html")
            //file.write(page.toString())
        }



        render(view: 'assessment',
               model: [production_unit: [id: params.id, name: name],
                       indicators: indicators,
                       indCategorical: indCategorical,
                       efficiencyFeatures: efficiencyFeatures,
                       effCategorical: effCategorical,
                       values: values,
                       report: report])
    }

    def report() {
        def production_unit_id = params.production_unit_id

        def num = slp.query('?id a :Evaluation. ?id :appliedTo :' + production_unit_id).size() + 1
        def evaluation_name = production_unit_id+"-evaluation-"+num

        slp.insert( ":" + evaluation_name +
                    " rdf:type :Evaluation;"+
                    " :appliedTo :"+ production_unit_id +";"+
                    " rdfs:label 'Avaliação "+  num +"'@pt.")

        /*slp.addNode(
            N(':'+evaluation_name,
                    'rdf:type': slp.v(':Evaluation'),
                    ':appliedTo': slp.v(':'+production_unit_id),
                    'rdfs:label': 'Avaliação '+ num)
        )
        slp.g.commit()*/

        def indicators = []

        dsl.dimensions.each{
            indicators += slp.select("distinct ?id ?class")
                             .query("?a rdfs:subClassOf $it. ?id rdfs:subClassOf ?a. ?id rdfs:subClassOf ?y. ?y owl:onClass ?class.")
        }

        def value = ''
        indicators.each{
            if(params[it.id]){
                //print "indicator: "
                //println it
                //println "indicator: $it.id, value: " + params[it.id]

                if(it.class == "http://bio.icmc.usp.br/sustenagro#Real"){
                    //value = params[it.id]
                    value = slp.dataSchema(Integer.parseInt(params[it.id]))
                }
                else{
                    // value = params[it.id]
                    value = "<" + params[it.id] + ">"
                }

                slp.insert( "<" +it.id+'-'+evaluation_name +">"+
                            " rdf:type <"+ it.id +">;"+
                            " dc:isPartOf :"+ evaluation_name +";"+
                            " :value "+  value +".")
                slp.insert( ":" + evaluation_name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+evaluation_name+">.")


                /*
                slp.addNode(

                    N(it.id+'-'+evaluation_name,
                        'rdf:type': slp.v(it.id),
                        'dc:isPartOf': slp.v(':'+evaluation_name),
                        ':value': value)
                )
                slp.g.addEdge(slp.v(':'+evaluation_name), slp.v(it.id+'-'+evaluation_name), 'http://purl.org/dc/terms/hasPart')
                */
            }
        }
        
        //slp.g.commit()

        //println slp.toURI(':'+evaluation_name)

        //Save evaluation data

        //def recommendations = []
        //dsl.recommendations.each{if (it[0]()) recommendations << new PegDownProcessor().markdownToHtml(it[1])}

        //println 'Recom1: '
        //recommendations.each{println it}

        redirect(action: 'assessment',
                id: params.production_unit_id,
                params: [evaluation: evaluation_name])

    }

    def getGrandchildren(String cls){
        slp.select('distinct ?id ?label ?subclass ?category ?valueType')
            .query('?subclass rdfs:subClassOf '+cls+''' .
                    ?id rdfs:subClassOf ?subclass; rdfs:label ?label.
                    ?id rdfs:subClassOf ?y.
                    ?y owl:onClass ?category.
                    ?category rdfs:subClassOf ?valueType. '''+
                    "FILTER(?subclass != $cls && ?subclass != ?id && (?valueType = :Categorical || ?valueType = :Real))")
    }

}