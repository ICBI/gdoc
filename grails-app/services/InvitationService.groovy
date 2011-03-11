class InvitationService {

	def securityService
	
	
	def getInvitesThatRequireAction(userId, lastLogin){
		def itra = [:]
		def inviteMessage =""
		def requestMessage=""
		def statusUpdates=false
		def invitations = []
		invitations = findAllInvitationsForUser(userId)
		if(invitations){
			def requestingPermissionFromYou = []
			def inviteYouToJoin = []
			def youRequestToJoin = []
			def youInviteSomeone = []
			if(invitations['inv']){
				requestingPermissionFromYou = invitations['inv'].findAll{it.status == InviteStatus.PENDING}
				if(requestingPermissionFromYou && requestingPermissionFromYou.size() > 1 ){
					requestMessage += requestingPermissionFromYou.size() + " requests"
				}
				else if(requestingPermissionFromYou && requestingPermissionFromYou.size() == 1 ){
					requestMessage += requestingPermissionFromYou.size() + " request"
				}
			}
				
			if(invitations['invNotMan']){

				inviteYouToJoin = invitations['invNotMan'].findAll{it.status == InviteStatus.PENDING}
				if(inviteYouToJoin && inviteYouToJoin.size() > 1 ){
					inviteMessage += inviteYouToJoin.size() + " invitations"
				}
				else if(inviteYouToJoin && inviteYouToJoin.size() == 1 ){
					inviteMessage += inviteYouToJoin.size() + " invitation"
				}
			}
			
			if(invitations['reqAndMan']){
				def rm = []
				rm = invitations['reqAndMan'].findAll{
					lastLogin > it.lastUpdated 
				}
				if(rm)
					statusUpdates = true
			}
			if(invitations['req']){
				def rm = []
				rm = invitations['req'].findAll{
					lastLogin > it.lastUpdated 
				}
				if(rm){
					statusUpdates = true
				}
					
			}
			
		}
		itra["inviteMessage"] = inviteMessage
		itra["requestMessage"] = requestMessage
		itra["statusUpdates"] = statusUpdates
		return itra
	}
	
	
	
	/**
	 * Allows a user to request access to a group, or allows a collaboration manager
	 * to invite a user to the group.
	 */
	def requestAccess(requestor, invitee, groupName) {
		def userRequested = GDOCUser.findByUsername(requestor)
		def userInvited = GDOCUser.findByUsername(invitee)
		def collabGroup = CollaborationGroup.findByName(groupName.toUpperCase())
		def invite = findRequest(requestor,invitee,groupName)
		if(invite){
			log.debug "found invite, do not create new invitation"
			invite.status = InviteStatus.PENDING
			invite.save(flush:true)
		}else{
			log.debug "create new invite"
			invite = new Invitation(invitee:userInvited, requestor:userRequested, group:collabGroup, status: InviteStatus.PENDING)
			invite.save(flush:true)
		}
		return invite
	}
	
	def findSimilarRequest(requestorName, inviteeName, groupName) {
		def c = Invitation.createCriteria()
		def results = c {
			and {
				requestor {
					or{
						eq("username", requestorName)
						eq("username", inviteeName)
					}
				}
				invitee {
					or{
						eq("username", requestorName)
						eq("username", inviteeName)
					}
				}
				group {
					eq("name", groupName)
				}
				eq("status", InviteStatus.PENDING)
			}
		}
		return (results.size() > 0) ? results[0] : null
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
		securityService.addUserToCollaborationGroup(groupManager, invitation.requestor.username, requestedGroup.name)
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
		if(invitee != invitation.invitee.username)
			throw new RuntimeException("User is not the invitee for this invitation.")
		securityService.addUserToCollaborationGroup(invitation.requestor.username, invitation.invitee.username, invitation.group.name)
		invitation.status = InviteStatus.ACCEPTED
		invitation.save()
		return invitation
	}
	
	/**
	 * This allows either the user to leave a group, or a collaboration 
	 * group manager to remove a user.
	 */
	def revokeAccess(username, targetUser, groupName) {
		securityService.removeUserFromCollaborationGroup(username, targetUser, groupName)
		def request = findRequest(username, targetUser, groupName)
		if(request) {
			log.debug "found original invite, now withdraw"
			request.status = InviteStatus.WITHDRAWN
			request.save(flush:true)
		}
	}
	
	def rejectAccess(invitationId){
		def invitation = Invitation.get(invitationId)
		if(invitation) {
			invitation.status = InviteStatus.REJECTED
			invitation.save(flush:true)
		}
		return invitation
	}
	
	
	def userAlreadyInGroup(username, groupName){
		def collabGroup = CollaborationGroup.findByName(groupName.toUpperCase())
		def memberships = []
		memberships = Membership.findAllByCollaborationGroup(collabGroup)
		def userExists = memberships.find{
			it.user.username == username
		}
		return userExists
	}
	
	/**
	 retrieves all invitation pertaining to user and organizes based on possible roles in each invite
	**/
	def findAllInvitationsForUser(username){
		def user = GDOCUser.findByUsername(username)
		def invitations = [:]
		invitations["req"] = []
		invitations["reqAndMan"] = []
		invitations["inv"] = []
		invitations["invNotMan"] = []
		invitations["inv"] = Invitation.findAllByInvitee(user)
		invitations["req"] = Invitation.findAllByRequestor(user)
		//def allInvitations = []
		invitations["req"].each { invite ->
			if(securityService.findCollaborationManager(invite.group.name).username == username){
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
			if(securityService.findCollaborationManager(invite.group.name).username != username){
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
	
	/**sorts invitations by date and removes invites(not deleted) that are older than 3 months, or 90 days old**/
	def sortAndFilterInvites(invitations){
		def today = new Date()
		def removeable = []
		invitations.each{ i ->
			if(today.minus(i.dateCreated) > 90){
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
					eq("username", requestorName)
				}
				invitee {
					eq("username", inviteeName)
				}
				group {
					eq("name", groupName)
				}
			}
		}
		return (results.size() > 0) ? results[0] : null
	}
	
}
