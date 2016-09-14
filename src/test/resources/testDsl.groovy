/*
 * Copyright (c) 2015-2016 Dilvan Moreira.
 * Copyright (c) 2015-2016 John Garavito.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
 '''

// Informações que serão lidas antes dos indicadores. No exemplo serão
// mostradas todas as culturas, tecnologias e meios de produção que
// estiverem na ontologia
// Mantemos crop, mas se a única que estiver na ontologia for Cana,
// a interface só mostra uma opção (sem possibilidade de escolha)
// Na ontologia, location definiria as microregiões do IBGE.
// Se a fazenda ficar em mais de uma micro-região?
features {
    instance 'Microregion'
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
prog {
    indice = indicator.'co2 emission'
    soil = 2.0 * indicator.soil + 5.1 *
            indicator.landBurn + 1000
    transport = 3 * indicator.'road length' + 7 *
            indicator.distância
}

//
//matrix 'm1', indice, soil {
//
//}
//
//// Cada recomendação terá uma fórmula lógica que permite especificar
//// quando ela deve ser mostrada. Essas fórmulas podem ser tão complexas
//// quanto necessário. Caso o resultado da fórmula dê verdadeiro, o texto
//// (em markdown) depois de action: vai ser mostrado.
//recommendation 'rec 1' {
//    condition environment > 3.5 || social <7 && indicator.'co2 emission' <9
//    text ''' markdown blah blah blah '''
//}
////
//map {
//
//}
//// A saída do programa tem APENAS:
//// 1. Matriz de sustentabilidade.
//// 2. Conjunto de remomendações
//// 3. Mapas da microregião

