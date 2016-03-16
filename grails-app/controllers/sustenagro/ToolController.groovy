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

        dsl._clean(controllerName, actionName)
        gui.selectEvaluationObject(gui.widgetAttrs['selectEvaluationObject'], dsl.evaluationObjectInstance.getURI())
        gui.createEvaluationObject(gui.widgetAttrs['createEvaluationObject'], dsl.evaluationObjectInstance.widgets, dsl.evaluationObjectInstance.getURI())
        gui._requestData(controllerName, actionName)

        //println "* Index Tree ${gui.viewsMap[controllerName][actionName].size()}*"
        //Uri.printTree(gui.viewsMap[controllerName][actionName])

        render(view: 'index', model: [data: dsl.props, inputs: gui.viewsMap[controllerName][actionName]])
    }

    def createEvaluationObject() {
        def id = ''
        def name = k.toURI(':hasName')
        def type = k.toURI(params['evalObjType'])

        if(params['evalObjType'] && params[name] && params[type]){

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
        def data = [:]
        def analyseUri
        def report

        if(params.analysis){
            analyseUri = k.toURI(":"+params.analysis)
        }
        data['indicators'] = [:]
        data['categories'] = [:]

        dsl.featureMap.each{ key, feature ->
            feature.features.each{
                data['indicators'][it.key] = it.value
            }
            data['categories'] += feature.categories
        }

        data['values'] = [:]
        data['weights'] = [:]
        data['submitLabel'] = gui.widgetAttrs['tabs'].submitLabel

        dsl._clean(controllerName, actionName)
        gui.paragraph([text: gui.widgetAttrs['paragraph'].text + '**'+ k[uri].label + '**'])
        gui.tabs(gui.widgetAttrs['tabs'], ['tab_0': data], uri)
        gui._requestData(controllerName, actionName)

        //println "* Index Tree ${gui.viewsMap[controllerName][actionName].size()}*"
        //Uri.printTree(gui.viewsMap[controllerName][actionName])


        if(analyseUri?.trim()){
            dsl.setData(new DataReader(k, analyseUri))
            dsl.program()
            report = dsl.report
        }

        render(view: 'assessment', model: [data: dsl.props, inputs: gui.viewsMap[controllerName][actionName]])

        /*
        def fea = dsl.featureMap[k.toURI(':TechnologicalEfficiencyFeature')]
        def technologyTypes = fea.evalObject(k.toURI([params.id]))
        def name = k[uri].label
        def report

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

    def report(){
        def evalObjInstance = k.toURI(params.evalObjInstance)
        def num = k[evalObjInstance].getAssessments().size() + 1
        def name = evalObjInstance.substring(evalObjInstance.lastIndexOf('#')+1)
        def analysisId = name+"-assessment-"+num
        def properties = [:]
        def node = new Node(k, '')
        def individualKeys = []
        def featureInstances = [:]
        def uri = ''

        properties[k.toURI('rdfs:label')] = k['ui:Analysis'].label+ " " + num
        properties[k.toURI(':appliedTo')] = evalObjInstance

        dsl.featureMap.each{
            individualKeys += it.value.getIndividualKeys()
        }

        individualKeys.each{
            uri = k.shortToURI(it)
            if(params[uri]){
                featureInstances[uri] = params[uri]
            }
        }

        node.insertAnalysis(analysisId, properties, featureInstances)


        /*
        def value

        indicators.each{
            if(params[it.id]){
                if(it.valueType == "http://bio.icmc.usp.br/sustenagro#Real"){
                    value = k.dataSchema(Integer.parseInt(params[it.id]))
                }
                else{
                    value = "<" + params[it.id] + ">"
                }

                k.insert( "<" +it.id+'-'+name +">"+
                        " rdf:type <"+ it.id +">;"+
                        " dc:isPartOf :"+ name +";"+
                        " :value "+  value +".")
                k.insert( ":" + name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+name+">.")
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

                k.insert( "<" +it.id+'-'+name +">"+
                        " rdf:type <"+ it.id +">;"+
                        " dc:isPartOf :"+ name +";"+
                        " :value "+  value +".")
                k.insert( ":" + name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+name+">.")

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

                k.insert( "<" +it.id+'-'+name +">"+
                        " rdf:type <"+ it.id +">;"+
                        " dc:isPartOf :"+ name +";"+
                        " :value "+ value +";"+
                        " :hasWeight "+ weighted +"." )

                k.insert( ":" + name +" <http://purl.org/dc/terms/hasPart> <"+ it.id+'-'+name+">.")
            }
        }
        */
        redirect(action: 'assessment',
                id: name,
                params: [analysis: analysisId])
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
}