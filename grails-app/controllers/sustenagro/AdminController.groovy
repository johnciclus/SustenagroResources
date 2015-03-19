package sustenagro

import groovy.xml.MarkupBuilder

class Dsl {

    def writer
    def markup
    def missing = [:]
    def dynamicMethods = ['p', 'mark', 'del', 'small', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6']

    public Dsl(){
        def method = this.dynamicMethods.each({
            Dsl.metaClass."$it" = { text -> 
                def node = this.markup.createNode(it, text)
                this.markup.nodeCompleted(null, node) 
            }
        })
    }

    def make(Closure closure) {
        println "Start of the analyze "

        this.writer = new StringWriter()
        this.markup = new MarkupBuilder(this.writer)

        closure.setDelegate(this)
        closure.setResolveStrategy(Closure.DELEGATE_ONLY)
        closure()

        return this.writer.toString()
    }

    /*def methodMissing(String mark, args) {
        def method = this.dynamicMethods.each({ it == mark })
        
        println "method missing way"

        if(method){
            this."$mark"(mark, args[0])
            this.p("p", args[0])
        }
        else{
            missing.put(methodName, args[0])    
        }
    }*/
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
    }

  
    def dslCreate() {
    	//"Language prototype = 
    	/*

		h1 "bootstrap"
		
		mark "bootstrap"
		
		del "bootstrap"
		
		small "hola mundo"
		
		p (class: 'lead') { bootstrap }
		
		blockquote{  p { Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante. }}
		
		ul {
		li{1}
		li{2}
		}
		
		div (class: 'well')   {
		  u { bootstrap }
		}
		
		div (class: 'well')   {
		    div (class: 'well')   {
		        p (class: 'lead')    {
		            hola mundo
		        }
		    }
		}
		
		div (class: 'well')   {
		    div (class: 'well')   {
		        div (class: 'well')   {
		            div (class: 'well')   {
		                p (class: 'lead text-center')    {
		                    hola mundo
		                }
		            }
		        }
		    }
		}
		
		ol (class: "c1"){
			li (class: "l1"){1}
			li (class: "l2"){2}
			li (class: "l2"){3}
			li (class: "l2"){4}
			li (class: "l2"){5}
			li (class: "l2"){6}
		}
		
		table (class:"table table-hover"){
			thead{
			        tr{
			          th{#}
			          th{First Name}
			          th{Last Name}
			          th{Username}
			        }
			}
			tbody{
			        tr{
			          th (scope:"row"){1}
			          td{ Mark }
			          td{ Otto }
			          td{ @mdo }
			        }
			  }
			}
		div (class:"form-group"){
		    label (for:"exampleInputEmail1"){ Email address }
		    input (type: "email", class:"form-control", id:"exampleInputEmail1['hola']", placeholder:"Enter email"){}
		}
		
    	*/
    	
    	Script dslScript = new GroovyShell().parse(params["code"])
    	
    	def writer = new StringWriter() 
    	def markup = new MarkupBuilder(writer)
    	
    	def elms   = analyze(params["code"])
    	
    	for( e in elms){	
    		struct(e, null, markup)
    	}
    	
    	render writer.toString()
    }    
    
    def struct(el, parent, markup){
    	def node
    	def elms
    	
    	if(el.content.contains("{")){
    		node	= markup.createNode(el.tag, (Map) el.params)
    		elms	= analyze(el.content)
    		for( e in elms){
	    		struct(e, node, markup)
	    	}
    	}
    	else{
    		node    = markup.createNode(el.tag, el.params, el.content)
    	}
    	markup.nodeCompleted(parent, node)
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
    	/*
		def map = [:]
		def list = params.tokenize(',')
    	def elems

    	list.each{ item ->
    		elems = item.tokenize(':')
    		map.put(elems[0].trim(), elems[1].replaceAll("\"|\'","").trim())
    	}
		return map
    	*/
    	    	
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