package sustenagro

import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.io.StringDocumentSource
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat

class AdminController {

    def dsl
    def slp

    def index(){



        render(view: 'index', model: [code: new File('dsl/dsl.groovy').text,
                                      ontology: new File('ontology/SustenAgroOntology.man').text])
    }

    def dsl() {

        def file = new File('dsl/dsl.groovy')
        file.write(params['code'])

        try{
            dsl.reLoad()
        }
        catch (Exception e){
            handleException(e, "DSL Error")
        }

        //FileUtils.deleteRecursively( new File( DB_PATH ) )

        //Binding binding = new Binding([html: new Html(elements)])
        //Html html = new Html()
        //binding.setVariable("html", html)
        
        //GroovyShell shell = new GroovyShell(new Binding([html: new Html()]))

        //Script script = shell.parse("html.make({" + params["code"] + "})")
        //render script.run()

        //Binding binding   = new Binding()
        //binding.setVariable("html", html)
        //GroovyShell shell = new GroovyShell(binding)

        //render shell.evaluate("html.make({" + params["code"] + "})")


        /*String sparqlQueryString= "select distinct ?Concept where {[] a ?Concept} LIMIT 100"

        Query query = QueryFactory.create(sparqlQueryString)
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)

        ResultSet results = qexec.execSelect()
        println results       

        qexec.close()*/

        render "ok"
    }

    def ontology(){
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(params['ontology']))

        OutputStream out = new ByteArrayOutputStream()
        manager.saveOntology(ontology, new RDFXMLDocumentFormat(), out)

        File file = new File("/home/dilvan/javabkp/var/www/sustenagro/SustenAgroRDF.rdf")

        manager.saveOntology(ontology, new RDFXMLDocumentFormat(), IRI.create(file.toURI()))

        slp.removeAll()

        slp.loadRDF(new ByteArrayInputStream(out.toByteArray()))

        //error not load data properties
        //slp.g.loadRDF(new ByteArrayInputStream(out.toByteArray()), 'http://bio.icmc.usp.br/sustenagro#', 'rdf-xml', null)

        //slp.g.commit()

        //File localFolder = new File("TestingOntology")
        //manager.addIRIMapper(new AutoIRIMapper(localFolder, true))
        //OWLOntology o = manager.createOntology(example_save_iri);
        //println 'Ontology loaded'
        render "ok"
    }

    def dslReset() {
        def file = new File('dsl/dsl.groovy')
        file.write(new File('dsl/dsl-bk.groovy').text)

        try{
            dsl.reLoad()
        }
        catch (Exception e){
            handleException(e, "DSL Error")
        }
        redirect(action: 'index')
    }

    private void handleException(Exception e, String message) {
        flash.message = message
        String eMessage = ExceptionUtils.getRootCauseMessage(e)
        log.error message(code: "sic.log.error.ExceptionOccurred", args: ["${eMessage}", "${e}"])
        //redirect(action:index)
    }
}