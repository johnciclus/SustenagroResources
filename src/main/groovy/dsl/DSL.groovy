package dsl

/**
 * Created by dilvan on 7/14/15.
 */
class DSL {
    DSL(String file){
        def script = new File(file).text

        new GroovyShell().evaluate(script)
    }
}
