class CollaborationGroupsController {
	def collaborationGroupService
	def securityService
	def invitationService
	
	def index = {
		def memberships = []
		def managedMemberships =  []
		def otherMemberships =  []
		def allMemberships = []
		memberships = collaborationGroupService.getUserMemberships(session.userId)
		managedMemberships = memberships[0]
		otherMemberships = memberships[1]
		allMemberships = memberships[2]
		def toDelete = []
		otherMemberships.each{ memGroup ->
			def isAlreadyListed = managedMemberships.find {
				it.collaborationGroup.name == memGroup.collaborationGroup.name
			}
			if(isAlreadyListed){
				toDelete << memGroup
			}
		}
		
		otherMemberships.removeAll(toDelete)
		def invitations = []
		invitations = invitationService.findAllInvitationsForUser(session.userId)
		session.invitations = invitations
		def manMem = managedMemberships.collect{it.collaborationGroup}
		if(manMem)
			manMem = manMem as Set
		def otherMem = otherMemberships.collect{it.collaborationGroup}
		if(otherMem)
			otherMem = otherMem as Set
		def allMem = allMemberships.collect{it.collaborationGroup}
		if(allMem)
			allMem = allMem as Set
		println manMem
		[managedMemberships:manMem,otherMemberships:otherMem,allMemberships:allMem]
	}
	
	def createCollaborationGroup = {CreateCollabCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		} else{
			flash['cmd'] = cmd
			try{
				def pg = securityService.createCollaborationGroup(session.userId, cmd.collaborationGroupName, cmd.description)
				if(pg){
					flash.message = cmd.collaborationGroupName + " has been created. To invite users, select the invite users tab."
					redirect(action:"index")
				}
			}catch(DuplicateCollaborationGroupException de){
				println "DUPLICATE! " + de
				flash.message = cmd.collaborationGroupName + " already exists as a collaboration group. Contact the " + 
					"collaboration manager of that group for access, or rename your group."
				redirect(action:"index")
			}
		}
	}
	
	/**
	A collab manager invites users to join a group, thus making him the requestor and they the invitees. They
	would then act as the the invitee in confirm access to a group. Works opposite if they 'wish' to be added
	to a group.
	**/
	def inviteUsers = {InviteCollabCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		} else{
			flash['cmd'] = cmd
			def existingUsers = []
			existingUsers = collaborationGroupService.getExistingUsers(cmd.users,cmd.collaborationGroupName)
			if(existingUsers){
				def exUserString = ""
				existingUsers.each{ u ->
					exUserString += u + " ,"
				}
				println "$exUserString already exist(s) in the group" + cmd.collaborationGroupName 
				flash.message = "$exUserString already exists in the " + cmd.collaborationGroupName  + " group. No users added to group."
				redirect(uri:"/collaborationGroups/index")
			}
			else{
				def manager = securityService.findCollaborationManager(cmd.collaborationGroupName)
				if(manager && (manager.loginName == session.userId)){
					cmd.users.each{ user ->
						if(invitationService.requestAccess(manager.loginName,user,cmd.collaborationGroupName))
							println session.userId + " invited user $user to " + cmd.collaborationGroupName 
						}
				}
				flash.message = "An invitation has been sent to join the " + cmd.collaborationGroupName + " collaboration group."
				redirect(action:"index")
			}
		}
	}
	
	/**requests access to a collaboration group**/
	def requestAccess = {
		if(params.collaborationGroupName){
			def manager = securityService.findCollaborationManager(params.collaborationGroupName)
			if(invitationService.requestAccess(session.userId,manager.loginName,params.collaborationGroupName)){
				println session.userId + " is requesting access to " + params.collaborationGroupName 
				flash.message = "An access request has been sent to join the " + params.collaborationGroupName + " collaboration group."
				redirect(action:"index")
			}
		}else{
			flash.message = "No collaboration group specified. Try again."
			redirect(action:"index")
		}
	}
	
	/*
	deletes a user or users from a collaboration group
	*/
	def deleteUsersFromGroup = {DeleteCollabUserCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		} else{
			flash['cmd'] = cmd
			def manager = securityService.findCollaborationManager(cmd.collaborationGroupName)
			def delString = ""
			cmd.users.each{ user ->
				invitationService.revokeAccess(manager.loginName, user, cmd.collaborationGroupName)
				println "$user has been removed from " + cmd.collaborationGroupName 
				delString += user + ", "
			}
			flash.message = delString + " removed from " + cmd.collaborationGroupName 
			redirect(action:'index')
		}
	}
	
	
	//grants some user access to a group
	def grantAccess = { 
		if(params.id && params.user && params.group){
			if(invitationService.confirmAccess(session.userId, params.id))
				flash.message = "$params.user user has been added to the $params.group"
			else
				flash.message = "$params.user user has NOT been added to the $params.group"
		}
		redirect(action:index)
	}
	
	//accepts an invitation to join a group
	def addUser = {
		println params.id
		if(params.user && params.id && params.group){
			if(invitationService.acceptAccess(params.user,params.id))
				flash.message = "$params.user user has been added to the $params.group"
			else
				flash.message = "$params.user user has NOT been added to the $params.group"
		}
		redirect(action:index)
	}
	
	//either rejects some user access to a group, or rejects an invitation to join a group
	def rejectInvite = {
		println params.id
		if(params.user && params.id && params.group){
			if(invitationService.rejectAccess(params.id))
				flash.message = "$params.user user will not be joining the $params.group group at this time"
			else
				flash.message = "$params.user user has NOT been rejected access to the $params.group"
		}
		redirect(action:index)
	}
	
}