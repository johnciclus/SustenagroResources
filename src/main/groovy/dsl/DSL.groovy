package dsl

import com.github.rjeschke.txtmark.Processor
import org.codehaus.groovy.control.CompilerConfiguration
import org.pegdown.PegDownProcessor
import rdfSlurper.RDFSlurper

/**
 * Created by dilvan on 7/14/15.
 */
class DSL {
    def name = ''
    def description = ''
    def featureLst = []
    def dimensions = []
    def report = []
    Closure program
    def data
    def props = [:]

    private def _nameFile = ''
    private Script _script
    private GroovyShell _shell
    private static md = new PegDownProcessor()

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

    def data(String name){
        data = name
        props[name]
    }

    def setData(obj){
        props[data]= obj
    }

    def description(String nameStr){
        description = toHTML(nameStr)
        //println  Processor.process(description, true)
        //println new PegDownProcessor().markdownToHtml(description)
    }

    def features(Closure closure){
        featureLst = []
        closure()
    }

//    def recommendation(Map map, String txt){
//        recommendations << [map['if'],txt]
//    }

    static toHTML(String txt) {md.markdownToHtml(txt)}

    def show(String txt){
        report << ['show', toHTML(txt)]
    }

    def recommendation(String txt){
        report << ['recommendation', toHTML(txt)]
    }

    def recommendation(boolean c, String txt){
        if (c) report << ['recommendation', toHTML(txt)]
    }

    def recommendation(Map map){
        if (map.if) report << ['recommendation', toHTML(map.show)]
    }

    def recommendation(Map map, String txt){
        if (map['if']) report << ['recommendation', toHTML(txt)]
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

    def _cleanProgram(){
        report = []
    }

    def propertyMissing(String name, arg) {
        props[name] = arg
    }

    def propertyMissing(String name) {
        props[name]
    }
}



