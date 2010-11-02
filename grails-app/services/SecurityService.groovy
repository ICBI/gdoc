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
	public static String GDOC_ADMIN = 'GDOC_ADMIN'
	public static String USER = 'USER'
	
	def jdbcTemplate
	//List studies
	def sharedItems = [:]
	
	AuthenticationManager authenticationManager
	AuthorizationManager authorizationManager
	
	def login(params) throws LoginException{
		
		def user = GDOCUser.findByLoginName(params.loginName)
		try{
			//print authenticationManager.getApplicationContextName();
			boolean loginOK = this.getAuthenticationManager().login(params.loginName, params.password);
			if (loginOK){
				log.debug("SUCESSFUL LOGIN");
			}
			else {
				log.error("ERROR IN LOGIN");
				throw new LoginException("error in authentication");
			}
			}catch (CSException cse){
				log.error("ERROR IN LOGIN -- CS Exception", cse);
				throw new LoginException("error in authentication");
		}
		
		return user
		
	}
	
	def setLastLogin(userId){
		def authManager = this.getAuthorizationManager()
		def csmUser = authManager.getUser(userId)
		if(csmUser){
			csmUser.setEndDate(new Date())
			authManager.modifyUser(csmUser)
			log.debug("set user's last login");
		}
	}
	
	def logout(session){
		session.invalidate()
	}
	
	def validateToken(token){
		log.debug "prepare to decrypt token $token"
		String decryptedToken = EncryptionUtil.decrypt(token);
		String[] info = decryptedToken.split("\\|\\|");
		String username = info[0];
		log.debug "$username accessing gdoc" 
		Long timeRequested = Long.parseLong(info[1]);
		Long currentTime = System.currentTimeMillis();
		Long diff = currentTime - timeRequested;
		Long hours = diff / (60 * 60 * 1000);
		if(hours > 24L) {
			log.debug "time has expired" 
            return false
		}
		else{
			return username
		}
	}
	
	
	def validateNetId(netId, department){
		Configuration config = Configuration.getConfiguration();
	    AppConfigurationEntry[] entries = config.getAppConfigurationEntry("gdoc");
		log.debug entries
	    AppConfigurationEntry entry = entries[0];
		def options = entry.getOptions();
		options.each {
		    log.debug it.getKey() + " " + it.getValue()
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
			log.debug "$netId user found, let's retrieve retrieve needed attributes to create a G-DOC account"
			BasicAttributes attr =  ctx.getAttributes("uid="+netId+","+options.get("ldapSearchableBase"))
				if(attr){
					def user = new User()
					if(attr.get("uid")){
						log.debug "found user id " + attr.get("uid").get()
						user.setLoginName(attr.get("uid").get())
					}
					if(attr.get("givenname")){
						log.debug "found givenname " + attr.get("givenname").get()
						user.setFirstName(attr.get("givenname").get())
					}
					if(attr.get("sn")){
						log.debug "found sn " + attr.get("sn").get()
						user.setLastName(attr.get("sn").get())
					}
					if(attr.get("ou")){
						log.debug "found ou " + attr.get("ou").get()
						user.setOrganization(attr.get("ou").get())
					}
					if(attr.get("mail")){
						log.debug "found mail " + attr.get("mail").get()
						user.setEmailId(attr.get("mail").get())
					}
					if(department){
						user.setDepartment(department)
					}
					return user
				}
				else{
					log.debug "no attributes found for $netId"
					return null
				}
		}
		else{
			log.debug "no user found for that id"
			return null
		}
		
	}
	
	def createUser(user){
		def authManager = this.getAuthorizationManager()
		try{
			authManager.createUser(user)
		}catch(CSTransactionException cste){
			log.debug cste
			return false
		}
		log.debug "added user " + user.getLoginName()
		return true
	}
	
	def populateNewUserAttributes(loginName, password, firstName,lastName,emailId,organization){
		def newUser = new User()
		if(loginName && firstName &&
			lastName && organization && emailId){
			newUser.setLoginName(loginName)
			newUser.setPassword(password)
			newUser.setFirstName(firstName)
			newUser.setLastName(lastName)
			newUser.setOrganization(organization)
			newUser.setEmailId(emailId)
		}
		else {
			throw new SecurityException("one or more required user attributes were not included");
		}
		return newUser
	}
	
	def changeUserPassword(userId, newPassword){
		if(userId && newPassword){
			def authManager = this.getAuthorizationManager()
			try{
				log.debug "enable encryption"
				authManager.setEncryptionEnabled(true)
				def user = authManager.getUser(userId)
				user.setPassword(newPassword)
				authManager.modifyUser(user)
				log.debug "$userId password was $user.password"
				log.debug "successfully changed password for $userId"
				log.debug "$userId password is now $user.password"
				return true
			}catch(CSTransactionException cste){
				log.debug cste
				return false
			}
			
		}else{
			return false
		}
		
	}
	
	/**if deleting a user from using the UPT web app, remember to delete 
	any invitations associated with this user**/
	def removeUser(userId){
		def user = GDOCUser.get(userId)
		def managedGroups = []
		try{
			if(user.memberships){
				user.memberships.each{
					if(it.collaborationGroup){
						if(isUserGroupManager(user.loginName, it.collaborationGroup.name)) {
							log.debug "user is manager of $it.collaborationGroup.name, mark it for deletion"
							managedGroups << it.collaborationGroup.name
						}
					}
				}		
			}
			Invitation.executeUpdate("delete Invitation invitation where invitation.invitee = :user", [user:user])
			log.debug "deleted requestee invitations of " + user.loginName
			managedGroups.each{ groupName ->
				deleteCollaborationGroup(user.loginName,groupName)
			}
			log.debug "deleted all groups managed by $user.loginName"
			user.delete()
			log.debug "deleted all other objects held by $userId"
		}
		catch(Exception e){
			log.debug e
			return false
		}
		log.debug "deleted the user " + userId
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
			log.debug "is $item already shared?"
			def authManager = this.getAuthorizationManager()
			def groupNames = []
		try{
				ProtectionElement pe = authManager.getProtectionElement(item.id.toString(), item.class.name)
				if(pe){
					def groups = authManager.getProtectionGroups(pe.protectionElementId.toString())
					if(groups){
						log.debug "item $item hs already been shared to "
							groups.each{
								groupNames << it.getProtectionGroupName()
							}
					}
				}
		}catch(CSObjectNotFoundException csoe){
			log.error("object not found", csoe)
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
		def isCollaborationManager
		if(!pg)
			throw new Exception("Collaboration group does not exist.")
		def authManager = this.getAuthorizationManager()
		def user = authManager.getUser(loginName)
		def groups = authManager.getProtectionGroupRoleContextForUser(user.userId.toString())
		def toDelete = groups.find {
			log.debug "${it.protectionGroup.protectionGroupName} and ${groupName}"
			it.protectionGroup.protectionGroupName == groupName
		}
		if(!toDelete){
			log.debug("user, $loginName does is not member of $groupName")
			return false
		}
		else{
			isCollaborationManager = toDelete.roles.find {
				it.name == GROUP_MANAGER
			}
			if(isCollaborationManager){
				log.debug("user, $loginName is THE manager of $groupName")
				return true
			}
			else{
				log.debug("user, $loginName is not manager of $groupName")
				return false
			}
		}	
			
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
	
	def isUserGDOCAdmin(userId){
		def isUserGDOCAdmin = false
		def user = GDOCUser.findByLoginName(userId)
		def userMemberships = user.memberships
		userMemberships.each{ membership->
			if(membership.collaborationGroup &&
				 membership.collaborationGroup.name == 'PUBLIC' &&
					membership.role){
						if(membership.role.name == GDOC_ADMIN){
							log.debug "$userId is a GDOC Administrator"
							isUserGDOCAdmin = true
						}
			}
			
		}
		return isUserGDOCAdmin
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
		if(sharedItems[itemType] != null) {
			return sharedItems[itemType]
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
		if(sharedItems[itemType] == null) {
			log.debug "CACHING ${itemType} $ids"
			sharedItems[itemType] = ids
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
		log.debug "LOOKING UP $objectId for $type"
		def item = klazz.get(objectId)
		if(!item)
			return false
		def access = item.studies.collectAll {
			studyNames.contains(it.shortName)
		}
		//log.debug "ACCESS FOR $objectId is $access $studyNames"
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