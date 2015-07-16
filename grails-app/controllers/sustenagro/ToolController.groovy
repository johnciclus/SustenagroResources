package sustenagro

import com.github.slugify.Slugify

import static rdfSlurper.RDFSlurper.N

class ToolController {
    def slp

    def index() {

        def microregions = slp.query('?id a dbp:MicroRegion; sa:name ?name.')
        def cultures = slp.query('?id a sa:SugarcaneProductionSystem; sa:name ?name.')
        def technologies = slp.query('?id a sa:AgriculturalTechnology; sa:name ?name; sa:description ?description.')
        def production_unit_types = slp.query('?id rdfs:subClassOf sa:ProductionUnit; rdfs:label ?name.')// filter (lang(?name) = "pt")')

        render(view: 'index',
               model:    [microregions: microregions,
                          cultures: cultures,
                          technologies: technologies,
                          production_unit_types: production_unit_types])
    }

    def productionUnitCreate() {
        Slugify slug = new Slugify()

        def production_unit_id = slug.slugify(params['production_unit_name'])

        slp.addNode(
            N('sa:'+production_unit_id,
                'rdf:type': slp.v(params['production_unit_type']),
                'sa:name': params['production_unit_name'],
                'sa:microregion': slp.v(params['production_unit_microregion']),
                'sa:culture': slp.v(params['production_unit_culture']),
                'sa:technology': slp.v(params['production_unit_technology'])
            ))

        slp.g.commit()
        //        g.saveRDF(new FileOutputStream('ontology/SustenAgroOntologyAndIndividuals.rdf'), 'rdf-xml')
        redirect(action: 'assessment', id: production_unit_id)
    }

    def assessment() {

        def indicators = {cls ->
            slp.query('?a  rdfs:subClassOf '+cls+''' .
                   ?id rdfs:subClassOf ?a; rdfs:label ?title.
                   ?id rdfs:subClassOf ?y.
                   ?y  owl:onClass ?class.
                   ?class rdfs:subClassOf ?valueType.''')
        }

        def environmental_indicators = indicators('sa:EnvironmentalIndicator')
        def economic_indicators = indicators('sa:EconomicIndicator')
        def social_indicators = indicators('sa:SocialIndicator')

        def categorical = [:]
        def categ = {ind ->
            ind.each{
                slp.query("<$it.id> rdfs:subClassOf ?a. ?a owl:onClass ?id").each{
                   categorical[it.id] = []
                }
            }
        }

        categ(environmental_indicators)
        categ(economic_indicators)
        categ(social_indicators)

        categorical.each{ k, v ->
            slp.query("?id a <$k>; rdfs:label ?title.").each{
                v.push(it)
            }
        }

        String name = slp."sa:$params.id".'$sa:name'

        render(view: 'assessment',
               model: [sustenagro: 'http://biomac.icmc.usp.br/sustenagro#',
                       production_unit_id: params.id,
                       production_unit_name: name,
                       environmental_indicators: environmental_indicators,
                       economic_indicators: economic_indicators,
                       social_indicators: social_indicators,
                       categorical: categorical ])
    }
}