package sustenagro

import com.tinkerpop.blueprints.impls.sail.impls.MemoryStoreSailGraph
import com.tinkerpop.gremlin.groovy.Gremlin

class MemStore extends MemoryStoreSailGraph{
    static{
        Gremlin.load()
    }

    public MemStore(){
        super()
        this.addNamespace('sustenagro','http://biomac.icmc.usp.br/sustenagro#')
        this.addNamespace('rdf','http://www.w3.org/1999/02/22-rdf-syntax-ns#')
        this.addNamespace('rdfs','http://www.w3.org/2000/01/rdf-schema#')
        this.addNamespace('terms','http://purl.org/dc/terms#')
        this.addNamespace('dbpedia','http://dbpedia.org/ontology/')
        this.loadRDF(new FileInputStream('ontology/SustenAgroOntology.rdf'), 'http://biomac.icmc.usp.br/sustenagro#', 'rdf-xml', null)
    }
}