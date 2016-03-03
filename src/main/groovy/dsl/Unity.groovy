package dsl

import org.springframework.context.ApplicationContext
/**
 * Created by john on 27/02/16.
 */
class Unity {
    def _id
    def _ctx
    def features = []
    def model = []
    def k
    def gui

    Unity(String id, ApplicationContext applicationContext){
        _id = id
        _ctx = applicationContext
        k = _ctx.getBean('k')
        gui = _ctx.getBean('gui')
    }

    def feature(Map args = [:], String id, String prop = ''){
        def uri = k.toURI(id)
        def featureId = k.shortURI(uri)
        def range = (id != _id)? k[uri].range : uri
        def dataType = (range)? range : 'http://www.w3.org/2001/XMLSchema#string'
        def widget

        if(args['widget']){
            widget = args['widget']
        }
        else{
            gui['dataTypeToWidget'].find{ key, value ->
                if(k.toURI(key) == dataType){
                    widget = value
                    return true
                }
            }
        }
        def request = (prop?.trim())? [prop, dataType] : []

        widget = (widget)? widget.toLowerCase() : 'string'
        args['id'] = featureId

        if(widget == 'category')
            args['selectType'] = (args['multipleSelection'])? 'checkbox' : 'radio'

        model << [id: featureId, dataType: dataType]

        features << [ id: featureId,
                        widget: widget,
                        request: request,
                        args: args]
    }

    def type(Map args = [:], String id=_id){
        feature(args, id, 'rdfs:subClassOf')
    }

    def instance(Map args = [:], String id){
        feature(args, id, 'rdf:type')
    }
}
