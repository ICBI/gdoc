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
    devserver {
        grails.serverURL = "https://dev.gdoc.georgetown.edu"
		jmsserver = "jnp://localhost:1099"
		responseQueue = "AnalysisResponse"
		genePatternUrl = "https://devcomp.gdoc.georgetown.edu"
		tempDir = "/opt/gdoc-temp"
		middlewareUrl = "https://dev.gdoc.georgetown.edu/gdoc-middleware"
		molecule3DstructuresPath = "https://dev.gdoc.georgetown.edu/content/targets/molecule3D"
		molecule2DstructuresPath = "https://dev.gdoc.georgetown.edu/content/targets/molecule2D"
		documentsPath = "https://dev.gdoc.georgetown.edu/content/documents"
		videosPath = "https://dev.gdoc.georgetown.edu/content/video"
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app", "listener"
		}
    }
    demo {
        grails.serverURL = "https://demo.gdoc.georgetown.edu"
		jmsserver = "jnp://localhost:1099"
		responseQueue = "AnalysisResponse"
		genePatternUrl = "https://democomp.gdoc.georgetown.edu"
		tempDir = "/opt/gdoc-temp"
		middlewareUrl = "https://demo.gdoc.georgetown.edu/gdoc-middleware"
		molecule3DstructuresPath = "https://demo.gdoc.georgetown.edu/content/targets/molecule3D"
		molecule2DstructuresPath = "https://demo.gdoc.georgetown.edu/content/targets/molecule2D"
		documentsPath = "https://demo.gdoc.georgetown.edu/content/documents"
		videosPath = "https://demo.gdoc.georgetown.edu/content/video"
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app", "listener"
		}
		grails {
		   mail {
		     host = "nitrogen.uis.georgetown.edu"
		     port = 25
		   }
		}
    }
	development {
		grails.serverURL = "http://localhost:8080"
	//	jmsserver = "jnp://141.161.30.205:1099"
		jmsserver = "jnp://localhost:1099"
 		responseQueue = "AnalysisResponseKevin"
		genePatternUrl = "https://devcomp.gdoc.georgetown.edu"
		tempDir = "/local/content/gdoc"
		middlewareUrl = "http://localhost:9090/gdoc-middleware"
		molecule3DstructuresPath = "https://demo.gdoc.georgetown.edu/content/targets/molecule3D"
		molecule2DstructuresPath = "https://demo.gdoc.georgetown.edu/content/targets/molecule2D"
		documentsPath = "https://demo.gdoc.georgetown.edu/content/documents"
		videosPath = "https://demo.gdoc.georgetown.edu/content/video"
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app", "listener"
		}
	}
	sandbox {
		jmsserver = "jnp://localhost:1099"
 		responseQueue = "AnalysisResponseKevin"
		genePatternUrl = "https://devcomp.gdoc.georgetown.edu"
		tempDir = "/local/content/gdoc"
		middlewareUrl = "http://localhost:9090/gdoc-middleware"
		molecule3DstructuresPath = "https://demo.gdoc.georgetown.edu/content/targets/molecule3D"
		molecule2DstructuresPath = "https://demo.gdoc.georgetown.edu/content/targets/molecule2D"
		documentsPath = "https://demo.gdoc.georgetown.edu/content/documents"
		videosPath = "https://demo.gdoc.georgetown.edu/content/video"
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app"
		}
	}
	stage {
        grails.serverURL = "https://gdoc-stage.georgetown.edu"
		jmsserver = "jnp://gdoc-stage.georgetown.edu:1099"
		responseQueue = "AnalysisResponse"
		//genePatternUrl = "https://democomp.gdoc.georgetown.edu"
		tempDir = "/opt/gdoc-temp"
		middlewareUrl = "https://gdoc-stage.georgetown.edu/gdoc-middleware"
		molecule3DstructuresPath = "https://gdoc-stage.georgetown.edu/content/targets/molecule3D"
		molecule2DstructuresPath = "https://gdoc-stage.georgetown.edu/content/targets/molecule2D"
		documentsPath = "https://gdoc-stage.georgetown.edu/content/documents"
		videosPath = "https://gdoc-stage.georgetown.edu/content/video"
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
		}
		grails {
		   mail {
		     host = "nitrogen.uis.georgetown.edu"
		     port = 25
		   }
		}
    }
	stage_load {
        grails.serverURL = "https://gdoc-stage.georgetown.edu"
		jmsserver = "jnp://gdoc-stage.georgetown.edu:1099"
		responseQueue = "AnalysisResponse"
		//genePatternUrl = "https://democomp.gdoc.georgetown.edu"
		tempDir = "/opt/gdoc-temp"
		middlewareUrl = "https://gdoc-stage.georgetown.edu/gdoc-middleware"
		molecule3DstructuresPath = "https://gdoc-stage.georgetown.edu/content/targets/molecule3D"
		molecule2DstructuresPath = "https://gdoc-stage.georgetown.edu/content/targets/molecule2D"
		documentsPath = "https://gdoc-stage.georgetown.edu/content/documents"
		videosPath = "https://gdoc-stage.georgetown.edu/content/video"
		log4j = {
			appenders {
				file name:'file', file:'dataLoad.log', append: true
			}
			root {
			    error 'stdout', 'file'
			    additivity = true
			}
			debug "grails.app"
			debug "org.hibernate.SQL"
		}
    }
	production {
        grails.serverURL = "https://gdoc.georgetown.edu"
		jmsserver = "jnp://gdoc.georgetown.edu:1099"
		responseQueue = "AnalysisResponse"
		//genePatternUrl = "https://democomp.gdoc.georgetown.edu"
		tempDir = "/opt/gdoc-temp"
		middlewareUrl = "https://gdoc.georgetown.edu/gdoc-middleware"
		molecule3DstructuresPath = "https://gdoc.georgetown.edu/content/targets/molecule3D"
		molecule2DstructuresPath = "https://gdoc.georgetown.edu/content/targets/molecule2D"
		documentsPath = "https://gdoc.georgetown.edu/content/documents"
		videosPath = "https://gdoc.georgetown.edu/content/video"
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
		}
		grails {
		   mail {
		     host = "neon.uis.georgetown.edu"
		     port = 25
		   }
		}
    }
	prod_load {
        grails.serverURL = "https://gdoc.georgetown.edu"
		jmsserver = "jnp://gdoc.georgetown.edu:1099"
		responseQueue = "AnalysisResponse"
		//genePatternUrl = "https://democomp.gdoc.georgetown.edu"
		tempDir = "/opt/gdoc-temp"
		middlewareUrl = "https://neon.uis.georgetown.edu/gdoc-middleware"
		molecule3DstructuresPath = "https://gdoc.georgetown.edu/content/targets/molecule3D"
		molecule2DstructuresPath = "https://gdoc.georgetown.edu/content/targets/molecule2D"
		documentsPath = "https://gdoc.georgetown.edu/content/documents"
		videosPath = "https://gdoc.georgetown.edu/content/video"
		log4j = {
			appenders {
				file name:'file', file:'dataLoad.log', append: true
			}
			root {
			    error 'stdout', 'file'
			    additivity = true
			}
			debug "grails.app"
			debug "org.hibernate.SQL"
		}
    }
	test {
		jmsserver = "jnp://localhost:1099"
 		responseQueue = "AnalysisResponseKevin"
		genePatternUrl = "https://democomp.gdoc.georgetown.edu"
		tempDir = "/local/content/gdoc"
		middlewareUrl = "http://localhost:9090/gdoc-middleware"
		structuresPath = "/opt/gdoc-data/"
	}
}

// log4j configuration

/*log4j = {
	root {
	    error 'stdout'
	    additivity = true
	}
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
}*/



// Jquery configuration
jquery.sources="jquery" 
jquery.version="1.3.2" 
