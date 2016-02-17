import org.semanticweb.owlapi.model.OWLOntologyManager

import rdfUtils.RDFSlurper
import rdfUtils.Ontology
import rdfUtils.Know
import dsl.DSL
import sustenagro.SecurityConfiguration

// Place your Spring DSL code here
beans = {
    //slp(RDFSlurper, 'http://10.62.9.236:9999/bigdata/namespace/kb/sparql')       //http://java.icmc.usp.br:9999/bigdata/namespace/kb/sparql
    dsl(DSL, 'dsl/dsl.groovy')
    ontology(Ontology, 'ontology/SustenAgroRDF.rdf')
    k(Know, 'http://localhost:9999/blazegraph/namespace/kb/sparql')
    //http://172.17.0.2:9999         http://10.62.9.236:9999/bigdata/namespace/kb/sparql
    webSecurityConfiguration(SecurityConfiguration)
}

// "/bigdata/namespace/kb/sparql"
// "/bigdata/namespace/kb/sparql"
