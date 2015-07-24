package dsl

import com.github.rjeschke.txtmark.Processor
import org.codehaus.groovy.control.CompilerConfiguration
import org.pegdown.PegDownProcessor

/**
 * Created by dilvan on 7/14/15.
 */
class DSL {
    def name = ''
    def description = ''
    def featureLst = []
    def dimensions = []
    Closure program
    def indicator
    def props = [:]

    private def _nameFile = ''
    private Script _script
    private GroovyShell _shell

    DSL(String file){
        // Create CompilerConfiguration and assign
        // the DelegatingScript class as the base script class.
        def compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.scriptBaseClass = DelegatingScript.class.name

        // Configure the GroovyShell and pass the compiler configuration.
        _shell = new GroovyShell(this.class.classLoader, new Binding(), compilerConfiguration)

        _nameFile = file

        _script = _shell.parse(new File(_nameFile).text)
        _script.setDelegate(this)

        // Run DSL script.
        _script.run()
    }

    def reLoad(){

        _script = _shell.parse(new File(_nameFile).text)
        _script.setDelegate(this)

        // Run DSL script.
        _script.run()
    }

    def name(String nameStr){
        name = nameStr
    }

    def description(String nameStr){
        description = nameStr
        //println  Processor.process(description, true)
        //println new PegDownProcessor().markdownToHtml(description)
    }

    def features(Closure closure){
        featureLst = []
        closure()
    }

    def instance(String str){
        featureLst << ['a', str]
    }

    def subclass(String str){
        featureLst << ['rdfs:subClassOf', str]
    }

    def dimension(String cls) {
        dimensions << cls
    }

    def prog(Closure c){
        program = c
    }

    def propertyMissing(String name, arg) {
        props[name] = arg
    }

    def propertyMissing(String name) {
        props[name]
    }
}
