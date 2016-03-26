import com.github.slugify.Slugify
import org.pegdown.PegDownProcessor
import semantics.Know
import dsl.DSL
import dsl.GUIDSL

beans = {
    //ontology(Ontology, 'ontology/SustenAgroRDF.rdf')
    slugify(Slugify)
    md(PegDownProcessor)
    k(Know, 'http://172.17.0.1:9999/blazegraph/namespace/kb/sparql')
    gui(GUIDSL, 'dsl/gui.groovy', grailsApplication.mainContext)
    dsl(DSL, 'dsl/dsl.groovy', grailsApplication.mainContext)
}
