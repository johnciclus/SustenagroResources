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
                if (role.role == k.toURI(':userRole')) {
                    UserRole.create user, userRole
                }
                if (role.role == k.toURI(':adminRole')) {
                    UserRole.create user, adminRole
                }
            }
        }

        UserRole.withSession {
            it.flush()
            it.clear()
        }

        println "User.count"
        println User.count()
        println "Role.count"
        println Role.count()
        println "UserRole.count"
        println UserRole.count()

    }
    def destroy = {

    }
}
