package rdfUtils

/**
 * Created by john on 10/17/15.
 */
class DataReader {

    RDFSlurper slp
    String id

    DataReader(slp, id){
        this.slp = slp
        this.id = id
    }

    def findNode(String name){

        def classList = name
        def uri = slp.toURI(name)
        def res

        if(uri){
            classList = getSuperClass(uri)
        }

        if(classList.contains(['subClass': 'http://bio.icmc.usp.br/sustenagro#Indicator']) || classList.contains(['subClass': 'http://bio.icmc.usp.br/sustenagro#ProductionEfficiencyFeature'])){
            try{
                res = slp.select('distinct ?ind ?valueType ?value ?weight')
                    .query("<$id> <http://purl.org/dc/terms/hasPart> ?ind." +
                           "?ind a ?id." +
                           "?id rdfs:subClassOf ?atribute." +
                           "?atribute rdfs:subClassOf <$uri>."+
                           "?ind <http://bio.icmc.usp.br/sustenagro#value> ?valueType."+
                           "?valueType <http://bio.icmc.usp.br/sustenagro#dataValue> ?value."+
                           "?ind :hasWeight ?weight"
                           )
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(['subClass': 'http://bio.icmc.usp.br/sustenagro#TechnologicalEfficiencyFeature'])){
            try{
                res = slp.select('distinct ?ind ?valueType ?value')
                        .query("<$id> <http://purl.org/dc/terms/hasPart> ?ind." +
                        "?ind a ?id." +
                        "?id rdfs:subClassOf ?atribute." +
                        "?atribute rdfs:subClassOf <$uri>."+
                        "?ind <http://bio.icmc.usp.br/sustenagro#value> ?valueType."+
                        "?valueType <http://bio.icmc.usp.br/sustenagro#dataValue> ?value."
                )
                println id
                println uri
                println res.size()
                println res
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(['subClass': 'http://dbpedia.org/ontology/MicroRegion'])){
            try {
                res = slp.select('?map')
                        .query("<$id> :appliedTo ?u. " +
                        "?u dbp:Microregion ?m. " +
                        "?m <http://dbpedia.org/property/pt/mapa> ?map."
                )
            }
            catch (e) {
                res = []
            }

            if (!res.empty && res.size() == 1)
                return res[0].map
            else
                throw new RuntimeException("Unknown value: $name")
        }
        else{
            res = slp.select('?v')
                    .query("<$id> dc:hasPart ?x." +
                    "?x a ?cls." +
                    "?cls rdfs:label '$name'@${slp.lang}." +
                    "?x :value ?ind." +
                    "?ind :dataValue ?v.")
            if (res.empty) {
                uri = slp.toURI(name)
                try {
                    res = slp
                            .select('?v')
                            .query("<$id> dc:hasPart ?x." +
                            "?x a <$uri>." +
                            "?x :value ?ind." +
                            "?ind :dataValue ?v.")
                }
                catch (e) {
                    res = []
                }
            }
        }

        if (res.empty) //throw new RuntimeException("Unknown value: $name")
            return null

        if (res.size() == 1)
            return res[0].value

        res.collect { it.value }
    }

    //    def findNode(String id, String name) {
//        // Try to find as class label
//        def res = slp
//                .select('?v')
//                .query("<$id> dc:hasPart ?x." +
//                "?x a ?cls." +
//                "?cls rdfs:label '$name'@${slp.lang}." +
//                "?x :value ?ind." +
//                "?ind :dataValue ?v.")
//        if (res.empty) {
//            // Try to find as class id
//            def uri = slp.toURI(name)
//            try {
//                res = slp
//                        .select('?v')
//                        .query("<$id> dc:hasPart ?x." +
//                        "?x a <$cls>." +
//                        "?x :value ?ind." +
//                        "?ind :dataValue ?v.")
//            }
//            catch (e) {
//                res = []
//            }
//        }
//        res
//    }

    def propertyMissing(String name) {
        getAt(name)
    }

    def getAt(String name) {
        findNode(name)
    }

    def getSuperClass(String cls){
        slp.select('?subClass')
           .query("<"+slp.toURI(cls)+"> rdfs:subClassOf ?subClass.")
    }

}