//  dataType "DataTypeID", "widgetName"

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
dataType "dbp:State",                                       widget: "categoryForm"
dataType ":SugarcaneSourceCategory",                        widget: "categoryForm"
dataType ":CanavialLongevityCategory",                      widget: "textForm"
dataType ":AgriculturalProductionSystemCategory",           widget: "categoryForm"
dataType ":AvailabilityOfEvaluationResultsCategory",        widget: "categoryForm"
dataType "http://dbpedia.org/page/Microregion_(Brazil)",    widget: "categoryForm"

widgetAttributes 'navBarRoute', evalObjTitle: "Unidade produtiva", newEvalObjLabel: "Nova unidade produtiva", analysesTitle: "Análises", newAnalysisLabel: "Nova análise"

widgetAttributes 'listEvaluationObjects', label : "Unidade produtiva", submitLabel: "Novo análise"

widgetAttributes 'createEvaluationObject', submitLabel: "Cadastrar"

widgetAttributes 'tabs', submitLabel: 'Avaliar', previousLabel: 'Anterior', nextLabel: 'Próximo', pagination: true, submitTopButton: false

widgetAttributes 'form', method: 'post', formClass: 'form-horizontal'