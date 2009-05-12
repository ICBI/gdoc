import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.*

class BootStrap {

     def init = { servletContext ->
		//load the properties file and put props into system properties
     	//Load the the application properties and set them as system properties
  		def config = ConfigurationHolder.config
		Properties gdocProperties = new Properties();
		
		FileInputStream inputStream

  		inputStream = new FileInputStream(config.gdoc.appPropertiesFile);
  		gdocProperties.load(inputStream);

  		if (gdocProperties.isEmpty()) {
  		   log.error("Error: no properties found when loading properties file: " + config.gdoc.appPropertiesFile);
  		}

  		String key = null;
  		String val = null;
  		for (Iterator i = gdocProperties.keySet().iterator(); i.hasNext(); ) {
  		  key = (String) i.next();
  		  val = gdocProperties.getProperty(key);
  		  System.setProperty(key, val);
  		}
	
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
		
		//Initialize sample ids
		stream = servletContext.getResourceAsStream("/WEB-INF/data/SampleIds.txt")
		def sampleIds = []
	 	stream.eachLine { line ->
			sampleIds = line.split('\t')
		}
		
		def idService = ctx.getBean("idService")
		idService.binaryFileIds = sampleIds.toList()
     }
     def destroy = {
     }
} 