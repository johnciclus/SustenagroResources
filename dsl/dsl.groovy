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

data 'evaluation'

// Para cada índice, é possível indicar fórmulas para o cálculo de cada
// atributo. Essas fórmulas podem ser tão complicadas como você queira.
prog {
    environment = sum(evaluation.':EnvironmentalIndicator'.value())
    economic    = sum(evaluation.':EconomicIndicator'.value())
    social      = sum(evaluation.':SocialIndicator'.value())

    sustainability = environment+social+economic

    TechnologicalEfficiencyInTheField = sum(evaluation.':TechnologicalEfficiencyInTheField'.equation({it.value*it.weight}))
    TechnologicalEfficiencyInTheIndustrial = sum(evaluation.':TechnologicalEfficiencyInTheIndustrial'.equation({it.value*it.weight}))
    cost_production_efficiency = sum(evaluation.':ProductionEfficiencyFeature'.value())

    efficiency = (TechnologicalEfficiencyInTheField+TechnologicalEfficiencyInTheIndustrial)*cost_production_efficiency

    //environment =   (evaluation.'BiologicalPestControl' ? 1:-1) +
    //        (evaluation.'PlanningSystematicPlanting' ? 1:-1) +
    //        (evaluation.'StandardAerialSpraying' ? 1:-1) +
    //        evaluation.'VinasseAndEthanolRelation'



    //economic =      2.0 * evaluation.'Eficiência operacional da Usina (crescimento vertical da usina, recuperação e avanço)' + 5.1 *
    //        evaluation.'Eficiência energética das caldeiras para cogeração de energia'

    //social =        3 * evaluation.EnergyEfficiencyOfBoilersForCogeneration + 7 *
    //        evaluation.OperationalEfficiencyPlant

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
    
    show 'Nome da unidade produtiva: ' + evaluation.'CurrentProductionUnit'.label()
    show 'Caracterização dos sistemas produtivos no Centro-Sul: ' + evaluation.'CurrentProductionUnit'.productionUnit()
    show 'Microrregião da unidade produtiva: ' + evaluation.'CurrentProductionUnit'.microregion()
    show 'Tecnologias disponíveis: ' + evaluation.'CurrentProductionUnit'.efficiency()
    
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
            rangeX: [-100,100],
            rangeY: [-50,50],
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
    
    table evaluation.':EnvironmentalIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor']
    
    show 'Índice ambiental: '+ environment
    
    linebreak()
    
    show '**Indicadores Econômicos**'
    
    table evaluation.':EconomicIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor']
    
    show 'Índice econômico: '+ economic
    
    linebreak()
    
    show '**Indicadores Sociais**'
    
    table evaluation.':SocialIndicator', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor']
    
    show 'Índice social: '+ social
    
    linebreak()
    
    show '**Avaliação da sustentabilidade**'
    
    show 'Índice da sustentabilidade '+ sustainability
    
    linebreak()
    
    show '**Eficiência da produção**'
    
    table evaluation.':ProductionEfficiencyFeature', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor']
    
    linebreak()
    
    show '**Eficiência tecnológica no campo**'
    
    table evaluation.':TechnologicalEfficiencyInTheField', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso']
    
    linebreak()
    
    show '**Eficiência tecnológica na industria**'
    
    table evaluation.':TechnologicalEfficiencyInTheIndustrial', ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso']
    
    linebreak()
    
    show '**Avaliação de eficiência**'
    
    show 'Índice de eficiência (Índice ambiental + ): '+ efficiency
    
    linebreak()
    
    show '**Mapa da microregião**'
    map evaluation.'MicroRegion'.map()
}