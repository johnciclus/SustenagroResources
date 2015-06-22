package sustenagro

import com.tinkerpop.blueprints.impls.sail.impls.MemoryStoreSailGraph
import com.tinkerpop.gremlin.groovy.Gremlin

class MemStore extends MemoryStoreSailGraph{
    static{
        Gremlin.load()
    }

    public MemStore(){
        super()
        this.addNamespace('sustenagro','http://www.biomac.icmc.usp.br:8080/sustenagro#')
        this.addNamespace('w3','http://www.w3.org/1999/02/22-rdf-syntax-ns#')
        this.addNamespace('dbpedia','http://dbpedia.org/ontology/')
        this.loadRDF(new FileInputStream('ontology/SustenAgroOntology.rdf'), 'http://www.biomac.icmc.usp.br:8080/sustenagro#', 'rdf-xml', null)
    }
}