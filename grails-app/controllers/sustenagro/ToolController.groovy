package sustenagro

import com.tinkerpop.blueprints.impls.sail.impls.MemoryStoreSailGraph

class ToolController {
    def memStore
    def index() {
        println "*** Tool index point"
        println memStore

        //MemoryStoreSailGraph g = new MemoryStoreSailGraph()
        //memStore.addNamespace('sustenagro','http://www.biomac.icmc.usp.br:8080/sustenagro#')
        //memStore.loadRDF(new FileInputStream('ontology/SustenAgroOntology.rdf'), 'http://www.biomac.icmc.usp.br:8080/sustenagro#', 'rdf-xml', null)
        def results = []

        println(' 1 -> ')
        memStore.v('sustenagro:IndicatorComponent').inE.id.each{println it}

        println("*** SustenAgroIndividuals ***")
        memStore.saveRDF(new FileOutputStream('ontology/SustenAgroIndividuals.rdf'), 'rdf-xml')
        println("*** SustenAgroIndividuals ***")


        /*def v = g.addVertex('"lixo"^^<http://www.w3.org/2001/XMLSchema#string>');
        g.addEdge(g.v('tg:1'), v, 'http://kjhkjh.com/jhgjh')

        println(' 1 -> ')
        g.v('tg:1').out.map.each{println it} 

        println(' 2 -> ')
        g.v('tg:1').out('http://kjhkjh.com/jhgjh').kind.each{println it}
        g.v('tg:1').out('http://kjhkjh.com/jhgjh').id.each{println it}



        def production_units = ProductionUnit.findAll()
        [production_units: production_units]*/
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
