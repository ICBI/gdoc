// set per-environment serverURL stem for creating absolute links
environments {
    devserver {
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app", "listener"
		}
    }
    demo {
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app", "listener"
		}
    }
	development {
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app", "listener"
			debug 'org.codehaus.groovy.grails.plugins.springsecurity'
		}
	}
	sandbox {
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app"
		}
	}
	stage {
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
		}
    }
	stage_load {
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