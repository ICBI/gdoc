grails.config.defaults.locations=["classpath:GcoreConfig.groovy", "file:${basedir}/grails-app/conf/GcoreConfig.groovy"]

grails.json.legacy.builder = false


appTitle="G-DOC &reg;"
appLongName="Georgetown Database of Cancer"
appVersion="3.0"
appLogo="gdocHeader.png"
appContactEmail="gdoc-help@georgetown.edu"
grails.newsFeedURL="https://wiki.uis.georgetown.edu/createrssfeed.action?types=blogpost&spaces=informatics&title=UIS+Wiki+RSS+Feedddd&labelString=news&excludedSpaceKeys%3D&sort=modified&maxResults=10&timeSpan=365&confirm=Create+RSS+Feed&showContent=false&showDiff=false"
grails.pubFeedURL="https://wiki.uis.georgetown.edu/createrssfeed.action?types=blogpost&spaces=informatics&title=UIS+Wiki+RSS+Feedddd&labelString=pub&excludedSpaceKeys%3D&sort=modified&maxResults=10&timeSpan=365&confirm=Create+RSS+Feed&showContent=false&showDiff=false"


environments {
	devserver {
		grails.serverURL = "https://dev.gdoc.georgetown.edu/gdoc"
		grails.ngsUrl = "http://devcomp.gdoc.georgetown.edu:8000"

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
		grails.serverURL = "https://demo.gdoc.georgetown.edu/gdoc"
		grails.ngsUrl = "http://democomp.gdoc.georgetown.edu:8000"

		log4j = {
			root {
				error 'stdout'
				additivity = true
			}
			debug "grails.app", "listener"
		}
	}
	development {
		grails.serverURL = "http://10.191.20.160:8080/gdoc"
		responseQueue = "AnalysisResponse"
		grails.ngsUrl = "http://sodium.uis.georgetown.edu"
		//grails.ngsUrl = "http://devcomp.gdoc.georgetown.edu:8000"
		jmsserver = "jnp://localhost:1099"
		//grails.ngsUrl = "http://oxygen.uis.georgetown.edu"
		/*grails.serverURL = "http://10.191.20.160:8080/gdoc"
		responseQueue = "AnalysisResponseKevin"
		grails.ngsUrl = "http://devcomp.gdoc.georgetown.edu:8000"*/
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
			debug "grails.plugin.searchable"
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
		grails.serverURL = "https://gdoc-stage.georgetown.edu/gdoc"
		jmsserver = "jnp://gdoc-stage.georgetown.edu:1099"
		grails.ngsUrl = "http://oxygen.uis.georgetown.edu"
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
		grails.serverURL = "https://gdoc-stage.georgetown.edu/gdoc"
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
		grails.serverURL = "https://gdoc.georgetown.edu/gdoc"
		jmsserver = "jnp://gdoc.georgetown.edu:1099"
		grails.ngsUrl = "http://sodium.uis.georgetown.edu"
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
		grails.serverURL = "https://gdoc.georgetown.edu/gdoc"
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
	dev_load {
		grails.serverURL = "http://localhost:8080/gdoc"
		responseQueue = "AnalysisResponseKevin"
		grails.ngsUrl = "http://localhost:8000"
/*		log4j = {
			appenders {
				file name:'file', file:'dataLoad.log', append: true
			}
			root {
			    error 'stdout', 'file'
			    additivity = true
			}
			debug "grails.app", "listener"
			debug 'org.codehaus.groovy.grails.plugins.springsecurity'
			debug "org.hibernate.SQL"
		}*/
	}
	test {
	}
}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
