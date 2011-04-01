import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.*
import grails.util.GrailsUtil
import org.apache.commons.lang.StringUtils
import grails.converters.*

class BootStrap {
     def init = { servletContext ->
		// Initialize custom json converter
		JSON.registerObjectMarshaller(new ExpressionLookupResultMarshaller())
     }
     def destroy = {
     }
} 