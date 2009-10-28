class CollaborationGroupService{
	def securityService
	def invitationService
	
	//gets all user memberships, sends back array. First list is managed memberships,
	//second is all others.
	def getUserMemberships(loginId){
		def allMemberships = []
		def managedMemberships =  []
		def otherMemberships =  []
		def user = GDOCUser.findByLoginName(loginId)
		allMemberships = user.memberships
		allMemberships.each{ cg ->
			if(securityService.isUserCollaborationManager(cg)){
				managedMemberships << cg
			}else{
				otherMemberships << cg
			}
		}
		return [managedMemberships,otherMemberships]
	}
	
	
	def getExistingUsers(loginNames, groupName){
		def names = []
		def existingUsers = []
		loginNames.each{
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