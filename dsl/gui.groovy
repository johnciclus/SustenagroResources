//  dataType "DataTypeID", "widgetName"

//dataType  "xsd:date", widget: "date"

//default widget: 'category',

dataType "rdfs:Literal",                                    widget: "textForm"
dataType "owl:real",                                        widget: "numberForm"
dataType "xsd:int",                                         widget: "numberForm"
dataType "xsd:integer",                                     widget: "numberForm"
dataType "xsd:float",                                       widget: "numberForm"
dataType "xsd:date",                                        widget: "dateForm"
dataType "xsd:string",                                      widget: "textForm"
dataType "xsd:language",                                    widget: "textForm"
dataType "xsd:boolean",                                     widget: "categoryForm"
dataType ":SimpleCategory",                                 widget: "categoryForm"
dataType ":ProductionUnit",                                 widget: "categoryForm"
dataType ":SugarcaneSourceCategory",                        widget: "categoryForm"
dataType ":CanavialLongevityCategory",                      widget: "textForm"
dataType ":AgriculturalProductionSystemCategory",           widget: "categoryForm"
dataType ":AvailabilityOfEvaluationResultsCategory",        widget: "categoryForm"
dataType "http://dbpedia.org/page/Microregion_(Brazil)",    widget: "categoryForm"

widgetAttributes 'navBarRoute', evalObjTitle: "Unidade produtiva", newEvalObjLabel: "Nova unidade produtiva", analysesTitle: "Análises", newAnalysisLabel: "Nova análise"

widgetAttributes 'selectEvaluationObject', title: "Seleccionar unidade produtiva", label : "Unidade produtiva", submitLabel: "Novo análise"

widgetAttributes 'listEvaluationObjects', label : "Unidade produtiva", submitLabel: "Novo análise"

widgetAttributes 'createEvaluationObject', submitLabel: "Cadastrar"

widgetAttributes 'tabs', submitLabel: 'Avaliar', previousLabel: 'Anterior', nextLabel: 'Próximo', pagination: true

widgetAttributes 'form', method: 'post', formClass: 'form-horizontal'

widgetAttributes 'analyses', title: 'Selecionar análise', submitLabel: 'Ver análise'


/*

view("tool/index"){
    title "Avaliação da sustentabilidade na agricultura"

    description """O processo de avaliação da sustentabilidade é composto pelas seguintes etapas:

1. Localização da lavoura
2. Caracterização da cultura, tecnologia e tipo de sistema produtivo
3. Definição dos indicadores
4. Recomendações de sustentabilidade"""

    
    selectEvaluationObject ':ProductionUnit', title: "Selecionar unidade produtiva", label : "Unidade produtiva", submit_label: "Nova avaliação"

    createEvaluationObject ':ProductionUnit', title: "Cadastrar nova unidade produtiva para realizar avaliação", submit_label: "Cadastrar"
}

view("tool/assessments"){
    text "Unidade produtiva atual: **" + Production_Unit.label + "**"
    tabs 'analysis', previousLabel: 'Anterior', nextLabel: 'Próximo', {
        tab 'sustainability_assessment', label: 'Avaliação da sustentabilidade', widgetClass: 'active', {
            //tabs 'sustainability', {
            indicatorList ':EnvironmentalIndicator'

            //}
        }

        tab 'efficiency_assessment', label: 'Avaliação da eficiência', {

        }

        tab 'report', label: 'Relatório'

        tab 'recomendation', label: 'Recomendação'
    }
}


//individual 'Production_Unit', ':ProductionUnit'

*/