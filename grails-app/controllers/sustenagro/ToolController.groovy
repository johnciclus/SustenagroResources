package sustenagro

class ToolController {

    def index() {

    }

    def location() {
        def production_unit = new ProductionUnit(name: params["production_unit_name"] , location: params["production_unit_location"])
        production_unit.save()
        render(template: 'form')
    }

}
