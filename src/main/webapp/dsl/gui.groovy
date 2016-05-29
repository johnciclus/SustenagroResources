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

widgetAttributes 'tabs', pagination: true, submitTopButton: false

widgetAttributes 'form', method: 'post', formClass: 'form-horizontal'