class InvitationService {

	def securityService
	/**
	 * Allows a user to request access to a group, or allows a collaboration manager
	 * to invite a user to the group.
	 */
	def requestAccess(requestor, invitee, groupName) {
		def userRequested = GDOCUser.findByLoginName(requestor)
		def userInvited = GDOCUser.findByLoginName(invitee)
		def collabGroup = CollaborationGroup.findByName(groupName)
		def invite = new Invitation(invitee:userInvited, requestor:userRequested, group:collabGroup, status: InviteStatus.PENDING)
		invite.save()
		return invite
	}
	
	/**
	 * This method allows a Collaboration group manager to confirm
	 * access for a user.
	 */
	def confirmAccess(groupManager, invitationId) {
		def invitation = Invitation.get(invitationId)
		if(!invitation)
			throw new RuntimeException("Invitation does not exist")
		def requestedGroup = invitation.group
		securityService.addUserToCollaborationGroup(groupManager, invitation.invitee.loginName, requestedGroup.name)
		invitation.status = InviteStatus.ACCEPTED
		invitation.save()
		return invitation
	}
	
	/**
	 * This allows a user to accept access that has been provided to them
	 * from a collaboration group.
	 */
	def acceptAccess(invitee, invitationId) {
		def invitation = Invitation.get(invitationId)
		if(!invitation)
			throw new RuntimeException("Invitation does not exist")
		if(invitee != invitation.invitee.loginName)
			throw new RuntimeException("User is not the invitee for this invitation.")
		securityService.addUserToCollaborationGroup(invitation.requestor.loginName, invitation.invitee.loginName, invitation.group.name)
		invitation.status = InviteStatus.ACCEPTED
		invitation.save()
		return invitation
	}
	
	/**
	 * This allows either the user to leave a group, or a collaboration 
	 * group manager to remove a user.
	 */
	def revokeAccess(requestor, requestee, groupName) {
		securityService.removeUserFromCollaborationGroup(requestor, requestee, groupName)
		def request = findRequest(requestee, groupName)
		if(request) {
			request.status = InviteStatus.WITHDRAWN
			request.save()
		}
	}
	
	/**
	 * Checks the status of an invite for a user and a group.
	 */
	def checkStatus(inviteeName, groupName) {
		return findRequest(inviteeName, groupName).status
	}
	
	def findRequest(inviteeName, groupName) {
		def c = Invitation.createCriteria()
		def results = c {
			and {
				invitee {
					eq("loginName", inviteeName)
				}
				group {
					eq("name", groupName)
				}
			}
		}
		return (results.size() > 0) ? results[0] : null
	}
}
