package rdfUtils

/**
 * Created by john on 10/17/15.
 */
class DataReader {

    Know k
    String id

    DataReader(k, id){
        this.k = k
        this.id = id
    }

    def findNode(String name){
        def uri = k.toURI(name)
        def res

        if(!uri){
            res = k[id].findURI(name)
            if( res.size()>0 )
                uri = res[0].uri
            else{
                throw new RuntimeException("Unknown value: $name")
            }
        }

        def classList = k[uri].getSuperClass()

        if(classList.contains(['subClass': 'http://bio.icmc.usp.br/sustenagro#Indicator']) || classList.contains(['subClass': 'http://bio.icmc.usp.br/sustenagro#ProductionEfficiencyFeature'])){
            try{
                res = k[uri].getIndividualsValue(id, '?ind ?label ?valueType ?valueTypeLabel ?value')
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(['subClass': 'http://bio.icmc.usp.br/sustenagro#TechnologicalEfficiencyFeature'])){
            try{
                res = k[uri].getIndividualsValueWeight(id, '?ind ?label ?valueType ?valueTypeLabel ?value ?weightType ?weightTypeLabel ?weight')
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(['subClass': 'http://dbpedia.org/ontology/MicroRegion'])){
            try {
                res = k[id].getMap('?map')
            }
            catch (e) {
                res = []
            }
        }

        return res
    }

    def propertyMissing(String name) {
        getAt(name)
    }

    def getAt(String name) {
        if(name == 'CurrentProductionUnit'){
            def res = k[id].getProductionUnity('?label ?productionUnit ?microregion ?efficiency')
            return res
        }
        else{
            findNode(name)
        }
    }

    /*
    def getSuperClass(String cls){
        k.select('?subClass')
           .query("<"+k.toURI(cls)+"> rdfs:subClassOf ?subClass.")
    }


    def getIndividualsValue(String evaluation, String cls){
        k.select('distinct ?ind ?valueType ?value')
           .query("<"+k.toURI(evaluation)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
                  "?ind a ?id." +
                  "?id rdfs:subClassOf ?subClass." +
                  "?subClass rdfs:subClassOf <"+k.toURI(cls)+">."+
                  "?ind <http://bio.icmc.usp.br/sustenagro#value> ?valueType."+
                  "?valueType <http://bio.icmc.usp.br/sustenagro#dataValue> ?value.", "ORDER BY ?ind")
    }

    def getIndividualsValueWeight(String evaluation, String cls) {
        k.select('distinct ?ind ?valueType ?value ?weightType ?weight')
            .query("<"+k.toURI(evaluation)+"> <http://purl.org/dc/terms/hasPart> ?ind." +
                  "?ind a ?id." +
                  "?id rdfs:subClassOf ?subClass." +
                  "?subClass rdfs:subClassOf <"+k.toURI(cls)+">." +
                  "?ind <http://bio.icmc.usp.br/sustenagro#value> ?valueType." +
                  "?valueType <http://bio.icmc.usp.br/sustenagro#dataValue> ?value." +
                  "?ind :hasWeight ?weightType." +
                  "?weightType <http://bio.icmc.usp.br/sustenagro#dataValue> ?weight.", "ORDER BY ?ind")
    }

    def getMap(String cls ){
        k.select('?map')
            .query("<$cls> :appliedTo ?u. " +
            "?u dbp:Microregion ?m. " +
            "?m <http://dbpedia.org/property/pt/mapa> ?map.")
    }
    */

}