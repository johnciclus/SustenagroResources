package sustenagro

import groovy.xml.MarkupBuilder

class AdminController {

    def index() { 
    	
    }
    
    def dslCreate() {
    	//"Language prototype = 
    	/*
    		div (class= "well")   {
			    p (class= "lead")    {
			         "hola mundo"
			    }
			}
    	*/
    	   	
    	def writer = new StringWriter() 
    	def markup = new MarkupBuilder(writer)
    	def parent = null
    	def code   = params["code"]
    	
    	def el	= analyze(code)
    	def node= markup.createNode(el.tag)
    	    	
    	struct(el, node, markup)
    	
    	markup.nodeCompleted(null, node)
    	
    	render writer.toString()
    }
    
    def struct(el, node, markup){
    	if(el.content.contains("{")){
    		el		= analyze(el.content)
    		def parent  = node
	    	if(el.content.contains("{")){
	    		node    = markup.createNode(el.tag)
	    		struct(el, parent, markup)
	    	}
	    	else{
	    		node    = markup.createNode(el.tag, el.content)
	    	}
	    	markup.nodeCompleted(parent, node)
    	}
    	else{
    		node.setValue(el.content)
    	}
    }
    
    def analyze(code){
    	
    	def ini      = code.indexOf('{')
    	def fin      = code.lastIndexOf('}')
    	
    	def head     = code.substring(0, ini)
    	def content  = code.substring(ini + 1, fin).trim()
    	
    	def tag      = head.substring(0, head.indexOf('(')).trim()
		def params   = head.substring(head.indexOf('(') + 1, head.lastIndexOf(')')).trim()
    	return [tag: tag, params: params, content: content]
    }
    
    def dslEdit() {
    	
    }
    
    def dslDelete() {
    	
    }
}
