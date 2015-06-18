package sustenagro

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import com.tinkerpop.gremlin.groovy.Gremlin

class Application extends GrailsAutoConfiguration {
    static{
        Gremlin.load()
    }
    static void main(String[] args) {
        GrailsApp.run(Application)
    }
}