package sustenagro

import com.github.slugify.Slugify
import semantics.DataReader
import semantics.Node
import utils.Uri

class ToolController {
    def dsl
    def gui
    def k
    def md
    def slugify

    def index() {

        println dsl.evaluationObjectInstance.getURI()
        dsl._clean(controllerName, actionName)
        gui.selectEvaluationObject(gui.widgetAttrs['selectEvaluationObject'], dsl.evaluationObjectInstance.getURI())
        gui.createEvaluationObject(gui.widgetAttrs['createEvaluationObject'], dsl.evaluationObjectInstance.getURI())
        gui._requestData(controllerName, actionName)

        //println "* Index Tree ${gui.viewsMap[controllerName][actionName].size()}*"
        //Uri.printTree(gui.viewsMap[controllerName][actionName])

        render(view: 'index', model: [data: dsl.props, inputs: gui.viewsMap[controllerName][actionName]])
    }

    def createEvaluationObject() {
        def id = ''
        def name = k.toURI(':hasName')
        def type = k.toURI(params['evaluationObject'])

        if(params['evaluationObject'] && params[name] && params[type]){

            def node = new Node(k, '')

            def propertyInstances = [:]

            def instances = dsl.evaluationObjectInstance.model

            instances.each{ ins ->
                if(params[ins.id] && ins.id != type){
                    propertyInstances[k.shortToURI(ins.id)] = [value: params[ins.id], dataType: ins.dataType]
                }
            }

            id = slugify.slugify(params[name])
            node.insertEvaluationObject(id, params[type], propertyInstances)
        }

        //k.g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
        redirect(action: 'assessment', id: id)
    }

    def assessment(){
        def uri = k.toURI(':'+params.id)

        dsl._clean(controllerName, actionName)
        gui.paragraph([text: gui.widgetAttrs['assessment/paragraph'].text + '**'+ k[uri].label + '**'])
        gui.tabs(gui.widgetAttrs['tabs'], 'assessment')

        gui._requestData(controllerName, actionName)

        println "* Index Tree ${gui.viewsMap[controllerName][actionName].size()}*"
        Uri.printTree(gui.viewsMap[controllerName][actionName])

        render(view: 'assessment', model: [data: dsl.props, inputs: gui.viewsMap[controllerName][actionName]])


        /*
        *
        *
        * */
        /*
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

        def method = 'getIndividualsIdLabel'

        categories.each { key, v ->
            k[key]."${method}"().each{            //getIndividualsIdLabel().each {
                v.push(it)
            }
        }

        println "* Tree *"
        Uri.printTree(features)

        println "Categories"
        categories.each{ category ->
            println category.key
            category.value.each{
                println "\t "+it
            }
        }


        def fea = dsl.featureMap[k.toURI(':TechnologicalEfficiencyFeature')]
        def technologyTypes = fea.evalObject(k.toURI([params.id]))
        def assessmentID = params.assessment
        def uri = k.toURI(':'+params.id)
        def name = k[uri].label
        def values = [:]
        def weights = [:]
        def report

        println uri
        println assessmentID

        dsl._cleanProgram()

        dsl.data = new DataReader(k, uri)
        dsl.assessmentProgram()
        dsl.viewsMap['tool']['assessment'] = dsl.analyzesMap['http://purl.org/biodiv/semanticUI#Analysis'].widgets

        //Closure within map for reference it


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


    }

    def selectEvaluationObject(){
        def id = k.shortURI(params.production_unit_id)

        redirect(   action: 'assessment',
                    id: id)
    }

    def selectAssessment(){
        def id = k.shortURI(params.production_unit_id)
        def name = k.shortURI(params.assessment)

        redirect(   action: 'assessment',
                id: id,
                params: [assessment: name])
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