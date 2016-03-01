package dsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.kohsuke.groovy.sandbox.SandboxTransformer
import org.springframework.context.ApplicationContext

/**
 * Created by john on 26/02/16.
 */
class GUIDSL {
    def _shell
    def _sandbox
    def _script
    def _ctx
    def dataTypeToWidget = [:]

    def GUIDSL(String file, ApplicationContext applicationContext){
        // Create CompilerConfiguration and assign
        // the DelegatingScript class as the base script class.
        def _cc = new CompilerConfiguration()
        _cc.addCompilationCustomizers(new SandboxTransformer())
        _cc.setScriptBaseClass(DelegatingScript.class.getName())

        _shell = new GroovyShell(new Binding(), _cc)
        _sandbox = new DSLSandbox()
        _sandbox.register()

        // Configure the GroovyShell and pass the compiler configuration.
        //_shell = new GroovyShell(this.class.classLoader, binding, cc)
        _ctx = applicationContext

        _script = (DelegatingScript) _shell.parse(new File(file).text)
        _script.setDelegate(this)

        // Run DSL script.
        try {
            _script.run()
        }
        finally {
            _sandbox.unregister()
        }
    }

    def reLoad(String code){
        _sandbox.register()

        //def stack = code.tokenize("\n")

        //for (c in stack){
        //    println c + "\n"
        //}

        _script = (DelegatingScript) _shell.parse(code)
        _script.setDelegate(this)

        def response  = [:]

        // Run DSL script.
        try {
            _script.run()
            response.status = 'ok'
        }
        catch(Exception e){
            response.error = [:]
            for (StackTraceElement el : e.getStackTrace()) {
                if(el.getMethodName() == 'run' && el.getFileName() ==~ /Script.+\.groovy/) {
                    response.error.line = el.getLineNumber()
                    response.error.message = e.getMessage()
                    response.error.filename = el.getFileName()
                }
            }
            response.status = 'error'
        }
        finally {
            _sandbox.unregister()
        }
        return response
    }

    def dataType(Map args = [:], String id){
        def k = _ctx.getBean('k')
        dataTypeToWidget[k.toURI(id)] = args['widget']
    }
}
