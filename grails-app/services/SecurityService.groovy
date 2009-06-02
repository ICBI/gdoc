import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException
import gov.nih.nci.security.exceptions.CSObjectNotFoundException
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup

import LoginException

class SecurityService {
	
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
		return ids
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