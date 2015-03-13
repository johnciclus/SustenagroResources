package sustenagro

import groovy.xml.MarkupBuilder

class AdminController {

    def index() { 
    	
    }
    
    def dslCreate() {
    	//"Language prototype = 
    	/*

h1 { bootstrap }

mark { bootstrap }

del { bootstrap }

small { hola mundo }

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
    	*/
    	   	
    	def writer = new StringWriter() 
    	def markup = new MarkupBuilder(writer)
    	def code   = params["code"]
    	
    	def el	   = analyze(code)	
    	struct(el, null, markup)
    	
    	render writer.toString()
    }
    
    def struct(el, parent, markup){
    	def node
    	if(el.content.contains("{")){
    		node	= markup.createNode(el.tag, (Map) el.params)
    		el		= analyze(el.content)
    		struct(el, node, markup)
    	}
    	else{
    		node    = markup.createNode(el.tag, el.params, el.content)
    	}
    	markup.nodeCompleted(parent, node)
    }
    
    def analyze(code){
    	
    	def iniHead = code.indexOf('{')
    	def endHead	= code.lastIndexOf('}')
    	
    	def head    = code.substring(0, iniHead)
    	def content = code.substring(iniHead + 1, endHead).trim()
    	
    	def iniParm = head.indexOf('(')
    	def endParm = head.lastIndexOf(')')
    	
    	def tag, params
    	
    	if(iniParm == -1 || endParm == -1){
    		tag		= head.substring(0, iniHead).trim()
    		params  = [:]
    	}
    	else{
    		tag     = head.substring(0, iniParm).trim()
    		params  = genMap(head.substring(iniParm + 1, endParm).trim())
    	}
    			
    	return [tag: tag, params: params, content: content]
    }
    
    def genMap(params){
    	def map  = [:]
    	def list = params.tokenize(',')
    	def elems
    	
    	list.each{ item ->
    		elems = item.tokenize(':')
    		map.put(elems[0].trim(), elems[1].replaceAll("\"|\'","").trim())
    	}    	
   		return map
    }
    
    def dslEdit() {
    	
    }
    
    def dslDelete() {
    	
    }
}
