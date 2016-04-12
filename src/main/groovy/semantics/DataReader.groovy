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

        /*
        println name
        println uri
        println classList
        */

        if(classList.contains(k.toURI(':TechnologicalEfficiencyFeature'))){
            try{
                res = k[uri].getChildrenIndividuals(id, '?ind ?label ?valueTypeLabel ?value ?weightTypeLabel ?weight')
                //res = k[uri].getIndividualsFeatureValueWeight(id, '?ind ?label ?valueTypeLabel ?value ?weightType ?weightTypeLabel ?weight')
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(k.toURI('ui:Feature'))){
            try{
                res = k[uri].getGrandChildrenIndividuals(id, '?ind ?label ?valueTypeLabel ?value ?relevance')
                k[uri].getChildrenExtraIndividuals(id, '?ind ?name ?justification ?valueTypeLabel ?value ?relevance').each{
                    res.push([ind: it.ind, label: 'Indicador específico: '+it.name+', justificativa: '+it.justification, valueTypeLabel: it.valueTypeLabel, value: it.value, relevance: it.relevance])
                }
            }
            catch (e){
                res = []
            }
        }
        else if(classList.contains(k.toURI('dbp:Region'))){
            try {
                //println "uri"
                //println uri
                res = k[id].getMap('?map')
            }
            catch (e) {
                res = []
            }
        }

        //println uri
        //println classList
        //println res

        return res
    }

    def propertyMissing(String name) {
        getAt(name)
    }

    def getAt(String name) {
        findNode(name)
    }
}