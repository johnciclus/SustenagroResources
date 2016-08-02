package sustenagro

import org.springframework.web.servlet.support.RequestContextUtils
import semantics.DataReader
import semantics.Node
import grails.plugin.springsecurity.annotation.Secured
import org.apache.commons.lang.StringEscapeUtils
import dsl.GUIDSL
import java.text.ParsePosition
import java.text.SimpleDateFormat

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class ToolController {
    static allowedMethods = [evaluationObject: "GET",
                             inputFeatures: "GET",
                             saveFeatures: "POST",
                             analysis: "GET",
                             createEvaluationObject: "POST",
                             createAnalysis: "POST",
                             updateAnalysis: "POST",
                             selectEvaluationObject: "POST",
                             selectAnalysis: "POST",
                             evaluationObjectNameAvailability: "GET",
                             evaluationObjectView: "POST",
                             analysesView: "POST",
                             microregionsView: "POST"
                            ]
    def dsl
    //def gui
    def k
    def slugify
    def springSecurityService

    def evaluationObject() {
        //println session.getClass()
        //println "Timeout: ${session.getMaxInactiveInterval()} seconds"
        //println session.getCreationTime()
        //println session.getLastAccessedTime()
        //println session.getLastAccessedTime() - session.getCreationTime()

        def locale = RequestContextUtils.getLocale(request)
        def username = springSecurityService.principal.username
        def userId = username
        def evalObjId = (params.id)? params.id : null
        def activeTab = 'tab_0'
        def roles = k['inds:'+username].getAttr('hasRole')
        def evaluationObjectURI = dsl.evaluationObject.getURI()
        def widgets = dsl.evaluationObject.getWidgets(locale)
        def _gui = new GUIDSL('dsl/gui.groovy', grailsApplication.mainContext)

        if(userId && (evalObjId == null || k['inds:'+evalObjId].exist())){
            if(roles.contains(k.toURI('ui:AdminRole'))){
                if(evalObjId){
                    userId = k['inds:'+evalObjId].getAttr('hasOwner')
                    userId = userId.substring(userId.lastIndexOf('#')+1)
                }
                if(params.user)
                    userId = params.user
            }

            if(evalObjId){
                activeTab = 'tab_1'
            }

            _gui.setView(controllerName, actionName)
            dsl.clean(controllerName, actionName)

            _gui.setData('username', username)
            _gui.setData('userId', userId)
            _gui.setData('evalObjId', evalObjId)
            _gui.setData('analysisId', null)
            _gui.setData('activeTab', activeTab)
            _gui.setData('evaluationObjectURI', evaluationObjectURI)
            _gui.setData('widgets', widgets)

            _gui.renderView(actionName)

            render(view: actionName, model: [inputs: _gui.viewsMap[controllerName][actionName]])
        }else{
            response.sendError(404)
        }
    }

    def createEvaluationObject() {
        def username = springSecurityService.principal.username
        def user = k.toURI('inds:'+username)
        def name = k.toURI('ui:hasName')
        def id = slugify.slugify(username+'-'+params[name])
        def type = k.toURI('rdfs:subClassOf')
        def evaluationObject = dsl.evaluationObject

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
        redirect(action: 'inputFeatures', id: id)
    }

    def inputFeatures() {
        def now = new Date()
        def username = springSecurityService.principal.username
        def userId = username
        def uri = k.toURI('inds:' + params.id)
        def sustainabilityTabs = []
        def efficiencyTabs = []
        def roles = k['inds:' + username].getAttr('hasRole')
        def evalObjId = params.id
        def analysisId = evalObjId+"-analysis-"+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(now)
        def _gui = new GUIDSL('dsl/gui.groovy', grailsApplication.mainContext)

        //println session['lang']

        if (userId && k['inds:' + evalObjId].exist()) {
            if (roles.contains(k.toURI('ui:AdminRole'))) {
                if (evalObjId) {
                    userId = k['inds:' + evalObjId].getAttr('hasOwner')
                    userId = userId.substring(userId.lastIndexOf('#') + 1)
                }
                if (params.user)
                    userId = params.user
            }

            def options = k[':SustainabilityCategory'].getIndividualsIdValueLabel()
            def widgets

            dsl.featureMap.eachWithIndex { key, feature, int i ->
                //println key
                //println feature
                //Uri.printTree(feature.model)
                //println key
                //println feature

                widgets = []
                widgets.push(['widget': 'individualsPanel', attrs: [data: feature.getModel(evalObjId).subClass, values: [:]]])
                if (feature.attrs.extraFeatures) {
                    widgets.push(['widget': 'extraFeatures', attrs: [id: key, name: feature.name, options: options, values: [:], title: 'Indicadores específicos', header: ['ui:hasName': 'Nome', ':hasJustification': 'Justificativa', 'ui:value': 'Valor']]])
                }

                if (feature.getModel(evalObjId).superClass.contains(k.toURI(':Variable')))
                    efficiencyTabs.push(['widget': 'tab', attrs: [label: feature.getModel(evalObjId).label], widgets: widgets])
                else if (feature.getModel(evalObjId).superClass.contains(k.toURI(':SustainabilityIndicator')))
                    sustainabilityTabs.push(['widget': 'tab', attrs: [label: feature.getModel(evalObjId).label], widgets: widgets])
            }

            _gui.setView(controllerName, actionName)
            dsl.clean(controllerName, actionName)

            _gui.setData('username', username)
            _gui.setData('userId', userId)
            _gui.setData('evalObjId', evalObjId)
            _gui.setData('analysisId', analysisId)
            _gui.setData('uri', uri)
            _gui.setData('efficiencyTabs', efficiencyTabs)
            _gui.setData('sustainabilityTabs', sustainabilityTabs)
            _gui.renderView(actionName)

            //println "* Index Tree ${gui.viewsMap[controllerName][actionName].size()}*"
            //Uri.printTree(gui.viewsMap[controllerName][actionName])

            render(view: actionName, model: [inputs: _gui.viewsMap[controllerName][actionName]])
        }else{
            response.sendError(404)
        }
    }

    def saveFeatures(){
        def username = springSecurityService.principal.username
        def userId = username
        def input = []
        def evalObjId
        def analysisId = params.analysisId
        def _gui = new GUIDSL('dsl/gui.groovy', grailsApplication.mainContext)

        createAnalysisAndFeatures(params)

        evalObjId = k['inds:'+analysisId].getAttr('appliedTo')
        evalObjId = evalObjId.substring(evalObjId.lastIndexOf('#') + 1)

        _gui.navBarRoute([username: username, userId: userId, evalObjId: evalObjId, analysisId: analysisId], input)

        render( template: '/widgets/navbarRoute', model: input[0].attrs)
    }

    def createAnalysis(){
        createAnalysisAndFeatures(params)
        redirect(action: 'analysis', id: params.analysisId)
    }

    def createAnalysisAndFeatures(parameters){
        println "params.analysisId: "+parameters.analysisId

        def evalObjURI = k.toURI(parameters.evalObjInstance)
        def analysisId = parameters.analysisId
        def node = new Node(k)
        def properties = [:]
        def exist = k['inds:'+analysisId].exist()
        def name = k[':Harvest'].label+ " " + k[evalObjURI].getAttr('?harvestYear')
        def timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(analysisId, new ParsePosition(analysisId.length()-19));
        def analysisSize = k[evalObjURI].getAnalysisLabel(name).size();

        if(exist){
            name = k['inds:'+analysisId].getAttr('label')
            properties[k.toURI('ui:updateAt')] = [value: new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()), dataType: k.toURI('xsd:dateTime')]

            node.deleteFeatures(analysisId)
            node.deleteAnalysis(analysisId)
        }
        else if(!exist && analysisSize > 0){
            name += " ($analysisSize)"
        }

        properties[k.toURI('rdfs:label')] = [value: name, dataType: k.toURI('rdfs:Literal')]     //new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now)
        properties[k.toURI(':appliedTo')] = [value: evalObjURI, dataType: k[':appliedTo'].range]
        properties[k.toURI('ui:createAt')] = [value: new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(timestamp), dataType: k.toURI('xsd:dateTime')]

        node.insertAnalysis(analysisId, properties)
        node.insertFeatures(analysisId, featuresInstances(parameters))
        node.insertExtraFeatures(analysisId, extraFeaturesInstances(parameters))
    }

    def updateAnalysis(){
        def now = new Date()
        def analysisId = params.analysisId
        def analysisURI = k.toURI('inds:'+analysisId)
        def evalObjURI = k[analysisURI].getAttr('appliedTo')
        def createAt = k[analysisURI].getAttr('createAt')
        def name = k[analysisURI].getAttr('label')
        def node = new Node(k)
        def properties = [:]

        properties[k.toURI('rdfs:label')] = [value: name, dataType: k.toURI('rdfs:Literal')]     //new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now)
        properties[k.toURI(':appliedTo')] = [value: evalObjURI, dataType: k[':appliedTo'].range]
        properties[k.toURI('ui:createAt')] = [value: createAt, dataType: k.toURI('xsd:dateTime')]
        properties[k.toURI('ui:updateAt')] = [value: new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(now), dataType: k.toURI('xsd:dateTime')]

        node.deleteFeatures(analysisId)
        node.deleteAnalysis(analysisId)

        node.insertAnalysis(analysisId, properties)
        node.insertFeatures(analysisId, featuresInstances(params))
        node.insertExtraFeatures(analysisId, extraFeaturesInstances(params))

        redirect(action: 'analysis', id: analysisId)
    }

    def featuresInstances(parameters){
        def featureInstances = [:]
        def valueIndividuals = [:]
        def weightIndividuals = [:]
        def individualKeys = []
        def paramValue
        def paramWeight
        def paramJustification
        def uri

        dsl.featureMap.each{ key, feature ->
            valueIndividuals << feature.getValueIndividuals()
            weightIndividuals << feature.getWeightIndividuals()
            individualKeys += feature.getIndividualKeys()
        }

        individualKeys.each{
            uri = k.toURI(it)
            paramValue = parameters[uri]
            paramWeight = parameters[uri+'-weight']
            paramJustification = escapeString(parameters[uri+'-justification'])

            if(paramJustification){
                println paramJustification
            }

            if(paramValue){
                featureInstances[uri] = k.isURI(paramValue)? ['value': paramValue] : ['value': valueIndividuals[paramValue]]
                if(paramWeight)
                    featureInstances[uri]['weight'] = k.isURI(paramWeight)? paramWeight : weightIndividuals[paramWeight]
                if(paramJustification)
                    featureInstances[uri]['justification'] = paramJustification
            }
        }

        return featureInstances;
    }

    def extraFeaturesInstances(parameters){
        def extraFeaturesInstances = [:]
        def attr

        dsl.featureMap.each{ key, feature ->
            extraFeaturesInstances[key] = [:]
        }

        extraFeaturesInstances.each{ key, map ->
            parameters.each{ pKey, value ->
                if(pKey.getClass() == String && pKey.startsWith(key) && value){
                    attr = pKey.tokenize('[]')
                    if(!map.hasProperty(attr[1]) && !map[attr[1]]){
                        map[attr[1]] = [:]
                    }
                    map[attr[1]][attr[2]] = [value: value, dataType: k[attr[2]].range]
                }
            }
        }
        println extraFeaturesInstances
        //println StringEscapeUtils.escapeJava(str)

        return extraFeaturesInstances;
    }

    def analysis(){
        def username = springSecurityService.principal.username
        def userId = username
        def analysisId = params.id
        def uri = analysisId ? k.toURI("inds:"+analysisId) : null
        def evalObjId = k[uri].getAttr('appliedTo')
        def _gui = new GUIDSL('dsl/gui.groovy', grailsApplication.mainContext)

        if(userId && evalObjId && analysisId) {
            def sustainabilityTabs = []
            def efficiencyTabs = []
            def id
            def res
            def extra
            def extraValues = [:]
            def values = [:]
            def roles = k['inds:'+username].getAttr('hasRole')

            evalObjId = evalObjId.substring(evalObjId.lastIndexOf('#') + 1)

            if (roles.contains(k.toURI('ui:AdminRole'))) {
                if (evalObjId) {
                    userId = k['inds:' + evalObjId].getAttr('hasOwner')
                    //println evalObjId
                    //println userId
                    userId = userId.substring(userId.lastIndexOf('#') + 1)
                }
                if (params.user)
                    userId = params.user
            }

            def options = k[':SustainabilityCategory'].getIndividualsIdValueLabel()
            def widgets

            //println options

            dsl.featureMap.eachWithIndex { key, feature, int i ->
                //res = k[key].getChildrenIndividuals(uri, '?id ?ind ?valueType ?weightType')
                res = k[key].getGrandChildrenIndividuals(uri, '?id ?ind ?justification ?valueType ?weightType')
                extra = k[key].getChildrenExtraIndividuals(uri, '?ind ?name ?justification ?valueTypeLabel ?value ?relevance')

                //println key
                //Uri.printTree(res)
                //Uri.printTree(extra)

                res.each {
                    values[it.id] = [:]
                    if (it.valueType)
                        values[it.id].value = it.valueType
                    if (it.weightType)
                        values[it.id].weight = it.weightType
                    if (it.justification)
                        values[it.id].justification = it.justification
                }

                extraValues = [:]

                extra.each {
                    id = it.ind.substring(key.size()+2)
                    id = Integer.parseInt(id.substring(0, id.indexOf('-')))
                    extraValues[id] = [:]
                    if (it.name)
                        extraValues[id].name = it.name
                    if (it.justification)
                        extraValues[id].justification = it.justification
                    if (it.value)
                        extraValues[id].value = it.value
                }

                widgets = []
                widgets.push(['widget': 'individualsPanel', attrs: [data: feature.getModel(evalObjId).subClass, values: values]])
                if (feature.attrs.extraFeatures) {
                    widgets.push(['widget': 'extraFeatures', attrs: [id: key, name: feature.name, options: options, values: extraValues, title: 'Indicadores específicos', header: ['ui:hasName': 'Nome', ':hasJustification': 'Justificativa', 'ui:value': 'Valor']]])
                }

                if (feature.getModel(evalObjId).superClass.contains(k.toURI(':Variable')))
                    efficiencyTabs.push(['widget': 'tab', attrs: [label: feature.getModel(evalObjId).label], widgets: widgets])
                if (feature.getModel(evalObjId).superClass.contains(k.toURI(':SustainabilityIndicator')))
                    sustainabilityTabs.push(['widget': 'tab', attrs: [label: feature.getModel(evalObjId).label], widgets: widgets])
            }

            //Uri.printTree(values)
            _gui.setView(controllerName, actionName)

            dsl.clean(controllerName, actionName)

            if (uri?.trim()) {
                dsl.setData(new DataReader(k, uri))
                dsl.runReport()
            }
            //gui.setData('evaluationObjects', evaluationObjects)
            _gui.setData('username', username)
            _gui.setData('userId', userId)
            _gui.setData('evalObjId', evalObjId)
            _gui.setData('analysisId', analysisId)
            _gui.setData('vars', dsl.getVariables())
            _gui.setData('dataReader', dsl.getData('data'))
            _gui.setData('sustainabilityTabs', sustainabilityTabs)
            _gui.setData('efficiencyTabs', efficiencyTabs)
            _gui.setData('reportView', dsl.getReportView())

            _gui.renderView(actionName)

            render( view: actionName, model: [inputs: _gui.viewsMap[controllerName][actionName]])

        }else{
            response.sendError(404)
        }
    }

    def selectEvaluationObject(){
        def uri = k.toURI(params.evaluation_object_id)
        def id =  uri.substring(uri.lastIndexOf('#')+1)
        redirect(action: 'inputFeatures', id: id)
    }

    def selectAnalysis(){
        def uri = k.toURI(params.analysis)
        def id =  uri.substring(uri.lastIndexOf('#')+1)

        redirect( action: 'analysis', id: id)
    }

    def evaluationObjectNameAvailability(){
        def username = springSecurityService.principal.username
        def name = slugify.slugify(username+'-'+params[k.toURI('ui:hasName')])
        //println name
        render !k['inds:'+name].exist()
    }

    def evaluationObjectView(){
        def uri = k.toURI(params.id)
        def data = []

        k[uri].getDataProperties().each{
            data.push([label: it.dataPropertyLabel.capitalize(), value: it.value])
        }

        k[uri].getObjectProperties().each{
            data.push([label: it.objectPropertyLabel.capitalize(), value: it.valueLabel])
        }

        render( template: '/widgets/tableReport', model: [header: [label: g.message(code: 'property'), value: g.message(code: 'value')], data: data]);
    }

    def analysesView(){
        def uri = k.toURI(params.id)
        def model = [:]
        model.analyses = k[uri].getLabelAppliedTo()
        model.evaluation_object_id = uri

        render( template: '/widgets/analyses', model: model);
    }

    def microregionsView(){
        def microregions = k[params['http://dbpedia.org/ontology/state']].getMicroregions()
        render( template: '/widgets/category', model: [id: 'http://purl.org/biodiv/semanticUI#hasMicroregion', data: microregions, header: 'Opções', selectType: 'radio']);
    }

    def report(){

        def username = 'admin'
        def userId = username
        def analysisId = params.id
        def uri = analysisId ? k.toURI("inds:"+analysisId) : null
        def evalObjId = k[uri].getAttr('appliedTo')
        def _gui = new GUIDSL('dsl/gui.groovy', grailsApplication.mainContext)

        if(userId && evalObjId && analysisId) {
            def roles = k['inds:'+username].getAttr('hasRole')

            evalObjId = evalObjId.substring(evalObjId.lastIndexOf('#') + 1)

            if (roles.contains(k.toURI('ui:AdminRole'))) {
                if (evalObjId) {
                    userId = k['inds:' + evalObjId].getAttr('hasOwner')
                    //println evalObjId
                    //println userId
                    userId = userId.substring(userId.lastIndexOf('#') + 1)
                }
            }

            _gui.setView(controllerName, actionName)
            dsl.clean(controllerName, actionName)

            if (uri?.trim()) {
                dsl.setData(new DataReader(k, uri))
                dsl.runReport()
            }

            //gui.setData('evaluationObjects', evaluationObjects)
            //gui.setData('username', username)
            //gui.setData('userId', userId)
            //gui.setData('evalObjId', evalObjId)
            _gui.setData('analysisId', analysisId)
            _gui.setData('vars', dsl.getVariables())
            _gui.setData('dataReader', dsl.getData('data'))
            _gui.setData('reportView', dsl.getReportView())

            _gui.renderView('report')

            render(view: '/tool/analysis', model: [inputs: _gui.viewsMap[controllerName][actionName], analysisId: analysisId])
        }else{
            response.sendError(404)
        }

    }

    def generatePdf(){
        def proc
        def analysisId = params.id
        def sessionId
        def path = grailsApplication.mainContext.servletContext.getRealPath("/")
        request.cookies.each{
            if(it.name == 'JSESSIONID')
                sessionId = it.value
        }
        def sout = new StringBuilder(), serr = new StringBuilder()
        def command = "wkhtmltopdf.sh --cookie JSESSIONID ${sessionId} http://localhost/tool/report/${analysisId} ${path}report.pdf"          //xvfb-run --server-args='-screen 0, 1024x768x24'
        proc = command.execute()       //http://localhost:8888/
        proc.consumeProcessOutput(sout, serr)
        proc.waitForProcessOutput()
        //println "out> $sout err> $serr"

        render(file: new File(path+'report.pdf'), fileName: "report.pdf")
    }

    def escapeString(String text){
        if(text){
            text = StringEscapeUtils.escapeJava(text.replaceAll(/"|'/, ""))
        }
        return text
    }
}
