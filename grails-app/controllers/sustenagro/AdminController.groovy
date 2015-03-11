package sustenagro

import groovy.xml.MarkupBuilder

class AdminController {

    def index() { 
    	
    }
    
    def dslCreate() {
    	//"Language prototype = 
    	/*
    		div (class: "well")   {
			    p (class: "lead")    {
			         "hola mundo"
			    }
			}
    	*/
    	   	
    	def writer = new StringWriter() 
    	def markup = new MarkupBuilder(writer)
    	def code   = params["code"]
    	def parts  = code.tokenize('{}')
    	
    	def head      = parts[0]
    	def headparts = head.tokenize('()')
   		def tag		  = headparts[0].trim()
   		def attrs     = ["class": "well"]
    	def content   = parts[1].replace('"', '').trim()
    	
    	//headparts[1].trim()
    	
    	def node      = markup.createNode(tag, attrs, content)
    	markup.nodeCompleted(null, node)
    	
    	render writer.toString()
    }
    
    def dslEdit() {
    	
    }
    
    def dslDelete() {
    	
    }
}
