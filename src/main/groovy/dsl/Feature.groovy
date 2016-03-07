package dsl

import org.springframework.context.ApplicationContext
/**
 * Created by john on 27/02/16.
 */
class Feature {
    def _id
    def _ctx
    def k
    def gui
    def model = []

    Feature(String id, ApplicationContext applicationContext){
        _id = id
        _ctx = applicationContext
        k = _ctx.getBean('k')
        gui = _ctx.getBean('gui')
    }

    def conditional(String arg, String type, Closure closure = {}){
        model << [conditional: [objectType: k.toURI(arg), isType: k.toURI(type), then: closure()]]
    }

    def include(String arg){
        [include: k.toURI(arg)]
    }

    def exclude(String arg){

    }

    def evalObject(String arg){
        def asserts = []
        model.each{
            def types = k[k.toURI(arg)].getType()
            if(it['conditional'])
                if(types.contains(it['conditional']['objectType']))
                    if(types.contains(it['conditional']['isType']))
                        if(it['conditional']['then']){
                            asserts << it['conditional']['then']['include']
                        }
        }
        return asserts
    }
}
