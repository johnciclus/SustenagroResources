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

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class UserRole implements Serializable {

    private static final long serialVersionUID = 1

    User user
    Role role

    UserRole(User u, Role r) {
        this()
        user = u
        role = r
    }

    @Override
    boolean equals(other) {
        if (!(other instanceof UserRole)) {
            return false
        }

        other.user?.id == user?.id && other.role?.id == role?.id
    }

    @Override
    int hashCode() {
        def builder = new HashCodeBuilder()
        if (user) builder.append(user.id)
        if (role) builder.append(role.id)
        builder.toHashCode()
    }

    static UserRole get(long userId, long roleId) {
        criteriaFor(userId, roleId).get()
    }

    static boolean exists(long userId, long roleId) {
        criteriaFor(userId, roleId).count()
    }

    private static DetachedCriteria criteriaFor(long userId, long roleId) {
        UserRole.where {
            user == User.load(userId) && role == Role.load(roleId)
        }
    }

    static UserRole create(User user, Role role, boolean flush = false) {
        def instance = new UserRole(user: user, role: role)
        instance.save(flush: flush, insert: true)
        instance
    }

    static boolean remove(User u, Role r, boolean flush = false) {
        if (u == null || r == null) return false

        int rowCount = UserRole.where { user == u && role == r }.deleteAll()

        if (flush) { UserRole.withSession { it.flush() } }

        rowCount
    }

    static void removeAll(User u, boolean flush = false) {
        if (u == null) return

        UserRole.where { user == u }.deleteAll()

        if (flush) { UserRole.withSession { it.flush() } }
    }

    static void removeAll(Role r, boolean flush = false) {
        if (r == null) return

        UserRole.where { role == r }.deleteAll()

        if (flush) { UserRole.withSession { it.flush() } }
    }

    static constraints = {
        role validator: { Role r, UserRole ur ->
            if (ur.user == null || ur.user.id == null) return
            boolean existing = false
            UserRole.withNewSession {
                existing = UserRole.exists(ur.user.id, r.id)
            }
            if (existing) {
                return 'userRole.exists'
            }
        }
    }

    static mapping = {
        id composite: ['user', 'role']
        version false
    }
}