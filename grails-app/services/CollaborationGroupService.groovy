class CollaborationGroupService{
	def securityService
	
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
	
	
}