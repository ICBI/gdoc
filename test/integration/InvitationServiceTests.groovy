import grails.test.*

class InvitationServiceTests extends BaseSecurityTest {
	
	void testNothing() {
		
	}
/*	def invitationService
	
    void testRequestAccess() {
		def invitation = invitationService.requestAccess("acs224", "acs224", "DEVELOPERS")
		println invitation.errors
		assertNotNull(invitation.id)
		assertEquals(invitation.status, InviteStatus.PENDING)
		assertNotNull(invitation.invitee)
		assertNotNull(invitation.requestor)
    }

	void testConfirmAccess() {
		def invitation = invitationService.requestAccess("yg63", "yg63", "DEVELOPERS")
		def acceptedInvite = invitationService.confirmAccess("acs224", invitation.id)
		assertEquals(acceptedInvite.status, InviteStatus.ACCEPTED)
	}
	
	void testAcceptAccess() {
		def invitation = invitationService.requestAccess("acs224", "kmr75", "DEVELOPERS")
		def acceptedInvite = invitationService.acceptAccess("kmr75", invitation.id)
		assertEquals(acceptedInvite.status, InviteStatus.ACCEPTED)
	}
	
	void testCheckStatus() {
		def invitation = invitationService.requestAccess("acs224", "acs224", "DEVELOPERS")
		def status = invitationService.checkStatus("acs224", "acs224", "DEVELOPERS")
		assertEquals(status, InviteStatus.PENDING)
	}
	
	void testRevokeAccess() {
		def invitation = invitationService.requestAccess("acs224","acs224", "DEVELOPERS")
		def acceptedInvite = invitationService.confirmAccess("acs224", invitation.id)
		invitationService.revokeAccess("acs224", "kmr75", "DEVELOPERS")
		def status = invitationService.checkStatus("kmr75" , "acs224" , "DEVELOPERS")
		assertEquals(status, InviteStatus.WITHDRAWN)
	}*/
}
