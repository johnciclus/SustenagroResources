package sustenagro

import semantics.DataReader
import semantics.Node

import grails.plugin.springsecurity.annotation.Secured

@Secured('ROLE_USER')
class ToolController {
        def dsl
        def gui
        def k
        def slugify
        def springSecurityService
        def index() {

            def evaluationObject = dsl.evaluationObject

            dsl.clean(controllerName, actionName)
            gui.setView(controllerName, actionName)
            gui.selectEvaluationObject(gui.widgetAttrs['selectEvaluationObject'], evaluationObject.getURI())
            gui.createEvaluationObject(gui.widgetAttrs['createEvaluationObject'], evaluationObject.widgets, evaluationObject.getURI())
            gui.requestData(controllerName, actionName)

            render(view: 'index', model: [data: gui._props, inputs: gui.viewsMap[controllerName][actionName]])
        }

        def createEvaluationObject() {
            def id = ''
            def name = k.toURI(':hasName')
            def type = k.toURI(params['evalObjType'])
            def evaluationObject = dsl.evaluationObject

            if(params['evalObjType'] && params[name] && params[type]){

                def node = new Node(k, '')
                def propertyInstances = [:]

                evaluationObject.model.each{ ins ->
                    if(params[ins.id] && ins.id != type){
                        propertyInstances[k.shortToURI(ins.id)] = [value: params[ins.id], dataType: ins.dataType]
                    }
                }

                id = slugify.slugify(params[name])
                node.insertEvaluationObject(id, params[type], propertyInstances)
            }

            //k.g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
            redirect(action: 'analysis', id: id)
        }

        def analysis(){
            def uri = k.toURI(':'+params.id)
            def tab_prefix = 'tab_'
            def tabsAttrs = gui.widgetAttrs['tabs']
            def tabsWidgets = [:]
            def formAttrs = gui.widgetAttrs['form']
            def formWidgets = []

            tabsAttrs.labels = [:]
            dsl.featureMap.eachWithIndex{ key, feature, int i ->
                feature.features.each{
                    tabsAttrs.labels[tab_prefix+i] = it.value.label
                    tabsWidgets[tab_prefix+i] = [['widget': 'individualsPanel', attrs: [data: it.value.subClass, values: [:], weights: [:]]]]
                }
            }
            tabsAttrs.submit = true

            formAttrs['action'] = '/tool/createAnalysis'
            formWidgets.push(['widget': 'tabs', attrs: tabsAttrs, widgets: tabsWidgets, id: uri])

            dsl.clean(controllerName, actionName)
            gui.setView(controllerName, actionName)
            gui.paragraph([text: gui.widgetAttrs['paragraph'].text + '**'+ k[uri].label + '**'])
            gui.form(formAttrs, formWidgets)
            gui.requestData(controllerName, actionName)

            //println "* Index Tree ${gui.viewsMap[controllerName][actionName].size()}*"
            //Uri.printTree(gui.viewsMap[controllerName][actionName])

            render(view: 'analysis', model: [data: gui._props, inputs: gui.viewsMap[controllerName][actionName]])
        }

        def createAnalysis(){
            def evalObjInstance = k.toURI(params.evalObjInstance)
            def num = k[evalObjInstance].getAssessments().size() + 1
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
            redirect(action: 'scenario',
                    id: analysisId)
        }

        def scenario(){
            def uri

            if(params.id){
                uri = k.toURI(":"+params.id)
            }

            if(uri?.trim()){
                dsl.setData(new DataReader(k, uri))
                dsl.runFormula()
            }

            dsl.clean(controllerName, actionName)
            gui.setView(controllerName, actionName)
            gui.setData('scenario', dsl.getScenario())
            gui.setData('dataReader', dsl.getData('data'))
            gui.setData('uri', uri)
            gui.renderView('scenario')
            gui.printData()
            gui.requestData(controllerName, actionName)

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

            render(view: 'scenario', model: [data: gui._props, inputs: gui.viewsMap[controllerName][actionName]])
        }

        def selectEvaluationObject(){
            def id = k.shortURI(params.production_unit_id)

            redirect(   action: 'analysis',
                    id: id)
        }

        def selectAnalysis(){
            def id = k.shortURI(params.production_unit_id)
            def name = k.shortURI(params.analysis)

            redirect(   action: 'analysis',
                    id: id,
                    params: [analysis: name])
        }

        def analyses(){
            def id = k.shortURI(params.id)
            //println params.id

            def analyses = k[id].labelAppliedTo

            render( template: 'analyses',
                    model:    [analyses: analyses,
                               production_unit_id: id]);
        }
    }
