package sustenagro

class HomeController {
    def dsl
    def gui

    def index() {

    }

    def contact(){
        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)
        gui.renderView(actionName)
        render(view: actionName, model: [inputs: gui.viewsMap[controllerName][actionName]])
    }
}
