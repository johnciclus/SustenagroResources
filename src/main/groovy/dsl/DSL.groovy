package dsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.pegdown.PegDownProcessor
import org.kohsuke.groovy.sandbox.SandboxTransformer
import semantics.Know

/**
 * Created by dilvan on 7/14/15.
 */

class DSL {
    def viewsStack = [:]
    def featureLst = []
    def dimensions = []
    def report = []
    Closure program
    def data
    def props = [:]

    def toolAssessmentStack = []

    def _shell
    def _sandbox
    def _script
    def _k
    Know k
    static md = new PegDownProcessor()

    DSL(String file, Know k){
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

        _k = k

        _script = (DelegatingScript) _shell.parse(new File(file).text)
        _script.setDelegate(this)
        viewsStack['tool'] = [:]
        viewsStack['tool']['index'] = []

        // Run DSL script.
        try {
            _k.DSL['dsl'] = this
            _script.run()
        }
        finally {
            _sandbox.unregister()
        }
    }

    def reLoad(String code){
        dimensions = []
        featureLst = []
        report = []

        viewsStack = [:]
        viewsStack['tool'] = [:]
        viewsStack['tool']['index'] = []
        toolAssessmentStack = []

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

    def title(String str){
        viewsStack['tool']['index'].push(['widget': 'title', 'args': ['title': str]])
    }

    def description(String str){
        viewsStack['tool']['index'].push(['widget': 'description', 'args': ['description': toHTML(str)]])

        //println  Processor.process(description, true)
        //println new PegDownProcessor().markdownToHtml(description)
    }

    def data(String str){
        data = str
        props[str]
    }

    def setData(obj){
        props[data]= obj
    }

    def unity(String clsName, Closure closure){
        def requestLst          = [:]
        requestLst['widgets']   = [:]
        def argLst              = [:]
        argLst['widgets']       = [:]
        featureLst              = []

        def unity = new Unity(clsName, _k)

        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = unity

        featureLst << closure()

        featureLst.each{
            if(it.request) {
                requestLst['widgets'][it.id] = it.request
            }
            argLst['widgets'][it.id] = ['widget': it.widget, 'args': it.args]
        }

        argLst['entity'] = clsName

        viewsStack['tool']['index'].push(['widget': 'selectEntity', 'request': ['production_units': ['a', clsName]], 'args': ['entity': clsName]])
        viewsStack['tool']['index'].push(['widget': 'createEntity', 'request': requestLst, 'args': argLst])
    }
//    def recommendation(Map map, String txt){
//        recommendations << [map['if'],txt]
//    }

    static toHTML(String txt) {md.markdownToHtml(txt)}

    def show(String txt){
        report << ['show', toHTML(txt)]

    }

    def linebreak(){
        report << ['linebreak']
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

    /*def subclass(String str){
        featureLst << ['subclass': ['rdfs:subClassOf', str]]
    }*/

    def table(ArrayList list, Map headers = [:]){
        report << ['table', list, headers]
    }

    def matrix(Map map){
        report << ['matrix', map.x, map.y, map.labelX, map.labelY, map.rangeX, map.rangeY, map.quadrants, map.recomendations]
    }

    def map(String url){
        report << ['map', url]
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

    def propertyMissing(String str, arg) {
        props[str] = arg
    }

    def propertyMissing(String str) {
        props[str]
    }

    def sum(obj){
        float val = 0
        float value
        //println obj

        if(obj instanceof ArrayList) {
            obj.each {
                value = (it.value.getClass() == Boolean ? (it.value ? 1 : -1) : it.value)
                val += (float) value
            }
            return val
        }
        else if(obj instanceof Boolean){
            return (obj ? 1.0 : -1.0)
        }
        else if(obj instanceof Double || obj instanceof Integer){
            return obj
        }
    }

    def weightedSum(obj){
        float val = 0
        float value
        float weight

        if(obj instanceof ArrayList) {
            obj.each {
                value = (it.value.getClass() == Boolean ? (it.value ? 1 : -1) : it.value)
                weight = (it.weight.getClass() == Boolean ? (it.weight ? 1 : -1) : it.weight)
                val += (float) value*weight
            }
        }

        return val
    }

    def average(obj){
        float val = 0
        float num
        //println obj

        if(obj instanceof ArrayList){
            obj.each{
                num = (it.getClass() == Boolean ? (it ? 1 : -1) : it)
                val += (float) num
            }
            return val/obj.size()
        }
        else if(obj instanceof Boolean){
            return (obj ? 1.0 : -1.0)
        }
        else if(obj instanceof Double || obj instanceof Integer){
            return obj
        }
    }
}