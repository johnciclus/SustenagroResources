package rdfUtils

/**
 * Created by john on 10/17/15.
 */
class DataReader {

    RDFSlurper slp
    String uri

    DataReader(slp, uri){
        this.slp = slp
        this.uri = uri
    }

//    def findNode(String uri, String name) {
//        // Try to find as class label
//        def res = slp
//                .select('?v')
//                .query("<$uri> dc:hasPart ?x." +
//                "?x a ?cls." +
//                "?cls rdfs:label '$name'@${slp.lang}." +
//                "?x :value ?ind." +
//                "?ind :dataValue ?v.")
//        if (res.empty) {
//            // Try to find as class uri
//            def cls = slp.toURI(name)
//            try {
//                res = slp
//                        .select('?v')
//                        .query("<$uri> dc:hasPart ?x." +
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

    def findNode(String name){
        def type = name
        def cls = slp.toURI(name)
        def res
        if(cls){
            res = slp.select('?c')
                    .query("<$cls> rdfs:subClassOf ?c.")
            if(res[0])
                type = res[0].c
        }
        println 'Find Node: ' + type
        switch(type) {
            case 'http://bio.icmc.usp.br/sustenagro#Indicator':
                try{
                    cls = slp.toURI(name)
                    res = slp.select('?v')
                            .query("<$uri> dc:hasPart ?x." +
                            "?x a ?i." +
                            "?i rdfs:subClassOf ?a." +
                            "?a rdfs:subClassOf <$cls>." +
                            "?x :value ?ind." +
                            "?ind :dataValue ?v."
                    )
                }
                catch (e){
                    res = []
                }
                break
            case 'Microregion':
                try {
                    res = slp.select('?map')
                            .query("<$uri> :appliedTo ?u. " +
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

                break

            default:
                res = slp.select('?v')
                        .query("<$uri> dc:hasPart ?x." +
                        "?x a ?cls." +
                        "?cls rdfs:label '$name'@${slp.lang}." +
                        "?x :value ?ind." +
                        "?ind :dataValue ?v.")
                if (res.empty) {
                    cls = slp.toURI(name)
                    try {
                        res = slp
                                .select('?v')
                                .query("<$uri> dc:hasPart ?x." +
                                "?x a <$cls>." +
                                "?x :value ?ind." +
                                "?ind :dataValue ?v.")
                    }
                    catch (e) {
                        res = []
                    }
                }
                break
        }
        if (res.empty) //throw new RuntimeException("Unknown value: $name")
            return 0

        if (res.size() == 1)
            return res[0].v

        res.collect { it.v }
    }

    def propertyMissing(String name) {
        getAt(name)
    }

    def getAt(String name) {
        findNode(name)
    }
}