package sustenagro

class Indicator {
	
	String name
	String description

	public Indicator(){
		this.name = "default name"
		this.name = "default description"
	}

	public Indicator(String name){
		this.name = name
		this.name = "default description"
	}

    static constraints = {
    	
    }
}
