import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException
import gov.nih.nci.security.exceptions.CSObjectNotFoundException
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup

import LoginException

class SecurityService {
	static scope = "session"
	public static String GROUP_MANAGER = 'COLLABORATION_GROUP_MANAGER'
	def jdbcTemplate
	
	AuthenticationManager authenticationManager
	AuthorizationManager authorizationManager
	
	def login(params) throws LoginException{
		def user = GDOCUser.findByLoginName(params.loginName)
		try{
			//print authenticationManager.getApplicationContextName();
			boolean loginOK = this.getAuthenticationManager().login(params.loginName, params.password);
			if (loginOK){
				System.out.println("SUCESSFUL LOGIN");
			}
			else {
				System.out.println("ERROR IN LOGIN");
				throw new LoginException("error in authentication");
			}
			}catch (CSException cse){
				System.out.println("ERROR IN LOGIN -- CS Exception");
				cse.printStackTrace(System.out);
				throw new LoginException("error in authentication");
		}
		
		try {
			print "got user " + this.getAuthorizationManager() + " from csm, ready to authorize "
			Set memberships = user.getGroups();
			memberships.each{ membership ->
				println membership.group.groupName
			}
		}catch ( CSException cse){
			System.out.println("ERROR IN AUTHORIZATION ");
			cse.printStackTrace(System.out);
			throw new LoginException("error in authorization");
		}
		
		return user
		
	}
	
	def logout(session){
		session.invalidate()
	}
	
	/**
	* Share an item with a collaboration groups
	*/
	def share(item, groups) {
		def authManager = this.getAuthorizationManager()
		
		ProtectionElement pe = authManager.getProtectionElement(item.id.toString(), item.class.name)
		if(!pe) {
			pe = new ProtectionElement()
			pe.protectionElementName = item.class.name + '_' + item.id.toString()
			
			pe.objectId = item.id.toString()
			pe.attribute = item.class.name
			authManager.createProtectionElement(pe)
		}
		groups.each{
			authManager.assignProtectionElement(it, item.id.toString(), item.class.name)
		}
	}
	
	/**
	Checks if the protection element has already been shared with any of the groups passed.
	If so, returns the groups it has been shared with
	**/
	def groupsShared(item){
			println "is $item already shared?"
			def authManager = this.getAuthorizationManager()
			def groupNames = []
		try{
				ProtectionElement pe = authManager.getProtectionElement(item.id.toString(), item.class.name)
				if(pe){
					def groups = authManager.getProtectionGroups(pe.protectionElementId.toString())
					if(groups){
						println "item $item hs already been shared to "
							groups.each{
								groupNames << it.getProtectionGroupName()
							}
					}
				}
		}catch(CSObjectNotFoundException csoe){
			csoe.printStackTrace(System.out);
			throw new SecurityException("object not found");
		}
			return groupNames
	}
	
	/**
	* Creates a new collaboration group
	**/
	def createCollaborationGroup(loginName, groupName) {
		groupName = groupName.toUpperCase()
		def authManager = this.getAuthorizationManager()
		def groups = authManager.getProtectionGroups()
		def currentProtectionGroup = findProtectionGroup(groupName)
		if(currentProtectionGroup != null)
			throw new DuplicateCollaborationGroupException()
		def pg = new ProtectionGroup()
		pg.protectionGroupName = groupName
		pg.protectionGroupDescription = "Protection group created by $loginName"
		authManager.createProtectionGroup(pg)
		String[] roles = new String[1]
		roles[0] = getRoleIdForName(GROUP_MANAGER).toString()
		def user = authManager.getUser(loginName)
		authManager.addUserRoleToProtectionGroup(user.userId.toString(), roles, pg.protectionGroupId.toString())
		return pg
	}
	
	/**
	* Adds a user to a collaboration group (including a study group)
	**/
	def addUserToCollaborationGroup(loginName, targetUser, groupName) {
	//	this.getAuthorizationManager().addUserRoleToProtectionGroup()
	}
	
	def removeUserFromCollaborationGroup(loginName, targetUser, groupName) {
	//	this.getAuthorizationManager().removeUserFromProtectionGroup()
	}
	
	def deleteCollaborationGroup(loginName, groupName) {
		groupName = groupName.toUpperCase()
		def pg = findProtectionGroup(groupName)
		def authManager = this.getAuthorizationManager()
		def user = authManager.getUser(loginName)
		def groups = authManager.getProtectionGroupRoleContextForUser(user.userId.toString())
		def toDelete = groups.find {
			it.protectionGroup.protectionGroupName == groupName
		}
		if(!toDelete)
			throw new Exception("User does not have permission to delete this group")
		def isCollaborationManager = false
		toDelete.roles.each {
			if(it.name == GROUP_MANAGER)
				isCollaborationManager = true
		}
		if(isCollaborationManager)
			authManager.removeProtectionGroup(pg.protectionGroupId.toString())
		else
			throw new Exception("User does not have permission to delete this group")
	}
	
	private findProtectionGroup(groupName) {
		def authManager = this.getAuthorizationManager()
		def groups = authManager.getProtectionGroups()
		def currentProtectionGroup = groups.find {
			it.protectionGroupName == groupName
		}
		return currentProtectionGroup
	}
	
	def getCollaborationGroups(loginName){
		def authManager = this.getAuthorizationManager()
		def user = authManager.getUser(loginName)
		def groups = authManager.getProtectionGroupRoleContextForUser(user.userId.toString()).collect { it.protectionGroup }
		def groupNames = []
		groups.each{
			groupNames << it.getProtectionGroupName()
		}
		return groupNames
	}
	
	def getSharedItemIds(loginName, itemType) {
		def sharedItems = [:]
		/**commented out for 'session' inconsistencies if(!sharedItems) {
			sharedItems = [:]
		}
		if(sharedItems[itemType]) {
			return sharedItems[itemType]
		} else {**/
			def authManager = this.getAuthorizationManager()
			def user = authManager.getUser(loginName)
			def groups = authManager.getProtectionGroupRoleContextForUser(user.userId.toString()).collect { it.protectionGroup }
			def elements = groups.collect {
				return authManager.getProtectionElements(it.protectionGroupId.toString())
			
			}
			elements = elements.flatten()
			def ids = []
			elements.each {
				if (itemType.equals(it.attribute))
					if(ids!=null && !ids.contains(it.objectId)){
						ids << it.objectId
					}
			}
			sharedItems[itemType] = ids
			return ids
		//}
	}
	
	private getRoleIdForName(roleName) {
		return jdbcTemplate.queryForLong("select ROLE_ID from CSM.CSM_ROLE where ROLE_NAME = '$roleName'")
	}
	
	public AuthenticationManager getAuthenticationManager() {
		if(!this.@authenticationManager) {
			this.@authenticationManager = SecurityServiceProvider.getAuthenticationManager("gdoc")
		} 
		return this.@authenticationManager 
	}
	
	public AuthorizationManager getAuthorizationManager() {
		if(!this.@authorizationManager) {
			this.@authorizationManager = SecurityServiceProvider.getAuthorizationManager("gdoc");
		} 
		return this.@authorizationManager 
	}
}