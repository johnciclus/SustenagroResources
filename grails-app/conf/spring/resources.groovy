import com.github.slugify.Slugify
import org.pegdown.PegDownProcessor
import semantics.Know
import dsl.DSL
import dsl.GUIDSL
import org.springframework.web.servlet.i18n.SessionLocaleResolver

beans = {
    localeResolver(SessionLocaleResolver) {
        defaultLocale = new Locale("pt")
        java.util.Locale.setDefault(defaultLocale)
    }
    slugify(Slugify)
    md(PegDownProcessor)
    k(Know, 'http://127.0.0.1:9999/blazegraph/namespace/kb/sparql')
    gui(GUIDSL, 'dsl/gui.groovy', grailsApplication.mainContext)
    dsl(DSL, 'dsl/main.groovy', grailsApplication.mainContext)
}
