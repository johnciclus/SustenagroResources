package sustenagro

import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.io.StringDocumentSource
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat
import grails.converters.*
import utils.Uri

class AdminController {

    def dsl
    def slp
    def ontology

    def index(){
        def indicators = getIndicators()
        def dimensions = getDimensions()

        Uri.simpleDomain(indicators, "http://bio.icmc.usp.br/sustenagro#", '')
        Uri.simpleDomain(dimensions, "http://bio.icmc.usp.br/sustenagro#", '')

        OutputStream out = new ByteArrayOutputStream()
        ontology.getManager().saveOntology(ontology.getOntology(), new ManchesterSyntaxDocumentFormat(), out)

        render(view: 'index', model: [code: new File('dsl/dsl.groovy').text,
                                      ontology: new String(out.toByteArray(), "UTF-8"),
                                      indicators: indicators,
                                      dimensions: dimensions])
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
        def outgoingLinks = slp.query(":${params.id_base} ?p ?o. FILTER( ?o != :${params.id_base})", '', '*')
        def incomingLinks = slp.query("?s ?p :${params.id_base}. FILTER( ?s != :${params.id_base})", '', '*')

        println outgoingLinks
        println incomingLinks

        if(params.id_base != params.id){
            println "different id"
            if(incomingLinks.size() == 0){
                println "Zero incoming links"
            }
        }

        def respond = ['result': 'ok']

        render respond as JSON
    }

    def indicatorsReset(){

    }

    def ontology(){
        def manager = ontology.getManager()
        OWLOntology ontologyMan = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(params['ontology']))

        OutputStream out = new ByteArrayOutputStream()
        manager.saveOntology(ontologyMan, new RDFXMLDocumentFormat(), out)

        File file = new File("/home/dilvan/javabkp/var/www/sustenagro/SustenAgroRDF.rdf")

        manager.saveOntology(ontologyMan, new RDFXMLDocumentFormat(), IRI.create(file.toURI()))

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

    def getIndicator(String id){
        slp.select("distinct ?valuetype ?label ?dimension ?attribute")
            .query("?dimension rdfs:subClassOf :Indicator."+
            "?attribute rdfs:subClassOf ?dimension."+
            "$id rdfs:subClassOf ?attribute; rdfs:label ?label."+
            "$id rdfs:subClassOf ?y."+
            "?y  owl:onClass ?valuetype."+
            "FILTER( ?dimension != :Indicator && ?dimension != ?attribute && ?attribute != $id )")
    }

    def getIndicators(){
        slp.select("distinct ?id ?valuetype ?label ?dimension ?attribute")
            .query('''?dimension rdfs:subClassOf :Indicator.
            ?attribute rdfs:subClassOf ?dimension.
            ?id rdfs:subClassOf ?attribute; rdfs:label ?label.
            ?id rdfs:subClassOf ?y.
            ?y  owl:onClass ?valuetype.
            FILTER( ?dimension != :Indicator && ?dimension != ?attribute && ?attribute != ?id )''',
            'ORDER BY ?id')
    }

    def getDataValues(){
        slp.query('''?valuetype rdfs:subClassOf :Value.
            FILTER( ?valuetype != :Value && !isBlank(?valuetype) )''')
    }

    def getDimensions(){
        slp.select("distinct ?id ?label")
            .query('''?id rdfs:subClassOf :Indicator.
            ?attribute rdfs:subClassOf ?id.
            ?indicator rdfs:subClassOf ?attribute.
            ?id rdfs:label ?label.
            FILTER( ?id != :Indicator && ?id != ?attribute && ?id != ?indicator && ?attribute != ?indicator)''')
    }

    def getAttributes(String dimension) {
        slp.select("distinct ?attribute")
            .query("?attribute rdfs:subClassOf ${dimension}."+
            "?indicator rdfs:subClassOf ?attribute."+
            "FILTER( ?attribute != ${dimension} && ?attribute != ?indicator)")
    }

    def getOptions(String cls) {
        slp.query("?id rdf:type $cls. "+
            "?id rdfs:label ?label. " +
            "?id :dataValue ?value.")
    }

    def attributes(){
        def attr = getAttributes(':'+params['dimension'])
        println attr

        Uri.simpleDomain(attr, 'http://bio.icmc.usp.br/sustenagro#')

        render attr as XML
    }

    def indicatorForm(){
        def id = params['id']
        def data = [:]
        def result = Uri.simpleDomain(getIndicator(':'+id), "http://bio.icmc.usp.br/sustenagro#", '')

        if(result.size() == 1){
            data['indicator'] = result[0]
            data['indicator']['id'] = id
            data['valuetypes'] = Uri.simpleDomain(getDataValues(), "http://bio.icmc.usp.br/sustenagro#", '')
            data['dimensions'] = Uri.simpleDomain(getDimensions(), "http://bio.icmc.usp.br/sustenagro#", '')
            data['attributes'] = [:]
            data['options'] = [:]

            data['dimensions'].each{
                data['attributes'][it.id] = Uri.simpleDomain(getAttributes(':'+it.id), "http://bio.icmc.usp.br/sustenagro#", '')
            }

            data['valuetypes'].each{
                data['options'][it.valuetype] = Uri.simpleDomain(getOptions(':'+it.valuetype), "http://bio.icmc.usp.br/sustenagro#", '')
            }

            if(data['indicator']['valuetype'] == 'Real'){

            }
            else{

            }
        }

        render( template: '/widgets/indicatorForm',
                model:    [indicator: data['indicator'],
                           valuetypes: data['valuetypes'],
                           dimensions: data['dimensions'],
                           attributes: data['attributes'],
                           options: data['options'],
                           ind_tags: ['id', 'label', 'dimension', 'attribute', 'valuetype']
                ]);
    }

    def autoComplete(){
        def list = []
        def commands = ['title', 'data', 'description', 'features', 'show', 'instance', 'subclass', 'matrix', 'map', 'dimension', 'prog', 'sum', 'average']

        if(params['word']){
            def cmds = commands.findAll{ it.contains(params['word']) }
            def identifiers = slp.select('distinct ?s').query("?s ?p ?o. FILTER regex(str(?s), 'http://bio.icmc.usp.br/sustenagro#$params.word', 'i')")
            Uri.simpleDomain(identifiers,'http://bio.icmc.usp.br/sustenagro#','')
            def labels = slp.select('distinct ?label').query("?s rdfs:label ?label. FILTER regex(str(?label), '$params.word', 'i')")
            cmds.each{list.push(['name': it, 'value': it, 'score': 2000, 'meta': 'command'])}
            identifiers.each{list.push(['name': it.s, 'value': it.s, 'score': 2000, 'meta': 'identifier'])}
            labels.each{list.push(['name': it.label, 'value': it.label, 'score': 2000, 'meta': 'label'])}
        }
        else{
            commands.each{list.push(['name': it, 'value': it, 'score': 2000, 'meta': 'command'])}
        }

        render list as JSON
    }
}