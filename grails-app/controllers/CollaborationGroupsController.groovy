class CollaborationGroupsController {
	def securityService
	
	def index = {
		def allMemberships = []
		def managedMemberships =  []
		def otherMemberships =  []
		def user = GDOCUser.findByLoginName(session.userId)
		allMemberships = user.memberships
		allMemberships.each{ cg ->
			if(securityService.isUserCollaborationManager(cg)){
				managedMemberships << cg
			}else{
				otherMemberships << cg
			}
		}
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
		
		[managedMemberships:managedMemberships,otherMemberships:otherMemberships]
	}
	
	
}