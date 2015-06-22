package sustenagro

class ToolController {
    def memStore
    def index() {
        ArrayList microregion_names = []
        ArrayList culture_names = []
        ArrayList technologies = []
        ArrayList production_units = []

        memStore.v('dbpedia:MicroRegion').in("rdf:type").out('sustenagro:name').value.fill(microregion_names)
        memStore.v('sustenagro:SugarcaneProductionSystem').in("rdf:type").out('sustenagro:name').value.fill(culture_names)
        memStore.v('sustenagro:AgriculturalTechnology').in("rdf:type").each{ technologies.push( ['id': it.id, 'name': it.out('sustenagro:name').next().value, 'description': it.out('sustenagro:description').next().value]) }
        memStore.v('sustenagro:ProductionUnit').in("rdfs:subClassOf").out('rdfs:label').has('lang','pt').value.fill(production_units)

        println technologies

        render(view: "index", model:    [microregion_names: microregion_names,
                                         culture_names: culture_names,
                                         technologies: technologies,
                                         production_units: production_units
                                        ])
    }

    def location() {
        render(template: 'form')
        /*def production_unit = new ProductionUnit(name: params["production_unit_name"], location: params["production_unit_location"], microregion: params["production_unit_microregion"])

        if (production_unit.save()) {
            render(template: 'form', model: [production_unit: production_unit])
		}
        else{
            production_unit.errors.each {
                println it
            }
        }*/

    }

}