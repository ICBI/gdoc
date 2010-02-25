// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text-plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]
// The default codec used to encode data with ${}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
grails.converters.encoding="UTF-8"

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

//props file
gdoc.appPropertiesFile="/local/content/gdoc/gdocApp.properties"

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://www.changeme.com"
		jmsserver = "jnp://localhost:1099"
		responseQueue = "AnalysisResponse"
		genePatternUrl = "https://democomp.gdoc.georgetown.edu"
		tempDir = "/opt/gdoc-temp"
		middlewareUrl = "http://localhost/gdoc-middleware"
    }
    devserver {
        grails.serverURL = "http://www.changeme.com"
		jmsserver = "jnp://localhost:1099"
		responseQueue = "AnalysisResponse"
		genePatternUrl = "https://devcomp.gdoc.georgetown.edu"
		tempDir = "/opt/gdoc-temp"
		middlewareUrl = "https://dev.gdoc.georgetown.edu/gdoc-middleware"
		molecule3DstructuresPath = "/opt/gdoc-data/molecule3D/"
		molecule2DstructuresPath = "/opt/gdoc-data/molecule2D/"
    }
    demo {
        grails.serverURL = "http://www.changeme.com"
		jmsserver = "jnp://localhost:1099"
		responseQueue = "AnalysisResponse"
		genePatternUrl = "https://democomp.gdoc.georgetown.edu"
		tempDir = "/opt/gdoc-temp"
		middlewareUrl = "https://demo.gdoc.georgetown.edu/gdoc-middleware"
		molecule3DstructuresPath = "/opt/gdoc-data/molecule3D/"
		molecule2DstructuresPath = "/opt/gdoc-data/molecule2D/"
    }
	development {
	//	jmsserver = "jnp://141.161.30.205:1099"
		jmsserver = "jnp://localhost:1099"
 		responseQueue = "AnalysisResponseKevin"
		genePatternUrl = "https://devcomp.gdoc.georgetown.edu"
		tempDir = "/local/content/gdoc"
		middlewareUrl = "http://localhost:9090/gdoc-middleware"
		molecule3DstructuresPath = "/opt/gdoc-data/molecule3D/"
		molecule2DstructuresPath = "/opt/gdoc-data/molecule2D/"
	}
	test {
		jmsserver = "jnp://localhost:1099"
 		responseQueue = "AnalysisResponseKevin"
		genePatternUrl = "http://141.161.54.201:8080"
		tempDir = "/local/content/gdoc"
		middlewareUrl = "http://localhost:9090/gdoc-middleware"
		structuresPath = "/opt/gdoc-data/"
	}
}

// log4j configuration

log4j = {
	root {
	    error 'stdout'
	    additivity = true
	}
	
	/*
    appender.stdout = "org.apache.log4j.ConsoleAppender"
    appender.'stdout.layout'="org.apache.log4j.PatternLayout"
    appender.'stdout.layout.ConversionPattern'='[%r] %c{2} %m%n'
    appender.stacktraceLog = "org.apache.log4j.FileAppender"
    appender.'stacktraceLog.layout'="org.apache.log4j.PatternLayout"
    appender.'stacktraceLog.layout.ConversionPattern'='[%r] %c{2} %m%n'
    appender.'stacktraceLog.File'="stacktrace.log"
    rootLogger="DEBUG,stdout"
    logger {
        grails="error"
        StackTrace="error,stacktraceLog"
        org {
            codehaus.groovy.grails.web.servlet="error"  //  controllers
            codehaus.groovy.grails.web.pages="error" //  GSP
            codehaus.groovy.grails.web.sitemesh="error" //  layouts
            codehaus.groovy.grails."web.mapping.filter"="error" // URL mapping
            codehaus.groovy.grails."web.mapping"="error" // URL mapping
            codehaus.groovy.grails.commons="info" // core / classloading
            codehaus.groovy.grails.plugins="error" // plugins
            codehaus.groovy.grails.orm.hibernate="error" // hibernate integration
            springframework="off"
        }
    }
    additivity.StackTrace=false
*/
}



// Jquery configuration
jquery.sources="jquery" 
jquery.version="1.3.2" 
