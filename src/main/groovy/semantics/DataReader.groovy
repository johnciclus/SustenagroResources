package semantics

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

        if(classList.contains(['superClass': 'http://bio.icmc.usp.br/sustenagro#ProductionEfficiencyFeature'])){
            try{
                res = k[uri].getIndividualsValue(id, '?ind ?label ?valueType ?valueTypeLabel ?value')
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(['superClass': 'http://bio.icmc.usp.br/sustenagro#Indicator'])){
            try{
                res = k[uri].getIndividualsValueWeight(id, '?ind ?label ?valueType ?valueTypeLabel ?value ?weight')
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(['superClass': 'http://bio.icmc.usp.br/sustenagro#TechnologicalEfficiencyFeature'])){
            try{
                res = k[uri].getIndividualsFeatureValueWeight(id, '?ind ?label ?valueType ?valueTypeLabel ?value ?weightType ?weightTypeLabel ?weight')
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(['superClass': 'http://dbpedia.org/ontology/MicroRegion'])){
            try {
                println "ID"
                println id
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
            println 'CurrentProductionUnit'
            //println id
            //println k[id].getLabel()
            //def res = k[id].getProductionEvaluationObject('?label ?productionUnit ?microregion ')
            return k[id].getLabel()
        }
        else{
            findNode(name)
        }
    }
}