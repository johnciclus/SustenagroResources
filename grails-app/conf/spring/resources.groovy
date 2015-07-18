import rdfSlurper.RDFSlurper
import dsl.DSL

// Place your Spring DSL code here
beans = {
    slp(RDFSlurper, 'ontology/SustenAgroOntology.rdf')
    dsl(DSL,'dsl.groovy')
}
