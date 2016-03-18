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
        gui._setView(controllerName, actionName)
        gui.selectEvaluationObject(gui.widgetAttrs['selectEvaluationObject'], dsl.evaluationObjectInstance.getURI())
        gui.createEvaluationObject(gui.widgetAttrs['createEvaluationObject'], dsl.evaluationObjectInstance.widgets, dsl.evaluationObjectInstance.getURI())
        gui._requestData(controllerName, actionName)

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
        redirect(action: 'analysis', id: id)
    }

    def analysis(){
        def uri = k.toURI(':'+params.id)
        def data = [:]

        data['individuals'] = [:]

        dsl.featureMap.each{ key, feature ->
            feature.features.each{
                data['individuals'][it.key] = it.value
            }
        }

        data['values'] = [:]
        data['weights'] = [:]
        data['submitLabel'] = gui.widgetAttrs['tabs'].submitLabel

        dsl._clean(controllerName, actionName)
        gui._setView(controllerName, actionName)
        gui.paragraph([text: gui.widgetAttrs['paragraph'].text + '**'+ k[uri].label + '**'])
        gui.tabs(gui.widgetAttrs['tabs'], data, uri)
        gui._requestData(controllerName, actionName)

        //println "* Index Tree ${gui.viewsMap[controllerName][actionName].size()}*"
        //Uri.printTree(gui.viewsMap[controllerName][actionName])

        render(view: 'analysis', model: [data: dsl.props, inputs: gui.viewsMap[controllerName][actionName]])
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
            dsl.program()
        }
        dsl.printData()

        dsl._clean(controllerName, actionName)
        gui._setView(controllerName, actionName)
        gui.paragraph('**Matrix de Avaliação**')
        gui.paragraph('Índice da sustentabilidade: '+dsl.getData('sustainability'))
        gui.paragraph('Indice de eficiência: '+dsl.getData('efficiency'))

        def recomendations = ["Cenário desfavorável, Muito baixo desempenho dos indicadores",
                              "Cenário desfavorável, Baixo desempenho dos indicadores",
                              "Cenário desfavorável, Médio desempenho dos indicadores",
                              "Cenário desfavorável, Alto desempenho dos indicadores",
                              "Cenário propício, Muito baixo desempenho dos indicadores",
                              "Cenário propício, Baixo desempenho dos indicadores",
                              "Cenário propício, Médio desempenho dos indicadores",
                              "Cenário propício, Alto desempenho dos indicadores",
                              "Cenário muito favorável, Muito baixo desempenho dos indicadores",
                              "Cenário muito favorável, Baixo desempenho dos indicadores",
                              "Cenário muito favorável, Médio desempenho dos indicadores",
                              "Cenário muito favorável, Alto desempenho dos indicadores"]
        gui.matrix([x: dsl.getData('sustainability'),
                    y: dsl.getData('efficiency'),
                    labelX: 'Indice da sustentabilidade',
                    labelY: 'Indice de eficiência',
                    rangeX: [-50,150],
                    rangeY: [-30,60],
                    quadrants: [4,3],
                    recomendations: recomendations])

        gui.paragraph('''**Avaliação da sustentabilidade** ''')
        gui.paragraph('**Indicadores Ambientais**')
        gui.table( dsl.getData('analysis').':EnvironmentalIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weight': 'Peso'])
        gui.paragraph('Índice ambiental: '+ dsl.getData('environment'))
        gui.linebreak()

        gui.paragraph('**Indicadores Econômicos**')
        gui.table( dsl.getData('analysis').':EconomicIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weight': 'Peso'])
        gui.paragraph('Índice econômico: '+ dsl.getData('economic'))
        gui.linebreak()

        gui.paragraph('**Indicadores Sociais**')
        gui.table( dsl.getData('analysis').':SocialIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weight': 'Peso'])
        gui.paragraph('Índice social: '+ dsl.getData('social'))
        gui.linebreak()

        gui.paragraph('''**Avaliação da sustentabilidade** ''')
        gui.paragraph('Índice da sustentabilidade '+ dsl.getData('sustainability'))
        gui.linebreak()

        gui.paragraph('**Eficiência da produção**')
        gui.table( dsl.getData('analysis').':ProductionEfficiencyFeature', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor'])
        gui.paragraph('Índice de eficiência da produção: '+ dsl.getData('cost_production_efficiency'))
        gui.linebreak()

        gui.paragraph('**Eficiência tecnológica no campo**')
        gui.table( dsl.getData('analysis').':TechnologicalEfficiencyInTheField', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso'])
        gui.paragraph('Índice de tecnológica no campo: '+ dsl.getData('TechnologicalEfficiencyInTheField'))
        gui.linebreak()

        /*
       def fea = dsl.featureMap[k.toURI(':TechnologicalEfficiencyFeature')]
       def technologyTypes = fea.evalObject(k.toURI([params.id]))
       def name = k[uri].label
       def report

       dsl.data = new DataReader(k, uri)
       dsl.assessmentProgram()
       dsl.viewsMap['tool']['analysis'] = dsl.analyzesMap['http://purl.org/biodiv/semanticUI#Analysis'].widgets

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


        render(view: 'scenario', model: [inputs: gui.viewsMap[controllerName][actionName]])
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
        println params.id

        def analyses = k[id].labelAppliedTo

        render( template: 'analyses',
                model:    [analyses: analyses,
                           production_unit_id: id]);
    }
}