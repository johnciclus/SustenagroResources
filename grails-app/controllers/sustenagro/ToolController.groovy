package sustenagro

import com.github.slugify.Slugify

class ToolController {
    def memStore

    def index() {
        ArrayList microregions = []
        ArrayList cultures = []
        ArrayList technologies = []
        ArrayList production_unit_types = []

        memStore.v('dbpedia:MicroRegion').in("rdf:type").each{ microregions.push(id: it.id, name: it.out('sustenagro:name').next().value) }
        memStore.v('sustenagro:SugarcaneProductionSystem').in("rdf:type").each{ cultures.push(id: it.id, name: it.out('sustenagro:name').next().value) }
        memStore.v('sustenagro:AgriculturalTechnology').in("rdf:type").each{ technologies.push(id: it.id, name: it.out('sustenagro:name').next().value, 'description': it.out('sustenagro:description').next().value) }
        memStore.v('sustenagro:ProductionUnit').in("rdfs:subClassOf").each{ production_unit_types.push(id: it.id, name: it.out('rdfs:label').has('lang','pt').next().value) }

        println technologies

        render(view: "index", model:    [microregions: microregions,
                                         cultures: cultures,
                                         technologies: technologies,
                                         production_unit_types: production_unit_types
                                        ])
    }

    def production_unit_create() {
        Slugify slug = new Slugify()

        println params["production_unit_name"]
        println params["production_unit_microregion"]
        println params["production_unit_culture"]
        println params["production_unit_technology"]
        println params["production_unit_type"]

        def v_production_unit = memStore.addVertex("sustenagro:"+slug.slugify(params["production_unit_name"]+'-')) //Add Microregion to id
        memStore.addEdge(v_production_unit, memStore.v(params["production_unit_type"]), 'rdf:type')

        def name = memStore.addVertex('"hello"^^<http://www.w3.org/2001/XMLSchema#string>')
        memStore.addEdge(v_production_unit, name, 'sustenagro:name')

        memStore.addEdge(v_production_unit, memStore.v(params["production_unit_microregion"]), 'sustenagro:microregion')
        memStore.addEdge(v_production_unit, memStore.v(params["production_unit_culture"]), 'sustenagro:culture')
        memStore.addEdge(v_production_unit, memStore.v(params["production_unit_technology"]), 'sustenagro:technology')


        //v = memStore.addVertex("sustenagro:"+slug.slugify(params["production_unit_name"]+'-'))
        //memStore.addEdge(v, params["production_unit_name"], 'sustenagro:name')

        memStore.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')

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