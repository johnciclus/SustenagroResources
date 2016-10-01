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

package dsl
//  dataType "DataTypeID", "widgetName"

//dataType  "xsd:date", widget: "date"

//default widget: 'category',

dataType "rdfs:Literal", widget: "textForm"
dataType "owl:real", widget: "numberForm"
dataType "xsd:int", widget: "numberForm"
dataType "xsd:integer", widget: "numberForm"
dataType "xsd:float", widget: "numberForm"
dataType "xsd:date", widget: "dateForm"
dataType "xsd:string", widget: "textForm"
dataType "xsd:language", widget: "textForm"
dataType "xsd:boolean", widget: "categoryForm"
dataType ":SimpleCategory", widget: "categoryForm"
dataType ":ProductionUnit", widget: "categoryForm"
dataType ":SugarcaneSourceCategory", widget: "categoryForm"
dataType ":CanavialLongevityCategory", widget: "categoryForm"
dataType ":AgriculturalProductionSystemCategory", widget: "categoryForm"
dataType ":AvailabilityOfEvaluationResultsCategory", widget: "categoryForm"
dataType "http://dbpedia.org/page/Microregion_(Brazil)", widget: "categoryForm"

/*
view("tool/index"){
    title "Avaliação da sustentabilidade na agricultura"

    description """O processo de avaliação da sustentabilidade é composto pelas seguintes etapas:

1. Localização da lavoura
2. Caracterização da cultura, tecnologia e tipo de sistema produtivo
3. Definição dos indicadores
4. Recomendações de sustentabilidade"""

    selectEntity ":ProductionUnit", label : "Selecionar unidade produtiva"

    createEntity ":ProductionUnit", label : "Cadastrar nova unidade produtiva para realizar avaliação", {

    }
}

view("tool/assessments"){

}
*/