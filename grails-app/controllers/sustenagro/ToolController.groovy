package sustenagro

import semantics.DataReader
import semantics.Node
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class ToolController {
    def dsl
    def gui
    def k
    def slugify
    def springSecurityService

    def evalobj() {
        def id = params.id
        def evalObjId = (id)? id : null
        def evaluationObject = dsl.evaluationObject
        def evaluationObjects = k['ui:EvaluationObject'].getIndividualsIdLabel()
        def analyses = (id)? k[':'+id].getAnalysesIdLabel() : []
        def activeTab = 'tab_0'
        def tabsWidgets = []

        evaluationObjects.each{
            it.id = it.id.substring(it.id.lastIndexOf('#')+1)
        }
        analyses.each{
            it.id = it.id.substring(it.id.lastIndexOf('#')+1)
        }

        if(id){
            evalObjId = evalObjId.substring(evalObjId.lastIndexOf('#')+1)
            activeTab = 'tab_1'
        }

        tabsWidgets.push(['widget': 'tab', attrs: [label: gui.widgetAttrs['createEvaluationObject'].title], widgets:[
                ['widget': 'createEvaluationObject', attrs : [id: evaluationObject.getURI()], widgets: evaluationObject.widgets]
        ]])
        tabsWidgets.push(['widget': 'tab', attrs: [label: gui.widgetAttrs['listEvaluationObjects'].title], widgets:[
                ['widget': 'listEvaluationObjects', attrs : [id: id]]
        ]])

        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)

        gui.navBarRoute([evaluationObjects: evaluationObjects, evalObjId: evalObjId, analyses: analyses])
        gui.title(['text': gui.variables.title])
        gui.description(['text': gui.variables.description])
        gui.tabs([activeTab: activeTab, pagination: false], tabsWidgets)

        render(view: 'evalobj', model: [inputs: gui.viewsMap[controllerName][actionName]])
    }

    def createEvaluationObject() {
        def id = ''
        def name = k.toURI(':hasName')
        def type = k.toURI(params['evalObjType'])
        def evaluationObject = dsl.evaluationObject
        def username = springSecurityService.getPrincipal().username
        //println username

        if(params['evalObjType'] && params[name] && params[type]){

            def node = new Node(k)
            def propertyInstances = [:]

            evaluationObject.model.each{ ins ->
                if(params[ins.id] && ins.id != type){
                    propertyInstances[k.toURI(ins.id)] = [value: params[ins.id], dataType: ins.dataType]
                }
            }

            id = slugify.slugify(params[name])
            node.insertEvaluationObject(id, params[type], propertyInstances)
        }

        //k.g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
        redirect(action: 'scenario', id: id)
    }

    def scenario(){
        def uri = k.toURI(':'+params.id)
        def evaluationObjects = k['ui:EvaluationObject'].getIndividualsIdLabel()
        def analyses = k[':'+params.id].getAnalysesIdLabel()
        def tabsWidgets = []
        def formAttrs = [:]
        def formWidgets = []
        def sustainabilityTabs = []

        evaluationObjects.each{
            it.id = it.id.substring(it.id.lastIndexOf('#')+1)
        }
        analyses.each{
            it.id = it.id.substring(it.id.lastIndexOf('#')+1)
        }

        dsl.featureMap.eachWithIndex { key, feature, int i ->
            sustainabilityTabs.push(['widget': 'tab', attrs: [label: feature.model.label], widgets: [['widget': 'individualsPanel', attrs : [data : feature.model.subClass,
                                                                                                                                             values: [:], weights: [:]]
                                                                                                     ]]
            ])
        }

        tabsWidgets.push(['widget': 'tab', attrs: [label: 'Avaliação da Sustentabilidade'], widgets: [
                ['widget': 'tabs', attrs: [id: 'sustainability', finalPag: 'efficiency_tab_0', finalPagLabel: 'Próximo', submit: true], widgets: sustainabilityTabs]
        ]])
        tabsWidgets.push(['widget': 'tab', attrs: [label: 'Avaliação da Eficiência'], widgets: [
                ['widget': 'tabs', attrs: [id : 'efficiency', initialPag: 'sustainability_tab_4', initialPagLabel: 'Anterior', submit: true], widgets: []]
        ]])

        /*
        dsl.featureMap.each{ key, feature ->
            println feature.model.label
            println feature.uri
            Uri.printTree(feature.model)
        }
        println evaluationObjects
        println analyses
        */

        formAttrs['action'] = '/tool/createScenario'
        formWidgets.push(['widget': 'hiddenInput', attrs: [id: 'evalObjInstance', value: uri]])
        formWidgets.push(['widget': 'tabs', attrs: [id: 'main', pagination: false], widgets: tabsWidgets])

        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)
        gui.navBarRoute([evaluationObjects: evaluationObjects, evalObjId: params.id, analyses: analyses])
        gui.form(formAttrs, formWidgets)

        //println "* Index Tree ${gui.viewsMap[controllerName][actionName].size()}*"
        //Uri.printTree(gui.viewsMap[controllerName][actionName])

        render(view: actionName, model: [data: gui.variables, inputs: gui.viewsMap[controllerName][actionName]])
    }

    def createScenario(){
        def evalObjURI = k.toURI(params.evalObjInstance)
        def num = k[evalObjURI].getAnalyses().size() + 1
        def name = evalObjURI.substring(evalObjURI.lastIndexOf('#')+1)
        def analysisId = name+"-analysis-"+num
        def properties = [:]
        def node = new Node(k)
        def individualKeys = []
        def weightedIndividualsKeys = []
        def weightedIndividuals = [:]
        def featureInstances = [:]
        def uri = ''

        properties[k.toURI('rdfs:label')] = k['ui:Analysis'].label+ " " + num
        properties[k.toURI(':appliedTo')] = evalObjURI

        dsl.featureMap.each{ key, feature ->
            individualKeys += feature.getIndividualKeys()
        }

        dsl.featureMap.each{ key, feature ->
            weightedIndividualsKeys += feature.getWeightedIndividualKeys()
        }

        weightedIndividualsKeys.each{
            uri = k.toURI(it)
            if(params[uri]){
                weightedIndividuals[uri.substring(0, uri.lastIndexOf('-'))] = params[uri]
            }
        }

        individualKeys.each{
            uri = k.toURI(it)
            if(params[uri]){
                if(!weightedIndividuals[uri]){
                    featureInstances[uri] = ['value': params[uri]]
                }
                else{
                    featureInstances[uri] = ['value': params[uri], 'weight': weightedIndividuals[uri]]
                }
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
        redirect(action: 'analysis', id: analysisId)
    }

    def analysis(){
        def uri = params.id ? k.toURI(":"+params.id) : null
        def evalObjId = k[uri].getAttr('appliedTo')
        def evaluationObjects = k['ui:EvaluationObject'].getIndividualsIdLabel()
        def analysisId = params.id
        def analyses = k['ui:Analysis'].getIndividualsIdLabel()

        evalObjId = evalObjId.substring(evalObjId.lastIndexOf('#')+1)
        evaluationObjects.each{
            it.id = it.id.substring(it.id.lastIndexOf('#')+1)
        }
        analyses.each{
            it.id = it.id.substring(it.id.lastIndexOf('#')+1)
        }

        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)

        /*
        println uri

        dsl.featureMap.each{ key, fea ->
            fea.model.subClass.each{ featureKey, feature ->
                feature.subClass.each{ indKey, ind->
                    println indKey
                    println ind
                }
            }
        }
        */


        if(uri?.trim()){
            dsl.setData(new DataReader(k, uri))
            dsl.runReport()
        }

        gui.setData('vars', dsl.getVariables())
        gui.setData('dataReader', dsl.getData('data'))
        gui.setData('reportView', dsl.getReportView())
        gui.navBarRoute([evaluationObjects: evaluationObjects, evalObjId: evalObjId, analyses: analyses, analysisId: analysisId ])
        gui.renderView(actionName)
        //gui.printData()

        /*
       def fea = dsl.featureMap[k.toURI(':TechnologicalEfficiencyFeature')]
       def technologyTypes = fea.evalObject(k.toURI([params.id]))
       def name = k[uri].label
       def report

       dsl.data = new DataReader(k, uri)
       dsl.assessmentProgram()
       dsl.viewsMap['tool']['data'] = dsl._analyzesMap['http://purl.org/biodiv/semanticUI#Analysis'].widgets

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

        render(view: actionName, model: [data: gui.variables, inputs: gui.viewsMap[controllerName][actionName]])
    }

    def analyses(){
        def uri = k.toURI(params.evaluation_object_id)
        def analyses = k[uri].labelAppliedTo
        def model = gui.widgetAttrs['analyses']
        model.analyses = analyses
        model.evaluation_object_id = uri

        render( template: '/widgets/analyses', model: model);
    }

    def selectEvaluationObject(){
        def uri = k.toURI(params.evaluation_object_id)
        def id =  uri.substring(uri.lastIndexOf('#')+1)
        redirect(action: 'scenario', id: id)
    }

    def selectAnalysis(){
        def uri = k.toURI(params.analysis)
        def id =  uri.substring(uri.lastIndexOf('#')+1)

        redirect( action: 'analysis', id: id)
    }
}
