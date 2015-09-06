import rdfSlurper.RDFSlurper
import dsl.DSL

// Place your Spring DSL code here
beans = {
    slp(RDFSlurper, 'http://localhost:9999/bigdata/sparql')
    dsl(DSL,'dsl.groovy')
}
