import org.springframework.mock.web.MockServletContext
import org.springframework.mock.jndi.SimpleNamingContextBuilder

class WorkflowsControllerTests extends grails.test.ControllerUnitTestCase {
	def securityService
	def savedAnalysisService
	def userListService
	def middlewareService
	def quickStartService
	def cleanupService
	def invitationService
	def jdbcTemplate
	def testDataSource
	def session
	def params
	
	void setUp(){
		SimpleNamingContextBuilder builder =
			SimpleNamingContextBuilder.emptyActivatedContextBuilder()

		builder.bind("java:/gdoc", testDataSource);
		def servletContext = new MockServletContext()
		session = [ : ]
		WorkflowsController.metaClass.getSession = { -> session }
		params = [ : ]
		WorkflowsController.metaClass.getParams = { -> params }
		
	}
	/**void testNothing() {
		
	}**/
	
	void testUserProfileLoad(){
		session.userId = "kmr75"
		WorkflowsController controller = new WorkflowsController()
		controller.quickStartService = quickStartService
		controller.securityService = securityService
		controller.savedAnalysisService = savedAnalysisService
		controller.userListService = userListService
		controller.middlewareService = middlewareService
		controller.cleanupService = cleanupService
		controller.invitationService = invitationService
		controller.index()
	}

	
}