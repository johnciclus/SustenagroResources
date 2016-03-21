//                          Decisioner

// Esta DSL descreve como o aplicativo será gerado. Ele é em inglês pois
// facilita na hora de publicarmos os papers. Porém, o aplicativo pode ser
// em português ou inglês. Isso vai depender da ontologia, nela teremos as
// definições nas duas linguas.
// nome vai aparecer onde um nome for necessário
title 'Avaliação da sustentabilidade na agricultura'

// Aba de descrição do conteúdo: um texto em markdown que você vai escrever
// (esse texto também pode estar num arquivo)
description '''O processo de avaliação da sustentabilidade é composto pelas seguintes etapas:

1. Localização da lavoura
2. Caracterização da cultura, tecnologia e tipo de sistema produtivo
3. Definição dos indicadores
4. Recomendações de sustentabilidade

'''

// Informações que serão lidas antes dos indicadores. No exemplo serão
// mostradas todas as culturas, tecnologias e meios de produção que
// estiverem na ontologia
// Mantemos crop, mas se a única que estiver na ontologia for Cana,
// a interface só mostra uma opção (sem possibilidade de escolha)
// Na ontologia, location definiria as microregiões do IBGE.
// Se a fazenda ficar em mais de uma micro-região?

// Caracterização dos sistemas produtivos no Centro-Sul
evaluationObject ':ProductionUnit', {
    //type or instance

    // Production unit name
    instance ':hasName', label: "Nome da unidade produtiva", placeholder: "Nome"

    // Production unit type
    // Tipo de organização (Greenfiled, usinas tradicionais, familiares...?).
    type label: "Tipo da unidade produtiva", header: "Opções"

    // Agricultural production system
    instance ':hasAgriculturalProductionSystem', label: "Sistema de produção agrícola", header: "Opções"

    //Identificação do sistema de produção

    // Origem da cana (própria, fornecedor, arrendamento)
    instance  ':hasSugarcaneSource', label: "Origem da cana", header: "Opções"

    // Microrregião produtora
    instance ':hasMicroRegion', label: "Microrregião da unidade produtiva", header: "Opções"

    // Data de fundação da unidade produção
    instance ':hasEstablishmentDate', label: "Data de fundação da unidade produção"

    // Parcerias para pesquisa ou aprimoramento do sistema (nome da instituição parceira, tipo da instituição – pública, privada, Cooperativas ou associações);
    instance ':hasPartnershipsForResearchOrImprovementOfTheSystem', label: "Parcerias para pesquisa ou aprimoramento do sistema (nome da instituição parceira, tipo da instituição – pública, privada, Cooperativas ou associações)", widget: 'textAreaForm', placeholder: "Descrição"

    //Ligação com outros grupos empresariais ou de investimentos
    instance ':hasLinkWithOtherBusinessOrInvestmentGroups', label: "Ligação com outros grupos empresariais ou de investimentos", placeholder: "Descrição"

    // Municípios envolvidos (localização da sede)

    // Projetos de inovação e/ou desenvolvimento (BNDES, Finep)
    instance ':hasInnovationDevelopmentProjects', label: "Projetos de inovação e/ou desenvolvimento (BNDES, Finep)", placeholder: "Descrição"

    //Financiamento (crédito agrícola, custeio de maquinário, BNDES);
    instance ':hasFinancing', label: "Financiamento (crédito agrícola, custeio de maquinário, BNDES)", placeholder: "Descrição"

    // Valor total investido em tecnologia na fase agrícola (até a fase atual)
    instance ':hasTotalValueInvestedInTechnologicInAgriculturalPhase', label: "Valor total investido em tecnologia na fase agrícola (até a fase atual)"

    // Valor total investido em tecnologia na fase industrial (até a fase atual)
    instance ':hasTotalValueInvestedInTechnologicInIndustrialPhase', label: "Valor total investido em tecnologia na fase industrial (até a fase atual)"

    // Valor total previsto para investimento para escoamento da produção
    instance ':hasTotalValuePlaneedForInvestmentToProductionDrainage', label: "Valor total previsto para investimento para escoamento da produção"

    // Data de início do plantio
    instance ':hasBeginningOfPlantingDate', label: "Data de início do plantio"

    // Data de término do plantio
    instance ':hasFinishOfPlantingDate', label: "Data de término do plantio"

    // Data de início da colheita
    instance ':hasBeginningOfHarvestDate', label: "Data de início da colheita"

    // Data de término da colheita
    instance ':hasFinishOfHarvestDate', label: "Data de término da colheita"

    // Longevidade do canvial (cana de ano, cana de ano e meio);
    instance ':hasCanavialLongevity', label: "Longevidade do canvial", header: "Opções"

    // Disponibilização dos resultados da avaliação: Público | privado
    instance ':hasAvailabilityOfEvaluationResults', label: "Disponibilização dos resultados da avaliação", header: "Opções"
}


// Cada dimensão que será mostrada. Em cada dimensão, serão mostrados
// todos os indicadores presentes na ontologia. Existe a opção de não
// mostrar algum indicador. No exemplo abaixo, o indicador "co2 emission"
// (fictício) não é mostrado.

indicators ':EnvironmentalIndicator'
//{
//    exclude 'co2 emission'
//}

indicators ':EconomicIndicator'

indicators ':SocialIndicator'

indicators ':ProductionEfficiencyFeature'

indicators ':TechnologicalEfficiencyFeature', {
    conditional ":ProductionUnit", 'http://dbpedia.org/ontology/Provider', {
        include ':TechnologicalEfficiencyInTheField'
    }
    conditional ":ProductionUnit", 'http://dbpedia.org/resource/PhysicalPlant', {
        include ':TechnologicalEfficiencyInTheIndustrial'
    }
}

data 'analysis'

// Para cada índice, é possível indicar fórmulas para o cálculo de cada
// atributo. Essas fórmulas podem ser tão complicadas como você queira.
prog {
    environment =   weightedSum(analysis.':EnvironmentalIndicator')
    economic    =   weightedSum(analysis.':EconomicIndicator')
    social      =   weightedSum(analysis.':SocialIndicator')

    //environment =   sum(analysis.':EnvironmentalIndicator'.equation({value*weight}))
    //social      =   sum(analysis.':SocialIndicator'.equation({value*weight}))
    //economic    =   sum(analysis.':EconomicIndicator'.equation({value*weight}))

    sustainability = (environment + social + economic)/3

    cost_production_efficiency = sum(analysis.':ProductionEfficiencyFeature')
    //cost_production_efficiency = sum(analysis.':ProductionEfficiencyFeature'.value())

    TechnologicalEfficiencyInTheField = weightedSum(analysis.':TechnologicalEfficiencyInTheField')
    TechnologicalEfficiencyInTheIndustrial = weightedSum(analysis.':TechnologicalEfficiencyInTheIndustrial')

    //TechnologicalEfficiencyInTheField = sum(analysis.':TechnologicalEfficiencyInTheField'.equation({value*weight}))
    //TechnologicalEfficiencyInTheIndustrial = sum(analysis.':TechnologicalEfficiencyInTheIndustrial'.equation({value*weight}))

    efficiency = cost_production_efficiency *
            (TechnologicalEfficiencyInTheField+TechnologicalEfficiencyInTheIndustrial)

    //environment =   (analysis.'BiologicalPestControl' ? 1:-1) +
    //        (analysis.'PlanningSystematicPlanting' ? 1:-1) +
    //        (analysis.'StandardAerialSpraying' ? 1:-1) +
    //        analysis.'VinasseAndEthanolRelation'



    //economic =      2.0 * analysis.'Eficiência operacional da Usina (crescimento vertical da usina, recuperação e avanço)' + 5.1 *
    //        analysis.'Eficiência energética das caldeiras para cogeração de energia'

    //social =        3 * analysis.EnergyEfficiencyOfBoilersForCogeneration + 7 *
    //        analysis.OperationalEfficiencyPlant

    // THE REPORT

    // Just showing text
    //show '***That is the report:***'

    // Cada recomendação terá uma fórmula lógica que permite especificar
    // quando ela deve ser mostrada. Essas fórmulas podem ser tão complexas
    // quanto necessário. Caso o resultado da fórmula seja verdadeiro, o texto
    // (em markdown) vai ser mostrado.
    // Aqui temos 4 possibilidades de implementação:

    // if (environment > 3.5 || social ==7)
    //    recommendation '**markdown** *First* option'

    // recommendation environment > 3.5 || social == 7, '**Second** *option*'
    // recommendation if:(environment > 3.5 || social == 7), '**Third** *option*'
    // recommendation if:(environment > 3.5 || social == 7), show: ''' *Fourth* *option* '''

    //show 'Nome da unidade produtiva: ' + analysis.'CurrentProductionUnit'.label()
    //show 'Caracterização dos sistemas produtivos no Centro-Sul: ' + analysis.'CurrentProductionUnit'.productionUnit()
    //show 'Microrregião da unidade produtiva: ' + analysis.'CurrentProductionUnit'.microregion()
    //show 'Tecnologias disponíveis: ' + analysis.'CurrentProductionUnit'.efficiency()

    //linebreak()

    paragraph '**Matrix de Avaliação**'

    paragraph 'Índice da sustentabilidade: ' + sustainability
    paragraph 'Indice de eficiência: ' + efficiency

    //can be set in the ontology
    recomendations = ["Cenário desfavorável, Muito baixo desempenho dos indicadores",
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

    // Matrix de sustentabilidade
    matrix([x: sustainability,
            y: efficiency,
            labelX: 'Indice da sustentabilidade',
            labelY: 'Indice de eficiência',
            rangeX: [-50,150],
            rangeY: [-30,60],
            quadrants: [4,3],
            recomendations: recomendations])

    //    matrix x: environment, y: environmentAvg, labelX: 'Indice de Magnitude', labelY: 'Indice de Segurança', rangeX: [-5,5], rangeY: [-2,2], quadrants: [4,6], [
    //    [0, 0, 'bla bla bla'],
    //    [0, 1, 'bla bla bla'],
    //    //quadrant2: 'bla bla bla',
    //    ...
    //    ]


    //Outra possibilidade
    //matrix  x: [label: 'kkk', value = environment, range: [1,9]],
    //        y: [label: 'kkk', value = social, range: [1,9]]

    paragraph '''**Avaliação da sustentabilidade** '''

    paragraph '**Indicadores Ambientais**'

    table analysis.':EnvironmentalIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weight': 'Peso']

    paragraph 'Índice ambiental: '+ environment

    //linebreak()

    paragraph '**Indicadores Econômicos**'

    table analysis.':EconomicIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weight': 'Peso']

    paragraph 'Índice econômico: '+ economic

    linebreak()

    paragraph '**Indicadores Sociais**'

    table analysis.':SocialIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weight': 'Peso']

    paragraph 'Índice social: '+ social

    linebreak()

    paragraph '**Avaliação da sustentabilidade**'

    paragraph 'Índice da sustentabilidade '+ sustainability

    linebreak()

    paragraph '**Eficiência da produção**'

    table analysis.':ProductionEfficiencyFeature', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor']

    paragraph 'Índice de eficiência da produção: '+ cost_production_efficiency

    linebreak()

    paragraph '**Eficiência tecnológica no campo**'

    table analysis.':TechnologicalEfficiencyInTheField', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso']

    paragraph 'Índice de tecnológica no campo: '+ TechnologicalEfficiencyInTheField

    linebreak()

    paragraph '**Eficiência tecnológica na industria**'

    table analysis.':TechnologicalEfficiencyInTheIndustrial', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso']

    paragraph 'Índice de tecnológica na industria: '+ TechnologicalEfficiencyInTheIndustrial

    linebreak()

    paragraph '**Avaliação de eficiência**'

    paragraph 'Índice de eficiência: '+ efficiency

    linebreak()

    paragraph '**Mapa da microregião**'
    //map analysis.'Microregion_(Brazil)'.map()
}