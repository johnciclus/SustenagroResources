package sustenagro

class ToolController {
    /*static {
        Gremlin.load()
    }*/


    def index() {

        /*def g = new MemoryStoreSailGraph()
        g.addNamespace('tg','http://tinkerpop.com#')

        g.loadRDF(new FileInputStream('/www/graph-example-1.ntriple'), 'http://tinkerpop.com#', 'n-triples', null)
        def results = []

        println(' 1 -> ')
        g.v('tg:1').outE.id.each{println it}

        def v = g.addVertex('"lixo"^^<http://www.w3.org/2001/XMLSchema#string>');
        g.addEdge(g.v('tg:1'), v, 'http://kjhkjh.com/jhgjh')

        println(' 1 -> ')
        g.v('tg:1').out.map.each{println it} 

        println(' 2 -> ')
        g.v('tg:1').out('http://kjhkjh.com/jhgjh').kind.each{println it}
        g.v('tg:1').out('http://kjhkjh.com/jhgjh').id.each{println it}

        g.saveRDF(new FileOutputStream('lixo.ntriple'), 'n-triples')
        */

        /*def production_units = ProductionUnit.findAll()
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
