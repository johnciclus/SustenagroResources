package sustenagro

class AdminController {

    def index() { 
    	
    }
    
    def dslCreate() {
    
    	//"Language prototype " h2 {"hola mundo"}
    	 
    	String code = params["code"]
    	def values  = code.tokenize('{}')
    	
    	String tag  = values[0]
    	String text = values[1]
    	   	
    	render "<" + tag + ">" + text + "</" + tag + ">"
    }
    
    def dslEdit() {
    	
    }
    
    def dslDelete() {
    	
    }
}
