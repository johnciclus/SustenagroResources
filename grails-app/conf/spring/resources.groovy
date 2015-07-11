import rdfSlurper.RDFSlurper
import sustenagro.MemStore
import sustenagro.SlurpRDF

// Place your Spring DSL code here
beans = {
    g(MemStore)
    slp(RDFSlurper, 'ontology/SustenAgroOntology.rdf')
}
