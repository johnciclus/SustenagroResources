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
        def indCategories = [:]
        def indSubClass = [:]
        def productionFeatures
        def proCategories
        def proSubClass
        def technologyFeatures
        def tecCategories
        def tecSubClass

        indicators['environmental'] = getGrandchildren(':EnvironmentalIndicator')
        indicators['economic'] = getGrandchildren(':EconomicIndicator')
        indicators['social'] = getGrandchildren(':SocialIndicator')

        indCategories += mapProperty(indicators['environmental'], 'category')
        indCategories += mapProperty(indicators['economic'], 'category')
        indCategories += mapProperty(indicators['social'], 'category')
        indCategories.each{ k, v ->
            slp.query("?id a <$k>; rdfs:label ?label.").each{
                v.push(it)
            }
        }

        indSubClass['environmental'] = mapProperty(indicators['environmental'], 'subClass')
        indSubClass['economic'] = mapProperty(indicators['economic'], 'subClass')
        indSubClass['social'] = mapProperty(indicators['social'], 'subClass')
        indSubClass.each{ dimension, list ->
            list.each{ k, v ->
                slp.query("<$k> rdfs:label ?label.").each{
                    v.push(it)
                }
            }
        }

        productionFeatures = getGrandchildren(':ProductionEfficiencyFeature')

        proCategories = mapProperty(productionFeatures, 'category')
        proCategories.each{ k, v ->
            slp.query("?id a <$k>; rdfs:label ?label.").each{
                v.push(it)
            }
        }

        proSubClass = mapProperty(productionFeatures, 'subClass')
        proSubClass.each{ k, v ->
            slp.query("<$k> rdfs:label ?label.").each{
                v.push(it)
            }
        }

        def technologyTypes = []
        technologyFeatures = []

        switch(slp.query(":$params.id rdf:type ?type. FILTER(?type != :ProductionUnit)")[0].type){
            case 'http://dbpedia.org/ontology/Farm':
                technologyTypes.push(':TechnologicalEfficiencyInTheField')
                break
            case 'http://dbpedia.org/resource/Physical_plant':
                technologyTypes.push(':TechnologicalEfficiencyInTheIndustrial')
                break
            case 'http://bio.icmc.usp.br/sustenagro#FarmAndPlant':
                technologyTypes.push(':TechnologicalEfficiencyInTheIndustrial')
                technologyTypes.push(':TechnologicalEfficiencyInTheField')
                break
        }

        technologyTypes.each{ type ->
            technologyFeatures.addAll(getChildren(type).each{it.subClass = slp.toURI(type)})
        }

        tecCategories = mapProperty(technologyFeatures, 'category')
        tecCategories.each{ k, v ->
            slp.query("?id a <$k>; rdfs:label ?label.").each{
                v.push(it)
            }
        }

        tecSubClass = mapProperty(technologyFeatures, 'subClass')
        tecSubClass.each{ k, v ->
            slp.query("<$k> rdfs:label ?label.").each{
                v.push(it)
            }
        }

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
                       indCategories: indCategories,
                       indSubClass: indSubClass,
                       productionFeatures: productionFeatures,
                       proCategories: proCategories,
                       proSubClass: proSubClass,
                       technologyFeatures: technologyFeatures,
                       tecCategories: tecCategories,
                       tecSubClass: tecSubClass,
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

    def mapProperty = { source, property ->
        def map = [:]
        source.each{
            map[it[property]] = []
        }
        return map
    }

    def getChildren(String cls){
        slp.select('distinct ?id ?label ?category ?valueType')
            .query('?id rdfs:subClassOf <'+slp.toURI(cls)+'''> ; rdfs:label ?label.
                ?id rdfs:subClassOf ?y.
                ?y owl:onClass ?category.
                ?category rdfs:subClassOf ?valueType. '''+
                "FILTER(?valueType = :Categorical || ?valueType = :Real)")
    }

    def getGrandchildren(String cls){
        slp.select('distinct ?id ?label ?subClass ?category ?valueType')
            .query('?subClass rdfs:subClassOf <'+slp.toURI(cls)+'''> .
                    ?id rdfs:subClassOf ?subClass; rdfs:label ?label.
                    ?id rdfs:subClassOf ?y.
                    ?y owl:onClass ?category.
                    ?category rdfs:subClassOf ?valueType. '''+
                    "FILTER(?subClass != <"+slp.toURI(cls)+"> && ?subClass != ?id && (?valueType = :Categorical || ?valueType = :Real))")
    }

}