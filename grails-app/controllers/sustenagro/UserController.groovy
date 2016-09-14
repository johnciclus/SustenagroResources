/*
 * Copyright (c) 2015-2016 Dilvan Moreira.
 * Copyright (c) 2015-2016 John Garavito.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package sustenagro

import dsl.GUIDSL
import semantics.Node

class UserController {
    static allowedMethods = [signup: "GET",
                             usernameAvailability: "GET",
                             emailAvailability: "GET",
                             createUser: "POST"]
    def k
    def dsl
    def springSecurityService

    def signup() {
        def _gui = new GUIDSL('dsl/gui.groovy', grailsApplication.mainContext)

        _gui.setView(controllerName, actionName)
        dsl.clean(controllerName, actionName)
        _gui.renderView(actionName)
        render(view: actionName, model: [inputs: _gui.viewsMap[controllerName][actionName]])
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
            properties[k.toURI('ui:hasRole')] = [value: k.toURI('ui:UserRole'), dataType: k.toURI('ui:Role')]

            def username = properties[usernameURI].value
            def uri = k.toURI('inds:'+username)

            if(!k[uri].exist()){
                node.insertUser(username, properties)

                user = new User(username, properties[passwordURI].value, true).save()
                UserRole.create user, userRole
            }

            flash.message = 'Usuário cadastrado com sucesso'
        }
        redirect(controller: 'login', action: 'auth')
    }

    def usernameAvailability(){
        def username = params[k.toURI('ui:hasUsername')]
        render !k['inds:'+username].exist()
    }

    def emailAvailability(){
        def hasEmail = k.toURI('ui:hasEmail')
        def email = params[hasEmail]
        render (k['ui:User'].findSubjectByEmail(email).size() == 0)
    }

    def setLang(){
        if(params.lang && (params.lang=='en' || params.lang == 'pt')){
            session['lang'] = params.lang
            k.setLang(session['lang'])

            java.util.Locale.setDefault(new Locale(session['lang']))

            dsl.evaluationObject.reload()

            dsl.featureMap.eachWithIndex { key, feature, int i ->
                 feature.reload()
            }
        }
        render 'ok'
    }
}
