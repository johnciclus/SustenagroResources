// Esta DSL descreve como o aplicativo será gerado. Ele é em inglês pois
// facilita na hora de publicarmos os papers. Porém, o aplicativo pode ser
// em português ou inglês. Isso vai depender da ontologia, nela teremos as
// definições nas duas linguas.
// nome vai aparecer onde um nome for necessário
name 'Avaliação da sustentabilidade em agricultura'

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
    instance 'Microregion'
    instance ':AgriculturalEfficiency'
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
    evaluation.'EnvironmentalIndicator'

    environment =   (evaluation.'BiologicalPestControl' ? 1:-1) +
                    (evaluation.'PlanningSystematicPlanting' ? 1:-1) +
                    (evaluation.'StandardAerialSpraying' ? 1:-1) +
                    evaluation.'VinasseAndEthanolRelation'

    environmentAvg= environment/4

    economic =      2.0 * evaluation.'Eficiência operacional da Usina (crescimento vertical da usina, recuperação e avanço)' + 5.1 *
                    evaluation.'Eficiência energética das caldeiras para cogeração de energia'

    social =        3 * evaluation.EnergyEfficiencyOfBoilersForCogeneration + 7 *
                    evaluation.OperationalEfficiencyPlant

    // THE REPORT

    // Just showing text
    show '***That is the report:***'

    // Cada recomendação terá uma fórmula lógica que permite especificar
    // quando ela deve ser mostrada. Essas fórmulas podem ser tão complexas
    // quanto necessário. Caso o resultado da fórmula seja verdadeiro, o texto
    // (em markdown) vai ser mostrado.
    // Aqui temos 4 possibilidades de implementação:
    if (environment > 3.5 || social ==7)
        recommendation '**markdown** *First* option'

    recommendation environment > 3.5 || social == 7, '**Second** *option*'
    recommendation if:(environment > 3.5 || social == 7), '**Third** *option*'
    recommendation if:(environment > 3.5 || social == 7), show: ''' *Fourth* *option* '''

    show 'Matrix de avaliação'

    show 'Indice de Magnitude: ' + environment
    show 'Indice de Segurança: ' + environmentAvg

    // Matrix de sustentabilidade
    matrix x: environment, y: environmentAvg, labelX: 'Indice de Magnitude', labelY: 'Indice de Segurança', rangeX: [-5,5], rangeY: [-2,2]

    //Outra possibilidade
    //matrix  x: [label: 'kkk', value = environment, range: [1,9]],
    //        y: [label: 'kkk', value = social, range: [1,9]]

    show 'Mapa da microregião'

    map evaluation.'Microregion'
}
