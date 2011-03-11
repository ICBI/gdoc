import org.springframework.mock.jndi.SimpleNamingContextBuilder
import javax.security.auth.login.Configuration
import javax.security.auth.login.AppConfigurationEntry
import javax.naming.*
import javax.naming.directory.*
import javax.naming.ldap.*
import javax.net.ssl.*
import org.springframework.jndi.*
import javax.naming.spi.*
import gov.nih.nci.security.authorization.domainobjects.User

class SecurityServiceTests extends BaseSecurityTest {

static transactional = false
def invitationService	
	/**
	void testUsersGroupsAndNames(){
		def groups = []
		def groupNames = []
		//get all groups for a user
		def user = GDOCUser.findByUsername("kmr75")
		groupNames = user.getGroupNames()
		//get all users in that group
		groupNames.each{ group ->
				println "group $group"
		}
		groups = user.getGroups()
		//get all users in that group
		groups.each{ group ->
				println "group $group"
		}
	}
	
	//grabs group for user...do a 'refresh' on user object to possibly get more current view of this
	void testGroupsUsers(){
		def users = []
		def group = CollaborationGroup.findByName("PUBLIC")
		users = group.getUsers()
		users.each{ user ->
			println "user " + user.lastName
		}
	}
	
	//grabs group for user...do a 'refresh' on user object to possibly get more current view of this
	void testFindCollaborationManager(){
		def groupName = "DEVELOPERS"
		def manager = securityService.findCollaborationManager(groupName)
		if(manager){
			assertTrue(manager.username == "acs224")
		}else{
			println "manager not found for group $groupName"
		}
	}
	**/
	
	void testCreateAndDeleteCollaborationGroup() {
		def testFailed = false
		try {
			def protectionGroup = securityService.createCollaborationGroup("kmr75", "testkmon group", "some description")
			assertTrue(protectionGroup.id != null)
		} catch(Exception e) {
			e.printStackTrace()
			testFailed = true
		} finally {
			securityService.deleteCollaborationGroup("kmr75", "testkmon group")
		}
		if(testFailed)
			fail("Exception during group create")
	}
	/**
	void testDeleteCollaborationGroupWithInvites() {
		try {
			def invite = invitationService.requestAccess("kmr75","acs224","KEVINS GROUP4")
		} catch(Exception e) {
			e.printStackTrace()
		} finally {
			securityService.deleteCollaborationGroup("kmr75", "KEVINS GROUP4")
		}
	}
	
	void testFailDeleteCollaborationGroup() {
		try {
			securityService.deleteCollaborationGroup("kmr75", "DEVELOPERS")
			fail("Should have thrown exception")
		} catch(Exception e) {
			assertTrue("exception caught", true)
		}
	}
	
	void testShare(){
		def protectionGroup = securityService.createCollaborationGroup("kmr75", "share group", "some description")
		println "created group $protectionGroup.name"
		def list = UserList.get("520144")
		println "found list $list.name"
		def groups = []
		groups << protectionGroup.name
		if(securityService.share(list,groups)){
			def groupsShared = securityService.groupsShared(list)
			println groupsShared
		}
	}
	
	
	void testAddAndRemoveUserToCollaborationGroup() {
		def testFailed = false
		try {
			def protectionGroup = securityService.createCollaborationGroup("kmr75", "user add group", "some description")
			assertTrue(protectionGroup.id != null)
			println "created group $protectionGroup.name"
			securityService.addUserToCollaborationGroup("kmr75", "acs224", "user add group")
			
			def groups = securityService.getCollaborationGroups("acs224")
			println "groups after add" + groups
			assertTrue(groups.contains("user add group"))
			
		} catch(Exception e) {
			e.printStackTrace()
			testFailed = true
		} finally {
			try {
				securityService.removeUserFromCollaborationGroup("kmr75", "acs224", "user add group")
				def groups = securityService.getCollaborationGroups("acs224")
				println "groups after remove" + groups
				assertFalse(groups.contains("user add group"))
			} catch(Exception e) {
				e.printStackTrace()
				testFailed = true
			} finally {
				securityService.deleteCollaborationGroup("kmr75", "user add group")
			}
		}
		if(testFailed)
			fail("Exception during group create")		
	}
	
	void testAddRoleForUser(){
		def user = GDOCUser.findByUsername("kmr75")
		println "all memberships " + user.memberships
		securityService.createMembership("kmr75","DEVELOPERS",SecurityService.GROUP_MANAGER)
		
	}**/
	
	/**
	
	void testCreateDeleteUser(){
		def user = new User()
		user.setUsername("test75")
		user.setFirstName("fn")
		user.setLastName("ln")
		user.setEmailId("test75@georgetown.edu")
		user.setDepartment("LOMBARDI COMPREHENSIVE CANCER CENTER")
		assertTrue(securityService.createUser(user))
		def newUser = GDOCUser.findByUsername("test75")
		assertNotNull(newUser)
		println "attempt to delete gdoc user " + newUser.id
		assertTrue(securityService.removeUser(newUser.id.toString()))
	}***/
	
	
}