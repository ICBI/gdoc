import grails.util.GrailsUtil;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.web.taglib.*
import org.codehaus.groovy.grails.plugins.jquery.JQueryConfig
import org.codehaus.groovy.grails.plugins.jquery.JQueryProvider

class JqueryGrailsPlugin {
    def version = "1.0RC2b"
    def dependsOn = [:]
    def author = "Sergey Nebolsin, Craig Jones and Finn Herpich"
    def authorEmail = "nebolsin@gmail.com, craigjones@maximsc.com and finn.herpich@marfinn-software.de"
    def title = "JQuery for Grails"
    def description = "Provides integration JQuery library"
    def documentation = "http://grails.org/JQuery+Plugin"

    def jQueryVersion = "1.3.2"

    def doWithSpring = {
        jQueryConfig(JQueryConfig)
    }

    def doWithApplicationContext = {applicationContext ->
        if(GrailsUtil.environment == GrailsApplication.ENV_DEVELOPMENT) {
            JavascriptTagLib.LIBRARY_MAPPINGS.jquery = ["jquery/jquery-${jQueryVersion}"]
        } else {
            JavascriptTagLib.LIBRARY_MAPPINGS.jquery = ["jquery/jquery-${jQueryVersion}.min"]
        }

        def jQueryConfig = applicationContext.jQueryConfig
        jQueryConfig.init()

        if(jQueryConfig.defaultPlugins) {
            jQueryConfig.defaultPlugins.each { pluginName ->
                jQueryConfig.plugins."$pluginName".each {fileName ->
                    JavascriptTagLib.LIBRARY_MAPPINGS.jquery += ["jquery/${fileName}"[0..-4]]
                }
            }
        }

        JavascriptTagLib.PROVIDER_MAPPINGS.jquery = JQueryProvider.class
    }
}
