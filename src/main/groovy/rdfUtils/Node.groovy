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

    def getAssessments(){
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
        k.select('?superClass').query("<$URI> rdfs:subClassOf ?superClass. FILTER(?superClass != <$URI>)")
    }

    def getSubClass(){
        def res = k.select('?subClass').query("?subClass rdfs:subClassOf <$URI>. FILTER(?subClass != <$URI>)")

        def prefixes = k.getPrefixesMap()

        res.metaClass.shortURI = {
            def uris = delegate.collect {
                if(it.subClass instanceof String){
                    prefixes.each{ key, value ->
                        it.subClass = it.subClass.replace(value, '')
                    }
                }
                it.subClass
            }
            if(uris.size()==1)
                return uris[0]
            else
                return uris
        }
        return res
    }

    def getDataType(){
        def res = k.select('?dataType').query("<$URI> rdfs:subClassOf ?dataType. ?dataType rdfs:subClassOf :DataType. FILTER (?dataType != :DataType && ?dataType != <$URI>)")
        //println k.getPrefixesMap()

        res.metaClass.shortURI = {
            def uris = delegate.collect {
                if(it.dataType instanceof String)
                    it.dataType = it.dataType.replace(k.getBasePrefix(), '')
            }
            if(uris.size()==1)
                return uris[0]
            else
                return uris
        }
        return res
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

    def getMap(String args){
        def res = k.select('distinct '+args)
            .query("<$URI> :appliedTo ?productionUnit. " +
            "?productionUnit dbp:MicroRegion ?microregion. " +
            "?microregion <http://dbpedia.org/property/pt/mapa> ?map.")

        res.metaClass.map = { (delegate.size()==1)? delegate[0]['map'] :delegate.collect { it['map'] } }
        return res
    }

    def getGrandchildren(String args){
        def argsList = args.split(' ')
        def query = "?subClass rdfs:subClassOf <$URI>. "+
                    "?id rdfs:subClassOf ?subClass"

        if (argsList.contains('?label'))
            query +="; rdfs:label ?label";

        if (argsList.contains('?weight'))
            query +="; :weight ?weight";

        query +='''. ?id rdfs:subClassOf ?y.
                     ?y owl:onClass ?category.
                     ?category rdfs:subClassOf ?valueType. '''+
                "FILTER(?subClass != <$URI> && ?subClass != ?id && (?valueType = :Categorical || ?valueType = :Real))"

        k.select('distinct '+args).query(query, "ORDER BY ?label")
    }

    def getProductionUnity(String args){
        def argsList = args.split(' ')

        def query = "<$URI> :appliedTo ?ins."

        if (argsList.contains('?label'))
            query += "?ins rdfs:label ?label.";

        if (argsList.contains('?productionUnit'))
            query += "?ins rdf:type ?productionUnitType."+
                     "?productionUnitType rdfs:label ?productionUnit.";

        if (argsList.contains('?efficiency'))
            query += "?ins :AgriculturalEfficiency ?efficiencyType."+
                     "?efficiencyType rdfs:label ?efficiency.";

        if (argsList.contains('?microregion'))
            query += "?ins dbp:MicroRegion ?microregionType." +
                     "?microregionType rdfs:label ?microregion.";

        if (argsList.contains('?productionUnit'))
            query += "FILTER( ?productionUnitType != :ProductionUnit )"

        def res = k.select('distinct '+args).query(query)

        /*def id
        argsList.each {
            id = it.substring(1)
            println id
            res.metaClass[id] = {
                println 'Array: ' + id
                return (delegate.size() == 1) ? delegate[0][id] : delegate.collect { it[id] }
            }
        }*/

        if (argsList.contains('?label'))
            res.metaClass['label'] = { (delegate.size()==1)? delegate[0]['label'] :delegate.collect { it['label'] } }
        if (argsList.contains('?productionUnit'))
            res.metaClass['productionUnit'] = { (delegate.size()==1)? delegate[0]['productionUnit'] :delegate.collect { it['productionUnit'] } }
        if (argsList.contains('?microregion'))
            res.metaClass['microregion'] = { (delegate.size()==1)? delegate[0]['microregion'] :delegate.collect { it['microregion'] } }
        if (argsList.contains('?efficiency'))
            res.metaClass['efficiency'] = { (delegate.size()==1)? delegate[0]['efficiency'] :delegate.collect { it['efficiency'] } }

        return res
    }

    def getGranchildrenIndividuals(String assessment, String args){
        def argsList = args.split(' ')

        def query = "?subClass rdfs:subClassOf <$URI>."+
                "?id rdfs:subClassOf ?subClass. "+
                "?in a ?id."+
                "?in dc:isPartOf <"+k.toURI(assessment)+">.";

        if (argsList.contains('?value'))
            query +="?in :value ?value.";

        if (argsList.contains('?weight'))
            query +="?in :hasWeight ?weight.";

        query += "FILTER( ?subClass != <$URI> && ?id != <$URI> && ?subClass != ?id)"

        //println 'Query'
        //println 'select distinct '+args+ ' where '+query

        k.select('distinct '+args).query(query, "ORDER BY ?in")
    }

    def getIndividualsValue(String assessment, String args){
        def argsList = args.split(' ')
        def res = k.select('distinct '+args)
            .query("<"+k.toURI(assessment)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
            "?subClass rdfs:subClassOf <$URI>."+
            "?id rdfs:subClassOf ?subClass." +
            "?id rdfs:label ?label." +
            "?ind a ?id." +
            "?ind <http://bio.icmc.usp.br/sustenagro#value> ?valueType."+
            "?valueType <http://bio.icmc.usp.br/sustenagro#dataValue> ?value."+
            "?valueType rdfs:label ?valueTypeLabel."+
            "FILTER( ?subClass != ?id && ?subClass != <$URI> )", "ORDER BY ?label")

        res.metaClass.ind = { (delegate.size()==1)? delegate[0]['ind'] :delegate.collect { it['ind'] } }
        res.metaClass.label = { (delegate.size()==1)? delegate[0]['label'] :delegate.collect { it['label'] } }
        res.metaClass.value = { (delegate.size()==1)? delegate[0]['value'] :delegate.collect { it['value'] } }
        res.metaClass.valueType = { (delegate.size()==1)? delegate[0]['valueType'] :delegate.collect { it['valueType'] } }
        res.metaClass.valueTypeLabel = { (delegate.size()==1)? delegate[0]['valueTypeLabel'] :delegate.collect { it['valueTypeLabel'] } }
        return res
    }

    def getIndividualsValueWeight(String assessment, String args){
        def argsList = args.split(' ')
        def res = k.select('distinct '+args)
                .query("<"+k.toURI(assessment)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
                "?subClass rdfs:subClassOf <$URI>."+
                "?id rdfs:subClassOf ?subClass." +
                "?id rdfs:label ?label." +
                "?id :weight ?weight." +
                "?ind a ?id." +
                "?ind <http://bio.icmc.usp.br/sustenagro#value> ?valueType."+
                "?valueType <http://bio.icmc.usp.br/sustenagro#dataValue> ?value."+
                "?valueType rdfs:label ?valueTypeLabel."+
                "FILTER( ?subClass != ?id && ?subClass != <$URI> )", "ORDER BY ?label")

        res.metaClass.ind = { (delegate.size()==1)? delegate[0]['ind'] :delegate.collect { it['ind'] } }
        res.metaClass.label = { (delegate.size()==1)? delegate[0]['label'] :delegate.collect { it['label'] } }
        res.metaClass.value = { (delegate.size()==1)? delegate[0]['value'] :delegate.collect { it['value'] } }
        res.metaClass.valueType = { (delegate.size()==1)? delegate[0]['valueType'] :delegate.collect { it['valueType'] } }
        res.metaClass.valueTypeLabel = { (delegate.size()==1)? delegate[0]['valueTypeLabel'] :delegate.collect { it['valueTypeLabel'] } }
        res.metaClass.weight = { (delegate.size()==1)? delegate[0]['weight'] :delegate.collect { it['weight'] } }
        res.metaClass.equation = { eq ->
            eq.resolveStrategy = Closure.DELEGATE_FIRST
            delegate.collect({ eq.delegate = it; eq()})
        }
        return res
    }

    def getIndividualsFeatureValueWeight(String assessment, String args) {
        def argsList = args.split(' ')
        def res = k.select('distinct '+args)
                   .query("<"+k.toURI(assessment)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
                        "?id rdfs:subClassOf <$URI>." +
                        "?id rdfs:label ?label." +
                        "?ind a ?id." +
                        "?ind <http://bio.icmc.usp.br/sustenagro#value> ?valueType." +
                        "?valueType <http://bio.icmc.usp.br/sustenagro#dataValue> ?value." +
                        "?valueType rdfs:label ?valueTypeLabel."+
                        "?ind :hasWeight ?weightType." +
                        "?weightType rdfs:label ?weightTypeLabel."+
                        "?weightType <http://bio.icmc.usp.br/sustenagro#dataValue> ?weight."+
                        "FILTER( ?id != <$URI> )", "ORDER BY ?label")

        /*def id
        argsList.each{
            id = it.substring(1)
            println id
            res.metaClass[id] = {
                println 'Array: '+id
                return (delegate.size()==1)? delegate[0][id] : delegate.collect { it[id] }
            }
        }*/
        res.metaClass.ind = { (delegate.size()==1)? delegate[0]['ind'] :delegate.collect { it['ind'] } }
        res.metaClass.label = { (delegate.size()==1)? delegate[0]['label'] :delegate.collect { it['label'] } }
        res.metaClass.value = { (delegate.size()==1)? delegate[0]['value'] :delegate.collect { it['value'] } }
        res.metaClass.valueType = { (delegate.size()==1)? delegate[0]['valueType'] :delegate.collect { it['valueType'] } }
        res.metaClass.valueTypeLabel = { (delegate.size()==1)? delegate[0]['valueTypeLabel'] :delegate.collect { it['valueTypeLabel'] } }
        res.metaClass.weight = { (delegate.size()==1)? delegate[0]['weight'] :delegate.collect { it['weight'] } }
        res.metaClass.weightType = { (delegate.size()==1)? delegate[0]['weightType'] :delegate.collect { it['weightType'] } }
        res.metaClass.equation = { eq ->
            eq.resolveStrategy = Closure.DELEGATE_FIRST
            delegate.collect({ eq.delegate = it; eq()})
        }
        return res
    }

    def getIndicator(){
        k.select("distinct ?valuetype ?label ?weight ?dimension ?attribute ")
            .query("?dimension rdfs:subClassOf :Indicator."+
            "?attribute rdfs:subClassOf ?dimension."+
            "<$URI> rdfs:subClassOf ?attribute; rdfs:label ?label."+
            "<$URI> rdfs:subClassOf ?y."+
            "<$URI> :weight ?weight."+
            "?y  owl:onClass ?valuetype."+
            "FILTER( ?dimension != :Indicator && ?dimension != ?attribute && ?attribute != <$URI> )",'','*')
    }

    def getIndicators(){
        k.select("distinct ?id ?valuetype ?label ?dimension ?weight ?attribute")
         .query("?dimension rdfs:subClassOf <$URI>. "+
         '''?attribute rdfs:subClassOf ?dimension.
            ?id rdfs:subClassOf ?attribute; rdfs:label ?label.
            ?id rdfs:subClassOf ?y.
            ?y  owl:onClass ?valuetype.
            ?id :weight ?weight.'''+
            "FILTER( ?dimension != <$URI> && ?dimension != ?attribute && ?attribute != ?id )",'ORDER BY ?id','*')
    }

    def getDataValues(){
        k.query('''?valuetype rdfs:subClassOf :Value.
            FILTER( ?valuetype != :Value && !isBlank(?valuetype) )''')
    }

    def getDimensions(){
        k.select("distinct ?id ?label").query('''?id rdfs:subClassOf :Indicator.
            ?attribute rdfs:subClassOf ?id.
            ?indicator rdfs:subClassOf ?attribute.
            ?id rdfs:label ?label.
            FILTER( ?id != :Indicator && ?id != ?attribute && ?id != ?indicator && ?attribute != ?indicator)''', "ORDER BY ?label")
    }

    def getAttributes() {
        k.select("distinct ?attribute").query("?attribute rdfs:subClassOf <$URI>. ?indicator rdfs:subClassOf ?attribute. FILTER( ?attribute != <$URI> && ?attribute != ?indicator)")
    }

    def getOptions() {
        k.query("?id rdf:type <$URI>. ?id rdfs:label ?label. ?id :dataValue ?value.")
    }

    def outgoingLinks(){
        k.query("<$URI> ?p ?o", '', '*')
    }

    def incomingLinks(){
        k.query("?s ?p <$URI>", '', '*')
    }

    def selectSubject(String word){
        k.select('distinct ?s').query("?s ?p ?o. FILTER regex(str(?s), 'http://bio.icmc.usp.br/sustenagro#$word', 'i')")
    }

    def selectLabel(String word){
        k.select('distinct ?label').query("?s rdfs:label ?label. FILTER regex(str(?label), '$word', 'i')")
    }

    def findURI(String label){
        k.select('distinct ?uri').query("?uri rdfs:label '$label'@${k.lang}.")
    }
}
