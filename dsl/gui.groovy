//  dataType "DataTypeID", "widgetName"

//dataType  "xsd:date", widget: "date"

//default widget: 'category',

dataType "rdfs:Literal", widget: "string"
dataType "owl:real", widget: "number"
dataType "xsd:int", widget: "number"
dataType "xsd:integer", widget: "number"
dataType "xsd:float", widget: "number"
dataType "xsd:date", widget: "date"
dataType "xsd:string", widget: "string"
dataType "xsd:language", widget: "string"
dataType "xsd:boolean", widget: "category"
dataType ":SimpleCategory", widget: "category"
dataType ":ProductionUnit", widget: "category"
dataType ":SugarcaneSourceCategory", widget: "category"
dataType ":CanavialLongevityCategory", widget: "category"
dataType ":AgriculturalProductionSystemCategory", widget: "category"
dataType ":AvailabilityOfEvaluationResultsCategory", widget: "category"
dataType "http://dbpedia.org/page/Microregion_(Brazil)", widget: "category"

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