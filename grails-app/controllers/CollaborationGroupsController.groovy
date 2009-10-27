class CollaborationGroupsController {
	def collaborationGroupService
	def securityService
	
	def index = {
		def allMemberships = []
		def managedMemberships =  []
		def otherMemberships =  []
		allMemberships = collaborationGroupService.getUserMemberships(session.userId)
		managedMemberships = allMemberships[0]
		otherMemberships = allMemberships[1]
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
}