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
		def testFailed = false
		try {
			def protectionGroup = securityService.createCollaborationGroup("acs224", "test group")
			assertTrue(protectionGroup.protectionGroupId != null)
		} catch(Exception e) {
			e.printStackTrace()
			testFailed = true
		} finally {
			securityService.deleteCollaborationGroup("acs224", "test group")
		}
		if(testFailed)
			fail("Exception during group create")
	}
	
	void testFailDeleteCollaborationGroup() {
		try {
			securityService.deleteCollaborationGroup("acs224", "DEVELOPERS")
			fail("Should have thrown exception")
		} catch(Exception e) {
			assertTrue("exception caught", true)
		}
	}
	
	void testAddAndRemoveUserToCollaborationGroup() {
		def testFailed = false
		try {
			def protectionGroup = securityService.createCollaborationGroup("acs224", "user add group")
			assertTrue(protectionGroup.protectionGroupId != null)
			securityService.addUserToCollaborationGroup("acs224", "kmr75", "user add group")
			def groups = securityService.getCollaborationGroups("kmr75")
			println groups
			assertTrue(groups.contains("user add group".toUpperCase()))
			
		} catch(Exception e) {
			e.printStackTrace()
			testFailed = true
		} finally {
			try {
				securityService.removeUserFromCollaborationGroup("acs224", "kmr75", "user add group")
				def groups = securityService.getCollaborationGroups("kmr75")
				assertFalse(groups.contains("user add group".toUpperCase()))
			} catch(Exception e) {
				e.printStackTrace()
				testFailed = true
			} finally {
				securityService.deleteCollaborationGroup("acs224", "user add group")
			}
		}
		if(testFailed)
			fail("Exception during group create")		
	}
	
	
}