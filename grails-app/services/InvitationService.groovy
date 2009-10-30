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
		def invite = findRequest(requestor,invitee,groupName)
		if(invite){
			invite.status = InviteStatus.PENDING
			invite.save()
		}else{
			invite = new Invitation(invitee:userInvited, requestor:userRequested, group:collabGroup, status: InviteStatus.PENDING)
			invite.save()
		}
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
		securityService.addUserToCollaborationGroup(groupManager, invitation.requestor.loginName, requestedGroup.name)
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
	def revokeAccess(loginName, targetUser, groupName) {
		securityService.removeUserFromCollaborationGroup(loginName, targetUser, groupName)
		def request = findRequest(targetUser,loginName, groupName)
		if(request) {
			request.status = InviteStatus.WITHDRAWN
			request.save()
		}
	}
	
	def rejectAccess(invitationId){
		def invitation = Invitation.get(invitationId)
		if(invitation) {
			invitation.status = InviteStatus.REJECTED
			invitation.save()
		}
		return invitation
	}
	
	
	def userAlreadyInGroup(loginName, groupName){
		def collabGroup = CollaborationGroup.findByName(groupName)
		def memberships = []
		memberships = Membership.findAllByCollaborationGroup(collabGroup)
		def userExists = memberships.find{
			it.user.loginName == loginName
		}
		return userExists
	}
	
	/**
	 retrieves all invitation pertaining to user and organizes based on possible roles in each invite
	**/
	def findAllInvitationsForUser(loginName){
		def user = GDOCUser.findByLoginName(loginName)
		def invitations = [:]
		invitations["req"] = []
		invitations["reqAndMan"] = []
		invitations["inv"] = []
		invitations["invNotMan"] = []
		invitations["inv"] = Invitation.findAllByInvitee(user)
		invitations["req"] = Invitation.findAllByRequestor(user)
		//def allInvitations = []
		invitations["req"].each { invite ->
			if(securityService.findCollaborationManager(invite.group.name).loginName == loginName){
				invitations["reqAndMan"] << invite
			}
		}
		if(invitations["reqAndMan"]){
			invitations["req"].removeAll(invitations["reqAndMan"])
			invitations["reqAndMan"] = sortAndFilterInvites(invitations["reqAndMan"])
		}
		if(invitations["req"]){
			invitations["req"] = sortAndFilterInvites(invitations["req"])
		}
		invitations["inv"].each { invite ->
			if(securityService.findCollaborationManager(invite.group.name).loginName != loginName){
				invitations["invNotMan"] << invite
			}
		}
		if(invitations["invNotMan"]){
			invitations["inv"].removeAll(invitations["invNotMan"])
			invitations["invNotMan"] = sortAndFilterInvites(invitations["invNotMan"])
		}
		if(invitations["inv"]){
			invitations["inv"] = sortAndFilterInvites(invitations["inv"])
		}
		return invitations
	}
	
	/**sorts invitations by date and removes invites(not deleted) that are older than 30 days old**/
	def sortAndFilterInvites(invitations){
		def today = new Date()
		def removeable = []
		invitations.each{ i ->
			if(today.minus(i.dateCreated) > 30){
				//println "remove " + n
				removeable << i
			}
		}
		invitations.removeAll(removeable)
		
		invitations = invitations.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}
		return invitations
	}
	
	/**
	 * Checks the status of an invite for a user and a group.
	 */
	def checkStatus(requestorName, inviteeName, groupName) {
		return findRequest(requestorName, inviteeName, groupName).status
	}
	
	def findRequest(requestorName, inviteeName, groupName) {
		def c = Invitation.createCriteria()
		def results = c {
			and {
				requestor {
					eq("loginName", requestorName)
				}
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
