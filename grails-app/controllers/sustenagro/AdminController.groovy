package sustenagro

import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.io.StringDocumentSource
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat
import grails.converters.*
import utils.Uri

class AdminController {

    def dsl
    def slp

    def index(){
        def indicators = slp.select("distinct ?id ?class ?title ?dimension ?attribute")
                            .query('''?dimension rdfs:subClassOf :Indicator.
                                      ?attribute rdfs:subClassOf ?dimension.
                                      ?id rdfs:subClassOf ?attribute; rdfs:label ?title.
                                      ?id rdfs:subClassOf ?y.
                                      ?y  owl:onClass ?class.
                                      FILTER( ?dimension != :Indicator && ?dimension != ?attribute && ?attribute != ?id )''',
                                      'ORDER BY ?id')

        def classes = slp.query('''?class rdfs:subClassOf :Value.
                                   FILTER( ?class != :Value)''')

        def dimensions = slp.select("distinct ?dimension")
                            .query('''?dimension rdfs:subClassOf :Indicator.
                                      ?attribute rdfs:subClassOf ?dimension.
                                      ?indicator rdfs:subClassOf ?attribute.
                                      FILTER( ?dimension != :Indicator && ?dimension != ?attribute && ?dimension != ?indicator && ?attribute != ?indicator)''')



        def attributes = []

        Uri.simpleDomain(indicators, "http://bio.icmc.usp.br/sustenagro#")
        Uri.simpleDomain(classes,    "http://bio.icmc.usp.br/sustenagro#")
        Uri.simpleDomain(dimensions, "http://bio.icmc.usp.br/sustenagro#")

        //println getAttributes(":EnvironmentalIndicator")

        render(view: 'index', model: [code: new File('dsl/dsl.groovy').text,
                                      ontology: new File('ontology/SustenAgroOntology.man').text,
                                      ind_tags: ['id', 'class', 'dimension', 'attribute', 'title'],
                                      indicators: indicators,
                                      classes: classes,
                                      dimensions: dimensions,
                                      attributes: attributes  ])
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

    def ontologyReset(){

    }

    def getAttributes(String dimension) {
        def result = slp.select("distinct ?attribute")
                .query("?attribute rdfs:subClassOf ${dimension}."+
                "?indicator rdfs:subClassOf ?attribute."+
                "FILTER( ?attribute != ${dimension} && ?attribute != ?indicator)")
        result
    }

    def attributes(){
        def attr = getAttributes(params['dimension'])
        println attr

        Uri.simpleDomain(attr, "http://bio.icmc.usp.br/sustenagro#")

        render attr as XML
    }
}