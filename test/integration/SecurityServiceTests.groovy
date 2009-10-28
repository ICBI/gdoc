import org.springframework.mock.jndi.SimpleNamingContextBuilder

class SecurityServiceTests extends BaseSecurityTest {
	
	void testGetUsersForProtectionGroup(){
		def groups = []
		//get all groups for a user
		groups = securityService.getProtectionGroupsForUser("kmr75")
		//get all users in that group
		groups.each{ group ->
			def users = []
			users = securityService.getUsersForProtectionGroup(group)
			if(users){
				println "users for $group: "
				users.each{ u ->
					println u.firstName + " " + u.lastName
				}
			}
		}
	}
	
	void testFindCollaborationManager(){
		def groupName = "DEVELOPERS"
		def manager = securityService.findCollaborationManager(groupName)
		if(manager){
			assertTrue(manager.loginName == "kmr75")
		}else{
			println "manager not found for group $groupName"
		}
	}
	
	void testCreateAndDeleteCollaborationGroup() {
		def testFailed = false
		try {
			def protectionGroup = securityService.createCollaborationGroup("acs224", "test group", "some description")
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
			def protectionGroup = securityService.createCollaborationGroup("acs224", "user add group", "some description")
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