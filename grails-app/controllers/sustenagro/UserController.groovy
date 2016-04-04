package sustenagro

import semantics.Node

class UserController {
    def springSecurityService
    def k

    def signup() {
    }

    def createUser(){
        if( params.username && params.password){
            def username = params.username
            def password = springSecurityService.encodePassword(params.password)
            def uri = k.toURI(':'+username)
            def usernameDataType = k[k.toURI('ui:hasUserName')].range
            def passwordDataType = k[k.toURI('ui:hasPassword')].range
            def roleDataType     = k.toURI('ui:Role')
            def user
            def userRole = Role.find{ authority == 'ROLE_USER'}
            println userRole

            if(!k[uri].exist()){
                def node = new Node(k, '')
                def properties = [:]

                properties[k.toURI('ui:hasUserName')] = [value: username, dataType: usernameDataType]
                properties[k.toURI('ui:hasPassword')] = [value: password, dataType: passwordDataType]
                properties[k.toURI('ui:hasRole')]     = [value: k.toURI(':userRole'), dataType: roleDataType]
                node.insertUser(uri, properties)

                user = new User(username, password, true).save()
                UserRole.create user, userRole
            }
        }

        flash.message = 'Usuário cadastrado com sucesso'

        redirect(controller: 'login', action: 'auth')
    }
}
