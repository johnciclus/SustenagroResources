//                          Decisioner

// Esta DSL descreve como o aplicativo será gerado. Ele é em inglês pois
// facilita na hora de publicarmos os papers. Porém, o aplicativo pode ser
// em português ou inglês. Isso vai depender da ontologia, nela teremos as
// definições nas duas linguas.

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

feature ':EnvironmentalIndicator'
//{
//    exclude 'co2 emission'
//}

feature ':EconomicIndicator'

feature ':SocialIndicator'

feature ':ProductionEfficiencyFeature'

feature ':TechnologicalEfficiencyFeature', {
    conditional ":ProductionUnit", 'http://dbpedia.org/ontology/Provider', {
        include ':TechnologicalEfficiencyInTheField'
    }
    conditional ":ProductionUnit", 'http://dbpedia.org/resource/PhysicalPlant', {
        include ':TechnologicalEfficiencyInTheIndustrial'
    }
}

data 'data'

// Para cada índice, é possível indicar fórmulas para o cálculo de cada
// atributo. Essas fórmulas podem ser tão complicadas como você queira.
formula {
    environment =   weightedSum(data.':EnvironmentalIndicator')             //.equation({value*weight}))
    economic    =   weightedSum(data.':EconomicIndicator')
    social      =   weightedSum(data.':SocialIndicator')
    
    sustainability = (environment + social + economic)/3

    cost_production_efficiency = sum(data.':ProductionEfficiencyFeature')

    technologicalEfficiencyInTheField = weightedSum(data.':TechnologicalEfficiencyInTheField')  //.equation({value*weight}))
    technologicalEfficiencyInTheIndustrial = weightedSum(data.':TechnologicalEfficiencyInTheIndustrial')

    efficiency = cost_production_efficiency *
                 (technologicalEfficiencyInTheField+technologicalEfficiencyInTheIndustrial)

    //economic =      2.0 * analysis.'Eficiência operacional da Usina (crescimento vertical da usina, recuperação e avanço)' + 5.1 *
    //        analysis.'Eficiência energética das caldeiras para cogeração de energia'

    //social =        3 * analysis.EnergyEfficiencyOfBoilersForCogeneration + 7 *
    //        analysis.OperationalEfficiencyPlant

    matrixWithSummary   x: sustainability,
                        y: efficiency,
                        label_x: 'Indice da sustentabilidade',
                        label_y: 'Indice de eficiência',
                        range_x: [-50,150],
                        range_y: [-30,60],
                        quadrants: [4,3],
                        recomendations: ["Cenário desfavorável, Muito baixo desempenho dos indicadores",
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

    textFormat '**Mapa da microregião**'

    map     url: data.'Microregion'.map()
}