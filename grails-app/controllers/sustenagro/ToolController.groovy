package sustenagro

import com.github.slugify.Slugify
import rdfUtils.DataReader
import utils.Uri

class ToolController {
    def slp
    def dsl

    def index() {
        if(slp.existOntology("http://bio.icmc.usp.br/sustenagro#")){
            dsl.toolIndexStack.each{ command ->
                if(command.request){
                    command.request.each{ key, args ->
                        if(key!='widgets'){
                            command.args[key] = getLabelDescription(args[1], args[0])
                        }
                        else if(key=='widgets'){
                            args.each{ subKey, subArgs ->
                                command.args.widgets[subKey]['args']['data'] = getLabelDescription(subArgs[1], subArgs[0])
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

        redirect(   action: 'assessment',
                    id: production_unit_alias,
                    params: [evaluation: evaluation_name])
    }

    def evaluations(){
        def production_unit_id = Uri.removeDomain(params.production_unit_id, 'http://bio.icmc.usp.br/sustenagro#')
        def evaluations = getLabelAppliedTo(production_unit_id)

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
        def tecAlignment
        def tecOptimization

        indicators['environmental'] = getGrandchildren(':EnvironmentalIndicator')
        indicators['economic'] = getGrandchildren(':EconomicIndicator')
        indicators['social'] = getGrandchildren(':SocialIndicator')

        indCategories += propertyToList(indicators['environmental'], 'category')
        indCategories += propertyToList(indicators['economic'], 'category')
        indCategories += propertyToList(indicators['social'], 'category')
        indCategories.each{ k, v ->
            getInstances(k).each{
                v.push(it)
            }
        }

        indSubClass['environmental'] = propertyToMap(indicators['environmental'], 'subClass')
        indSubClass['economic'] = propertyToMap(indicators['economic'], 'subClass')
        indSubClass['social'] = propertyToMap(indicators['social'], 'subClass')
        indSubClass.each{ dimension, map ->
            map.each{ k, v ->
                v['label']= getLabel(k)
            }
            indSubClass[dimension] = map.sort{ it.value.label.toLowerCase() }
        }

        productionFeatures = getGrandchildren(':ProductionEfficiencyFeature')

        proCategories = propertyToList(productionFeatures, 'category')
        proCategories.each{ k, v ->
            getInstances(k).each{
                v.push(it)
            }
        }

        proSubClass = propertyToMap(productionFeatures, 'subClass')
        proSubClass.each{ k, v ->
            v['label']= getLabel(k)
        }
        proSubClass = proSubClass.sort{ it.value.label.toLowerCase() }

        def technologyTypes = []
        technologyFeatures = []

        switch(getProductionUnitType(params.id)){
            case 'http://dbpedia.org/ontology/Provider':
                technologyTypes.push(':TechnologicalEfficiencyInTheField')
                break
            case 'http://dbpedia.org/resource/PhysicalPlant':
                technologyTypes.push(':TechnologicalEfficiencyInTheIndustrial')
                break
            case 'http://bio.icmc.usp.br/sustenagro#FarmAndProvider':
                technologyTypes.push(':TechnologicalEfficiencyInTheIndustrial')
                technologyTypes.push(':TechnologicalEfficiencyInTheField')
                break
        }

        technologyTypes.each{ type ->
            technologyFeatures.addAll(getChildren(type).each{it.subClass = slp.toURI(type)})
        }

        tecCategories = propertyToList(technologyFeatures, 'category')
        tecCategories.each{ k, v ->
            getInstances(k).each{
                v.push(it)
            }
        }

        tecSubClass = propertyToMap(technologyFeatures, 'subClass')
        tecSubClass.each{ k, v ->
            v['label']= getLabel(k)
        }
        tecSubClass = tecSubClass.sort{ it.value.label.toLowerCase() }

        tecAlignment = getInstances(':ProductionEnvironmentAlignmentCategory')
        tecOptimization = getInstances(':SugarcaneProcessingOptimizationCategory')

        def evaluationID = params.evaluation
        def name = getLabel(params.id)
        def values = [:]
        def weights = [:]
        def report
        dsl._cleanProgram()

        if (evaluationID != null) {

            getGranchildrenIntances(':EnvironmentalIndicator', evaluationID, '?id ?subClass ?in ?value').each{
                values[it.id] = it.value
            }
            getGranchildrenIntances(':EconomicIndicator', evaluationID, '?id ?subClass ?in ?value').each{
                values[it.id] = it.value
            }
            getGranchildrenIntances(':SocialIndicator', evaluationID, '?id ?subClass ?in ?value').each{
                values[it.id] = it.value
            }
            getGranchildrenIntances(':ProductionEfficiencyFeature', evaluationID, '?id ?subClass ?in ?value ?weight').each{
                values[it.id] = it.value
                weights[it.id] = it.weight
            }
            getGranchildrenIntances(':TechnologicalEfficiencyFeature', evaluationID, '?id ?subClass ?in ?value ?weight').each{
                values[it.id] = it.value
                weights[it.id] = it.weight
            }

            dsl.data = new DataReader(slp, slp.toURI(':'+evaluationID))
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
                       tecAlignment: tecAlignment,
                       tecOptimization: tecOptimization,
                       values: values,
                       weights: weights,
                       report: report])
    }

    def report() {
        def production_unit_id = params.production_unit_id

        def num = getEvaluations(production_unit_id).size() + 1
        def evaluation_name = production_unit_id+"-evaluation-"+num

        slp.insert( ":" + evaluation_name +
                    " rdf:type :Evaluation;"+
                    " :appliedTo :"+ production_unit_id +";"+
                    " rdfs:label 'Avaliação "+  num +"'@pt.")

        def indicators = []

        dsl.dimensions.each{
            indicators += getGrandchildren(it)
        }

        def value

        indicators.each{
            if(params[it.id]){
                if(it.valueType == "http://bio.icmc.usp.br/sustenagro#Real"){
                    value = slp.dataSchema(Integer.parseInt(params[it.id]))
                }
                else{
                    value = "<" + params[it.id] + ">"
                }

                slp.insert( "<" +it.id+'-'+evaluation_name +">"+
                            " rdf:type <"+ it.id +">;"+
                            " dc:isPartOf :"+ evaluation_name +";"+
                            " :value "+  value +".")
                slp.insert( ":" + evaluation_name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+evaluation_name+">.")
            }
        }

        def productionFeatures = getGrandchildren(':ProductionEfficiencyFeature')

        productionFeatures.each{
            if(params[it.id]){
                if(it.valueType == "http://bio.icmc.usp.br/sustenagro#Real"){
                    value = slp.dataSchema(Integer.parseInt(params[it.id]))
                }
                else{
                    value = "<" + params[it.id] + ">"
                }

                slp.insert( "<" +it.id+'-'+evaluation_name +">"+
                        " rdf:type <"+ it.id +">;"+
                        " dc:isPartOf :"+ evaluation_name +";"+
                        " :value "+  value +".")
                slp.insert( ":" + evaluation_name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+evaluation_name+">.")

            }
        }

        def TechnologicalEfficiency = getGrandchildren(':TechnologicalEfficiencyFeature')
        def weight

        TechnologicalEfficiency.each{
            if(params[it.id]){
                if(it.valueType == "http://bio.icmc.usp.br/sustenagro#Real"){
                    value = slp.dataSchema(Integer.parseInt(params[it.id]))
                }
                else{
                    value = "<" + params[it.id] + ">"
                }

                if(it.subClass == 'http://bio.icmc.usp.br/sustenagro#TechnologicalEfficiencyInTheIndustrial'){
                    weight = "<" +params[it.id+'-optimization'] + ">"
                }
                else if(it.subClass == 'http://bio.icmc.usp.br/sustenagro#TechnologicalEfficiencyInTheField'){
                    weight = "<" +params[it.id+'-alignment'] + ">"
                }

                slp.insert( "<" +it.id+'-'+evaluation_name +">"+
                        " rdf:type <"+ it.id +">;"+
                        " dc:isPartOf :"+ evaluation_name +";"+
                        " :value "+ value +";"+
                        " :hasWeight "+ weight +"." )
                slp.insert( ":" + evaluation_name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+evaluation_name+">.")

            }
        }

        redirect(action: 'assessment',
                id: params.production_unit_id,
                params: [evaluation: evaluation_name])
    }

    def propertyToList = { source, property ->
        def map = [:]
        source.each{
            map[it[property]] = []
        }
        return map
    }

    def propertyToMap = { source, property ->
        def map = [:]
        source.each{
            map[it[property]] = [:]
        }
        return map
    }

    def getLabel(String cls){
        slp.query("<"+slp.toURI(cls)+"> rdfs:label ?label.")[0].label
    }

    def getLabelAppliedTo(String cls){
        slp.query("?id :appliedTo <"+slp.toURI(cls)+">. ?id rdfs:label ?label")
    }

    def getLabelDataValue(String cls){
        slp.query("?id a <"+slp.toURI(cls)+">; rdfs:label ?label; :dataValue ?dataValue")
    }

    def getEvaluations(String cls){
        slp.query("?id a :Evaluation. ?id :appliedTo <"+slp.toURI(cls)+">")
    }

    def getLabelDescription(String cls, String property) {
        def uri = '<'+slp.toURI(cls)+'>'
        def result = slp.query("$uri rdfs:label ?label. optional {$uri dc:description ?description}")

        if (result.size == 0) {
            try{
                result = slp.query("?id rdfs:label ?label. FILTER (STR(?label)='$cls')", '', '')
                if (result.size > 0){
                    uri = "<${result[0].id}>"
                }
            }
            catch(RuntimeException e){
                new RuntimeException("Unknown label: $cls")
            }
        }
        return slp.query("?id $property $uri; rdfs:label ?label. optional {?id dc:description ?description}. FILTER ( ?id != $uri )")
    }

    def getProductionUnitType(String ind){
        slp.query("<"+slp.toURI(ind)+"> rdf:type ?type. FILTER(?type != :ProductionUnit)")[0].type
    }

    def getInstances(String cls){
        slp.select('distinct ?id ?label')
            .query("?id a <"+slp.toURI(cls)+">; rdfs:label ?label.",
            "ORDER BY ?label")
    }

    def getChildren(String cls){
        slp.select('distinct ?id ?label ?category ?valueType')
            .query('?id rdfs:subClassOf <'+slp.toURI(cls)+'''> ; rdfs:label ?label.
                ?id rdfs:subClassOf ?y.
                ?y owl:onClass ?category.
                ?category rdfs:subClassOf ?valueType. '''+
                "FILTER(?valueType = :Categorical || ?valueType = :Real)",
                "ORDER BY ?label")
    }

    def getGrandchildren(String cls){
        slp.select('distinct ?id ?label ?subClass ?category ?valueType')
            .query('?subClass rdfs:subClassOf <'+slp.toURI(cls)+'''> .
                    ?id rdfs:subClassOf ?subClass; rdfs:label ?label.
                    ?id rdfs:subClassOf ?y.
                    ?y owl:onClass ?category.
                    ?category rdfs:subClassOf ?valueType. '''+
                    "FILTER(?subClass != <"+slp.toURI(cls)+"> && ?subClass != ?id && (?valueType = :Categorical || ?valueType = :Real))",
                    "ORDER BY ?label")
    }

    def getGranchildrenIntances(String cls, String evaluation, String args){
        def argsList = args.split(' ')

        def query = "?subClass rdfs:subClassOf <"+slp.toURI(cls)+">."+
                    "?id rdfs:subClassOf ?subClass."+
                    "?in a ?id."+
                    "?in dc:isPartOf <"+slp.toURI(evaluation)+">.";

        if (argsList.contains('?value'))
            query +="?in :value ?value.";

        if (argsList.contains('?weight'))
            query +="?in :hasWeight ?weight.";

        query += "FILTER( ?subClass != <"+slp.toURI(cls)+"> && ?id != <"+slp.toURI(cls)+"> && ?subClass != ?id)"

        slp.select('distinct '+args)
            .query(query, "ORDER BY ?in")
    }
}