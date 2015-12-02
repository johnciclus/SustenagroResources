package sustenagro

import com.github.slugify.Slugify
import rdfUtils.DataReader
import utils.Uri

class ToolController {
    def dsl
    def k

    def index() {
        if(k.existOntology("http://bio.icmc.usp.br/sustenagro#")){
            dsl.toolIndexStack.each{ command ->
                if(command.request){
                    command.request.each{ key, args ->
                        if(key!='widgets'){
                            command.args[key] = k[args[1]].getLabelDescription(args[0])
                        }
                        else if(key=='widgets'){
                            args.each{ subKey, subArgs ->
                                //command.args.widgets[subKey]['args']['data'] = getLabelDescription(subArgs[1], subArgs[0])
                                command.args.widgets[subKey]['args']['data'] = k[subArgs[1]].getLabelDescription(subArgs[0])
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

        k.insert(sparql)

        //"dbp:Microregion <http://pt.dbpedia.org/resource/Microrregião_de_São_Carlos>;"
        //":AgriculturalEfficiency :HighAgriculturalEfficiency.")


        /*k.addNode(
            N(':'+production_unit_id,
            'rdf:type': k.v(params['productionunit_types']),
            'rdfs:label': params['productionunit_name']
            //'dbp:Microregion': k.v(params['production_unit_microregion']),
            //'sa:culture': k.v(params['production_unit_culture']),
            //':AgriculturalEfficiency': k.v(params['production_unit_technology'])
        ))



        if(params['agriculturalefficiency'])
            k.g.addEdge(k.v(':' + production_unit_id), k.v(params['agriculturalefficiency']), k.toURI(':AgriculturalEfficiency'))

        k.g.commit()*/
        //k.g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
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
        def evaluations = k[production_unit_id].getLabelAppliedTo()

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

        indicators['environmental'] = k[':EnvironmentalIndicator'].getGrandchildren()
        indicators['economic'] = k[':EconomicIndicator'].getGrandchildren()
        indicators['social'] = k[':SocialIndicator'].getGrandchildren()

        indCategories += propertyToList(indicators['environmental'], 'category')
        indCategories += propertyToList(indicators['economic'], 'category')
        indCategories += propertyToList(indicators['social'], 'category')
        indCategories.each{ key, v ->
            k[key].getInstances().each{
                v.push(it)
            }
        }

        indSubClass['environmental'] = propertyToMap(indicators['environmental'], 'subClass')
        indSubClass['economic'] = propertyToMap(indicators['economic'], 'subClass')
        indSubClass['social'] = propertyToMap(indicators['social'], 'subClass')
        indSubClass.each{ dimension, map ->
            map.each{ key, v ->
                v['label']= k[key].getLabel()
            }
            indSubClass[dimension] = map.sort{ it.value.label.toLowerCase() }
        }

        productionFeatures = k[':ProductionEfficiencyFeature'].getGrandchildren()

        proCategories = propertyToList(productionFeatures, 'category')
        proCategories.each{ key, v ->
            k[key].getInstances().each{
                v.push(it)
            }
        }

        proSubClass = propertyToMap(productionFeatures, 'subClass')
        proSubClass.each{ key, v ->
            v['label']= k[key].getLabel()
        }
        proSubClass = proSubClass.sort{ it.value.label.toLowerCase() }

        def technologyTypes = []
        technologyFeatures = []

        switch(k[params.id].getProductionUnitType()){
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
            technologyFeatures.addAll(k[type].getChildren().each{it.subClass = k.toURI(type)})
        }

        tecCategories = propertyToList(technologyFeatures, 'category')
        tecCategories.each{ key, v ->
            k[key].getInstances().each{
                v.push(it)
            }
        }

        tecSubClass = propertyToMap(technologyFeatures, 'subClass')
        tecSubClass.each{ key, v ->
            v['label']= k[key].getLabel()
        }
        tecSubClass = tecSubClass.sort{ it.value.label.toLowerCase() }

        tecAlignment = k[':ProductionEnvironmentAlignmentCategory'].getInstances()
        tecOptimization = k[':SugarcaneProcessingOptimizationCategory'].getInstances()

        def evaluationID = params.evaluation
        def name = k[params.id].getLabel()
        def values = [:]
        def weights = [:]
        def report
        dsl._cleanProgram()

        if (evaluationID != null) {

            k[':EnvironmentalIndicator'].getGranchildrenIndividuals(evaluationID, '?id ?subClass ?in ?value').each{
                values[it.id] = it.value
            }
            k[':EconomicIndicator'].getGranchildrenIndividuals(evaluationID, '?id ?subClass ?in ?value').each{
                values[it.id] = it.value
            }
            k[':SocialIndicator'].getGranchildrenIndividuals(evaluationID, '?id ?subClass ?in ?value').each{
                values[it.id] = it.value
            }
            k[':ProductionEfficiencyFeature'].getGranchildrenIndividuals(evaluationID, '?id ?subClass ?in ?value ?weight').each{
                values[it.id] = it.value
                weights[it.id] = it.weight
            }
            k[':TechnologicalEfficiencyFeature'].getGranchildrenIndividuals(evaluationID, '?id ?subClass ?in ?value ?weight').each{
                values[it.id] = it.value
                weights[it.id] = it.weight
            }

            dsl.data = new DataReader(k, k.toURI(':'+evaluationID))
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

        def num = k[production_unit_id].getEvaluations().size() + 1
        def evaluation_name = production_unit_id+"-evaluation-"+num

        k.insert( ":" + evaluation_name +
                    " rdf:type :Evaluation;"+
                    " :appliedTo :"+ production_unit_id +";"+
                    " rdfs:label 'Avaliação "+  num +"'@pt.")

        def indicators = []

        dsl.dimensions.each{
            indicators += k[it].getGrandchildren()
        }

        def value

        indicators.each{
            if(params[it.id]){
                if(it.valueType == "http://bio.icmc.usp.br/sustenagro#Real"){
                    value = k.dataSchema(Integer.parseInt(params[it.id]))
                }
                else{
                    value = "<" + params[it.id] + ">"
                }

                k.insert( "<" +it.id+'-'+evaluation_name +">"+
                            " rdf:type <"+ it.id +">;"+
                            " dc:isPartOf :"+ evaluation_name +";"+
                            " :value "+  value +".")
                k.insert( ":" + evaluation_name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+evaluation_name+">.")
            }
        }

        def productionFeatures = k[':ProductionEfficiencyFeature'].getGrandchildren()

        productionFeatures.each{
            if(params[it.id]){
                if(it.valueType == "http://bio.icmc.usp.br/sustenagro#Real"){
                    value = k.dataSchema(Integer.parseInt(params[it.id]))
                }
                else{
                    value = "<" + params[it.id] + ">"
                }

                k.insert( "<" +it.id+'-'+evaluation_name +">"+
                        " rdf:type <"+ it.id +">;"+
                        " dc:isPartOf :"+ evaluation_name +";"+
                        " :value "+  value +".")
                k.insert( ":" + evaluation_name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+evaluation_name+">.")

            }
        }

        def TechnologicalEfficiency = k[':TechnologicalEfficiencyFeature'].getGrandchildren()
        def weight

        TechnologicalEfficiency.each{
            if(params[it.id]){
                if(it.valueType == "http://bio.icmc.usp.br/sustenagro#Real"){
                    value = k.dataSchema(Integer.parseInt(params[it.id]))
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

                k.insert( "<" +it.id+'-'+evaluation_name +">"+
                        " rdf:type <"+ it.id +">;"+
                        " dc:isPartOf :"+ evaluation_name +";"+
                        " :value "+ value +";"+
                        " :hasWeight "+ weight +"." )
                k.insert( ":" + evaluation_name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+evaluation_name+">.")
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

    /*def getLabel(String cls){
        k.query("<"+k.toURI(cls)+"> rdfs:label ?label.")[0].label
    }

    def getLabelAppliedTo(String cls){
        k.query("?id :appliedTo <"+k.toURI(cls)+">. ?id rdfs:label ?label")
    }

    def getLabelDataValue(String cls){
        k.query("?id a <"+k.toURI(cls)+">; rdfs:label ?label; :dataValue ?dataValue")
    }

    def getEvaluations(String cls){
        k.query("?id a :Evaluation. ?id :appliedTo <"+k.toURI(cls)+">")
    }

    def getLabelDescription(String cls, String property) {
        def uri = '<'+k.toURI(cls)+'>'
        def result = k.query("$uri rdfs:label ?label. optional {$uri dc:description ?description}")

        if (result.size == 0) {
            try{
                result = k.query("?id rdfs:label ?label. FILTER (STR(?label)='$cls')", '', '')
                if (result.size > 0){
                    uri = "<${result[0].id}>"
                }
            }
            catch(RuntimeException e){
                new RuntimeException("Unknown label: $cls")
            }
        }
        return k.query("?id $property $uri; rdfs:label ?label. optional {?id dc:description ?description}. FILTER ( ?id != $uri )")
    }

    def getProductionUnitType(String ind){
        k.query("<"+k.toURI(ind)+"> rdf:type ?type. FILTER(?type != :ProductionUnit)")[0].type
    }

    def getInstances(String cls){
        k.select('distinct ?id ?label')
            .query("?id a <"+k.toURI(cls)+">; rdfs:label ?label.",
            "ORDER BY ?label")
    }

    def getChildren(String cls){
        k.select('distinct ?id ?label ?category ?valueType')
            .query('?id rdfs:subClassOf <'+k.toURI(cls)+'''> ; rdfs:label ?label.
                ?id rdfs:subClassOf ?y.
                ?y owl:onClass ?category.
                ?category rdfs:subClassOf ?valueType. '''+
                "FILTER(?valueType = :Categorical || ?valueType = :Real)",
                "ORDER BY ?label")
    }

    def getGrandchildren(String cls){
        k.select('distinct ?id ?label ?subClass ?category ?valueType')
            .query('?subClass rdfs:subClassOf <'+k.toURI(cls)+'''> .
                    ?id rdfs:subClassOf ?subClass; rdfs:label ?label.
                    ?id rdfs:subClassOf ?y.
                    ?y owl:onClass ?category.
                    ?category rdfs:subClassOf ?valueType. '''+
                    "FILTER(?subClass != <"+k.toURI(cls)+"> && ?subClass != ?id && (?valueType = :Categorical || ?valueType = :Real))",
                    "ORDER BY ?label")
    }

    def getGranchildrenIndividuals(String cls, String evaluation, String args){
        def argsList = args.split(' ')

        def query = "?subClass rdfs:subClassOf <"+k.toURI(cls)+">."+
                    "?id rdfs:subClassOf ?subClass."+
                    "?ind a ?id."+
                    "?ind dc:isPartOf <"+k.toURI(evaluation)+">.";

        if (argsList.contains('?value'))
            query +="?ind :value ?value.";

        if (argsList.contains('?weight'))
            query +="?ind :hasWeight ?weight.";

        query += "FILTER( ?subClass != <"+k.toURI(cls)+"> && ?id != <"+k.toURI(cls)+"> && ?subClass != ?id)"

        k.select('distinct '+args)
            .query(query, "ORDER BY ?ind")
    }*/
}