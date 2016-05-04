package sustenagro

import grails.converters.JSON
import semantics.Node

class UserController {
    def k
    def dsl
    def gui
    def springSecurityService

    def signup() {
        dsl.clean(controllerName, actionName)
        gui.setView(controllerName, actionName)
        gui.renderView(actionName)
        render(view: actionName, model: [inputs: gui.viewsMap[controllerName][actionName]])
    }

    def createUser(){
        def node = new Node(k, '')
        def base = k.toURI('ui:')
        def user
        def properties = [:]
        def usernameURI = k.toURI('ui:hasUsername')
        def passwordURI = k.toURI('ui:hasPassword')
        def passwordConfirmURI = k.toURI('ui:hasPassword-confirm')
        def termsofuseURI = k.toURI('ui:hasTermsofuse')
        def userRole = Role.find{ authority == 'ROLE_USER'}

        if( params[usernameURI] && params[passwordURI] && params[passwordURI] == params[passwordConfirmURI] && params[termsofuseURI] == 'yes'){
            params.each{
                if(it.key == passwordURI){
                    properties[it.key] = [value: springSecurityService.encodePassword(it.value), dataType: k[it.key].range]
                }
                else if(it.key.startsWith(base) && it.key != passwordConfirmURI && it.key != termsofuseURI)
                    properties[it.key] = [value: it.value, dataType: k[it.key].range]
            }
            properties[k.toURI('ui:hasRole')] = [value: k.toURI(':userRole'), dataType: k.toURI('ui:Role')]

            def uri = k.toURI(':'+properties[usernameURI].value)

            if(!k[uri].exist()){
                node.insertUser(uri, properties)

                user = new User(properties[usernameURI].value, properties[passwordURI].value, true).save()
                UserRole.create user, userRole
            }

            flash.message = 'Usuário cadastrado com sucesso'
        }
        redirect(controller: 'login', action: 'auth')
    }

    def usernameAvailability(){
        def username = params['http://purl.org/biodiv/semanticUI#hasUsername']
        render !k[':'+username].exist()
    }

    def emailAvailability(){
        def email = params['http://purl.org/biodiv/semanticUI#email']
        render (!k['http://purl.org/biodiv/semanticUI#email'].findSubject(email).size() > 0)
    }
}
