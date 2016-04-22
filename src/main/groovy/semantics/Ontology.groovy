package semantics

import org.semanticweb.owlapi.model.OWLOntology
import org.semanticweb.owlapi.model.OWLOntologyManager
import org.semanticweb.owlapi.apibinding.OWLManager

/**
 * Created by john on 11/19/15.
 */
class Ontology {
    OWLOntologyManager manager
    OWLOntology ontology

    public Ontology(String path){
        manager = OWLManager.createOWLOntologyManager()
        ontology = manager.loadOntologyFromOntologyDocument(new File(path))
    }

    public OWLOntologyManager getManager(){
        return manager
    }

    public OWLOntology getOntology(){
        return ontology
    }


}
