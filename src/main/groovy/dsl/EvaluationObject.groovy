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

    def instance(Map attrs = [:], String id, String prop = ''){
        def uri = k.toURI(id)
        //def featureId = k.shortURI(uri)
        def range = (id != _id)? k[uri].range : uri
        def dataType = (range)? range : k.toURI('xsd:string')
        def widget = (attrs['widget'])? attrs['widget'] : gui['dataTypeToWidget'].find { k.toURI(it.key) == dataType }.value
        def request = []

        if(prop?.trim()){
            request = [prop, dataType]
        }
        else{
            if(k[uri].type.contains(k.toURI('owl:ObjectProperty'))){
                prop = 'rdf:type'
                request = [prop, dataType]
            }
        }

        widget = (widget)? widget : 'textForm'

        if((id == _id) && prop == 'rdfs:subClassOf')
            widget = 'multipleCategoryForm'

        if(widget == 'categoryForm')
            attrs['selectType'] = (attrs['multipleSelection'])? 'checkbox' : 'radio'

        attrs['id'] = uri

        //println uri

        model << [id: uri,
                  range: range,
                  dataType: dataType,
                  prop: prop,
                  attrs: attrs]

        widgets << [ id: uri,
                     widget: widget,
                     request: request,
                     attrs: attrs]
    }

    def type(Map attrs = [:], String id=_id){
        instance(attrs, id, 'rdfs:subClassOf')
    }

    def getURI(){
        return k.toURI(_id)
    }

}
