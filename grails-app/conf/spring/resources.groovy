import org.pegdown.PegDownProcessor
import semantics.Know
import dsl.DSL
import dsl.GUIDSL

// Place your Spring DSL code here
beans = {
    //slp(RDFSlurper, 'http://10.62.9.236:9999/bigdata/namespace/kb/sparql')       //http://java.icmc.usp.br:9999/bigdata/namespace/kb/sparql
    //ontology(Ontology, 'ontology/SustenAgroRDF.rdf')
    md(PegDownProcessor)
    k(Know, 'http://localhost:9999/blazegraph/namespace/kb/sparql')
    gui(GUIDSL, 'dsl/gui.groovy', grailsApplication.mainContext)
    dsl(DSL, 'dsl/dsl.groovy', grailsApplication.mainContext)
    //http://172.17.0.2:9999         http://10.62.9.236:9999/bigdata/namespace/kb/sparql
    //webSecurityConfiguration(SecurityConfiguration)
}
