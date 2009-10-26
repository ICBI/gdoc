class CollaborationGroupsController {
	def securityService
	
	def index = {
		def collaborationGroups = []
		def managedGroups =  []
		def memberGroups =  []
		def user = GDOCUser.findByLoginName(session.userId)
		collaborationGroups = user.groups
		collaborationGroups.each{ cg ->
			if(securityService.isUserCollaborationManager(cg)){
				managedGroups << cg
			}else{
				memberGroups << cg
			}
		}
		def toDelete = []
		memberGroups.each{ memGroup ->
			def isAlreadyListed = managedGroups.find {
				it.protectionGroup.name == memGroup.protectionGroup.name
			}
			if(isAlreadyListed){
				toDelete << memGroup
			}
		}
		
		memberGroups.removeAll(toDelete)
		
		[managedGroups:managedGroups,memberGroups:memberGroups]
	}
	
	
}