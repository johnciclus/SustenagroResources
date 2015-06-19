package sustenagro

class ProductionUnit {

    String name 
    String location
    String microregion
    Crop evaluatedCrop

    static constraints = {
    	name unique: true
        evaluatedCrop nullable: true
    }
}
