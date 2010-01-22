import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException
import gov.nih.nci.security.exceptions.CSTransactionException
import gov.nih.nci.security.exceptions.CSObjectNotFoundException
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup
import gov.nih.nci.security.authorization.domainobjects.User
import javax.security.auth.login.Configuration
import javax.security.auth.login.AppConfigurationEntry
import javax.naming.*
import javax.naming.directory.*
import javax.naming.ldap.*
import javax.net.ssl.*

import LoginException

class SecurityService {
	static scope = "session"
	public static String GROUP_MANAGER = 'COLLABORATION_GROUP_MANAGER'
	public static String USER = 'USER'
	
	def jdbcTemplate
	List studies
	def sharedItems
	
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
		
		return user
		
	}
	
	def logout(session){
		session.invalidate()
	}
	
	
	def validateNetId(netId, department){
		Configuration config = Configuration.getConfiguration();
	    AppConfigurationEntry[] entries = config.getAppConfigurationEntry("gdoc");
		println entries
	    AppConfigurationEntry entry = entries[0];
		def options = entry.getOptions();
		options.each {
		    println it.getKey() + " " + it.getValue()
		 }
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_PROTOCOL, "ssl");
		env.put(Context.PROVIDER_URL, options.get("ldapHost"));
		env.put(Context.SECURITY_AUTHENTICATION,"simple");
		env.put("java.naming.ldap.factory.socket",SSLSocketFactory.class.getName());
		env.put(Context.SECURITY_PRINCIPAL,options.get("ldapAdminUserName")); 
		env.put(Context.SECURITY_CREDENTIALS,options.get("ldapAdminPassword")); 
		DirContext ctx = new InitialDirContext(env);
		SearchControls ctls = new SearchControls();
		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String filter = "(&(uid="+netId+"))";
		NamingEnumeration list = ctx.search(options.get("ldapSearchableBase"), filter, ctls);
		if(list){
			println "$netId user found, retrieve needed attributes"
			BasicAttributes attr =  ctx.getAttributes("uid="+netId+","+options.get("ldapSearchableBase"))
				if(attr){
					def user = new User()
					if(attr.get("uid")){
						println "found user id " + attr.get("uid").get()
						user.setLoginName(attr.get("uid").get())
					}
					if(attr.get("givenname")){
						println "found givenname " + attr.get("givenname").get()
						user.setFirstName(attr.get("givenname").get())
					}
					if(attr.get("sn")){
						println "found sn " + attr.get("sn").get()
						user.setLastName(attr.get("sn").get())
					}
					if(attr.get("ou")){
						println "found ou " + attr.get("ou").get()
						user.setOrganization(attr.get("ou").get())
					}
					if(attr.get("mail")){
						println "found mail " + attr.get("mail").get()
						user.setEmailId(attr.get("mail").get())
					}
					if(department){
						user.setDepartment(department)
					}
					return user
				}
				else{
					println "no attributes found for $netId"
					return null
				}
		}
		else{
			println "no user found for that id"
			return null
		}
		
	}
	
	def createUser(user){
		def authManager = this.getAuthorizationManager()
		try{
			authManager.createUser(user)
		}catch(CSTransactionException cste){
			println cste
			return false
		}
		println "added user " + user.getLoginName()
		return true
	}
	
	/**if deleting a user from using the UPT web app, remember to delete 
	any invitations associated with this user**/
	def removeUser(userId){
		def authManager = this.getAuthorizationManager()
		try{
			authManager.removeUser(userId)
		}catch(CSTransactionException cste){
			println cste
			return false
		}
		println "deleted user " + userId
		return true
	}
	
	/**
	* Share an item with collaboration group(s)
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
	def createCollaborationGroup(loginName, groupName, description) throws DuplicateCollaborationGroupException{
		groupName = groupName.toUpperCase()
		def authManager = this.getAuthorizationManager()
		def groups = authManager.getProtectionGroups()
		def currentProtectionGroup = findProtectionGroup(groupName)
		if(currentProtectionGroup != null)
			throw new DuplicateCollaborationGroupException()
		def pg = new ProtectionGroup()
		pg.protectionGroupName = groupName
		if(description){
			pg.protectionGroupDescription = description
		}else{
			pg.protectionGroupDescription = "Protection group created by $loginName"
		}
		authManager.createProtectionGroup(pg)
		addUserRoleToProtectionGroup(loginName, pg, GROUP_MANAGER)
		return pg
	}
	
	/**
	* Adds a user to a collaboration group (including a study group)
	**/
	def addUserToCollaborationGroup(loginName, targetUser, groupName) {
		groupName = groupName.toUpperCase()
		if(isUserGroupManager(loginName, groupName)) {
			def pg = findProtectionGroup(groupName)
			addUserRoleToProtectionGroup(targetUser, pg, USER)
		} else {
			throw new Exception("User $loginName is not a Collaboration Group Manager.")
		}
	}
	
	def removeUserFromCollaborationGroup(loginName, targetUser, groupName) {
		groupName = groupName.toUpperCase()
		if(isUserGroupManager(loginName, groupName)) {
			def pg = findProtectionGroup(groupName)
			def user = this.getAuthorizationManager().getUser(targetUser)
			this.getAuthorizationManager().removeUserFromProtectionGroup(pg.protectionGroupId.toString(), user.userId.toString())
		} else {
			throw new Exception("User $loginName is not a Collaboration Group Manager.")
		}
	}
	
	def deleteCollaborationGroup(loginName, groupName) {
		groupName = groupName.toUpperCase()
		if(isUserGroupManager(loginName, groupName)) {
			def pg = findProtectionGroup(groupName)
			this.getAuthorizationManager().removeProtectionGroup(pg.protectionGroupId.toString())
		} else {
			throw new Exception("User $loginName does not have permission to delete this group")
		}
	}
	
	private addUserRoleToProtectionGroup(loginName, protectionGroup, roleName) {
		String[] roles = new String[1]
		roles[0] = getRoleIdForName(roleName).toString()
		def user = this.getAuthorizationManager().getUser(loginName)
		this.getAuthorizationManager().addUserRoleToProtectionGroup(user.userId.toString(), roles, protectionGroup.protectionGroupId.toString())
		
	}
	
	def isUserGroupManager(loginName, groupName) {
		def pg = findProtectionGroup(groupName)
		if(!pg)
			throw new Exception("Collaboration group does not exist.")
		def authManager = this.getAuthorizationManager()
		def user = authManager.getUser(loginName)
		def groups = authManager.getProtectionGroupRoleContextForUser(user.userId.toString())
		def toDelete = groups.find {
			println "${it.protectionGroup.protectionGroupName} and ${groupName}"
			it.protectionGroup.protectionGroupName == groupName
		}
		if(!toDelete)
			throw new Exception("User1 $loginName does not have permission to delete this group")
		def isCollaborationManager = toDelete.roles.find {
			it.name == GROUP_MANAGER
		}
		return isCollaborationManager
	}
	
	def findCollaborationManager(groupName){
		def collabGroup = CollaborationGroup.findByName(groupName)
		def memberships = []
		memberships = Membership.findAllByCollaborationGroup(collabGroup)
		if(!memberships)
			throw new Exception("Collaboration group does not exist.")
		def managerMem = memberships.find{ m ->
			m.role.name == GROUP_MANAGER
		}
		if(managerMem){
			return managerMem.user
		}
		
	}
	
	private isUserCollaborationManager(membership){
		if(membership.role){
			if(membership.role.name == GROUP_MANAGER){
				return true
			}
			else return false
		}
		return false
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
		if(itemType == 'StudyDataSource' && studies) {
			return studies
		}
		def authManager = this.getAuthorizationManager()
		def user = authManager.getUser(loginName)
		def groups = authManager.getProtectionGroupRoleContextForUser(user.userId.toString()).collect { it.protectionGroup }
		def elements = groups.collect {
			return authManager.getProtectionElements(it.protectionGroupId.toString())
		
		}
		elements = elements.flatten()
		def ids = []
		elements.each {
			if (itemType.equals(it.attribute)) {
				if(ids!=null && !ids.contains(it.objectId)){
					// check to make sure user has access to study
					if(isStudy(it) || userCanAccess(user, it.objectId, itemType)) {
						ids << it.objectId
					}
				}
			}
		}
		if(itemType == 'StudyDataSource' && !studies) {
			println "CACHING STUDIES $ids"
			studies = ids
		}
		
		return ids
	}
	
	/**
	* This uses reflection to instantiate the object type and then check if the
	* user has access to the associated studies.
	*/
	private userCanAccess(user, objectId, type) {
		def studyNames = this.getSharedItemIds(user.loginName, StudyDataSource.class.name)
		def klazz = Thread.currentThread().contextClassLoader.loadClass(type)
		println "LOOKING UP $objectId for $type"
		def item = klazz.get(objectId)
		if(!item)
			return false
		def access = item.studies.collectAll {
			studyNames.contains(it.shortName)
		}
		println "ACCESS FOR $objectId is $access $studyNames"
		return !access.contains(false)
	}
	private isStudy(pe) {
		return pe.attribute == 'StudyDataSource'
	}
	
	private getProtectionGroupsForUser(loginName){
		def authManager = this.getAuthorizationManager()
		def user = authManager.getUser(loginName)
		def groups = []
		groups = authManager.getProtectionGroupRoleContextForUser(user.userId.toString()).collect { it.protectionGroup }
		return groups
	}
	
	private getRoleIdForName(roleName) {
		return jdbcTemplate.queryForLong("select ROLE_ID from CSM.CSM_ROLE where ROLE_NAME = '$roleName'")
	}
	
	
	private getUsersForProtectionGroup(protectionGroup){
		def pgId = protectionGroup.getProtectionGroupId()
		def userIds = []
		def users = jdbcTemplate.queryForList("select USER_ID from CSM.CSM_USER_GROUP_ROLE_PG where PROTECTION_GROUP_ID = '$pgId'")
		users.each{
			userIds << it.get("USER_ID")
		}
		def protGroupUsers = []
		protGroupUsers = GDOCUser.getAll(userIds)
		return protGroupUsers
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