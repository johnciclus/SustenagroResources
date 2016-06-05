package sustenagro

class HomeController {
    static allowedMethods = [index: "GET",
                             contact: "GET"]

    def dsl
    def gui

    def index() {

    }

    def contact(){
        gui.setView(controllerName, actionName)
        dsl.clean(controllerName, actionName)
        gui.renderView(actionName)
        render(view: actionName, model: [inputs: gui.viewsMap[controllerName][actionName]])
    }
}
