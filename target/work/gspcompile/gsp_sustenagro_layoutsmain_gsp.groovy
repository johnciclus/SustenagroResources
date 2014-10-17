import org.codehaus.groovy.grails.plugins.metadata.GrailsPlugin
import org.codehaus.groovy.grails.web.pages.GroovyPage
import org.codehaus.groovy.grails.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

class gsp_sustenagro_layoutsmain_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/grails-app/views/layouts/main.gsp" }
public Object run() {
Writer out = getOut()
Writer expressionOut = getExpressionOut()
registerSitemeshPreprocessMode()
printHtmlPart(0)
createTagBody(1, {->
printHtmlPart(1)
invokeTag('captureMeta','sitemesh',4,['gsp_sm_xmlClosingForEmptyTag':(""),'charset':("utf-8")],-1)
printHtmlPart(2)
invokeTag('captureMeta','sitemesh',5,['gsp_sm_xmlClosingForEmptyTag':(""),'http-equiv':("X-UA-Compatible"),'content':("IE=edge")],-1)
printHtmlPart(2)
invokeTag('captureMeta','sitemesh',6,['gsp_sm_xmlClosingForEmptyTag':(""),'name':("viewport"),'content':("width=device-width, initial-scale=1")],-1)
printHtmlPart(2)
invokeTag('captureMeta','sitemesh',7,['gsp_sm_xmlClosingForEmptyTag':(""),'name':("description"),'content':("Platform to support decision making on sustainability in agriculture")],-1)
printHtmlPart(2)
invokeTag('captureMeta','sitemesh',8,['gsp_sm_xmlClosingForEmptyTag':(""),'name':("author"),'content':("John Gararavito Suárez")],-1)
printHtmlPart(3)
expressionOut.print(createLinkTo(dir:'images',file:'favicon.ico'))
printHtmlPart(4)
createTagBody(2, {->
createTagBody(3, {->
invokeTag('layoutTitle','g',11,['default':("SustenAgro")],-1)
})
invokeTag('captureTitle','sitemesh',11,[:],3)
})
invokeTag('wrapTitleTag','sitemesh',11,[:],2)
printHtmlPart(5)
invokeTag('stylesheet','asset',13,['href':("bootstrap-readable.min.css")],-1)
printHtmlPart(1)
invokeTag('stylesheet','asset',14,['href':("style.css")],-1)
printHtmlPart(1)
invokeTag('javascript','asset',15,['src':("jquery-1.11.1.min.js")],-1)
printHtmlPart(1)
invokeTag('javascript','asset',16,['src':("bootstrap.min.js")],-1)
printHtmlPart(5)
invokeTag('layoutHead','g',18,[:],-1)
printHtmlPart(6)
})
invokeTag('captureHead','sitemesh',26,[:],1)
printHtmlPart(7)
createTagBody(1, {->
printHtmlPart(8)
invokeTag('img','g',40,['dir':("images"),'file':("logo.png"),'class':("logo")],-1)
printHtmlPart(9)
if(true && (controllerName == null)) {
printHtmlPart(10)
}
printHtmlPart(11)
if(true && (controllerName == 'tool')) {
printHtmlPart(10)
}
printHtmlPart(12)
if(true && (controllerName == 'tool')) {
printHtmlPart(10)
}
printHtmlPart(13)
if(true && (controllerName == 'tool' && actionName == 'index')) {
printHtmlPart(10)
}
printHtmlPart(14)
if(true && (controllerName == 'tool' && actionName == 'location')) {
printHtmlPart(15)
}
printHtmlPart(16)
if(true && (controllerName == 'tool' && actionName == 'crop')) {
printHtmlPart(17)
}
printHtmlPart(18)
if(true && (controllerName == 'tool' && actionName == 'technology')) {
printHtmlPart(19)
}
printHtmlPart(20)
if(true && (controllerName == 'tool' && actionName == 'characterization')) {
printHtmlPart(10)
}
printHtmlPart(21)
if(true && (controllerName == 'tool' && actionName == 'indicators')) {
printHtmlPart(19)
}
printHtmlPart(22)
if(true && (controllerName == 'tool' && actionName == 'recomendations')) {
printHtmlPart(23)
}
printHtmlPart(24)
if(true && (controllerName == 'contact')) {
printHtmlPart(10)
}
printHtmlPart(25)
invokeTag('layoutBody','g',72,[:],-1)
printHtmlPart(26)
invokeTag('img','g',76,['dir':("images"),'file':("embrapa.jpg"),'class':("img-centered"),'width':("150")],-1)
printHtmlPart(27)
invokeTag('img','g',79,['dir':("images"),'file':("icmc.png"),'class':("img-centered"),'width':("150")],-1)
printHtmlPart(27)
invokeTag('img','g',82,['dir':("images"),'file':("ufscar.png"),'class':("img-centered"),'width':("150")],-1)
printHtmlPart(28)
})
invokeTag('captureBody','sitemesh',87,[:],1)
printHtmlPart(29)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1413548990000L
public static final String EXPRESSION_CODEC = 'html'
public static final String STATIC_CODEC = 'none'
public static final String OUT_CODEC = 'html'
public static final String TAGLIB_CODEC = 'none'
}
