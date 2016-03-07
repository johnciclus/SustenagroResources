package sustenagro

import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.io.StringDocumentSource
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat
import grails.converters.*
import utils.Uri

class AdminController {

    def ontology
    def dsl
    def k

    def index(){
        def indicators = k[':Indicator'].getIndicators()
        def dimensions = k[':Indicator'].getDimensions()

        Uri.simpleDomain(indicators, "http://bio.icmc.usp.br/sustenagro#", '')
        Uri.simpleDomain(dimensions, "http://bio.icmc.usp.br/sustenagro#", '')

        OutputStream out = new ByteArrayOutputStream()
        //ontology.getManager().saveOntology(ontology.getOntology(), new ManchesterSyntaxDocumentFormat(), out)

        render(view: 'index', model: [dsl_code: new File('dsl/dsl.groovy').text,
                                      gui_code: new File('dsl/gui.groovy').text,
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

    def dslReset(){
        def file = new File('dsl/dsl.groovy')
        file.write(new File('dsl/dsl-bk.groovy').text)

        def response = dsl.reLoad(file.text)

        redirect(action: 'index')
    }

    def gui(){
        println params
        def response  = [:]

        response.status = 'ok'

        render response as XML
    }

    def updateIndicator(){
        def id = params.id_base

        if(params.id_base != params.id){
            def labels = [:]

            params.each{ key, value ->
                if(key.startsWith('label@')) {
                    labels[key.substring(key.indexOf('@')+1)] = value
                }
            }

            String sparql = "<"+ k.toURI(":" + params.id) +">"+
                    " rdf:type <http://bio.icmc.usp.br/sustenagro#Indicator>; "+
                    " rdfs:subClassOf <"+ k.toURI(":" + params.attribute) +">; "+
                    " rdf:type owl:Class; "+
                    " rdf:type owl:NamedIndividual; "+
                    " <http://bio.icmc.usp.br/sustenagro#weight> \""+params.weight+"\"^^xsd:double; "

            labels.each{ key, value ->
                sparql += " rdfs:label \""+value+"\"@"+key+"; "
            }

            sparql += " rdfs:subClassOf _:b. "+
                      " _:b owl:onClass <"+ k.toURI(":" + params.valuetype) +">"

            println sparql

            k.insert(sparql)

            //k.delete()
        }
        else{
            def indicator = k[id].getIndicator()
            Uri.simpleDomain(indicator, "http://bio.icmc.usp.br/sustenagro#", '')

            def lang

            indicator[0].each{ key, value ->
                if(indicator[0][key] != params[key]){
                    if(key.startsWith('label')){
                        lang = key.getAt((key.indexOf('@')+1)..(key.size()-1))
                        k.update("DELETE {<http://bio.icmc.usp.br/sustenagro#$id> rdfs:label ?label}\n" +
                                "INSERT {<http://bio.icmc.usp.br/sustenagro#$id> rdfs:label \""+params[key]+"\"@$lang}\n" +
                                "WHERE {<http://bio.icmc.usp.br/sustenagro#$id> rdfs:label ?label. \nFILTER (lang(?label) = '$lang')}")

                    }
                }
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

        k.removeAll()

        k.loadRDF(new ByteArrayInputStream(out.toByteArray()))

        //error not load data properties
        //k.g.loadRDF(new ByteArrayInputStream(out.toByteArray()), 'http://bio.icmc.usp.br/sustenagro#', 'rdf-xml', null)

        //k.g.commit()

        //File localFolder = new File("TestingOntology")
        //manager.addIRIMapper(new AutoIRIMapper(localFolder, true))
        //OWLOntology o = manager.createOntology(example_save_iri);
        //println 'Ontology loaded'
        render "ok"
    }

    def ontologyReset(){

    }

    def attributes(){
        def attr = k[':'+params['dimension']].getAttributes()
        println attr

        Uri.simpleDomain(attr, 'http://bio.icmc.usp.br/sustenagro#')

        render attr as XML
    }

    def indicatorForm(){
        def id = params['id']
        def data = [:]
        def result = Uri.simpleDomain(k[':'+id].getIndicator(), "http://bio.icmc.usp.br/sustenagro#", '')

        println result

        if(result.size() == 1){
            data['indicator'] = result[0]
            data['indicator']['id'] = id
            data['valuetypes'] = Uri.simpleDomain(k[':Value'].getDataValues(), "http://bio.icmc.usp.br/sustenagro#", '')
            data['dimensions'] = Uri.simpleDomain(k[':Indicator'].getDimensions(), "http://bio.icmc.usp.br/sustenagro#", '')
            data['attributes'] = [:]
            data['options'] = [:]

            data['dimensions'].each{
                data['attributes'][it.id] = Uri.simpleDomain(k[':'+it.id].getAttributes(), "http://bio.icmc.usp.br/sustenagro#", '')
            }

            data['valuetypes'].each{
                data['options'][it.valuetype] = Uri.simpleDomain(k[':'+it.valuetype].getOptions(), "http://bio.icmc.usp.br/sustenagro#", '')
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
                           ind_tags: ['id', 'label@en', 'label@pt', 'weight', 'dimension', 'attribute', 'valuetype']
                ]);
    }

    def autoComplete(){
        def list = []
        def commands = ['title', 'data', 'description', 'features', 'show', 'instance', 'subclass', 'matrix', 'map', 'dimension', 'prog', 'sum', 'average']

        if(params['word']){
            def cmds = commands.findAll{ it.contains(params['word']) }
            def identifiers = k[':Indicator'].selectSubject(params.word)
            Uri.simpleDomain(identifiers,'http://bio.icmc.usp.br/sustenagro#','')
            def labels = k[':Indicator'].selectLabel(params.word)
            cmds.each{list.push(['name': it, 'value': it, 'score': 2000, 'meta': 'command'])}
            identifiers.each{list.push(['name': it.s, 'value': it.s, 'score': 2000, 'meta': 'identifier'])}
            labels.each{list.push(['name': it.label, 'value': it.label, 'score': 2000, 'meta': 'label'])}
        }
        else{
            commands.each{list.push(['name': it, 'value': it, 'score': 2000, 'meta': 'command'])}
        }

        render list as JSON
    }

    /*
    def getIndicator(String id){
        k.select("distinct ?valuetype ?label ?dimension ?attribute")
            .query("?dimension rdfs:subClassOf :Indicator."+
            "?attribute rdfs:subClassOf ?dimension."+
            "$id rdfs:subClassOf ?attribute; rdfs:label ?label."+
            "$id rdfs:subClassOf ?y."+
            "?y  owl:onClass ?valuetype."+
            "FILTER( ?dimension != :Indicator && ?dimension != ?attribute && ?attribute != $id )")
    }


    def getIndicators(){
        k.select("distinct ?id ?valuetype ?label ?dimension ?attribute")
            .query('''?dimension rdfs:subClassOf :Indicator.
            ?attribute rdfs:subClassOf ?dimension.
            ?id rdfs:subClassOf ?attribute; rdfs:label ?label.
            ?id rdfs:subClassOf ?y.
            ?y  owl:onClass ?valuetype.
            FILTER( ?dimension != :Indicator && ?dimension != ?attribute && ?attribute != ?id )''',
            'ORDER BY ?id')
    }

    def getDataValues(){
        k.query('''?valuetype rdfs:subClassOf :Value.
            FILTER( ?valuetype != :Value && !isBlank(?valuetype) )''')
    }

    def getDimensions(){
        k.select("distinct ?id ?label")
            .query('''?id rdfs:subClassOf :Indicator.
            ?attribute rdfs:subClassOf ?id.
            ?indicator rdfs:subClassOf ?attribute.
            ?id rdfs:label ?label.
            FILTER( ?id != :Indicator && ?id != ?attribute && ?id != ?indicator && ?attribute != ?indicator)''')
    }

    def getAttributes(String dimension) {
        k.select("distinct ?attribute")
            .query("?attribute rdfs:subClassOf ${dimension}."+
            "?indicator rdfs:subClassOf ?attribute."+
            "FILTER( ?attribute != ${dimension} && ?attribute != ?indicator)")
    }

    def getOptions(String cls) {
        k.query("?id rdf:type $cls. "+
            "?id rdfs:label ?label. " +
            "?id :dataValue ?value.")
    }

    def outgoingLinks(String cls){
        k.query(":$cls ?p ?o. FILTER( ?o != :$cls)", '', '*')
    }

    def incomingLinks(String cls){
        k.query("?s ?p :$cls. FILTER( ?s != :$cls)", '', '*')
    }

    def selectSubject(String word){
        k.select('distinct ?s').query("?s ?p ?o. FILTER regex(str(?s), 'http://bio.icmc.usp.br/sustenagro#$word', 'i')")
    }

    def selectLabel(String word){
        k.select('distinct ?label').query("?s rdfs:label ?label. FILTER regex(str(?label), '$word', 'i')")
    }
    */
}