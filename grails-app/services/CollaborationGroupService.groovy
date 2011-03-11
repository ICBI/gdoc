class CollaborationGroupService{
	def securityService
	def invitationService
	
	
	//gets all user memberships, sends back array. First list is managed memberships,
	//second is all others, third is memberships that exist in groups where user doesnt have access
	def getUserMemberships(loginId){
		def allMemberships = []
		allMemberships = Membership.getAll()
		//println allMemberships.size()
		def managedMemberships =  []
		def otherMemberships =  []
		def user = GDOCUser.findByUsername(loginId)
		def userMemberships = user.memberships
		allMemberships.removeAll(userMemberships)
		def dontShow = []
		userMemberships.each{ mm ->
			def foundMemberships = []
			foundMemberships = allMemberships.findAll{ am ->
				mm.collaborationGroup.name == am.collaborationGroup.name
			}
			if(foundMemberships){
				dontShow.addAll(foundMemberships)
			}
		}
		if(allMemberships){
			if(dontShow){
				allMemberships.removeAll(dontShow)
			}
		}
		//println allMemberships.size()
		userMemberships.each{ cg ->
			if(securityService.isUserCollaborationManager(cg)){
				managedMemberships << cg
			}else{
				otherMemberships << cg
			}
		}
		
		return [managedMemberships,otherMemberships,allMemberships]
	}
	
	
	def getExistingUsers(usernames, groupName){
		def names = []
		def existingUsers = []
		usernames.each{
			names << it
		}
		names.each{ name ->
			if(invitationService.userAlreadyInGroup(name, groupName)){
				existingUsers << name
			}
		}
		return existingUsers
	}
	
}