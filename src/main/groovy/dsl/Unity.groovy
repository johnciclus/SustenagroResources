package dsl

import org.springframework.context.ApplicationContext
/**
 * Created by john on 27/02/16.
 */
class Unity {
    def _id
    def _ctx
    def featureLst = []

    Unity(String id, ApplicationContext applicationContext){
        _id = id
        _ctx = applicationContext
    }

    def feature(Map args = [:], String clsName, String prop = ''){
        def k = _ctx.getBean('k')
        def gui = _ctx.getBean('gui')
        def propertyId = k.shortURI(clsName)
        def range = k[clsName].range
        def dataType = (range)? range : 'http://www.w3.org/2001/XMLSchema#string'
        def widget = (!args['widget'])? gui['dataTypeToWidget'][(String) dataType] : args['widget']
        def object = (!args['object'])? dataType : args['object']
        widget = (widget)? widget : 'string'

        args['id'] = propertyId
        if(widget == 'category')
            args['selectType'] = (args['multipleSelection'])? 'checkbox' : 'radio'

        featureLst << [ 'id': propertyId,
                        'widget': widget.toLowerCase(),
                        'request': (prop?.trim()) ? [prop, object] : [],
                        'args': args]
    }

    def subclass(Map args = [:], String clsName){
        feature(args, clsName, 'rdfs:subClassOf')
    }

    def instance(Map args = [:], String clsName){
        feature(args, clsName, 'rdf:type')
    }
}
