package rdfUtils

/**
 * Created by john on 12/2/15.
 */

class Know extends RDFSlurper{

    public Know(String url){
        super(url)
    }

    def propertyMissing(String name) {
        getAt(name)
    }

    def getAt(String name) {
        findNode(name)
    }

    def findNode(String name) {
        //println name
        //println toURI(name)
        new Node(this, toURI(name))
    }

}
