package sustenagro

import semantics.DataReader
import semantics.Node
import grails.plugin.springsecurity.annotation.Secured
import utils.Uri

import java.text.SimpleDateFormat

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
        def evaluationObjects = [:]
        def analyses = [:]
        def activeTab = 'tab_0'
        def username = springSecurityService.getPrincipal().username

        k['inds:'+username].getEvaluationObjectsIdLabel().each{
            evaluationObjects[it.id.substring(it.id.lastIndexOf('#')+1)] = [label: it.label]
        }

        if(id){
            k['inds:'+id].getAnalysesIdLabel().each{
                analyses[it.id.substring(it.id.lastIndexOf('#')+1)] = [label: it.label]
            }

            evalObjId = evalObjId.substring(evalObjId.lastIndexOf('#')+1)
            activeTab = 'tab_1'
        }

        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)

        //Uri.printTree(evaluationObject.widgets)

        gui.setData('evaluationObjects', evaluationObjects)
        gui.setData('evalObjId', evalObjId)
        gui.setData('analyses', analyses)
        gui.setData('analysisId', null)
        gui.setData('activeTab', activeTab)
        gui.setData('evaluationObject', evaluationObject)
        gui.setData('username', username)
        gui.setData('id', id)
        gui.renderView(actionName)

        render(view: actionName, model: [inputs: gui.viewsMap[controllerName][actionName]])
    }

    def createEvaluationObject() {
        def name = k.toURI('ui:hasName')
        def id = slugify.slugify(params[name])
        def type = k.toURI('rdfs:subClassOf')
        def evaluationObject = dsl.evaluationObject
        def username = springSecurityService.getPrincipal().username
        def user = k.toURI('inds:'+username)

        if(params[name] && params[type]){

            def node = new Node(k)
            def propertyInstances = [:]
            def now = new Date()
            def value

            propertyInstances[k.toURI(':hasOwner')] = [value: user, dataType: k.toURI('ui:User')]
            propertyInstances[k.toURI('ui:createAt')] = [value: new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(now), dataType: k.toURI('xsd:dateTime')]
            evaluationObject.model.each{ ins ->
                if(params[ins.id] && ins.id != type) {
                    value = params[ins.id]
                    if (ins.dataType == 'http://www.w3.org/2001/XMLSchema#date') {
                        value =  new SimpleDateFormat("dd/MM/yyyy").parse(value).format("yyyy-MM-dd");
                    }
                    propertyInstances[k.toURI(ins.id)] = [value: value, dataType: ins.dataType]
                }
            }

            //println id
            //println propertyInstances

            node.insertEvaluationObject(id, params[type], propertyInstances)

            propertyInstances = [:]
            propertyInstances[k.toURI(':hasEvaluationObject')] = [value: k.toURI('inds:'+id), dataType: k.toURI('ui:EvaluationObject')]

            node.insertTriples(user, propertyInstances)
        }

        //k.g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
        redirect(action: 'scenario', id: id)
    }

    def scenario(){
        def uri = k.toURI('inds:'+params.id)
        def evaluationObjects = [:]
        def analyses = [:]
        def sustainabilityTabs = []
        def efficiencyTabs = []
        def username = springSecurityService.getPrincipal().username

        k['inds:'+username].getEvaluationObjectsIdLabel().each{
            evaluationObjects[it.id.substring(it.id.lastIndexOf('#')+1)] = [label: it.label]
        }

        k[uri].getAnalysesIdLabel().each {
            analyses[it.id.substring(it.id.lastIndexOf('#') + 1)] = [label: it.label]
        }

        def options = k[':SustainabilityCategory'].getIndividualsIdValueLabel()
        def widgets

        dsl.featureMap.eachWithIndex{ key, feature, int i ->
            //println key
            //println feature
            //println feature.model
            widgets = []
            widgets.push(['widget': 'individualsPanel', attrs : [data : feature.model.subClass, values: [:]]])
            if(feature.attrs.extraFeatures){
                widgets.push(['widget': 'extraFeatures', attrs: [id: key, name: feature.name, options: options, title: 'Indicadores específicos', header: ['ui:hasName': 'Nome', ':hasJustification': 'Justificativa', 'ui:value': 'Valor']]])
            }

            if(feature.model.superClass.contains(k.toURI(':Variable')))
                efficiencyTabs.push(['widget': 'tab', attrs: [label: feature.model.label], widgets: widgets])
            else if(feature.model.superClass.contains(k.toURI(':SustainabilityIndicator')))
                sustainabilityTabs.push(['widget': 'tab', attrs: [label: feature.model.label], widgets: widgets])
        }

        /*
        dsl.featureMap.each{ key, feature ->
            println feature.model.label
            println feature.uri
            Uri.printTree(feature.model)
        }
        /*
        println evaluationObjects
        println analyses
        */

        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)
        gui.setData('evaluationObjects', evaluationObjects)
        gui.setData('evalObjId', params.id)
        gui.setData('analyses', analyses)
        gui.setData('analysisId', null)
        gui.setData('uri', uri)
        gui.setData('efficiencyTabs', efficiencyTabs)
        gui.setData('sustainabilityTabs', sustainabilityTabs)
        gui.renderView(actionName)

        //println "* Index Tree ${gui.viewsMap[controllerName][actionName].size()}*"
        //Uri.printTree(gui.viewsMap[controllerName][actionName])

        render(view: actionName, model: [inputs: gui.viewsMap[controllerName][actionName]])
    }

    def createScenario(){
        def now = new Date()
        def evalObjURI = k.toURI(params.evalObjInstance)
        def evalObjName = evalObjURI.substring(evalObjURI.lastIndexOf('#')+1)
        def name = k[':Harvest'].label+ " " + k[evalObjURI].getAttr('?harvestYear')
        def analysisSize = k[evalObjURI].getAnalysisLabel(name).size();
        def analysisId = evalObjName+"-analysis-"+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(now)
        def properties = [:]
        def node = new Node(k)
        def individualKeys = []
        def paramValue
        def paramWeight
        def paramJustification
        def valueIndividuals = [:]
        def weightIndividuals = [:]
        def featureInstances = [:]
        def extraFeatures = [:]
        def uri = ''

        if(analysisSize > 0)
            name += " ($analysisSize)"

        properties[k.toURI('rdfs:label')] = [value: name, dataType: k.toURI('rdfs:Literal')]     //new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now)
        properties[k.toURI(':appliedTo')] = [value: evalObjURI, dataType: k[':appliedTo'].range]
        properties[k.toURI('ui:createAt')] = [value: new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(now), dataType: k.toURI('xsd:dateTime')]

        /*dsl.featureMap.each{ key, feature ->
            println feature.model.label
            println feature.uri
            Uri.printTree(feature.model)
        }*/

        dsl.featureMap.each{ key, feature ->
            valueIndividuals << feature.getValueIndividuals()
            weightIndividuals << feature.getWeightIndividuals()
            individualKeys += feature.getIndividualKeys()
            extraFeatures[key] = [:]
        }

        individualKeys.each{
            uri = k.toURI(it)
            paramValue = params[uri]
            paramWeight = params[uri+'-weight']
            paramJustification = params[uri+'-justification']
            if(paramValue){
                featureInstances[uri] = k.isURI(paramValue)? ['value': paramValue] : ['value': valueIndividuals[paramValue]]
                if(paramWeight)
                    featureInstances[uri]['weight'] = k.isURI(paramWeight)? paramWeight : weightIndividuals[paramWeight]
                if(paramJustification)
                    featureInstances[uri]['justification'] = paramJustification

                //println uri + ' - ' +featureInstances[uri]
            }
        }


        def attr
        extraFeatures.each{ key, map ->
            params.each{ pKey, value ->
                if(pKey.getClass() == String && pKey.startsWith(key) && value){
                    attr = pKey.tokenize('[]')
                    if(!map.hasProperty(attr[1]) && !map[attr[1]]){
                        map[attr[1]] = [:]
                    }
                    map[attr[1]][attr[2]] = [value: value, dataType: k[attr[2]].range]
                }
            }
        }
        //println analysisId
        //Uri.printTree(featureInstances)

        //println extraFeatures

        node.insertAnalysis(analysisId, properties)

        node.insertFeatures(analysisId, featureInstances)

        node.insertExtraFeatures(analysisId, extraFeatures)

        redirect(action: 'analysis', id: analysisId)
    }

    def updateScenario(){
        def now = new Date()
        def analysisId = params.analysisId
        def analysisURI = k.toURI('inds:'+analysisId)
        def evalObjURI = k[analysisURI].getAttr('appliedTo')
        def evalObjName = evalObjURI.substring(evalObjURI.lastIndexOf('#')+1)
        def name = k[analysisURI].getAttr('label')
        def node = new Node(k)
        def properties = [:]

        println evalObjURI
        println evalObjName
        println name

        properties[k.toURI('rdfs:label')] = [value: name, dataType: k.toURI('rdfs:Literal')]     //new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now)
        properties[k.toURI(':appliedTo')] = [value: evalObjURI, dataType: k[':appliedTo'].range]
        properties[k.toURI('ui:createAt')] = [value: new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(now), dataType: k.toURI('xsd:dateTime')]

        node.deleteAnalysis(analysisId)

        redirect(action: 'analysis', id: analysisId)
    }

    def analysis(){
        def uri = params.id ? k.toURI("inds:"+params.id) : null
        def evalObjId = k[uri].getAttr('appliedTo')
        def evaluationObjects = [:]
        def analysisId = params.id
        def analyses = [:]
        def sustainabilityTabs = []
        def efficiencyTabs = []
        def username = springSecurityService.getPrincipal().username
        def res
        def values = [:]

        k['inds:'+username].getEvaluationObjectsIdLabel().each{
            evaluationObjects[it.id.substring(it.id.lastIndexOf('#')+1)] = [label: it.label]
        }

        k[evalObjId].getAnalysesIdLabel().each {
            analyses[it.id.substring(it.id.lastIndexOf('#') + 1)] = [label: it.label]
        }

        evalObjId = evalObjId.substring(evalObjId.lastIndexOf('#')+1)
        def options = k[':SustainabilityCategory'].getIndividualsIdValueLabel()
        def widgets

        dsl.featureMap.eachWithIndex { key, feature, int i ->
            //res = k[key].getChildrenIndividuals(uri, '?id ?ind ?valueType ?weightType')
            res = k[key].getGrandChildrenIndividuals(uri, '?id ?ind ?justification ?valueType ?weightType')

            //println key
            //Uri.printTree(res)

            res.each{
                values[it.id] = [:]
                if(it.valueType)
                    values[it.id].value = it.valueType
                if(it.weightType)
                    values[it.id].weight = it.weightType
                if(it.justification)
                    values[it.id].justification = it.justification
            }

            //Uri.printTree(values)

            widgets = []
            widgets.push(['widget': 'individualsPanel', attrs : [data : feature.model.subClass, values: values]])
            if(feature.attrs.extraFeatures){
                widgets.push(['widget': 'extraFeatures', attrs: [id: key, name: feature.name, options: options, title: 'Indicadores específicos', header: ['ui:hasName': 'Nome', ':hasJustification': 'Justificativa', 'ui:value': 'Valor']]])
            }

            if(feature.model.superClass.contains(k.toURI(':Variable')))
                efficiencyTabs.push(['widget': 'tab', attrs: [label: feature.model.label], widgets: widgets])
            if(feature.model.superClass.contains(k.toURI(':SustainabilityIndicator')))
                sustainabilityTabs.push(['widget': 'tab', attrs: [label: feature.model.label], widgets: widgets])
        }

        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)

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

        if(uri?.trim()){
            dsl.setData(new DataReader(k, uri))
            dsl.runReport()
        }

        gui.setData('evaluationObjects', evaluationObjects)
        gui.setData('evalObjId', evalObjId)
        gui.setData('analyses', analyses)
        gui.setData('analysisId', analysisId)
        gui.setData('vars', dsl.getVariables())
        gui.setData('dataReader', dsl.getData('data'))
        gui.setData('sustainabilityTabs', sustainabilityTabs)
        gui.setData('efficiencyTabs', efficiencyTabs)
        gui.setData('reportView', dsl.getReportView())

        gui.renderView(actionName)

        render(view: actionName, model: [inputs: gui.viewsMap[controllerName][actionName]])
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

    def evaluationObjectNameAvailability(){
        def name = slugify.slugify(params['http://purl.org/biodiv/semanticUI#hasName'])
        render !k['inds:'+name].exist()
    }

    def microregions(){
        def microregions = k[params['http://dbpedia.org/ontology/state']].getMicroregions()
        render( template: '/widgets/category', model: [id: 'http://purl.org/biodiv/semanticUI#hasMicroregion', data: microregions, header: 'Opções', selectType: 'radio']);
    }
}
