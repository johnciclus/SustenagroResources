package utils

/**
 * Created by john on 20/07/15.
 */
class Uri {

    def static simpleDomain(ArrayList list, String dom, String prefix=":"){
        list.each{ el ->
            el.each{
                if(it.value instanceof String)
                    it.value = it.value.replace(dom,prefix)
            }
        }
        return list
    }

    def static fullDomain(ArrayList list, String dom){
        list.each{ el ->
            el.each{
                it.value = it.value.replace(":", dom)
            }
        }
        return list
    }

    def static printTree(Object object, int level=-1){
        if(object.getClass() == LinkedHashMap){
            level++
            object.each{
                print "\t"*level + it.key + " : "
                if(it.value.getClass() != LinkedHashMap) {
                    println it.value
                }
                else{
                    println ""
                    printTree(it.value, level)
                }
            }
        }
        else if(object.getClass() == ArrayList){
            level++
            object.eachWithIndex{ it, index ->
                print "\t"*level + '['+index+"] : "
                if(it.getClass() != LinkedHashMap) {
                    println it
                }
                else{
                    println ""
                    printTree(it, level)
                }
            }
        }
    }

}
