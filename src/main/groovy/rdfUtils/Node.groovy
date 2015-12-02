package rdfUtils

/**
 * Created by john on 12/2/15.
 */
class Node {
    String URI
    Know k

    public Node(Know k, String uri){
        this.k = k
        this.URI = uri
    }

    def getLabel(){
        k.query("<$URI> rdfs:label ?label.")[0].label
    }

    def getProductionUnitType(){
        k.query("<$URI> rdf:type ?type. FILTER(?type != :ProductionUnit)")[0].type
    }

    def getLabelDescription(String property) {
        k.query("?id $property <$URI>; rdfs:label ?label. optional {?id dc:description ?description}. FILTER ( ?id != <$URI> )")
    }

    def getEvaluations(){
        k.query("?id a :Evaluation. ?id :appliedTo <$URI>")
    }

    def getLabelDataValue(){
        k.query("?id a <$URI>; rdfs:label ?label; :dataValue ?dataValue")
    }

    def getLabelAppliedTo(){
        k.query("?id :appliedTo <$URI>. ?id rdfs:label ?label")
    }

    def getInstances(){
        k.select('distinct ?id ?label')
         .query("?id a <$URI>; rdfs:label ?label.",
                "ORDER BY ?label")
    }

    def getSuperClass(){
        k.select('?subClass')
         .query("<$URI> rdfs:subClassOf ?subClass.")
    }

    def getChildren(){
        k.select('distinct ?id ?label ?category ?valueType')
                .query("?id rdfs:subClassOf <$URI>; rdfs:label ?label."+
                     '''?id rdfs:subClassOf ?y.
                        ?y owl:onClass ?category.
                        ?category rdfs:subClassOf ?valueType.
                        FILTER(?valueType = :Categorical || ?valueType = :Real)''',
                        "ORDER BY ?label")
    }

    def getMap(){
        k.select('?map')
            .query("<$URI> :appliedTo ?productionUnit. " +
            "?productionUnit dbp:Microregion ?microregion. " +
            "?microregion <http://dbpedia.org/property/pt/mapa> ?map.")
    }

    def getGrandchildren(){
        k.select('distinct ?id ?label ?subClass ?category ?valueType')
                .query("?subClass rdfs:subClassOf <$URI>."+
                     '''?id rdfs:subClassOf ?subClass; rdfs:label ?label.
                        ?id rdfs:subClassOf ?y.
                        ?y owl:onClass ?category.
                        ?category rdfs:subClassOf ?valueType. '''+
                       "FILTER(?subClass != <$URI> && ?subClass != ?id && (?valueType = :Categorical || ?valueType = :Real))",
                       "ORDER BY ?label")
    }

    def getGranchildrenIndividuals(String evaluation, String args){
        def argsList = args.split(' ')

        def query = "?subClass rdfs:subClassOf <$URI>."+
                "?id rdfs:subClassOf ?subClass."+
                "?ind a ?id."+
                "?ind dc:isPartOf <"+k.toURI(evaluation)+">.";

        if (argsList.contains('?value'))
            query +="?ind :value ?value.";

        if (argsList.contains('?weight'))
            query +="?ind :hasWeight ?weight.";

        query += "FILTER( ?subClass != <$URI> && ?id != <$URI> && ?subClass != ?id)"

        k.select('distinct '+args).query(query, "ORDER BY ?ind")
    }

    def getIndividualsValue(String evaluation){
        k.select('distinct ?ind ?valueType ?value')
            .query("<"+k.toURI(evaluation)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
            "?ind a ?id." +
            "?id rdfs:subClassOf ?subClass." +
            "?subClass rdfs:subClassOf <$URI>."+
            "?ind <http://bio.icmc.usp.br/sustenagro#value> ?valueType."+
            "?valueType <http://bio.icmc.usp.br/sustenagro#dataValue> ?value.", "ORDER BY ?ind")
    }

    def getIndividualsValueWeight(String evaluation) {
        k.select('distinct ?ind ?valueType ?value ?weightType ?weight')
            .query("<"+k.toURI(evaluation)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
            "?ind a ?id." +
            "?id rdfs:subClassOf ?subClass." +
            "?subClass rdfs:subClassOf <$URI>." +
            "?ind <http://bio.icmc.usp.br/sustenagro#value> ?valueType." +
            "?valueType <http://bio.icmc.usp.br/sustenagro#dataValue> ?value." +
            "?ind :hasWeight ?weightType." +
            "?weightType <http://bio.icmc.usp.br/sustenagro#dataValue> ?weight.", "ORDER BY ?ind")
    }

    def getIndicator(){
        k.select("distinct ?valuetype ?label ?dimension ?attribute")
            .query("?dimension rdfs:subClassOf :Indicator."+
            "?attribute rdfs:subClassOf ?dimension."+
            "<$URI> rdfs:subClassOf ?attribute; rdfs:label ?label."+
            "<$URI> rdfs:subClassOf ?y."+
            "?y  owl:onClass ?valuetype."+
            "FILTER( ?dimension != :Indicator && ?dimension != ?attribute && ?attribute != <$URI> )")
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

    def getAttributes() {
        k.select("distinct ?attribute")
            .query("?attribute rdfs:subClassOf <$URI>."+
            "?indicator rdfs:subClassOf ?attribute."+
            "FILTER( ?attribute != <$URI> && ?attribute != ?indicator)")
    }

    def getOptions() {
        k.query("?id rdf:type <$URI>. "+
                "?id rdfs:label ?label. " +
                "?id :dataValue ?value.")
    }

    def outgoingLinks(){
        k.query("<$URI> ?p ?o. FILTER( ?o != <$URI>)", '', '*')
    }

    def incomingLinks(){
        k.query("?s ?p <$URI>. FILTER( ?s != <$URI>)", '', '*')
    }

    def selectSubject(String word){
        k.select('distinct ?s').query("?s ?p ?o. FILTER regex(str(?s), 'http://bio.icmc.usp.br/sustenagro#$word', 'i')")
    }

    def selectLabel(String word){
        k.select('distinct ?label').query("?s rdfs:label ?label. FILTER regex(str(?label), '$word', 'i')")
    }
}
