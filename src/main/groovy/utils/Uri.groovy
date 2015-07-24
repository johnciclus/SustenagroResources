package utils

/**
 * Created by john on 20/07/15.
 */
class Uri {
    def static removeDomain(String uri, String dom){
        return uri[dom.length() - uri.length() .. -1]
    }
}
