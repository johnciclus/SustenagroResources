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

        k[':'+username].getEvaluationObjectsIdLabel().each{
            evaluationObjects[it.id.substring(it.id.lastIndexOf('#')+1)] = [label: it.label]
        }

        if(id){
            k[':'+id].getAnalysesIdLabel().each{
                analyses[it.id.substring(it.id.lastIndexOf('#')+1)] = [label: it.label]
            }

            evalObjId = evalObjId.substring(evalObjId.lastIndexOf('#')+1)
            activeTab = 'tab_1'
        }

        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)

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
        def name = k.toURI(':hasName')
        def id = slugify.slugify(params[name])
        def type = k.toURI(params['evalObjType'])
        def evaluationObject = dsl.evaluationObject
        def username = springSecurityService.getPrincipal().username
        def user = k.toURI(':'+username)
        //println username

        if(params['evalObjType'] && params[name] && params[type]){

            def node = new Node(k)
            def propertyInstances = [:]
            def now = new Date()
            def value
            //println new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now)

            //http://www.w3.org/2001/XMLSchema#dateTime
            propertyInstances[k.toURI(':hasOwner')] = [value: user, dataType: k.toURI('ui:User')]
            evaluationObject.model.each{ ins ->
                if(params[ins.id] && ins.id != type) {
                    value = params[ins.id]
                    if (ins.dataType == 'http://www.w3.org/2001/XMLSchema#date') {
                        value =  new SimpleDateFormat("dd/MM/yyyy").parse(value).format("yyyy-MM-dd");
                    }
                    propertyInstances[k.toURI(ins.id)] = [value: value, dataType: ins.dataType]
                }
            }

            node.insertEvaluationObject(id, params[type], propertyInstances)

            propertyInstances = [:]
            propertyInstances[k.toURI(':hasEvaluationObject')] = [value: k.toURI(':'+id), dataType: k.toURI('ui:EvaluationObject')]

            //println propertyInstances

            node.insertTriples(user, propertyInstances)
        }

        //k.g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
        redirect(action: 'scenario', id: id)
    }

    def scenario(){
        def uri = k.toURI(':'+params.id)
        def evaluationObjects = [:]
        def analyses = [:]
        def sustainabilityTabs = []
        def efficiencyTabs = []
        def username = springSecurityService.getPrincipal().username

        k[':'+username].getEvaluationObjectsIdLabel().each{
            evaluationObjects[it.id.substring(it.id.lastIndexOf('#')+1)] = [label: it.label]
        }

        k[uri].getAnalysesIdLabel().each {
            analyses[it.id.substring(it.id.lastIndexOf('#') + 1)] = [label: it.label]
        }

        def options = k[':SustainabilityCategory'].getIndividualsIdValueLabel()

        dsl.featureMap.eachWithIndex { key, feature, int i ->
            //println key
            //println feature
            //println feature.model

            if(feature.model.superClass.contains(k.toURI(':EfficiencyIndicator')))
                efficiencyTabs.push(['widget': 'tab', attrs: [label: feature.model.label], widgets: [
                    ['widget': 'individualsPanel', attrs : [data : feature.model.subClass, values: [:], weights: [:]]]
                ]])

            if(feature.model.superClass.contains(k.toURI(':SustainabilityIndicator')))
                sustainabilityTabs.push(['widget': 'tab', attrs: [label: feature.model.label], widgets: [
                    ['widget': 'individualsPanel', attrs : [data : feature.model.subClass, values: [:], weights: [:]]],
                    ['widget': 'specificIndicators', attrs: [id: key, name: feature.name, options: options, title: 'Indicadores específicos', header: [':hasName': 'Nome', ':hasJustification': 'Justificativa', 'ui:value': 'Valor']]]
                ]])
        }

        /*
        dsl.featureMap.each{ key, feature ->
            println feature.model.label
            println feature.uri
            Uri.printTree(feature.model)
        }
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
        def name = evalObjURI.substring(evalObjURI.lastIndexOf('#')+1)
        def analysisId = name+"-analysis-"+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(now)
        def properties = [:]
        def node = new Node(k)
        def individualKeys = []
        def weightedIndividualsKeys = []
        def weightedIndividuals = [:]
        def featureInstances = [:]
        def extraFeatures = [:]
        def uri = ''

        properties[k.toURI('rdfs:label')] = [value: k['ui:Analysis'].label+ " " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now), dataType: k.toURI('rdfs:Literal')]
        properties[k.toURI(':appliedTo')] = [value: evalObjURI, dataType: k[':appliedTo'].range]

        dsl.featureMap.each{ key, feature ->
            individualKeys += feature.getIndividualKeys()
            weightedIndividualsKeys += feature.getWeightedIndividualKeys()
            extraFeatures[key] = [:]
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


        //Uri.printTree(extraFeatures)

        node.insertAnalysis(analysisId, properties)

        node.insertFeatures(analysisId, featureInstances)

        node.insertExtraFeatures(analysisId, extraFeatures)
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
        def evaluationObjects = [:]
        def analysisId = params.id
        def analyses = [:]
        def sustainabilityTabs = []
        def efficiencyTabs = []
        def username = springSecurityService.getPrincipal().username

        k[':'+username].getEvaluationObjectsIdLabel().each{
            evaluationObjects[it.id.substring(it.id.lastIndexOf('#')+1)] = [label: it.label]
        }

        k[evalObjId].getAnalysesIdLabel().each {
            analyses[it.id.substring(it.id.lastIndexOf('#') + 1)] = [label: it.label]
        }

        evalObjId = evalObjId.substring(evalObjId.lastIndexOf('#')+1)


        dsl.featureMap.eachWithIndex { key, feature, int i ->
            if(feature.model.superClass.contains(k.toURI(':SustainabilityIndicator')))
                sustainabilityTabs.push(['widget': 'tab', attrs: [label: feature.model.label], widgets: [['widget': 'individualsPanel', attrs : [data : feature.model.subClass, values: [:], weights: [:]]]]])
            if(feature.model.superClass.contains(k.toURI(':EfficiencyIndicator')))
                efficiencyTabs.push(['widget': 'tab', attrs: [label: feature.model.label], widgets: [['widget': 'individualsPanel', attrs : [data : feature.model.subClass, values: [:], weights: [:]]]]])
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
}
