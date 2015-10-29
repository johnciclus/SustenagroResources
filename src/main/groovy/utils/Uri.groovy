package utils

/**
 * Created by john on 20/07/15.
 */
class Uri {
    def static removeDomain(String uri, String dom){
        return uri[dom.length() - uri.length() .. -1]
    }

    def static addDomain(String uri, String dom){
        return dom + uri
    }

    def static simpleDomain(ArrayList list, String dom){
        list.each{ el ->
            el.each{
                it.value = it.value.replace(dom,":")
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

}
