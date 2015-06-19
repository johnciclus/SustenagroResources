package sustenagro

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import com.tinkerpop.gremlin.groovy.Gremlin
import com.tinkerpop.blueprints.impls.sail.impls.MemoryStoreSailGraph

class SustenAgroMemStore extends MemoryStoreSailGraph{
    public SustenAgroMemStore(){
        super()
        this.addNamespace('sustenagro','http://www.biomac.icmc.usp.br:8080/sustenagro#')
        this.loadRDF(new FileInputStream('ontology/SustenAgroOntology.rdf'), 'http://www.biomac.icmc.usp.br:8080/sustenagro#', 'rdf-xml', null)
    }    
}

class Application extends GrailsAutoConfiguration {
    static{
        Gremlin.load()
    }
    static void main(String[] args) {
        GrailsApp.run(Application)
    }
}