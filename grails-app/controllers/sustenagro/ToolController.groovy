package sustenagro

import com.github.slugify.Slugify
import semantics.DataReader
import semantics.Node
import utils.Uri

class ToolController {
    def dsl
    def k

    def index() {
        dsl.viewsMap[controllerName][actionName].each{ command ->
            if(command.request){
                command.request.each{ key, args ->
                    if(key!='widgets'){
                        command.args[key] = k[args[1]].getLabelDescription(args[0].toString())
                    }
                    else if(key=='widgets'){
                        args.each{ subKey, subArgs ->
                            //command.args.widgets[subKey]['args']['data'] = getLabelDescription(subArgs[1], subArgs[0])
                            command.args.widgets[subKey]['args']['data'] = k[subArgs[1]].getLabelDescription(subArgs[0].toString())
                        }
                    }
                }
            }
        }
        render(view: 'index', model: [inputs: dsl.viewsMap[controllerName][actionName]])
    }

    def createUnity() {
        def id = ''
        def name = k.shortURI(':hasName')
        def type = k.shortURI(params['unity'])

        if(params['unity'] && params[name] && params[type]) {

            def node = new Node(k, '')
            def unityFeatures = [:]

            def features = dsl.evaluationObjectMap[k.shortToURI(params['unity'])].model

            features.each{ feature ->
                if(params[feature.id] && feature.id != type){
                    unityFeatures[k.shortToURI(feature.id)] = [value: params[feature.id], dataType: feature.dataType]
                }
            }

            id = new Slugify().slugify(params[name])
            node.insertUnity(id, params[type], unityFeatures)
        }

        //k.g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
        redirect(action: 'assessment', id: id)
    }

    def selectUnity(){
        def production_unit_id = k.shortURI(params.production_unit_id)

        redirect(   action: 'assessment',
                    id: production_unit_id)
    }

    def selectAssessment(){
        def production_unit_alias = k.shortURI(params.production_unit_id)
        def assessment_name = k.shortURI(params.assessment)

        redirect(   action: 'assessment',
                    id: production_unit_alias,
                    params: [assessment: assessment_name])
    }

    def assessment() {
        def features = [:]
        def categories = [:]
        def grandChildren
        def technologyTypes

        dsl.dimensionsMap.each{ feature ->
            features[feature.key] = ['subClass': [:]]
            grandChildren = k[feature.key].getGrandchildren('?id ?label ?subClass ?category ?valueType ?weight')
            k[feature.key].getSubClass('?label').each{ subClass ->
                features[feature.key]['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
                grandChildren.each{
                    if(it.subClass == subClass.subClass) {
                        features[feature.key]['subClass'][subClass.subClass]['subClass'][it.id] = it
                    }
                }
            }
            categories += grandChildren.categoryList()
        }

        dsl.featureMap.each{ feature ->
            features[feature.key] = ['subClass': [:]]
            grandChildren = k[feature.key].getGrandchildren('?id ?label ?subClass ?category ?valueType')
            k[feature.key].getSubClass('?label').each{ subClass ->
                features[feature.key]['subClass'][subClass.subClass] = [label: subClass.label, 'subClass': [:]]
                grandChildren.each{
                    if(it.subClass == subClass.subClass) {
                        features[feature.key]['subClass'][subClass.subClass]['subClass'][it.id] = it
                    }
                }
            }
            categories += grandChildren.categoryList()
        }

        categories[k.toURI(':ProductionEnvironmentAlignmentCategory')] = []
        categories[k.toURI(':SugarcaneProcessingOptimizationCategory')] = []

        categories.each { key, v ->
            k[key].getIndividualsIdLabel().each {
                v.push(it)
            }
        }

        def fea = dsl.featureMap[k.toURI(':TechnologicalEfficiencyFeature')]
        technologyTypes = fea.evalObject(k.toURI([params.id]))

        println "* Tree *"
        Uri.printTree(features)

        /*
        println "Categories"
        categories.each{ category ->
            println category.key
            category.value.each{
                println "\t "+it
            }
        }
        */
        /*
        def assessmentID = params.assessment
        def name = k[':'+params.id].label
        def values = [:]
        def weights = [:]
        def report

        dsl._cleanProgram()

        if (assessmentID != null) {

            dsl.dimensions.each{ String dim ->
                k[dim].getGranchildrenIndividuals(assessmentID, '?id ?subClass ?in ?value ?weight').each{
                    values[it.id] = it.value
                }
            }

            k[':ProductionEfficiencyFeature'].getGranchildrenIndividuals(assessmentID, '?id ?subClass ?in ?value').each{
                values[it.id] = it.value
            }
            k[':TechnologicalEfficiencyFeature'].getGranchildrenIndividuals(assessmentID, '?id ?subClass ?in ?value ?weight').each{
                values[it.id] = it.value
                weights[it.id] = it.weight
            }

            dsl.data = new DataReader(k, k.toURI(':'+assessmentID))
            dsl.program()
            report = dsl.report

            //def page = g.render(template: 'report', model: [report: report])
            //lack generate the report file

            //def file = new File("reports/${evaluationID}.html")
            //file.write(page.toString())
        }
        */

        def name = k[':'+params.id].label
        def values = [:]
        def weights = [:]
        def report

        println params.id

        render(view: 'assessment',
               model: [evaluationObject: [id: params.id, name: name],
                       features: features,
                       categories: categories,
                       technologyTypes: technologyTypes,

                       values: values,
                       weights: weights,
                       report: report])
    }



    def assessments(){
        def id = k.shortURI(params.id)
        println params.id

        def assessments = k[id].labelAppliedTo

        render( template: 'assessments',
                model:    [assessments: assessments,
                           production_unit_id: id]);
    }

    def report(){
        def production_unit_id = params.production_unit_id

        def num = k[production_unit_id].getAssessments().size() + 1
        def assessment_name = production_unit_id+"-assessment-"+num

        k.insert( "<"+ k.toURI(":" + assessment_name) +">"+
                    " rdf:type :Evaluation;"+
                    " :appliedTo :"+ production_unit_id +";"+
                    " rdfs:label 'Avaliação "+  num +"'@pt.")

        def indicators = []

        dsl.dimensions.each{
            indicators += k[it].getGrandchildren('?id ?label ?subClass ?category ?valueType ?weight')
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

                k.insert( "<" +it.id+'-'+assessment_name +">"+
                            " rdf:type <"+ it.id +">;"+
                            " dc:isPartOf :"+ assessment_name +";"+
                            " :value "+  value +".")
                k.insert( ":" + assessment_name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+assessment_name+">.")
            }
        }

        def features = k[':ProductionEfficiencyFeature'].getGrandchildren('?id ?label ?subClass ?category ?valueType')

        features.each{
            if(params[it.id]){
                if(it.valueType == "http://bio.icmc.usp.br/sustenagro#Real"){
                    value = k.dataSchema(Integer.parseInt(params[it.id]))
                }
                else{
                    value = "<" + params[it.id] + ">"
                }

                k.insert( "<" +it.id+'-'+assessment_name +">"+
                        " rdf:type <"+ it.id +">;"+
                        " dc:isPartOf :"+ assessment_name +";"+
                        " :value "+  value +".")
                k.insert( ":" + assessment_name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+assessment_name+">.")

            }
        }

        def TechnologicalEfficiency = k[':TechnologicalEfficiencyFeature'].getGrandchildren('?id ?label ?subClass ?category ?valueType')
        def weighted

        TechnologicalEfficiency.each{
            if(params[it.id]){
                if(it.valueType == "http://bio.icmc.usp.br/sustenagro#Real"){
                    value = k.dataSchema(Integer.parseInt(params[it.id]))
                }
                else{
                    value = "<" + params[it.id] + ">"
                }

                if(it.subClass == 'http://bio.icmc.usp.br/sustenagro#TechnologicalEfficiencyInTheIndustrial'){
                    weighted = "<" +params[it.id+'-optimization'] + ">"
                }
                else if(it.subClass == 'http://bio.icmc.usp.br/sustenagro#TechnologicalEfficiencyInTheField'){
                    weighted = "<" +params[it.id+'-alignment'] + ">"
                }

                k.insert( "<" +it.id+'-'+assessment_name +">"+
                        " rdf:type <"+ it.id +">;"+
                        " dc:isPartOf :"+ assessment_name +";"+
                        " :value "+ value +";"+
                        " :hasWeight "+ weighted +"." )

                k.insert( ":" + assessment_name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+assessment_name+">.")
            }
        }

        redirect(action: 'assessment',
                id: params.production_unit_id,
                params: [assessment: assessment_name])
    }


}