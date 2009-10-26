class CollaborationGroupsController {
	def securityService
	
	def index = {
		def collaborationGroups = []
		def managedGroups =  []
		def user = GDOCUser.findByLoginName(session.userId)
		collaborationGroups = user.groups
		collaborationGroups.each{ cg ->
			if(securityService.isUserCollaborationManager(cg)){
				managedGroups << cg
			}
		}
		collaborationGroups.removeAll(managedGroups)
		[managedGroups:managedGroups,memberGroups:collaborationGroups]
	}
	
	
}