import org.springframework.mock.jndi.SimpleNamingContextBuilder

class SecurityServiceTests extends GroovyTestCase {
	
	def securityService
	def jdbcTemplate
	def testDataSource
	
	void setUp() {
		SimpleNamingContextBuilder builder =
			SimpleNamingContextBuilder.emptyActivatedContextBuilder()

		builder.bind("java:/gdoc", testDataSource);
		securityService.jdbcTemplate = jdbcTemplate
	}
	
	void testCreateAndDeleteCollaborationGroup() {
		def protectionGroup = securityService.createCollaborationGroup("acs224", "test group")
		assertTrue(protectionGroup.protectionGroupId != null)
		securityService.deleteCollaborationGroup("acs224", "test group")
	}
	
	void testFailDeleteCollaborationGroup() {
		try {
			securityService.deleteCollaborationGroup("acs224", "DEVELOPERS")
			fail("Should have thrown exception")
		} catch(Exception e) {
			assertTrue("exception caught", true)
		}
	}
}