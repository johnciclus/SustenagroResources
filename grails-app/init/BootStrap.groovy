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
        def user

        if(users.getClass() != ArrayList){
            user = new User(users.username, users.password, true).save()
            UserRole.create user, userRole
            UserRole.create user, adminRole
        }
        else{
            users.each{
                user = new User(it.username, it.password, true).save()
                UserRole.create user, userRole
                UserRole.create user, adminRole
            }
        }

        /*
        users.each{
            println it
            user = new User(it.username, it.password).save()
            UserRole.create user, adminRole
        }
        */

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
