package sustenagro

import groovy.xml.MarkupBuilder
import com.hp.hpl.jena.query.Query
import com.hp.hpl.jena.query.QueryExecution
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QueryFactory
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP
import groovy.json.JsonSlurper

class Dsl {

    def writer
    def markup
    def missing     = [:]
    def properties

    //def elems       = [ 'a', 'p', 'b', 'i', 'mark', 'del', 's', 'ins', 'u', 'small', 'strong', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'abbr', 'footer', 'cite', 'dl', 'dt', 'dd', 'code', 'pre', 'var', 'samp', 'span']
    def containers  = [ 'div', 'blockquote', 'address', 'form' ]                    //containers
    def lists       = [ 'ul', 'ol', 'li']                                           //lists
    def tables      = [ 'table', 'thead', 'tbody', 'tr', 'th', 'td']                //tables
    def forms       = [ 'label', 'input', 'textarea', 'select', 'option', 'button'] //forms
    def imgs        = [ 'img' ]

    def currentParent   = null
 
    public Dsl(elems){
        elems.each({func(it)})
        containers.each({baseFunction(it)})
        lists.each({baseFunction(it)})
        tables.each({baseFunction(it)})
        forms.each({baseFunction(it)})
        imgs.each({baseFunction(it)})
    }

    def baseFunction = { it ->
        Dsl.metaClass."$it" = { Map map = [:], content ->
            
            if(content.metaClass.respondsTo(content, "call")){
                println "closure"

                def node   = markup.createNode(it, map)
                currentParent = node
                                    
                def text = content()
                if (text != null ){
                    markup.getMkp().yield(text)
                }
                
                currentParent = null
                markup.nodeCompleted(currentParent, node)
            }
            else if (content.class.simpleName == "String"){
                println "string"
                def node = markup.createNode(it, map, content)
                markup.nodeCompleted(currentParent, node)
            }
        }
    }

    void properties(Map map) { println "properties"}

    def func = { it ->
        Dsl.metaClass."$it" = { Closure cl ->
            def map     = [:]
            def node    = markup.createNode(it, map)
            currentParent = node

            def text = cl()
            if (text != null ){
                markup.getMkp().yield(text)
            }
            currentParent = null
            markup.nodeCompleted(currentParent, node)

            println "Success"
        }
    }

    def make(Closure closure) {
        println "\nStart of the analyze "

        writer = new StringWriter()
        markup = new MarkupBuilder(writer)

        closure.setDelegate(this)
        closure.setResolveStrategy(Closure.DELEGATE_ONLY)
        closure()

        return writer.toString()
    }
}

class EmailSpec {
    void from(String from) { println "From: $from"}
    void to(String to) { println "To: $to"}
    void subject(String subject) { println "Subject: $subject"}
    void body(Closure body) {
        def bodySpec = new BodySpec()
        def code = body.rehydrate(bodySpec, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }
}

class BodySpec {
    void content(String from) { println "From: $from"}
}

class AdminController {

    def elems = []

    /*def AdminController(){
        String queryStr =   "PREFIX ui_ontology: <http://bio.icmc.usp.br:8888/sustenagro/ui_ontology#>" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                                "select distinct ?name where {" +
                                    "?s rdf:type ui_ontology:Tag ." +
                                    "?s ui_ontology:name ?name" +
                                "}"

        Query query = QueryFactory.create(queryStr)
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://bio.icmc.usp.br:8888/openrdf-workbench/repositories/SustenAgro/query", query)
        
        ((QueryEngineHTTP)qexec).addParam("timeout", "10000")

        ResultSet rs = qexec.execSelect()

        while(rs.hasNext()){
            elems += rs.next().get("name")
        }
    }*/

    def index() {
    }
    
    def readDSL(){
        String code = params["code"]
        println code

        def jsonSlurper = new JsonSlurper()
        def obj = jsonSlurper.parseText(code)

        render obj.name
    }

    def GroovyArchitecture(){
        
        Binding binding = new Binding()
        Dsl dsl = new Dsl(elems)
        binding.setVariable("dsl", dsl)
        
        GroovyShell shell = new GroovyShell(binding)
        Script script = shell.parse("dsl.make({" + params["code"] + "})")
        render script.run()

        /*String sparqlQueryString= "select distinct ?Concept where {[] a ?Concept} LIMIT 100"

        Query query = QueryFactory.create(sparqlQueryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

        ResultSet results = qexec.execSelect();
        println results       

        qexec.close();*/
    }

 
    def analyze(code){
    	
    	def ini, mid, end = -1
    	def elms = []
    	def count
    	 
    	while(end != (code.length()-1)){
	    	ini = end+1
	    	mid  = code.indexOf('{', ini)
	    	count = 0
	    	
	    	if(mid != -1){
	    		count++
	    		
	    		for(int i = mid+1; i<code.length(); i++){
	    			if(code.charAt(i) == '{')
	    				count++
	    			else if(code.charAt(i) == '}')
	    				count--
	    			if(count == 0){
	    				end = i
	    				break
	    			}
	    		}
	    		
	    		elms.add(createEl(code.substring(ini, end), mid-ini))
	    	}
    	}
    	
    	return elms
    }
    
    def createEl(code, mid){
    	def end		= code.length()
    	def head    = code.substring(0, mid)
    	def content = code.substring(mid + 1, end).trim()
    	
    	def iniParm = head.indexOf('(')
    	def endParm = head.lastIndexOf(')')
    	
    	def tag, params
    	
    	if(iniParm == -1 || endParm == -1){
    		tag		= head.substring(0, mid).trim()
    		params  = [:]
    	}
    	else{
    		tag     = head.substring(0, iniParm).trim()
    		params  = genMap(head.substring(iniParm + 1, endParm).trim())
    	}
    			
    	return [tag: tag, params: params, content: content]
    }
        
    def genMap(params){   	
   		return params.tokenize(',').inject([:]) { map, token ->
			token.trim().tokenize(':').with { map[it[0].trim()] = it[1].replaceAll("\"|\'","").trim() }
			map
		}
    }
    
    def dslEdit() {
    	
    }
    
    def dslDelete() {
    	
    }
}

//"Language prototype = 
        /*

        h1 "bootstrap"
        
        mark "bootstrap"
        
        del "bootstrap"
        
        small "hola mundo"
        
        p 'class': 'lead', "bootstrap"
        
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