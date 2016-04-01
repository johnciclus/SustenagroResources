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
dataType ":CanavialLongevityCategory",                      widget: "categoryForm"
dataType ":AgriculturalProductionSystemCategory",           widget: "categoryForm"
dataType ":AvailabilityOfEvaluationResultsCategory",        widget: "categoryForm"
dataType "http://dbpedia.org/page/Microregion_(Brazil)",    widget: "categoryForm"

widgetAttributes 'navBarRoute', evalObjTitle: "Unidade produtiva", newEvalObjLabel: "Nova unidade produtiva", analysesTitle: "Análises", newAnalysisLabel: "Novo análise"

widgetAttributes 'selectEvaluationObject', title: "Seleccionar unidade produtiva", label : "Unidade produtiva", submitLabel: "Novo análise"

widgetAttributes 'listEvaluationObjects', title: "Unidades produtivas", label : "Unidade produtiva", submitLabel: "Novo análise"

widgetAttributes 'createEvaluationObject', title: "Cadastrar nova unidade produtiva", submitLabel: "Cadastrar"

widgetAttributes 'tabs', submitLabel: 'Avaliar', previousLabel: 'Anterior', nextLabel: 'Próximo', pagination: true

widgetAttributes 'form', method: 'post', formClass: 'form-horizontal'

widgetAttributes 'analyses', title: 'Selecionar análise', submitLabel: 'Ver análise'

// nome vai aparecer onde um nome for necessário
title 'Etapas para preenchimento do Software:'

// Aba de descrição do conteúdo: um texto em markdown que você vai escrever
// (esse texto também pode estar num arquivo)
description '''O emprego do Software SustenAgro para avaliação da sustentabilidade do sistema de produção de cana-de-açúcar prevê as seguintes etapas::

1. Cadastro ou seleção da unidade produtiva
2. Caracterização do sistema de produção
3. Seleção e avaliação dos indicadores de sustentabilidade – Índice de Sustentabilidade
4. Análise da Eficiência da Tecnologia e de custos
5. Análise dos resultados na Matriz de Sustentabilidade
6. Gerenciamento da sustentabilidade

'''


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