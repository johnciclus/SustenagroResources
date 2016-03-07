package dsl

import org.springframework.context.ApplicationContext
/**
 * Created by john on 27/02/16.
 */
class EvaluationObject {
    def _id
    def _ctx
    def model
    def widgets
    def k
    def gui

    EvaluationObject(String id, ApplicationContext applicationContext){
        _id = id
        _ctx = applicationContext
        k = _ctx.getBean('k')
        gui = _ctx.getBean('gui')
        model = []
        widgets = []
    }

    def feature(Map args = [:], String id, String prop = ''){
        def uri = k.toURI(id)
        def featureId = k.shortURI(uri)
        def range = (id != _id)? k[uri].range : uri
        def dataType = (range)? range : 'http://www.w3.org/2001/XMLSchema#string'
        def request = (prop?.trim())? [prop, dataType] : []
        def widget = (args['widget'])? args['widget'] : gui['dataTypeToWidget'].find { k.toURI(it.key) == dataType }.value

        widget = (widget)? widget : 'textForm'

        if((id == _id) && prop == 'rdfs:subClassOf')
            widget = 'multipleCategoryForm'

        if(widget == 'categoryForm')
            args['selectType'] = (args['multipleSelection'])? 'checkbox' : 'radio'

        args['id'] = featureId

        model << [id: featureId,
                  dataType: dataType]

        widgets << [ id: featureId,
                     widget: widget,
                     request: request,
                     args: args]
    }

    def type(Map args = [:], String id=_id){
        feature(args, id, 'rdfs:subClassOf')
    }

    def instance(Map args = [:], String id){  //automatize if ID is Object Property or Data Property
        feature(args, id, 'rdf:type')
    }
}
