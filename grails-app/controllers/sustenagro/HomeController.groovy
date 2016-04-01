package sustenagro

class HomeController {
    def dsl
    def gui
    def k

    def index() {

    }

    def contact(){
        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)
        gui.title([text: 'Contato', widgetClass: 'page-header'])
        gui.text([text: 'O projeto SustenAgro é um projeto da unidade de meio Ambiente da Embrapa, do laboratorio Intermidia (area de pesquisa Web Semântica do ICMC - USP São Carlos). '])
        gui.text([text: 'SustenAgro busca fornecer uma ferramenta para opiar a avaliação da sustentabilidade em sistemas agrícolas. '])
        gui.text([text: 'A informação de contato é: '])
        gui.img([src: '/assets/contact.jpg', widgetClass: 'img-centered', width: '480'])
        render(view: actionName, model: [inputs: gui.viewsMap[controllerName][actionName]])

    }
}
