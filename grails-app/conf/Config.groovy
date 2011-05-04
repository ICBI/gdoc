grails.json.legacy.builder = false
// set per-environment serverURL stem for creating absolute links
// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'GDOCUser'
//grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.gdoc.demo.UserRole'
grails.plugins.springsecurity.authority.className = 'Role'
grails.plugins.springsecurity.successHandler.defaultTargetUrl = "/workflows"

grails.exceptionresolver.params.exclude = ['password']
//grails.plugins.springsecurity.providerNames=['ldapAuthProvider','myAuthenticationProvider','anonymousAuthenticationProvider']

grails.plugins.springsecurity.ldap.context.managerDn = 'uid=gdocdevUIDWebMapping,ou=Specials,dc=georgetown,dc=edu'
grails.plugins.springsecurity.ldap.context.managerPassword = 'gTQ8me5dGjjJ783guu'
grails.plugins.springsecurity.ldap.context.server = 'ldaps://directory.georgetown.edu:636'
grails.plugins.springsecurity.ldap.authorities.groupSearchBase = 'ou=People,dc=georgetown,dc=edu'
grails.plugins.springsecurity.ldap.search.base = 'dc=georgetown,dc=edu'

appTitle="G-DOC"
appLongName="Georgetown Database of Cancer"

environments {
    devserver {
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app", "listener"
			debug 'org.codehaus.groovy.grails.plugins.springsecurity'
		}
		responseQueue = "AnalysisResponse"
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
		mail {
		     host = "smtp.gmail.com"
		     port = 465
		     username = "rossokr@gmail.com"
		     password = "Positano2006!"
		     props = ["mail.smtp.auth":"true", 					   
		              "mail.smtp.socketFactory.port":"465",
		              "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
		              "mail.smtp.socketFactory.fallback":"false"]

		}
		responseQueue = "AnalysisResponseKevin"
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