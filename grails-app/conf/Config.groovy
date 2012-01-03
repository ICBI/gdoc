grails.config.defaults.locations=["classpath:GcoreConfig.groovy", "file:${basedir}/grails-app/conf/GcoreConfig.groovy"]

grails.json.legacy.builder = false


appTitle="G-DOC &reg;"
appLongName="Georgetown Database of Cancer"
appVersion="1.4.2"
appLogo="gdocHeader.png"
appContactEmail="gdoc-help@georgetown.edu"

environments {
    devserver {
		grails.serverURL = "https://dev.gdoc.georgetown.edu"
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app", "listener"
			debug 'org.codehaus.groovy.grails.plugins.springsecurity'
		}
    }
    demo {
		grails.serverURL = "https://demo.gdoc.georgetown.edu"
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
			debug "grails.app", "listener"
		}
    }
	development {
		grails.serverURL = "http://localhost:8080"
		responseQueue = "AnalysisResponseKevin"
		//molecule3DstructuresPath = "https://demo.gdoc.georgetown.edu/content/targets/molecule3D"
		//molecule2DstructuresPath = "https://demo.gdoc.georgetown.edu/content/targets/molecule2D"
		//documentsPath = "https://demo.gdoc.georgetown.edu/content/documents"
		//videosPath = "https://demo.gdoc.georgetown.edu/content/video"
		log4j = {
			appenders {
				file name:'file', file:'dataLoad.log', append: true
			}
			root {
			    error 'stdout', 'file'
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
		grails.serverURL = "https://gdoc-stage.georgetown.edu"
		jmsserver = "jnp://gdoc-stage.georgetown.edu:1099"
		grails {
		   mail {
		     host = "localhost"
		     port = 25
		   }
		}
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
		}
    }
	stage_load {
		grails.serverURL = "https://gdoc-stage.georgetown.edu"
		jmsserver = "jnp://gdoc-stage.georgetown.edu:1099"
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
		grails {
		   mail {
		     host = "localhost"
		     port = 25
		   }
		}
		log4j = {
			root {
			    error 'stdout'
			    additivity = true
			}
		}
    }
	prod_load {
		grails.serverURL = "https://gdoc.georgetown.edu"
		jmsserver = "jnp://gdoc.georgetown.edu:1099"
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
