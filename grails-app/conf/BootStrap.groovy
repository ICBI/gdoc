import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.*
import grails.util.GrailsUtil
import org.apache.commons.lang.StringUtils

class BootStrap {
	def quickStartService
	
     def init = { servletContext ->
		
		switch (GrailsUtil.environment) {
	       case ["development","sandbox"]:
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
	           break;
			case "test":
				break;	
	   	}
		
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