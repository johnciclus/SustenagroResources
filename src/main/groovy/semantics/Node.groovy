package semantics

/**
 * Created by john on 12/2/15.
 */
class Node {
    String URI
    Know k
    Map patterns = [:]

    public Node(Know k, String uri = null){
        this.k = k
        this.URI = uri

        //rdf:Property
        this.patterns['type']       = "<$URI> rdf:type ?type. "
        this.patterns['superClass'] = "<$URI> rdfs:subClassOf ?superClass. "

        //owl:ObjectProperty
        this.patterns['appliedTo']  = "<$URI> :appliedTo ?appliedTo. "
        this.patterns['range']      = "<$URI> rdfs:range ?range. "

        //owl:DatatypeProperty
        this.patterns['mapa']       = "<$URI> <http://dbpedia.org/property/pt/mapa> ?mapa. "
        this.patterns['harvestYear']= "<$URI> :harvestYear ?harvestYear. "
        //owl:TransitiveProperty

        // owl:AnnotationProperty
        this.patterns['label']      = "<$URI> rdfs:label ?label. "
        this.patterns['comment']    = "<$URI> rdfs:comment ?comment. "
        this.patterns['weight']     = "<$URI> :weight ?weight. "
        this.patterns['description']= "<$URI> dcterm:description ?description. "
        this.patterns['title']      = "<$URI> dcterm:title ?title. "
        this.patterns['creator']    = "<$URI> dc:creator ?creator. "

    }

    def getAttr(String args='', Map params = [:]) {
        def argsList = args.tokenize(' ?')
        def select = ''
        def query = ''
        def order = ''
        def result

        //println argsList
        argsList.each{
            if(argsList.contains(it))
                query += patterns[it]
        }
        /*
        if(argsList.contains('label'))
            query += patterns['label']
        if(argsList.contains('superClass'))
            query += patterns['superClass']
        */

        if(params.containsKey('FILTER')){           //(?type != :ProductionUnit && ?type != ui:EvaluationObject && !isBlank(?type) )"
            query += "FILTER(" + params['FILTER'] + ")"
        }

        //println query

        if(argsList.contains('label')){
            order = 'order by ?label'
        }
        else if(argsList.size()==1){
            order = 'order by ?'+argsList[0]
        }

        select = 'distinct '
        argsList.each{
            select += '?'+it
        }

        result = k.select(select).query(query, order)

        //println result

        if(argsList.size()==1){
            result = result.collect{ it[argsList[0]]}
        }

        (result.size()==1)? result[0] : result
    }

    def getLabel(Map params = [:]){
        getAttr('?label', params)
    }

    def getType(Map params = [:]){
        getAttr('?type', params)
    }

    def getRange(Map params = [:]) {
        //def result = k.select("distinct ?range").query("<$URI> rdfs:range ?range.")
        getAttr('?range', params)
    }

    def getDataProperties(){
        def result = k.select('?dataProperty ?dataPropertyLabel ?value').query("<$URI> ?dataProperty ?value. ?dataProperty rdf:type owl:DatatypeProperty; rdfs:label ?dataPropertyLabel. ")
        return result
    }

    def getObjectProperties(){
        def result = k.select('?objectProperty ?objectPropertyLabel ?value ?valueLabel').query("<$URI> ?objectProperty ?value. ?value rdfs:label ?valueLabel. ?objectProperty rdf:type owl:ObjectProperty; rdfs:label ?objectPropertyLabel. ")
        return result
    }

    def getDataType(){
        def result = k.select('?dataType').query("<$URI> rdfs:subClassOf ?dataType. ?dataType rdfs:subClassOf :DataType. FILTER (?dataType != :DataType && ?dataType != <$URI>)")
        //println k.getPrefixesMap()

        result.metaClass.shortURI = {
            def uris = delegate.collect {
                if(it.dataType instanceof String)
                    it.dataType = it.dataType.replace(k.getBasePrefix(), '')
            }
            if(uris.size()==1)
                return uris[0]
            else
                return uris
        }
        return result
    }

    def getMap(String args){
        def sparql = "<$URI> :appliedTo ?evalobj. " +
                     "?evalobj ui:hasMicroregion ?microregion. " +
                     "?microregion <http://dbpedia.org/property/pt/mapa> ?map."
        def result = k.select('distinct '+args).query(sparql)

        result.metaClass.map = { (delegate.size()==1)? delegate[0]['map'] :delegate.collect { it['map'] } }
        return result
    }

    def getLabelDescription(String property) {
        k.query("?id $property <$URI>; rdfs:label ?label. optional {?id dc:description ?description}. FILTER ( ?id != <$URI> )")
    }

    def getLabelDataValue(){
        k.query("?id a <$URI>; rdfs:label ?label; ui:dataValue ?dataValue")
    }

    def getLabelAppliedTo(){
        k.query("?id :appliedTo <$URI>. ?id rdfs:label ?label")
    }

    def getAnalyses(){
        k.query("?id a ui:Analysis. ?id :appliedTo <$URI>")
    }

    def getIndividuals(){
        /*
        select * where {
            ?individual rdf:type owl:NamedIndividual.
                    ?individual ?outProperty ?outObject.

        }
        */
    }

    def getIndividualsIdLabel(){
        k.select('distinct ?id ?label')
         .query("?id a <$URI>; rdfs:label ?label.",
                "ORDER BY ?label")
    }

    def getCollectionIndividuals(){
        def query = ''
        def result

        query += "<$URI> rdfs:subClassOf ?y. " +
                "?y owl:onProperty ui:value. "+
                "?y owl:onClass*/owl:someValuesFrom ?category. "+
                "optional {"+
                "   ?category owl:oneOf ?collection. "+
                "   ?collection rdf:rest*/rdf:first ?id. "+
                "}"+
                "optional {"+
                "   ?id a ?category. "+
                "}"+
                "?id rdfs:label ?label. "+
                "?id ui:dataValue ?dataValue. "+
                "FILTER(?category != <http://purl.org/biodiv/semanticUI#Categorical>)"

        k.select('distinct ?category ?id ?label ?dataValue').query(query, "ORDER BY ?label")
    }

    def getWeightIndividuals(){
        def query = ''
        def result

        query += "<$URI> rdfs:subClassOf ?y. " +
                "?y owl:onProperty ui:hasWeight. "+
                "?y owl:onClass*/owl:someValuesFrom ?weights. "+
                "?weights owl:oneOf ?collection. "+
                "?collection rdf:rest*/rdf:first ?id. "+
                "?id rdfs:label ?label. "+
                "?id ui:asNumber ?dataValue. "

        k.select('distinct ?id ?label ?dataValue').query(query, "ORDER BY ?label")
    }

    def getCollectionIndividualsTypes(){
        def query = ''
        def result

        query += "<$URI> rdfs:subClassOf ?y. " +
                "?y owl:onProperty ui:value. "+
                "?y owl:onClass*/owl:someValuesFrom ?category. "+
                "optional {"+
                "   ?category owl:oneOf ?collection. "+
                "   ?collection rdf:rest*/rdf:first ?element. "+
                "   ?element a ?types. "+
                "}"+
                "optional{"+
                "   ?category rdfs:subClassOf ?types. "+
                "}"+
                "FILTER(?category != <http://purl.org/biodiv/semanticUI#Categorical>)"

        result = k.select('distinct ?types').query(query, "ORDER BY ?elementLabel")
        result.collect{ it['types'] }
    }

    def getIndividualsIdValueLabel(){
        k.select('distinct ?id ?value ?label')
            .query("?id a <$URI>; rdfs:label ?label; ui:dataValue ?value.",
            "ORDER BY ?label")
    }

    def getEvaluationObjectsIdLabel(){
        k.select('distinct ?id ?label').query("<$URI> :hasEvaluationObject ?id. ?id rdfs:label ?label.", "ORDER BY ?label")
    }

    def getAnalysesIdLabel(){
        k.select('distinct ?id ?label').query("?id :appliedTo <$URI>; rdfs:label ?label.", "ORDER BY ?label")
    }

    def getOptions() {
        k.query("?id rdf:type <$URI>. ?id rdfs:label ?label. ?id ui:dataValue ?value.")
    }

    def getSuperClass(Map params = [:]){
        getAttr('?superClass', ['FILTER': "?superClass != <$URI>"])
        //k.select('?superClass').query("<$URI> rdfs:subClassOf ?superClass. FILTER(?superClass != <$URI>)")
    }

    def getSubClass(String args='', Map params = [:]){
        def argsList = args.tokenize(' ?')
        def query = ''
        def result

        argsList.each{
            if(argsList.contains('label'))
                query += "?subClass rdfs:label ?label."
        }

        query += "?subClass rdfs:subClassOf <$URI>." +
                "?subSubClass rdfs:subClassOf ?subClass."+
                "FILTER(?subClass != <$URI> && ?subClass != ?subSubClass)"

        def arg = ''

        if(argsList.size()>0){
            arg = "?" + ['label', 'subClass'].join(" ?");
            //println arg;
        }

        result = k.select('distinct ?subClass '+arg).query(query)

        //println query

        def prefixes = k.getPrefixesMap()

        /*result.metaClass.shortURI = {
            def uris = delegate.collect {
                if(it.subClass instanceof String){
                    prefixes.each{ key, value ->
                        it.subClass = it.subClass.replace(value, key+'-')
                    }
                }
                it.subClass
            }
            if(uris.size()==1)
                return uris[0]
            else
                return uris
        }

        if(result.size()==1)
            return result[0].subClass
        else
            return result*/

        result
    }

    def getGrandchildren(String args){
        def argsList = args.tokenize(' ?')
        def query = ''
        def result

        if (argsList.contains('subClass') && argsList.contains('id')) {
            query += "?subClass rdfs:subClassOf <$URI>"

            if (argsList.contains('subClassLabel'))
                query += "; rdfs:label ?subClassLabel";

            query += "."

            query +="?id rdfs:subClassOf ?subClass"

            if (argsList.contains('label'))
                query +="; rdfs:label ?label";

            if (argsList.contains('relevance'))
                query +=". optional {?id :relevance ?relevance}";

            query += "."

            if(argsList.contains('category')){
                query += ''' ?id rdfs:subClassOf ?y.
                             ?y owl:onProperty ui:value.
                             ?y owl:onClass*/owl:someValuesFrom ?category. '''
            }
            if(argsList.contains('weight')){
                query += ''' optional {
                                ?id rdfs:subClassOf ?z.
                                ?z owl:onProperty ui:hasWeight.
                                ?z owl:onClass*/owl:someValuesFrom ?weight.
                             } '''
            }

            query += "FILTER(?subClass != <$URI> && ?subClass != ?id && ?category != ui:Categorical)"
        }

        result = k.select('distinct '+args).query(query, "ORDER BY ?label")

        result.metaClass.category = {
            delegate.collect {it['category']}
        }
        result.metaClass.categoryList = {
            propertyToList(delegate, 'category')
        }
        result.metaClass.subClass = {
            delegate.collect {it['subClass']}
        }
        result.metaClass.subClassMap = { attrs ->
            propertyToMap(delegate, 'subClass', attrs)
        }
        result
    }

    def getChildren(String args){
        def argsList = args.split(' ')
        def query = ''
        def result

        //'?id ?label ?category ?valueType'

        if (argsList.contains('?id')){
            query +="?id rdfs:subClassOf <$URI>"

            if (argsList.contains('?label'))
                query +="; rdfs:label ?label";

            if (argsList.contains('?weight'))
                query +="; :weight ?weight";

            query += "."
        }
        if (argsList.contains('?id') && argsList.contains('?category') && argsList.contains('?valueType')) {
            query += ''' ?id rdfs:subClassOf ?y.
                    ?y owl:onClass ?category.
                    ?category rdfs:subClassOf ?valueType. ''' +
                    "FILTER( ?valueType = ui:Categorical || ?valueType = ui:Real)"
        }

        result = k.select('distinct '+args).query(query, "ORDER BY ?label")

        result.each{it.subClass = URI; it.subClassLabel = getLabel()}

        result.metaClass.category = {
            delegate.collect {it['category']}
        }
        result.metaClass.categoryList = {
            propertyToList(delegate, 'category')
        }
        result.metaClass.subClass = {
            delegate.collect {it['subClass']}
        }
        result.metaClass.subClassMap = { attrs ->
            propertyToMap(delegate, 'subClass', attrs)
        }
        result
    }

    def getEvaluationObject(String args){
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

        def result = k.select('distinct '+args).query(query)

        /*def id
        argsList.each {
            id = it.substring(1)
            println id
            result.metaClass[id] = {
                println 'Array: ' + id
                return (delegate.size() == 1) ? delegate[0][id] : delegate.collect { it[id] }
            }
        }*/

        if (argsList.contains('?label'))
            result.metaClass['label'] = { (delegate.size()==1)? delegate[0]['label'] :delegate.collect { it['label'] } }
        if (argsList.contains('?productionUnit'))
            result.metaClass['productionUnit'] = { (delegate.size()==1)? delegate[0]['productionUnit'] :delegate.collect { it['productionUnit'] } }
        if (argsList.contains('?microregion'))
            result.metaClass['microregion'] = { (delegate.size()==1)? delegate[0]['microregion'] :delegate.collect { it['microregion'] } }
        if (argsList.contains('?efficiency'))
            result.metaClass['efficiency'] = { (delegate.size()==1)? delegate[0]['efficiency'] :delegate.collect { it['efficiency'] } }

        return result
    }

    def getGrandChildrenIndividuals(String analysis, String args){
        def argsList = args.split(' ')
        def result
        def query = "<"+k.toURI(analysis)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
                "?subClass rdfs:subClassOf <$URI>."+
                "?id rdfs:subClassOf ?subClass." +
                "?id rdfs:label ?label." +
                "optional {?id :relevance ?relevance.}" +

                "?ind a ?id." +

                "?ind ui:value ?valueType."+
                "?valueType ui:dataValue ?value."+
                "?valueType rdfs:label ?valueTypeLabel."+

                "optional {"+
                    "?ind ui:hasWeight ?weightType." +
                    "?weightType ui:hasWeight ?weight."+
                    "?weightType rdfs:label ?weightTypeLabel."+
                "}"+
                "optional {"+
                    "?ind :hasJustification ?justification."+
                "}"+
                "FILTER( ?subClass != ?id && ?subClass != <$URI> )"

        result = k.select('distinct '+args).query(query, "ORDER BY ?label")

        /*
        println URI
        println k.toURI(analysis)
        println query
        println result
        */

        result.each{
            if(it['relevance'])
                it['totalValue'] = it.value * it.relevance;
        }

        result.metaClass.ind = { (delegate.size()==1)? delegate[0]['ind'] :delegate.collect { it['ind'] } }
        result.metaClass.label = { (delegate.size()==1)? delegate[0]['label'] :delegate.collect { it['label'] } }
        result.metaClass.relevance = { (delegate.size()==1)? delegate[0]['relevance'] :delegate.collect { it['relevance'] } }
        result.metaClass.value = { (delegate.size()==1)? delegate[0]['value'] :delegate.collect { it['value'] } }
        result.metaClass.valueType = { (delegate.size()==1)? delegate[0]['valueType'] :delegate.collect { it['valueType'] } }
        result.metaClass.valueTypeLabel = { (delegate.size()==1)? delegate[0]['valueTypeLabel'] :delegate.collect { it['valueTypeLabel'] } }
        result.metaClass.weight = { (delegate.size()==1)? delegate[0]['weight'] :delegate.collect { it['weight'] } }
        result.metaClass.weightType = { (delegate.size()==1)? delegate[0]['weightType'] :delegate.collect { it['weightType'] } }
        result.metaClass.weightTypeLabel = { (delegate.size()==1)? delegate[0]['weightTypeLabel'] :delegate.collect { it['weightTypeLabel'] } }
        result.metaClass.justification = { (delegate.size()==1)? delegate[0]['justification'] :delegate.collect { it['justification'] } }

        result.metaClass.equation = { eq ->
            eq.resolveStrategy = Closure.DELEGATE_FIRST
            delegate.collect({ eq.delegate = it; eq()})
        }
        return result
    }

    def getChildrenIndividuals(String analysis, String args) {
        def argsList = args.split(' ')
        def result
        def query = "<"+k.toURI(analysis)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
                    "?id rdfs:subClassOf <$URI>." +
                    "?id rdfs:label ?label." +
                    "optional {?id :relevance ?relevance.}" +

                    "?ind a ?id." +

                    "?ind ui:value ?valueType." +
                    "?valueType ui:dataValue ?value." +
                    "?valueType rdfs:label ?valueTypeLabel."+
                    "optional {"+
                        "?ind :hasJustification ?justification."+
                    "}"+
                    "optional {"+
                        "?ind ui:hasWeight ?weightType." +
                        "?weightType ui:dataValue ?weight."+
                        "?weightType rdfs:label ?weightTypeLabel."+
                    "}"+
                    "FILTER( ?id != <$URI> )"

        result = k.select('distinct '+args).query(query, "ORDER BY ?label")

        /*def id
        argsList.each{
            id = it.substring(1)
            println id
            result.metaClass[id] = {
                println 'Array: '+id
                return (delegate.size()==1)? delegate[0][id] : delegate.collect { it[id] }
            }
        }*/

        result.each{
            if(it['weight'])
                it['totalValue'] = it.value * it.weight;
        }

        result.metaClass.ind = { (delegate.size()==1)? delegate[0]['ind'] :delegate.collect { it['ind'] } }
        result.metaClass.label = { (delegate.size()==1)? delegate[0]['label'] :delegate.collect { it['label'] } }
        result.metaClass.value = { (delegate.size()==1)? delegate[0]['value'] :delegate.collect { it['value'] } }
        result.metaClass.valueType = { (delegate.size()==1)? delegate[0]['valueType'] :delegate.collect { it['valueType'] } }
        result.metaClass.valueTypeLabel = { (delegate.size()==1)? delegate[0]['valueTypeLabel'] :delegate.collect { it['valueTypeLabel'] } }
        result.metaClass.weight = { (delegate.size()==1)? delegate[0]['weight'] :delegate.collect { it['weight'] } }
        result.metaClass.weightType = { (delegate.size()==1)? delegate[0]['weightType'] :delegate.collect { it['weightType'] } }
        result.metaClass.weightTypeLabel = { (delegate.size()==1)? delegate[0]['weightTypeLabel'] :delegate.collect { it['weightTypeLabel'] } }
        result.metaClass.justification = { (delegate.size()==1)? delegate[0]['justification'] :delegate.collect { it['justification'] } }
        result.metaClass.equation = { eq ->
            eq.resolveStrategy = Closure.DELEGATE_FIRST
            delegate.collect({ eq.delegate = it; eq()})
        }
        return result
    }

    def getChildrenExtraIndividuals(String analysis, String args) {
        def argsList = args.split(' ')
        def result
        def query = "<"+k.toURI(analysis)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
                "?ind ui:hasName ?name."+
                "?ind :hasJustification ?justification."+
                "?ind ui:value ?valueType." +
                "?valueType ui:dataValue ?value." +
                "?valueType rdfs:label ?valueTypeLabel."+
                "?ind a <$URI>." +
                "<$URI> <http://semantic.icmc.usp.br/sustenagro#relevance> ?relevance."

        result = k.select('distinct '+args).query(query, "ORDER BY ?label")

        /*def id
        argsList.each{
            id = it.substring(1)
            println id
            result.metaClass[id] = {
                println 'Array: '+id
                return (delegate.size()==1)? delegate[0][id] : delegate.collect { it[id] }
            }
        }*/
        result.metaClass.ind = { (delegate.size()==1)? delegate[0]['ind'] :delegate.collect { it['ind'] } }
        result.metaClass.name = { (delegate.size()==1)? delegate[0]['name'] :delegate.collect { it['name'] } }
        result.metaClass.justification = { (delegate.size()==1)? delegate[0]['justification'] :delegate.collect { it['justification'] } }
        result.metaClass.value = { (delegate.size()==1)? delegate[0]['value'] :delegate.collect { it['value'] } }
        result.metaClass.valueType = { (delegate.size()==1)? delegate[0]['valueType'] :delegate.collect { it['valueType'] } }
        result.metaClass.valueTypeLabel = { (delegate.size()==1)? delegate[0]['valueTypeLabel'] :delegate.collect { it['valueTypeLabel'] } }
        result.metaClass.equation = { eq ->
            eq.resolveStrategy = Closure.DELEGATE_FIRST
            delegate.collect({ eq.delegate = it; eq()})
        }
        return result
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

    def getFeaturesURI(){
        def result = k.select('?featuresURI').query("<$URI> :features ?featuresURI.")
        def prefixes = k.getPrefixesMap()

        result.metaClass.shortURI = {
            def uris = delegate.collect {
                if(it.featuresURI instanceof String){
                    prefixes.each{ key, value ->
                        it.featuresURI = it.featuresURI.replace(value, key+'-')
                    }
                }
                it.featuresURI
            }
            if(uris.size()==1)
                return uris[0]
            else
                return uris
        }
        if(result.size()==1)
            return result[0].featuresURI
        else
            return result
    }

    def getUser(){
        def select = ''
        def query = ''
        def result

        query =    "?user a <http://semantic.icmc.usp.br/sustenagro#User>. "+
                "?user <http://semantic.icmc.usp.br/sustenagro#username> ?username. "+
                "?user <http://semantic.icmc.usp.br/sustenagro#password> ?password. "+
                "FILTER (?username = 'root' && ?password = SHA256('root'))"

        result = k.query(query)

        (result.size()==1)? result[0] : result
    }

    def getUsers(){
        def select = ''
        def query = ''
        def result

        query = "?user a ui:User. "+
                "?user ui:hasUsername ?username. "+
                "?user ui:hasPassword ?password. "

        result = k.query(query)
        //(result.size()==1)? result[0] : result
    }

    def getRoles(){
        def select = ''
        def query = ''
        def result

        query = "<$URI> ui:hasRole ?role. "

        result = k.query(query)
        //(result.size()==1)? result[0] : result
    }

    def getRestriction(String property){
        def select = ''
        def query = ''
        def result
        def propertyURI = k.toURI(property);

        query = "<$URI> rdfs:subClassOf ?o. "+
                "?o owl:onProperty ?property. "+
                "optional {?o owl:onClass ?class. } "+
                "optional {?o owl:cardinality ?cardinality. }"+
                "optional {?o owl:qualifiedCardinality ?cardinality. }"+
                "FILTER (?property = <$propertyURI>)"

        result = k.query(query)
    }

    def getMicroregions(){
        def select = ''
        def query = ''
        def result

        query = "?id rdf:type <http://dbpedia.org/page/Microregion_(Brazil)>. "+
                "?id <http://dbpedia.org/ontology/state> <$URI>. " +
                "?id rdfs:label ?label. "

        result = k.query(query)
    }

    def findSubject(String args){
        k.query("?subject <$URI> '$args'")
    }

    def isFunctional(){
        def query = "<$URI> a owl:FunctionalProperty"
        return (k.query(query).size() > 0)
    }

    def outgoingLinks(){
        k.query("<$URI> ?p ?o", '', '*')
    }

    def incomingLinks(){
        k.query("?s ?p <$URI>", '', '*')
    }

    def selectSubject(String word){
        k.select('distinct ?s').query("?s ?p ?o. FILTER regex(str(?s), ':$word', 'i')")
    }

    def findByLabel(String word){
        k.select('distinct ?label').query("?s rdfs:label ?label. FILTER regex(str(?label), '$word', 'i')")
    }

    def findURI(String label){
        k.select('distinct ?uri').query("?uri rdfs:label '$label'@${k.lang}.")
    }

    def existOntology(String uri){
        def existOnt = false
        def result = k.query("?o rdf:type owl:Ontology")

        println "******** Result ********"
        println result

        result.each{
            if(it.o == uri)
                existOnt = true
        }

        existOnt
    }

    def exist(){
        k.query("<$URI> rdf:type ?type").size() > 0 ? true : false
    }

    def getAnalysisLabel(String label){
        k.query("?id :appliedTo <$URI>. ?id rdfs:label ?label. filter contains(?label,'$label')")
    }

    def insertEvaluationObject(String id, Object type, Map properties = [:]){
        def evalObjId = k.toURI("inds:"+id)
        def name = k.toURI('ui:hasName')

        String sparql = "<" + evalObjId + "> "

        if(type.class.isArray()){
            type.each{
                sparql += "rdf:type <" + it + ">;"
            }
        }
        else if(type.getClass() == String){
            sparql += "rdf:type <" + type + ">;"
        }

        sparql += "rdfs:label '" + properties[name].value + "'@pt;"+
                  "rdfs:label '" + properties[name].value + "'@en. "

        sparql += createTriples(evalObjId, properties)


        /*sparql.split(';').each{
            println it
        }*/

        k.insert(sparql)
    }

    def insertAnalysis(String id, Map properties = [:]){
        def analysisId = k.toURI("inds:"+id)
        String sparql = "<" + analysisId + "> rdf:type ui:Analysis. "

        //println properties

        sparql += createTriples(analysisId, properties)

        k.insert(sparql)
    }

    def insertFeatures(String id, Map individuals = [:]){
        def analysisId = k.toURI("inds:"+id)
        String sparql = ''
        String featureId = ''
        String indsBase = k.toURI('inds:')
        String domainBase = k.toURI(':')

        individuals.each{
            featureId = (it.key+'-'+id).replace(domainBase, indsBase)
            sparql += "<" + featureId +"> rdf:type <"+ it.key +">. "

            if(it.value.justification)
                sparql += "<" + featureId + "> :hasJustification '"+ it.value.justification +"'. "

            if(k.isURI(it.value.value)){
                sparql += "<" + featureId +"> ui:value <"+ it.value.value +">. "
            }
            else{
                sparql += "<" + featureId +"> ui:value _:"+ it.value.value.id +". "
                sparql += "_:"+it.value.value.id+" ui:dataValue '"+ it.value.value.dataValue +"'^^xsd:double. "
                sparql += "_:"+it.value.value.id+" rdfs:label '"+ it.value.value.label +"'. "
            }

            if(it.value.weight){
                if(k.isURI(it.value.weight)){
                    sparql += "<" + featureId +"> ui:hasWeight <"+ it.value.weight +">. "
                }
                else {
                    sparql += "<" + featureId + "> ui:hasWeight _:" + it.value.weight.id + ". "
                    sparql += "_:" + it.value.weight.id + " ui:dataValue '" + it.value.weight.dataValue + "'^^xsd:double. "
                    sparql += "_:" + it.value.weight.id + " rdfs:label '" + it.value.weight.label + "'. "
                }
            }
            sparql += "<" + featureId +"> dc:isPartOf <"+ analysisId +">. "
            sparql += "<" + analysisId + "> dc:hasPart <" + featureId +">. "

        }
        //println sparql
        k.insert(sparql)
    }

    def insertExtraFeatures(String id, Map individuals = [:]){
        def analysisId = k.toURI("inds:"+id)
        String sparql = ''
        String featureId = ''

        individuals.each{ individual ->
            individual.value.each{ list ->
                list.each{ item ->
                    featureId = individual.key+'-'+item.key+'-'+id
                    sparql += "<" + featureId +"> rdf:type <" + individual.key + ">. "
                    sparql += "<" + featureId +"> dc:isPartOf <" + analysisId + ">. "
                    sparql += "<" + analysisId + "> dc:hasPart <" + featureId +">. "
                    sparql += createTriples(featureId, item.value)
                }
            }
        }

        k.insert(sparql)
    }

    def insertUser(String id, Map properties = [:]){
        def userId = k.toURI('inds:'+id)
        String sparql = "<" + userId + "> rdf:type ui:User. "

        sparql += createTriples(userId, properties)

        //println sparql

        k.insert(sparql)
    }

    def insertTriples(String id, Map properties = [:]){
        k.insert(createTriples(id, properties))
    }

    def createTriples(String id, Map properties = [:]){
        String sparql = "<" + k.toURI(id) + "> "

        properties.each { key, property ->
            switch (property.dataType) {
                case k.toURI('xsd:string'):
                    if(property.value.getClass() == String[] || property.value.getClass() == Object[]){
                        property.value.each{ sparql += "<${k.toURI(key)}> \"" + it + "\"^^xsd:string; " }
                    }
                    else{
                        sparql += "<${k.toURI(key)}> \"" + property.value + "\"^^xsd:string; "
                    }
                    break
                case k.toURI('xsd:double'):
                    if(property.value.getClass() == String[] || property.value.getClass() == Object[]){
                        property.value.each{ sparql += "<${k.toURI(key)}> \"" + it + "\"^^xsd:double; " }
                    }
                    else{
                        sparql += "<${k.toURI(key)}> \"" + property.value + "\"^^xsd:double; "
                    }
                    break
                case k.toURI('xsd:float'):
                    if(property.value.getClass() == String[] || property.value.getClass() == Object[]){
                        property.value.each{ sparql += "<${k.toURI(key)}> \"" + it + "\"^^xsd:float; " }
                    }
                    else{
                        sparql += "<${k.toURI(key)}> \"" + property.value + "\"^^xsd:float; "
                    }
                    break
                case k.toURI('owl:real'):
                    if(property.value.getClass() == String[] || property.value.getClass() == Object[]){
                        property.value.each{ sparql += "<${k.toURI(key)}> \"" + it + "\"^^owl:real; " }
                    }
                    else{
                        sparql += "<${k.toURI(key)}> \"" + property.value + "\"^^owl:real; "
                    }
                    break
                case k.toURI('xsd:date'):
                    if(property.value.getClass() == String[] || property.value.getClass() == Object[]){
                        property.value.each{ sparql += "<${k.toURI(key)}> \"" + it + "\"^^xsd:date; " }
                    }
                    else{
                        sparql += "<${k.toURI(key)}> \"" + property.value + "\"^^xsd:date; "
                    }
                    break
                case k.toURI('xsd:time'):
                    if(property.value.getClass() == String[] || property.value.getClass() == Object[]){
                        property.value.each{ sparql += "<${k.toURI(key)}> \"" + it + "\"^^xsd:time; " }
                    }
                    else{
                        sparql += "<${k.toURI(key)}> \"" + property.value + "\"^^xsd:time; "
                    }
                    break
                case k.toURI('xsd:dateTime'):
                    if(property.value.getClass() == String[] || property.value.getClass() == Object[]){
                        property.value.each{ sparql += "<${k.toURI(key)}> \"" + it + "\"^^xsd:dateTime; " }
                    }
                    else{
                        sparql += "<${k.toURI(key)}> \"" + property.value + "\"^^xsd:dateTime; "
                    }
                    break
                case k.toURI('xsd:duration'):
                    if(property.value.getClass() == String[] || property.value.getClass() == Object[]){
                        property.value.each{ sparql += "<${k.toURI(key)}> \"" + it + "\"^^xsd:duration; " }
                    }
                    else{
                        sparql += "<${k.toURI(key)}> \"" + property.value + "\"^^xsd:duration; "
                    }
                    break
                case k.toURI('rdfs:Literal'):
                    if(property.value.getClass() == String[] || property.value.getClass() == Object[]){
                        property.value.each{ sparql += "<${k.toURI(key)}> '" + it + "'@" + k.lang + "; " }
                    }
                    else{
                        sparql += "<${k.toURI(key)}> '" + property.value + "'@" + k.lang + "; "
                    }
                    break
                default:
                    if(property.value.getClass() == String[] || property.value.getClass() == Object[]){
                        property.value.each{ if(k.isURI(it)) {  sparql += "<${k.toURI(key)}> <" + it + ">; " } }
                    }
                    else if(property.value.getClass() == String && k.isURI(property.value)){
                        sparql += "<${k.toURI(key)}> <" + property.value + ">; "
                    }
                    else {
                        println "Default: " + key + " : " + property.value
                        sparql += "<${k.toURI(key)}> '" + property.value + "'@" + k.lang + "; "
                    }
            }
        }
        if(sparql.length()>2 && sparql.contains('; '))
            sparql = sparql[0..-3]+"."

        return sparql
    }

    def propertyToList = {ArrayList source, String property ->
        def map = [:]
        source.each{
            map[it[property]] = []
        }
        return map
    }

    def propertyToMap = {ArrayList source, String property , String attrs ->
        def map = [:]
        def args = attrs.tokenize(' ?')
        source.each{
            map[it[property]] = [:]
            args.each{ attr ->
                map[it[property]][attr] = it[attr]
            }
        }

        if(args.size() == 1){
            map = map.sort { it.value[args[0]].toLowerCase() }
        }

        return map
    }
}

// slp.query("?id rdfs:label ?label. FILTER (STR(?label)='$cls')", '', '')
