package sustenagro

import groovy.xml.MarkupBuilder

class Dsl {

    def writer
    def markup
    def missing     = [:]
    def elems       = [ 'a', 'p', 'b', 'i', 'mark', 'del', 's', 'ins', 'u', 'small', 'strong', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'abbr', 'footer', 'cite', 'dl', 'dt', 'dd', 'code', 'pre', 'var', 'samp', 'span']
    def containers  = [ 'div', 'blockquote', 'address', 'form' ]                    //containers
    def lists       = [ 'ul', 'ol', 'li']                                           //lists
    def tables      = [ 'table', 'thead', 'tbody', 'tr', 'th', 'td']                //tables
    def forms       = [ 'label', 'input', 'textarea', 'select', 'option', 'button'] //forms
    def imgs        = [ 'img' ]

    def currentParent   = null

    public Dsl(){
        elems.each({baseFunction(it)})
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

class AdminController {

    def index() {
    
    }
    
    def GroovyArchitecture(){
        
        Binding binding = new Binding()
        Dsl dsl = new Dsl()
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