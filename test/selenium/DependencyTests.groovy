import grails.plugins.selenium.SeleniumTest
import org.codehaus.groovy.grails.commons.ConfigurationHolder

@Mixin(SeleniumTest) 
class DependencyTests extends GroovyTestCase { 
	void testGdocDev() { 
		
		selenium.open "https://dev.gdoc.georgetown.edu/gdoc/" 
		assertTrue selenium.isTextPresent("CONTACT US")
		
	} 
	
	void testGdocDemo() { 
		selenium.open "https://demo.gdoc.georgetown.edu/gdoc/" 
		assertTrue selenium.isTextPresent("CONTACT US")
		
	}
	
	void testCatissueDev() { 
		selenium.open "https://devtisu.gdoc.georgetown.edu/catissuecore/RedirectHome.do" 
		assertTrue selenium.isTextPresent("Welcome to caTissue Suite")
		
	}
	
	void testCatissueDemo() { 
		selenium.open "https://demotisu.gdoc.georgetown.edu/catissuecore/RedirectHome.do" 
		assertTrue selenium.isTextPresent("Welcome to caTissue Suite")
		
	}
	
/*	void testGpDemo() { 
		selenium.open "https://democomp.gdoc.georgetown.edu/gp" 
		assertTrue selenium.isTextPresent("Sign-in to GenePattern")
		
	}*/
}