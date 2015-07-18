/**
 * Created by dilvan on 7/15/15.
 */

// Esta DSL descreve como o aplicativo será gerado. Ele é em inglês pois
// facilita na hora de publicarmos os papers. Porém, o aplicativo pode ser
// em português ou inglês. Isso vai depender da ontologia, nela teremos as
// definições nas duas linguas.
// nome vai aparecer onde um nome for necessário
name 'Avaliação da sustentabilidade em agricultura'

// Aba de descrição do conteúdo: um texto em markdown que você vai escrever
// (esse texto também pode estar num arquivo)
description '''
O processo de avaliação da sustentabilidade é composto pelas seguintes etapas:

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
features {
    instance 'dbp:MicroRegion'
    instance ':AgriculturalEfficiency'
    subclass ':ProductionUnit'
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

// Para cada índice, é possível indicar fórmulas para o cálculo de cada
// atributo. Essas fórmulas podem ser tão complicadas como você queira.
//index 'environment' {
//    indice = ind.'co2 emission'
//    attribute.soil = 2.0 * indicator.soil + 5.1 *
//            indicator.landBurn
//    attribute.transport = 3 * indicator.'road Length' + 7 *
//            indicator.distance
//    atributo.transporte = 3 * indicador.'comprimento da estrada' + 7 *
//            indicador.distância
//}
//
//matrix 'm1'{
//
//}
//
//// Cada recomendação terá uma fórmula lógica que permite especificar
//// quando ela deve ser mostrada. Essas fórmulas podem ser tão complexas
//// quanto necessário. Caso o resultado da fórmula dê verdadeiro, o texto
//// (em markdown) depois de action: vai ser mostrado.
//recommendation 'rec 1' {
//    condition i.environment > 3.5 || i.social <7 && attribute.transport <9
//    text ''' markdown blah blah blah '''
//}
//
//map {
//
//}
//// A saída do programa tem APENAS:
//// 1. Matriz de sustentabilidade.
//// 2. Conjunto de remomendações
//// 3. Mapas da microregião

