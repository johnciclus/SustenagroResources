package sustenagro

import com.github.slugify.Slugify

class ToolController {
    def g

    def index() {
        def microregions = []
        def cultures = []
        def technologies = []
        def production_unit_types = []

        g.v('dbpedia:MicroRegion').in("rdf:type").each{ microregions.push(id: it.id, name: it.out('sustenagro:name').next().value) }
        g.v('sustenagro:SugarcaneProductionSystem').in("rdf:type").each{ cultures.push(id: it.id, name: it.out('sustenagro:name').next().value) }
        g.v('sustenagro:AgriculturalTechnology').in("rdf:type").each{ technologies.push(id: it.id, name: it.out('sustenagro:name').next().value, 'description': it.out('sustenagro:description').next().value) }
        g.v('sustenagro:ProductionUnit').in("rdfs:subClassOf").each{ production_unit_types.push(id: it.id, name: it.out('rdfs:label').has('lang','pt').next().value) }

        render(view: "index", model:    [microregions: microregions,
                                         cultures: cultures,
                                         technologies: technologies,
                                         production_unit_types: production_unit_types
                                        ])
    }

    def productionUnitCreate() {
        Slugify slug = new Slugify()

        def production_unit_id = slug.slugify(params["production_unit_name"])

        def v_production_unit = g.addVertex("sustenagro:"+production_unit_id) //Add Microregion to id
        g.addEdge(v_production_unit, g.v(params["production_unit_type"]), 'rdf:type')

        def name = g.addVertex('"'+params["production_unit_name"]+'"^^<http://www.w3.org/2001/XMLSchema#string>')
        g.addEdge(v_production_unit, name, 'sustenagro:name')
        g.addEdge(v_production_unit, g.v(params["production_unit_microregion"]), 'sustenagro:microregion')
        g.addEdge(v_production_unit, g.v(params["production_unit_culture"]), 'sustenagro:culture')
        g.addEdge(v_production_unit, g.v(params["production_unit_technology"]), 'sustenagro:technology')

        g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')

        redirect(action: "assessment", id: production_unit_id)
    }

    def assessment() {

        def environmental_indicators = []

        g.v('sustenagro:EnvironmentalIndicator').in("rdfs:subClassOf").each{ environmental_indicators.push(  id: it.id, title: it.out('terms:title').has('lang','pt').next().value, description: it.out('terms:description').has('lang','pt').next().value, assessmentQuestion: it.out('sustenagro:assessmentQuestion').has('lang','pt').next().value) }

        String name = g.v('sustenagro:'+params.id).out('sustenagro:name').next().value

        render(view: "assessment", model: [production_unit_id: params.id,
                                           production_unit_name: name,
                                           environmental_indicators: environmental_indicators ])
    }

}