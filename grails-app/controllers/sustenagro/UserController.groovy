package sustenagro

class UserController {
    def signup() {

    }

    def createUser(){
        println params

        if(params.username && params.password){
            println "create new user"
        }
        render 'ok'
    }
}
