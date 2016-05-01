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

    def instance(Map attrs = [:], String id){
        def uri = k.toURI(id)
        def range = (id != 'rdfs:subClassOf')? k[uri].range : _id
        def dataType = (range)? range : k.toURI('xsd:string')
        def widget = (attrs['widget'])? attrs['widget'] : gui['dataTypeToWidget'].find { k.toURI(it.key) == dataType }.value
        def request = []
        def prop

        attrs['id'] = uri
        attrs['required'] = (attrs['required'])? attrs['required'] : (k[_id].getRestriction(uri).size > 0)

        if(id == 'rdfs:subClassOf'){
            prop = id
            request = [prop, dataType]
            attrs['required'] = true
        }
        else if(k[uri].type.contains(k.toURI('owl:ObjectProperty'))){
            prop = 'rdf:type'
            request = [prop, dataType]
        }

        widget = (widget)? widget : 'textForm'

        if(widget == 'categoryForm')
            attrs['selectType'] = (attrs['multipleSelection'])? 'checkbox' : 'radio'

        if(!k[uri].isFunctional())
            widget = 'multipleCategoryForm'

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

    def type(Map attrs = [:], String id='rdfs:subClassOf'){
        instance(attrs, id)
    }

    def getURI(){
        return k.toURI(_id)
    }

}
