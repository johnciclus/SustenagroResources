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
features(':ProductionUnit') {
    instance 'MicroRegion', 'label': "Microrregião da unidade produtiva"
    instance ':AgriculturalEfficiency', 'label': "Tecnologias disponíveis"
    
    // Data de fundação da unidade produção
    // Projetos de inovação e/ou desenvolvimento (BNDES, Finep)
    // Financiamento (crédito agrícola, custeio de maquinário, BNDES);
    // Valor total investido em tecnologia na fase agrícola (até a fase atual)
    // Valor total investido em tecnologia na fase industrial (até a fase atual)
    // Valor total previsto para investimento para escoamento da produção
    // Parcerias para pesquisa ou aprimoramento do sistema (nome da instituição parceira, tipo da instituição – pública, privada, Cooperativas ou associações);
    // Ligação com outros grupos empresariais ou de investimentos
    // Tipo de organização (Greenfiled, usinas tradicionais, familiares...?).
    
    
    // Identificação do sistema de produção
    // Origem da cana (própria, fornecedor, arrendamento)
    // Data de início e término do plantio
    // Data de início e término da última colheita
    // Longevidade do canvial (cana de ano, cana de ano e meio);
    
    // Municípios envolvidos (localização da sede)
    
    // Disponibilização dos resultados da avaliação: Público | privado
}

// Cada dimensão que será mostrada. Em cada dimensão, serão mostrados
// todos os indicadores presentes na ontologia. Existe a opção de não
// mostrar algum indicador. No exemplo abaixo, o indicador "co2 emission"
// (fictício) não é mostrado.

dimension ':EnvironmentalIndicator'
//{
//    exclude 'co2 emission'
//}

dimension ':EconomicIndicator'

dimension ':SocialIndicator'

data 'assessment'

// Para cada índice, é possível indicar fórmulas para o cálculo de cada
// atributo. Essas fórmulas podem ser tão complicadas como você queira.
prog {
    environment =   sum(assessment.':EnvironmentalIndicator'.equation({value*weight}))
    social      =   sum(assessment.':SocialIndicator'.equation({value*weight}))
    economic    =   sum(assessment.':EconomicIndicator'.equation({value*weight}))
    
    sustainability = (environment + social + economic)/3
                     

    TechnologicalEfficiencyInTheField = sum(assessment.':TechnologicalEfficiencyInTheField'.equation({value*weight}))
    TechnologicalEfficiencyInTheIndustrial = sum(assessment.':TechnologicalEfficiencyInTheIndustrial'.equation({value*weight}))
    cost_production_efficiency = sum(assessment.':ProductionEfficiencyFeature'.value())

    efficiency = cost_production_efficiency *
                 (TechnologicalEfficiencyInTheField+TechnologicalEfficiencyInTheIndustrial)

    //environment =   (assessment.'BiologicalPestControl' ? 1:-1) +
    //        (assessment.'PlanningSystematicPlanting' ? 1:-1) +
    //        (assessment.'StandardAerialSpraying' ? 1:-1) +
    //        assessment.'VinasseAndEthanolRelation'



    //economic =      2.0 * assessment.'Eficiência operacional da Usina (crescimento vertical da usina, recuperação e avanço)' + 5.1 *
    //        assessment.'Eficiência energética das caldeiras para cogeração de energia'

    //social =        3 * assessment.EnergyEfficiencyOfBoilersForCogeneration + 7 *
    //        assessment.OperationalEfficiencyPlant

    // THE REPORT

    // Just showing text
    //show '***That is the report:***'
    show '**Avaliação da sustentabilidade e da eficiência produtiva**'
    
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
    
    show 'Nome da unidade produtiva: ' + assessment.'CurrentProductionUnit'.label()
    show 'Caracterização dos sistemas produtivos no Centro-Sul: ' + assessment.'CurrentProductionUnit'.productionUnit()
    show 'Microrregião da unidade produtiva: ' + assessment.'CurrentProductionUnit'.microregion()
    show 'Tecnologias disponíveis: ' + assessment.'CurrentProductionUnit'.efficiency()
    
    linebreak()
    
    show '**Matrix de Avaliação**'

    show 'Índice da sustentabilidade: ' + sustainability
    show 'Indice de eficiência: ' + efficiency
    
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
    /*
        matrix x: environment, y: environmentAvg, labelX: 'Indice de Magnitude', labelY: 'Indice de Segurança', rangeX: [-5,5], rangeY: [-2,2], quadrants: [4,6], [ 
        [0, 0, 'bla bla bla'],
        [0, 1, 'bla bla bla'],
        //quadrant2: 'bla bla bla',
        ...
    ]

    */
    //Outra possibilidade
    //matrix  x: [label: 'kkk', value = environment, range: [1,9]],
    //        y: [label: 'kkk', value = social, range: [1,9]]
    
    show '**Indicadores Ambientais**'
    
    table assessment.':EnvironmentalIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weight': 'Peso']
    
    show 'Índice ambiental: '+ environment
    
    linebreak()
    
    show '**Indicadores Econômicos**'
    
    table assessment.':EconomicIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weight': 'Peso']
    
    show 'Índice econômico: '+ economic
    
    linebreak()
    
    show '**Indicadores Sociais**'
    
    table assessment.':SocialIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weight': 'Peso']
    
    show 'Índice social: '+ social
    
    linebreak()
    
    show '**Avaliação da sustentabilidade**'
    
    show 'Índice da sustentabilidade '+ sustainability
    
    linebreak()
    
    show '**Eficiência da produção**'
    
    table assessment.':ProductionEfficiencyFeature', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor']
    
    show 'Índice de eficiência da produção: '+ cost_production_efficiency 
    
    linebreak()
    
    show '**Eficiência tecnológica no campo**'
    
    table assessment.':TechnologicalEfficiencyInTheField', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso']
    
    show 'Índice de tecnológica no campo: '+ TechnologicalEfficiencyInTheField
    
    linebreak()
    
    show '**Eficiência tecnológica na industria**'
    
    table assessment.':TechnologicalEfficiencyInTheIndustrial', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso']
    
    show 'Índice de tecnológica na industria: '+ TechnologicalEfficiencyInTheIndustrial
    
    linebreak()
    
    show '**Avaliação de eficiência**'
    
    show 'Índice de eficiência: '+ efficiency
    
    linebreak()
    
    show '**Mapa da microregião**'
    map assessment.'MicroRegion'.map()
}