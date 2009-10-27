import grails.test.*

class InvitationServiceTests extends BaseSecurityTest {
	
	def invitationService
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testRequestAccess() {
		def invitation = invitationService.requestAccess("acs224", "acs224", "TEST_GROUP")
		assertNotNull(invitation.id)
		assertEquals(invitation.status, InviteStatus.PENDING)
		assertNotNull(invitation.invitee)
		assertNotNull(invitation.requestor)
    }

	void testConfirmAccess() {
		def invitation = invitationService.requestAccess("gdocUser2", "gdocUser2", "TEST_GROUP")
		def acceptedInvite = invitationService.confirmAccess("acs224", invitation.id)
		assertEquals(acceptedInvite.status, InviteStatus.ACCEPTED)
	}
	
	void testAcceptAccess() {
		def invitation = invitationService.requestAccess("acs224", "gdocUser2", "TEST_GROUP")
		def acceptedInvite = invitationService.acceptAccess("gdocUser2", invitation.id)
		assertEquals(acceptedInvite.status, InviteStatus.ACCEPTED)
	}
	
	void testCheckStatus() {
		def invitation = invitationService.requestAccess("acs224", "acs224", "TEST_GROUP")
		def status = invitationService.checkStatus("acs224", "TEST_GROUP")
		assertEquals(status, InviteStatus.PENDING)
	}
	
	void testRevokeAccess() {
		def invitation = invitationService.requestAccess("acs224", "gdocUser2", "TEST_GROUP")
		def acceptedInvite = invitationService.confirmAccess("acs224", invitation.id)
		invitationService.revokeAccess("acs224", "gdocUser2", "TEST_GROUP")
		def status = invitationService.checkStatus("gdocUser2", "TEST_GROUP")
		assertEquals(status, InviteStatus.WITHDRAWN)
	}
}
