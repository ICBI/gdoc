// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.naming.entries = 0 
grails.json.legacy.builder = false
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

appTitle="G-CORE"
appLongName="GCORE"
appVersion="1.0"
appLogo="gcodeLogo.png"
appContactEmail="gdoc-help@georgetown.edu"
jmsserver = "jnp://localhost:1099"
responseQueue = "AnalysisResponse"
grails {
	mail {
		host = "localhost"
		port = 25
	}
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'GDOCUser'
//grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.gdoc.demo.UserRole'
grails.plugins.springsecurity.authority.className = 'Role'
grails.plugins.springsecurity.successHandler.defaultTargetUrl = "/workflows"
grails.plugins.springsecurity.successHandler.alwaysUseDefault = false
grails.plugins.springsecurity.successHandler.targetUrlParameter = "desiredPage"

grails.exceptionresolver.params.exclude = ['password']
//grails.plugins.springsecurity.providerNames=['ldapAuthProvider','myAuthenticationProvider','anonymousAuthenticationProvider']

grails.plugins.springsecurity.ldap.context.managerDn = 'uid=gdocdevUIDWebMapping,ou=Specials,dc=georgetown,dc=edu'
grails.plugins.springsecurity.ldap.context.managerPassword = 'gTQ8me5dGjjJ783guu'
grails.plugins.springsecurity.ldap.context.server = 'ldaps://directory.georgetown.edu:636'
grails.plugins.springsecurity.ldap.authorities.groupSearchBase = 'ou=People,dc=georgetown,dc=edu'
grails.plugins.springsecurity.ldap.search.base = 'dc=georgetown,dc=edu'

grails.plugins.springsecurity.errors.login.disabled = "Sorry, your account is disabled."
grails.plugins.springsecurity.errors.login.expired = "Sorry, your account has expired."
grails.plugins.springsecurity.errors.login.passwordExpired = "Sorry, your password has expired."
grails.plugins.springsecurity.errors.login.locked = "Sorry, your account is locked."
grails.plugins.springsecurity.errors.login.fail = "Sorry, we were unable to find a user with that username and password."

// GCORE Configuration URLs (Must go after env definitions due to templating)
molecule3DstructuresPath = "/content/targets/molecule3D"
molecule2DstructuresPath = "/content/targets/molecule2D"
documentsPath = "/content/documents"
videosPath = "/content/video"

// set per-environment serverURL stem for creating absolute links
environments {
}




