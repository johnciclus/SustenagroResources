package sustenagro

import semantics.DataReader
import semantics.Node
import org.apache.commons.io.FilenameUtils

import grails.plugin.springsecurity.annotation.Secured
import utils.Uri

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class ToolController {
    def dsl
    def gui
    def k
    def slugify
    def springSecurityService

    def index() {

        def evaluationObject = dsl.evaluationObject
        def tabsAttrs = [:]
        def tabsWidgets

        tabsAttrs.labels = ['tab_0': gui.widgetAttrs['createEvaluationObject'].title,
                            'tab_1': gui.widgetAttrs['selectEvaluationObject'].title]
        tabsAttrs.pager = false

        tabsWidgets     =  ['tab_0': [['widget': 'createEvaluationObject', widgets: evaluationObject.widgets, id: evaluationObject.getURI()]],
                            'tab_1': [['widget': 'selectEvaluationObject', id: evaluationObject.getURI()]]]

        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)
        gui.tabs(tabsAttrs, tabsWidgets)

        render(view: 'index', model: [data: gui._props, inputs: gui.viewsMap[controllerName][actionName]])
    }

    def createEvaluationObject() {
        def id = ''
        def name = k.toURI(':hasName')
        def type = k.toURI(params['evalObjType'])
        def evaluationObject = dsl.evaluationObject

        def username = springSecurityService.getPrincipal().username
        //println username

        if(params['evalObjType'] && params[name] && params[type]){

            def node = new Node(k, '')
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
        def tabsAttrs = [:]
        def tabsWidgets = [:]
        def formAttrs = [:]
        def formWidgets = []
        def tab_prefix = 'tab_'

        tabsAttrs.labels = [:]
        dsl.featureMap.eachWithIndex{ key, feature, int i ->

            tabsAttrs.labels[tab_prefix+i] = feature.model.label
            tabsWidgets[tab_prefix+i] = [['widget': 'individualsPanel',
                                          attrs: [data: feature.model.subClass,
                                                  values: [:], weights: [:]]
                                        ]]

        }

        dsl.featureMap.each{ key, feature ->
            println feature.model.label
            println feature.uri
            Uri.printTree(feature.model)
        }

        tabsAttrs.submit = true

        formAttrs['action'] = '/tool/createScenario'
        formWidgets.push(['widget': 'hiddenInput', attrs: [id: 'evalObjInstance', value: uri]])
        formWidgets.push(['widget': 'tabs', attrs: tabsAttrs, widgets: tabsWidgets])

        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)
        gui.text([text: gui.widgetAttrs['text'].text + '**'+ k[uri].label + '**'])
        gui.form(formAttrs, formWidgets)

        //println "* Index Tree ${gui.viewsMap[controllerName][actionName].size()}*"
        //Uri.printTree(gui.viewsMap[controllerName][actionName])

        render(view: 'scenario', model: [data: gui._props, inputs: gui.viewsMap[controllerName][actionName]])
    }

    def createScenario(){
        def evalObjInstance = k.toURI(params.evalObjInstance)
        def num = k[evalObjInstance].getAnalyses().size() + 1
        def name = evalObjInstance.substring(evalObjInstance.lastIndexOf('#')+1)
        def analysisId = name+"-analysis-"+num
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
            uri = k.toURI(it)
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
        redirect(action: 'analysis', id: analysisId)
    }

    def analysis(){
        def uri

        if(params.id){
            uri = k.toURI(":"+params.id)
        }

        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)

        if(uri?.trim()){
            dsl.setData(new DataReader(k, uri))
            dsl.runReport()
        }

        gui.setData('variables', dsl.getVariables())
        gui.setData('dataReader', dsl.getData('data'))
        gui.setData('reportView', dsl.getReportView())
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

        render(view: 'analysis', model: [data: gui._props, inputs: gui.viewsMap[controllerName][actionName]])
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
