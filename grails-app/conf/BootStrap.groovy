import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext

class BootStrap {

     def init = { servletContext ->
	//	new GDOCUser(username:'gdocUser').save();
	
		// Initialize annotation service
	 	InputStream stream = servletContext.getResourceAsStream("/WEB-INF/data/Affy_U133Plus2_reporter_symbol_entrezid.tab")
		def annotations = [:]
		stream.eachLine { line ->
			if(line[0] != '#') {
				def data = line.split('\t')
				annotations[data[0]] = data[1]
			}
		}
		ApplicationContext ctx = servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
		def annotationService = ctx.getBean("fileBasedAnnotationService")
		annotationService.annotations = annotations
     }
     def destroy = {
     }
} 