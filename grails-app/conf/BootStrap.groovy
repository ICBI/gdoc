import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.*
import grails.util.GrailsUtil
import org.apache.commons.lang.StringUtils

class BootStrap {
	def quickStartService
	
     def init = { servletContext ->
		
		// Setup metaclass methods for string 
		String.metaClass.decamelize = {
			def displayValue = StringUtils.capitalize(delegate)
			displayValue = displayValue.replaceAll(/([^A-Z])([A-Z])/, '$1 $2').trim()
			return displayValue
		}
		
     }
     def destroy = {
     }
} 