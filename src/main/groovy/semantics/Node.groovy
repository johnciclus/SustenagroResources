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
        this.patterns.put('type', "<$URI> rdf:type ?type. ")
        this.patterns.put('superClass', "<$URI> rdfs:subClassOf ?superClass. ")

        //owl:ObjectProperty
        this.patterns.put('appliedTo', "<$URI> :appliedTo ?appliedTo. ")
        this.patterns.put('range', "<$URI> rdfs:range ?range. ")

        //owl:DatatypeProperty
        this.patterns.put('mapa', "<$URI> <http://dbpedia.org/property/pt/mapa> ?mapa. ")

        //owl:TransitiveProperty

        // owl:AnnotationProperty
        this.patterns.put('label', "<$URI> rdfs:label ?label. ")
        this.patterns.put('comment', "<$URI> rdfs:comment ?comment. ")
        this.patterns.put('weight', "<$URI> :weight ?weight. ")
        this.patterns.put('description', "<$URI> dcterm:description ?description. ")
        this.patterns.put('title', "<$URI> dcterm:title ?title. ")
        this.patterns.put('creator', "<$URI> dc:creator ?creator. ")

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
                     "?evalobj :hasMicroRegion ?microregion. " +
                     "?microregion <http://dbpedia.org/property/pt/mapa> ?map."
        def result = k.select('distinct '+args).query(sparql)
        result.metaClass.map = { (delegate.size()==1)? delegate[0]['map'] :delegate.collect { it['map'] } }
        return result
    }

    def getLabelDescription(String property) {
        k.query("?id $property <$URI>; rdfs:label ?label. optional {?id dc:description ?description}. FILTER ( ?id != <$URI> )")
    }

    def getLabelDataValue(){
        k.query("?id a <$URI>; rdfs:label ?label; ui:hasDataValue ?dataValue")
    }

    def getLabelAppliedTo(){
        k.query("?id :appliedTo <$URI>. ?id rdfs:label ?label")
    }

    def getAssessments(){
        k.query("?id a ui:Analysis. ?id :appliedTo <$URI>")
    }

    def getIndividualsIdLabel(){
        k.select('distinct ?id ?label')
         .query("?id a <$URI>; rdfs:label ?label.",
                "ORDER BY ?label")
    }

    def getOptions() {
        k.query("?id rdf:type <$URI>. ?id rdfs:label ?label. ?id ui:hasDataValue ?value.")
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
        def argsList = args.split(' ')
        def query = ''
        def result

        if (argsList.contains('?subClass')) {
            query += "?subClass rdfs:subClassOf <$URI>"

            if (argsList.contains('?subClassLabel'))
                query += "; rdfs:label ?subClassLabel";

            query += "."
        }
        if (argsList.contains('?id')){
            query +="?id rdfs:subClassOf ?subClass"

            if (argsList.contains('?label'))
                query +="; rdfs:label ?label";

            if (argsList.contains('?weight'))
                query +="; :weight ?weight";

            query += "."
        }
        if (argsList.contains('?id') && argsList.contains('?subClass') && argsList.contains('?category') && argsList.contains('?valueType')) {
            query += ''' ?id rdfs:subClassOf ?y.
                    ?y owl:onClass ?category.
                    ?category rdfs:subClassOf ?valueType. ''' +
                    "FILTER(?subClass != <$URI> && ?subClass != ?id && ?valueType = ui:Categorical)"
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

    def getGranchildrenIndividuals(String analysis, String args){
        def argsList = args.split(' ')

        def query = "?subClass rdfs:subClassOf <$URI>."+
                "?id rdfs:subClassOf ?subClass. "+
                "?in a ?id."+
                "?in dc:isPartOf <"+k.toURI(analysis)+">.";

        if (argsList.contains('?value'))
            query +="?in :value ?value.";

        if (argsList.contains('?weight'))
            query +="?in :hasWeight ?weight.";

        query += "FILTER( ?subClass != <$URI> && ?id != <$URI> && ?subClass != ?id)"

        //println 'Query'
        //println 'select distinct '+args+ ' where '+query

        k.select('distinct '+args).query(query, "ORDER BY ?in")
    }

    def getIndividualsValue(String id, String args){
        def argsList = args.split(' ')

        def sparql =    "<"+k.toURI(id)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
                        "?subClass rdfs:subClassOf <$URI>."+
                        "?id rdfs:subClassOf ?subClass." +
                        "?id rdfs:label ?label." +
                        "?ind a ?id." +
                        "?ind ui:value ?valueType."+
                        "?valueType ui:hasDataValue ?value."+
                        "?valueType rdfs:label ?valueTypeLabel."+
                        "FILTER( ?subClass != ?id && ?subClass != <$URI> )"

        //println sparql
        def result = k.select('distinct '+args).query(sparql, "ORDER BY ?label")

        result.metaClass.ind = { (delegate.size()==1)? delegate[0]['ind'] :delegate.collect { it['ind'] } }
        result.metaClass.label = { (delegate.size()==1)? delegate[0]['label'] :delegate.collect { it['label'] } }
        result.metaClass.value = { (delegate.size()==1)? delegate[0]['value'] :delegate.collect { it['value'] } }
        result.metaClass.valueType = { (delegate.size()==1)? delegate[0]['valueType'] :delegate.collect { it['valueType'] } }
        result.metaClass.valueTypeLabel = { (delegate.size()==1)? delegate[0]['valueTypeLabel'] :delegate.collect { it['valueTypeLabel'] } }
        return result
    }

    def getIndividualsValueWeight(String analysis, String args){
        def argsList = args.split(' ')
        def result = k.select('distinct '+args)
                .query("<"+k.toURI(analysis)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
                "?subClass rdfs:subClassOf <$URI>."+
                "?id rdfs:subClassOf ?subClass." +
                "?id rdfs:label ?label." +
                "?id :weight ?weight." +
                "?ind a ?id." +
                "?ind ui:value ?valueType."+
                "?valueType ui:hasDataValue ?value."+
                "?valueType rdfs:label ?valueTypeLabel."+
                "FILTER( ?subClass != ?id && ?subClass != <$URI> )", "ORDER BY ?label")

        result.metaClass.ind = { (delegate.size()==1)? delegate[0]['ind'] :delegate.collect { it['ind'] } }
        result.metaClass.label = { (delegate.size()==1)? delegate[0]['label'] :delegate.collect { it['label'] } }
        result.metaClass.value = { (delegate.size()==1)? delegate[0]['value'] :delegate.collect { it['value'] } }
        result.metaClass.valueType = { (delegate.size()==1)? delegate[0]['valueType'] :delegate.collect { it['valueType'] } }
        result.metaClass.valueTypeLabel = { (delegate.size()==1)? delegate[0]['valueTypeLabel'] :delegate.collect { it['valueTypeLabel'] } }
        result.metaClass.weight = { (delegate.size()==1)? delegate[0]['weight'] :delegate.collect { it['weight'] } }
        result.metaClass.equation = { eq ->
            eq.resolveStrategy = Closure.DELEGATE_FIRST
            delegate.collect({ eq.delegate = it; eq()})
        }
        return result
    }

    def getIndividualsFeatureValueWeight(String analysis, String args) {
        def argsList = args.split(' ')
        def result = k.select('distinct '+args)
                   .query("<"+k.toURI(analysis)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
                        "?id rdfs:subClassOf <$URI>." +
                        "?id rdfs:label ?label." +
                        "?ind a ?id." +
                        "?ind ui:value ?valueType." +
                        "?valueType ui:hasDataValue ?value." +
                        "?valueType rdfs:label ?valueTypeLabel."+
                        "?ind :hasWeight ?weightType." +
                        "?weightType rdfs:label ?weightTypeLabel."+
                        "?weightType ui:hasDataValue ?weight."+
                        "FILTER( ?id != <$URI> )", "ORDER BY ?label")

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
        result.metaClass.label = { (delegate.size()==1)? delegate[0]['label'] :delegate.collect { it['label'] } }
        result.metaClass.value = { (delegate.size()==1)? delegate[0]['value'] :delegate.collect { it['value'] } }
        result.metaClass.valueType = { (delegate.size()==1)? delegate[0]['valueType'] :delegate.collect { it['valueType'] } }
        result.metaClass.valueTypeLabel = { (delegate.size()==1)? delegate[0]['valueTypeLabel'] :delegate.collect { it['valueTypeLabel'] } }
        result.metaClass.weight = { (delegate.size()==1)? delegate[0]['weight'] :delegate.collect { it['weight'] } }
        result.metaClass.weightType = { (delegate.size()==1)? delegate[0]['weightType'] :delegate.collect { it['weightType'] } }
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

    def outgoingLinks(){
        k.query("<$URI> ?p ?o", '', '*')
    }

    def incomingLinks(){
        k.query("?s ?p <$URI>", '', '*')
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
                    "?user <http://semantic.icmc.usp.br/sustenagro#hasUserName> ?username. "+
                    "?user <http://semantic.icmc.usp.br/sustenagro#hasPassword> ?password. "+
                    "FILTER (?username = 'root' && ?password = SHA256('root'))"

        result = k.query(query)

        (result.size()==1)? result[0] : result
    }

    def getUsers(){
        def select = ''
        def query = ''
        def result

        query = "?user a <http://semantic.icmc.usp.br/sustenagro#User>. "+
                "?user <http://semantic.icmc.usp.br/sustenagro#hasUserName> ?username. "+
                "?user <http://semantic.icmc.usp.br/sustenagro#hasPassword> ?password. "

        result = k.query(query)

        (result.size()==1)? result[0] : result
    }

    def selectSubject(String word){
        k.select('distinct ?s').query("?s ?p ?o. FILTER regex(str(?s), ':$word', 'i')")
    }

    def selectLabel(String word){
        k.select('distinct ?label').query("?s rdfs:label ?label. FILTER regex(str(?label), '$word', 'i')")
    }

    def findURI(String label){
        k.select('distinct ?uri').query("?uri rdfs:label '$label'@${k.lang}.")
    }

    def insertEvaluationObject(String id, Object type, Map properties = [:]){
        def name = k.toURI(':hasName')

        String sparql = "<" + k.toURI(":"+id) + "> "

        if(type.class.isArray()){
            type.each{
                sparql += "rdf:type <" + it + ">;"
            }
        }
        else if(type.getClass() == String){
            sparql += "rdf:type <" + type + ">;"
        }

        sparql += "rdfs:label '" + properties[name].value + "'@pt;"+
                  "rdfs:label '" + properties[name].value + "'@en"

        properties.each{ key, property ->
            switch (property.dataType){
                case k.toURI('xsd:date'):
                    sparql += ";<${k.shortToURI(key)}> \"" + property.value + "\"^^xsd:date "
                    break
                case k.toURI('xsd:double'):
                    sparql += ";<${k.shortToURI(key)}> \"" + property.value + "\"^^xsd:double "
                    break
                case k.toURI('xsd:float'):
                    sparql += ";<${k.shortToURI(key)}> \"" + property.value + "\"^^xsd:float "
                    break
                case k.toURI('owl:real'):
                    sparql += ";<${k.shortToURI(key)}> \"" + property.value + "\"^^owl:real "
                    break
                case k.toURI('rdfs:Literal'):
                    sparql += ";<${k.shortToURI(key)}> '" + property.value + "'@"+ k.lang+" "
                    break
                default:
                    // Implement for arraylist
                    if(k.isURI(property.value))
                        sparql += ";<${k.shortToURI(key)}> <" + property.value + ">"
                    else{
                        println "Default: "+key+" : "+property.value
                        sparql += ";<${k.shortToURI(key)}> '" + property.value + "'@"+ k.lang+" "
                    }
                    break
            }

        }

        sparql += '.'

        //sparql.split(';').each{
        //    println it
        //}

        k.insert(sparql)
    }

    def insertAnalysis(String id, Map properties = [:], Map individuals = [:]){
        def analysisId = k.toURI(":"+id)
        String sparql = "<" + analysisId + "> "+
                        "rdf:type ui:Analysis;"

        properties.each { key, property ->
            switch (k[key].range) {
                case k.toURI('rdfs:Literal'):
                    sparql += ";<${k.toURI(key)}> '" + property + "'@" + k.lang + " "
                    break
                default:
                    // Implement for arraylist
                    if(k.isURI(property))
                        sparql += ";<${k.shortToURI(key)}> <" + property.value + ">"
                    else{
                        println "Default: "+key+" : "+property.value
                        sparql += ";<${k.shortToURI(key)}> '" + property.value + "'@"+ k.lang+" "
                    }
                    break
            }
        }
        k.insert(sparql)

        sparql = ''

        individuals.each{
            sparql +=   " <" +it.key+'-'+id +">"+
                        " rdf:type <"+ it.key +">"+
                        "; dc:isPartOf <"+ analysisId + ">" +
                        "; ui:value <"+  it.value +">."

            sparql +=   " <" +analysisId + ">" +
                        " dc:hasPart <" + it.key +'-'+ id +">."

        }

        k.insert(sparql)
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
