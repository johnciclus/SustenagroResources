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

import sustenagro.User
import sustenagro.Role
import sustenagro.UserRole
import semantics.Node

class BootStrap {
    def k
    def init = { servletContext ->
        def adminRole = new Role('ROLE_ADMIN').save()
        def userRole = new Role('ROLE_USER').save()
        def users = new Node(k).getUsers()
        def roles
        def user

        users.each {
            user = new User(it.username, it.password, true).save()
            roles = k[it.user].getRoles()

            roles.each { role ->
                if (role.role == k.toURI('ui:UserRole')) {
                    UserRole.create user, userRole
                }
                if (role.role == k.toURI('ui:AdminRole')) {
                    UserRole.create user, adminRole
                }
            }
        }

        UserRole.withSession {
            it.flush()
            it.clear()
        }
        /*
        println "User.count"
        println User.count()
        println "Role.count"
        println Role.count()
        println "UserRole.count"
        println UserRole.count()
        */
    }
    def destroy = {

    }
}
