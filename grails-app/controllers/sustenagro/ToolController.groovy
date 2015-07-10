package sustenagro

import com.github.slugify.Slugify
import static rdfSlurper.RDFSlurper.N

class ToolController {
    def g
    //def s
    def slp

    def index() {
        def microregions = []
        def cultures = []
        def technologies = []
        def production_unit_types = []

        //println slp.isa('dbp:MicroRegion').where

        microregions = slp.sparql('select * where {?id a dbp:MicroRegion; sa:name ?name.}')

        //slp.'dbp:MicroRegion'.in('rdf:type').each{ microregions.push(id: it.id, name: it.out('sa:name').next().value) }

        cultures = slp.sparql('select * where {?id a sa:SugarcaneProductionSystem; sa:name ?name.}')
        technologies = slp.sparql('select * where {?id a sa:AgriculturalTechnology; sa:name ?name; sa:description ?description.}')
        production_unit_types = slp.sparql("select * where {?id rdfs:subClassOf sa:ProductionUnit; rdfs:label ?name. filter (lang(?name) = 'pt')}")

        //slp.'sa:SugarcaneProductionSystem'._().in("rdf:type").each{ cultures.push(id: it.id, name: it.out('sa:name').next().value) }
        //slp.'sa:AgriculturalTechnology'._().in("rdf:type").each{ technologies.push(id: it.id, name: it.out('sa:name').next().value, 'description': it.out('sa:description').next().value) }
        //slp.'sa:ProductionUnit'._().in("rdfs:subClassOf").each{ production_unit_types.push(id: it.id, name: it.out('rdfs:label').has('lang','pt').next().value) }

        render(view: "index", model:    [microregions: microregions,
                                         cultures: cultures,
                                         technologies: technologies,
                                         production_unit_types: production_unit_types])
    }

    def productionUnitCreate() {
        Slugify slug = new Slugify()

        //def g = slp.g

        def production_unit_id = slug.slugify(params["production_unit_name"])
//        N('sa:'+production_unit_id,
//                'rdf:type': slp[params["production_unit_type"]],
//                'sa:name': params["production_unit_name"],
//                'sa:microregion': slp[params["production_unit_microregion"]],
//                'sa:culture': slp[params["production_unit_culture"]],
//                'sa:technology': slp[params["production_unit_technology"]]
//        )

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
        def economic_indicators = []
        def social_indicators = []
        def categorical = [:]

        //def g = slp.g

//        g.v('sustenagro:EnvironmentalIndicator').in("rdfs:subClassOf").each{ environmental_indicators.push(  id: it.id, title: it.out('terms:title').has('lang','pt').next().value, description: it.out('terms:description').has('lang','pt').next().value, assessmentQuestion: it.out('sustenagro:assessmentQuestion').has('lang','pt').next().value) }
        g.v('sustenagro:EnvironmentalIndicator').in("rdfs:subClassOf").in("rdfs:subClassOf").each{
            environmental_indicators.push(
                    id: it.id,
                    title: it.out('rdfs:label').has('lang','pt').next().value,
                    'class': it.out("rdfs:subClassOf").has("kind","bnode").out("owl:onClass").next().id,
                    'valueType': it.out("rdfs:subClassOf").has("kind","bnode").out("owl:onClass").out("rdfs:subClassOf").next().id)
        }
//        slp.sparql('?id rdfs:subClassOf ?a. ?a rdfs:subClassOf sa:EnvironmentalIndicator.') each {
//            environmental_indicators.push(
//                    id:
//            )
//        }
//        environmental_indicators = slp.sparql(
//                'select * where {' +
//                        '?id rdfs:subClassOf ?a. ' +
//                        '?a  rdfs:subClassOf sa:EnvironmentalIndicator. ' +
//                        '?id rdfs:label ?title. filter (lang(?title) = "pt")}')
//

        g.v('sustenagro:EconomicIndicator').in("rdfs:subClassOf").in("rdfs:subClassOf").each{ economic_indicators.push(id: it.id, title: it.out('rdfs:label').has('lang','pt').next().value, 'class': it.out("rdfs:subClassOf").has("kind","bnode").out("owl:onClass").next().id, 'valueType': it.out("rdfs:subClassOf").has("kind","bnode").out("owl:onClass").out("rdfs:subClassOf").next().id) }
        g.v('sustenagro:SocialIndicator').in("rdfs:subClassOf").in("rdfs:subClassOf").each{ social_indicators.push(id: it.id, title: it.out('rdfs:label').has('lang','pt').next().value, 'class': it.out("rdfs:subClassOf").has("kind","bnode").out("owl:onClass").next().id, 'valueType': it.out("rdfs:subClassOf").has("kind","bnode").out("owl:onClass").out("rdfs:subClassOf").next().id) }

        //println "economic_indicators"
        //println economic_indicators

        environmental_indicators.each{ g.v(it.id).out("rdfs:subClassOf").has("kind","bnode").out("owl:onClass").each{ categorical[it.id] = [] } }
        economic_indicators.each{ g.v(it.id).out("rdfs:subClassOf").has("kind","bnode").out("owl:onClass").each{ categorical[it.id] = [] } }
        social_indicators.each{ g.v(it.id).out("rdfs:subClassOf").has("kind","bnode").out("owl:onClass").each{ categorical[it.id] = [] } }

        categorical.each{ k, v -> g.v(k).in('rdf:type').each{ println "indicator id: "+it.id; v.push(id: it.id, title: it.out('rdfs:label').has('lang','pt').next().value)} }

        String name = g.v('sustenagro:'+params.id).out('sustenagro:name').next().value

        render(view: "assessment", model: [sustenagro: "http://biomac.icmc.usp.br/sustenagro#",
                                           production_unit_id: params.id,
                                           production_unit_name: name,
                                           environmental_indicators: environmental_indicators,
                                           economic_indicators: economic_indicators,
                                           social_indicators: social_indicators,
                                           categorical: categorical ])
    }
}