package sustenagro

import groovy.xml.MarkupBuilder
import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.query.QueryExecution
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.QuerySolution
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP

class OntologyAdmin{
    
    String url_service  = "http://bio.icmc.usp.br:8888/openrdf-workbench/repositories/SustenAgro/query"

    Map query_map       =   [   'tag_names':  "PREFIX ui_ontology: <http://bio.icmc.usp.br:8888/sustenagro/ui_ontology#>" +
                                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                                    "select distinct ?name where {" +
                                    "?s rdf:type ui_ontology:Tag ." +
                                    "?s ui_ontology:name ?name" +
                                "}"
                            ]
    Map params_map      = ["timeout": "10000"]

    public OntologyAdmin(){
        println "Make queries"

        println execQuery(query_map["tag_names"])
    }

    public execQuery(String query){
        QueryExecution qexec = QueryExecutionFactory.sparqlService(url_service, QueryFactory.create(query))
        
        ((QueryEngineHTTP)qexec).addParam('timeout', params_map['timeout'])

        ResultSet rs = qexec.execSelect()
        
        String[] names = rs.getResultVars()

        Map[] result = []
        Map object
        QuerySolution element 
        

        while(rs.hasNext()){
            element = rs.next()
            object = [:]
            names.each{ key ->
                object[key] = element.get(key)
            }
            result += object
        }
        result
    }
}

class Html {

    def writer
    def markup
    def missing         = [:]

    def audio_video     = [ 'audio', 'source', 'track', 'video' ]
    def basic           = [ 'br', 'hr', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p' ]
    def containers      = [ 'address', 'article', 'aside', 'details', 'dialog', 'blockquote', 'div', 'footer', 'header', 'main', 'section', 'summary' ]                             //containers
    def format          = [ 'abbr', 'b', 'bdi', 'bdo', 'cite', 'code', 'del', 'dfn', 'em', 'i', 'ins', 'kbd', 'mark', 'meter', 'pre', 'progress', 'q', 'rp', 'rt', 'ruby', 's', 'samp', 'small', 'strong', 'sub', 'sup', 'time', 'u', 'var', 'wbr' ]
    def form            = [ 'form' ]                                                                                                                                                //form
    def form_elems      = [ 'button', 'datalist', 'fieldset', 'input', 'keygen', 'label', 'legend', 'optgroup', 'option', 'output', 'select', 'textarea']                           //forms elements
    def frames          = [ 'iframe' ]
    def images          = [ 'img', 'map', 'area', 'canvas', 'figcaption', 'figure' ]
    def links           = [ 'a', 'link', 'nav']
    def lists           = [ 'ul', 'ol', 'li', 'dl', 'dt', 'dd']                                                                                                                     //lists
    def style           = [ 'span', 'style' ]
    def table           = [ 'table' ]                                                                                                                                               //
    def table_parts     = [ 'caption', 'thead', 'tbody', 'tfoot', 'colgroup', 'col' ]                                                                                               //table parts
    def table_elems     = [ 'td', 'th', 'tr']                                                                                                                                       //table elems
    
    

    def current   = null
 
    public Html(){
        OntologyAdmin ui_onto_admin = new OntologyAdmin()

        audio_video.each({baseFunction(it)})
        basic.each({baseFunction(it)})
        containers.each({baseFunction(it)})
        format.each({baseFunction(it)})
        form.each({baseFunction(it)})
        form_elems.each({baseFunction(it)})
        frames.each({baseFunction(it)})
        images.each({baseFunction(it)})
        links.each({baseFunction(it)})
        lists.each({baseFunction(it)})
        style.each({baseFunction(it)})
        table.each({baseFunction(it)})
        table_parts.each({baseFunction(it)})
        table_elems.each({baseFunction(it)})
    }

    def indicator(Closure cl){
        def code      = cl.rehydrate(new Indicator(), this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }

    def baseFunction = { it ->
        Html.metaClass."$it" = {  Map map = [:], Closure cl ->
            //def tag       = new Tag(this)
            //def code      = cl.rehydrate(tag, this, this)
            //code.resolveStrategy = Closure.DELEGATE_ONLY

            def node      = markup.createNode(it, map)
            //def parent    = this.current
            //this.current  = node

            def text      = cl()

            if (text != null ){
                markup.getMkp().yield(text)
            }

            markup.nodeCompleted(null, node)
        }
    }

    def make(Closure closure) {
        writer = new StringWriter()
        markup = new MarkupBuilder(writer)

        closure.setDelegate(this)
        closure.setResolveStrategy(Closure.DELEGATE_ONLY)
        closure()

        return writer.toString()
    }
}

class Indicator{
    def title
    def description
    def threshold
    def management_measure
    def justification

    def Indicator(){
        println "Indicator constructor"
    }

    def title(String title_arg){
        this.title = title_arg
        println this.title
    }

    def description(String desc_arg){
        this.description = desc_arg
        println this.description
    }

    def threshold(String thre_arg){
        this.threshold = thre_arg
        println this.threshold
    }

    def management_measure(String meas_arg){
        this.management_measure = meas_arg
        println this.management_measure
    }

    def justification(String just_arg){
        this.justification = just_arg
        println this.justification   
    }
}

class AdminController {

    def html
    
    def index(){
        
    }

    def AdminController(){
        html = new Html()
    }

    def GroovyArchitecture(){
        
        //Binding binding = new Binding([html: new Html(elements)])
        //Html html = new Html(elements)
        //binding.setVariable("html", html)
        
        //GroovyShell shell = new GroovyShell(new Binding([html: new Html()]))

        //Script script = shell.parse("html.make({" + params["code"] + "})")
        //render script.run()

        Binding binding   = new Binding()
        binding.setVariable("html", html)
        GroovyShell shell = new GroovyShell(binding)

        render shell.evaluate("html.make({" + params["code"] + "})")


        /*String sparqlQueryString= "select distinct ?Concept where {[] a ?Concept} LIMIT 100"

        Query query = QueryFactory.create(sparqlQueryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

        ResultSet results = qexec.execSelect();
        println results       

        qexec.close();*/
    }
}

//"Language prototype = 
/*

h1 {"bootstrap"}

mark {"bootstrap"}

del {"bootstrap"}

small {"hola mundo"

p 'class': 'lead', {"bootstrap"}

div 'class':'container-fluid', { p 'class': 'lead', "bootstrap" }

blockquote { p { "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante." } }

ul {
    li{"1"}
    li{"2"}
}

div 'class': 'well', { p { 'bootstrap' }  }

div 'class': 'well',  {
    div 'class': 'well', {
        p 'class': 'lead', {
            "hola mundo"
        }
    }
}
        
ol 'class': "c1", {
    li 'class': "l1", {1}
    li 'class': "l2", {2}
    li 'class': "l2", {3}
    li 'class': "l2", {4}
    li 'class': "l2", {5}
    li 'class': "l2", {6}
}

table 'class':"table table-hover", {
    thead{
        tr{
          th{'#'}
          th{'First Name'}
          th{'Last Name'}
          th{'Username'}
        }
    }
    tbody{
        tr{
          th 'scope':"row", {'1'}
          td{ 'Mark' }
          td{ 'Otto' }
          td{ '@mdo' }
        }
    }
}

div 'class':"form-group", {
    label 'for':"exampleInputEmail1", { 'Email address' }
    input 'type': "email", 'class':"form-control", 'id':"exampleInputEmail1['hola']", 'placeholder':"Enter email", {}
}

input 'type': "email", 'class':"form-control", 'id':"exampleInputEmail", {}

div 'class':"row", {
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
  div 'class':"col-md-1",{".col-md-1"}
}

*/