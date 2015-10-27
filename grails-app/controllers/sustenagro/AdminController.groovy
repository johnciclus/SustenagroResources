package sustenagro

import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.io.StringDocumentSource
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat

import grails.converters.*

class AdminController {

    def dsl
    def slp

    def index(){
        def indicators = slp.select("distinct ?id ?class ?title")
                            .query('''?dim rdfs:subClassOf :Indicator.
                                      ?att rdfs:subClassOf ?dim.
                                      ?id rdfs:subClassOf ?att; rdfs:label ?title.
                                      ?id rdfs:subClassOf ?y.
                                      ?y  owl:onClass ?class.''')

        indicators.each{ indicator ->
            indicator.each{
                //println it.value
                it.value = it.value.replace("http://bio.icmc.usp.br/sustenagro#",":")
            }
        }
        println indicators.size()

        render(view: 'index', model: [code: new File('dsl/dsl.groovy').text,
                                      ontology: new File('ontology/SustenAgroOntology.man').text,
                                      indicators: indicators,
                                      ind_tags: ["id", "class", "title"]])
    }

    def dsl(){
        def response = dsl.reLoad(params['code'])

        if(response.status == 'ok')
            new File('dsl/dsl.groovy').write(params['code'])

        render response as XML
    }

    def dslReset() {
        def file = new File('dsl/dsl.groovy')
        file.write(new File('dsl/dsl-bk.groovy').text)

        def response = dsl.reLoad(file.text)

        redirect(action: 'index')
    }

    def indicators(){

    }

    def indicatorsReset(){

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


}