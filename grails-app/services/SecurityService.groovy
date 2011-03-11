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
		
		def user = GDOCUser.findByUsername(params.username)
		try{
			//print authenticationManager.getApplicationContextName();
			boolean loginOK = this.getAuthenticationManager().login(params.username, params.password);
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
						user.setUsername(attr.get("uid").get())
					}
					if(attr.get("givenname")){
						log.debug "found givenname " + attr.get("givenname").get()
						user.setFirstName(attr.get("givenname").get())
					}
					else{
						log.debug "didn't find first name, set to netid "
						user.setFirstName(attr.get("uid").get())
					}
					if(attr.get("sn")){
						log.debug "found sn " + attr.get("sn").get()
						user.setLastName(attr.get("sn").get())
					}else{
						log.debug "didn't find lasr name, set to netid "
						user.setLastName(attr.get("uid").get())
					}
					if(attr.get("ou")){
						log.debug "found ou " + attr.get("ou").get()
						user.setOrganization(attr.get("ou").get())
					}else{
						log.debug "didn't find ou, set to GU or affiliate "
						user.setOrganization("Georgetown_University")
					}
					if(attr.get("mail")){
						log.debug "found mail " + attr.get("mail").get()
						user.setEmailId(attr.get("mail").get())
					}
					if(department){
						user.setDepartment(department)
					}
					user.setPassword("gdocLCCC")
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
		log.debug "added user " + user.getUsername()
		return true
	}
	
	def populateNewUserAttributes(username, password, firstName,lastName,emailId,organization, title){
		def newUser = new User()
		if(username && firstName &&
			lastName && organization && emailId){
			newUser.setUsername(username)
			newUser.setPassword(password)
			newUser.setFirstName(firstName)
			newUser.setLastName(lastName)
			newUser.setOrganization(organization)
			newUser.setEmailId(emailId)
			newUser.setTitle(title)
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
				log.debug "successfully changed password for $userId"
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
			if(!user.lastLogin){
				log.debug "set lastLoginFor this user before deletion"
				setLastLogin(user.username)
			}
			if(user.memberships){
				log.debug "delete all groups $user.username is a member of..." + user.memberships
				user.memberships.each{
					if(it.collaborationGroup){
						if(isUserGroupManager(user.username, it.collaborationGroup.name)) {
							log.debug "user is manager of $it.collaborationGroup.name, mark it for deletion"
							managedGroups << it.collaborationGroup.name
						}
					}
				}		
			}
			Invitation.executeUpdate("delete Invitation invitation where invitation.invitee = :user", [user:user])
			log.debug "deleted requestee invitations of " + user.username
			managedGroups.each{ groupName ->
				deleteCollaborationGroup(user.username,groupName)
			}
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
		ProtectedArtifact protectedArtifact = ProtectedArtifact.findByObjectId(item.id)
		if(!protectedArtifact){
			println "create protected artifact"
			def name =  item.class.name + '_' + item.id.toString()
			def objectId = item.id.toString()
			def type = item.class.name
			protectedArtifact = createProtectedArtifact(name,objectId,type)
		}
		if(protectedArtifact){
			println "completed creating protected artifact, $protectedArtifact.name"
			groups.each{
				println "group, $it"
				def group = CollaborationGroup.findByName(it.toUpperCase())
				println "found group, $group"
				if(group){
					protectedArtifact.addToGroups(group)
					println "groups now" + protectedArtifact.groups
				}
			}
		}
		return true
	}
	
	def createProtectedArtifact(name, objectId, type){
		def protectedArtifact = new ProtectedArtifact()
		protectedArtifact.name =  name
		protectedArtifact.objectId = objectId
		protectedArtifact.type = type
		if(protectedArtifact.save(flush:true)){
			return protectedArtifact
		}else
		return null
	}
	
	
	/**
	Checks if the protection element has already been shared with any of the groups passed.
	If so, returns the groups it has been shared with
	**/
	def groupsShared(item){
			log.debug "is $item already shared?"
			def groupNames = []
		try{
				ProtectedArtifact protectedArtifact = ProtectedArtifact.findByObjectId(item.id)
				if(protectedArtifact){
					def groups = protectedArtifact.groups
					if(groups){
						println "item $item hs already been shared to "
							groups.each{
								groupNames << it.name
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
	def createCollaborationGroup(username, groupName, description) throws DuplicateCollaborationGroupException{
		def currentProtectionGroup = CollaborationGroup.findByName(groupName.toUpperCase())
		if(currentProtectionGroup)
			throw new DuplicateCollaborationGroupException()
		def collabGroup = new CollaborationGroup()
		collabGroup.name = groupName
		if(description){
			collabGroup.description = description
		}else{
			collabGroup.description = "Collaboration group created by $username"
		}
		if(collabGroup.save(flush:true)){
			createMembership(username, collabGroup.name, GROUP_MANAGER)
		}
		
		return collabGroup
	}
	
	
	def deleteCollaborationGroup(username, groupName) {
		def invites = []
		def cg = CollaborationGroup.findByName(groupName.toUpperCase())
		
			def sinvites= Invitation.count()
			println "invites? $sinvites"
			invites= cg.invitations
			println "invites $invites"
			/**def ids = []
			invites.each{
				ids << new Long(it.id)
				cg.invitations.remove(it)
			}
			println "removed invitations from group, now delete actual objects"
			Invitation.executeUpdate("delete Invitation i WHERE i.id IN (:ids)", [ids: ids])
			if(invites){
				def ids = []
				ids = invites.collect{it.id}
				
				ids.each{
					def i = Invitation.get(it)
					i.delete(flush:true)
				}
			}**/
			if(isUserGroupManager(username, cg.name)) {
				println "delete user from group then delete group"
				removeUserFromCollaborationGroup(username,username,cg.name)
				println "trying to delete the group"
				cg.delete(flush:true)
				println "deleted group, still invites?"
				def oinvites= Invitation.count()
				println "invites? $oinvites"
				return true
			} else {
				throw new Exception("User $username does not have permission to delete this group")
			}

		return false
	}
	
	def isUserGroupManager(username, groupName) {
		def collabGroup = CollaborationGroup.findByName(groupName.toUpperCase())
		def isCollaborationManager
		if(!collabGroup)
			throw new Exception("Collaboration group does not exist.")
		def user = GDOCUser.findByUsername(username)
		def memberships = user.memberships
		def desiredMemberships = memberships.find {
			println "${it.collaborationGroup.name} and ${it.role.name}"
			it.collaborationGroup.name == groupName
		}
		if(!desiredMemberships){
			 println "user, $username does is not member of $groupName"
			return false
		}
		else{
			isCollaborationManager = desiredMemberships.find {
				it.role.name == GROUP_MANAGER
			}
			if(isCollaborationManager){
				println "user, $username is THE manager of $groupName"
				return true
			}
			else{
				println "user, $username is not manager of $groupName"
				return false
			}
		}	
			
	}
	
	private createMembership(username, groupName, roleName){
		def collabGroup = CollaborationGroup.findByName(groupName.toUpperCase())
		def role = Role.findByName(roleName)
		def user = GDOCUser.findByUsername(username)
		println "$username, $groupName, $roleName"
		if(user && collabGroup && role){
			def membership = new Membership(user:user,collaborationGroup:collabGroup,role:role)
			if(membership.save(flush:true)){
				println "membership created $membership"
			}
		}
	}
	
	/**
	* Adds a user to a collaboration group (including a study group)
	**/
	def addUserToCollaborationGroup(username, targetUser, groupName) {
		if(isUserGroupManager(username, groupName)) {
			def collabGroup = CollaborationGroup.findByName(groupName.toUpperCase())
			def role = Role.findByName(USER)
			def user = GDOCUser.findByUsername(targetUser)
			def membership = new Membership(user:user, collaborationGroup:collabGroup,role:role)
			if(membership.save(flush:true)){
				return true
			}
		} else {
			throw new Exception("User $username is not a Collaboration Group Manager.")
		}
	}
	
	def removeUserFromCollaborationGroup(username, targetUser, groupName) {
		if(isUserGroupManager(username, groupName)) {
			def user =  GDOCUser.findByUsername(targetUser)
			def collabGroup = CollaborationGroup.findByName(groupName.toUpperCase())
			def memberships = Membership.findAllByUserAndCollaborationGroup(user,collabGroup)
			println "delete all memberships $memberships"
			if(memberships){
				def ids = []
				memberships.each{
					ids << new Long(it.id)
					user.memberships.remove(it)
				}
				Membership.executeUpdate("delete Membership m WHERE m.id IN (:ids)", [ids: ids])
			}
			println "deleted membership, can it still be found?"
			def membershipf = Membership.findAllByUserAndCollaborationGroup(user,collabGroup)
			println "found? " + membershipf
		} else {
			throw new Exception("User $username is not a Collaboration Group Manager.")
		}
	}
	
	
	
	
	def findCollaborationManager(groupName){
		def collabGroup = CollaborationGroup.findByName(groupName.toUpperCase())
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
		def user = GDOCUser.findByUsername(userId)
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
	
	
	def getCollaborationGroups(username){
		def user = GDOCUser.findByUsername(username)
		def groups = new HashSet()
		def groupNames = new HashSet()
		if(user.memberships){
			groups = user.memberships.collect{it.collaborationGroup}
		}
		if(groups){
			groups.each{
						groupNames << it.name
			}
		}
		return groupNames
	}
	
	def getSharedItemIds(username, itemType,refresh) {
			println "find all artifacts for type $itemType"
			if(sharedItems[itemType] != null && !refresh) {
				return sharedItems[itemType]
			}
			def user = GDOCUser.findByUsername(username)
			def groups = new HashSet()
			def groupIds = new HashSet()
			if(user.memberships){
				groups = user.memberships.collect{it.collaborationGroup}
				if(groups){
					groups.each{
						groupIds << new Long(it.id)
					}
				}
			}
			def artifacts = []
			println "look for collab groups $groupIds"
			def artifactHQL = "SELECT distinct artifact FROM ProtectedArtifact artifact JOIN artifact.groups groups " + 
			"WHERE artifact.type = :type " + 
			"AND groups IN (:groups) "
			artifacts = ProtectedArtifact.executeQuery(artifactHQL, [type: itemType, groups: groups])
			artifacts.flatten()
			def ids = []
			if(itemType == StudyDataSource.class.name){
				artifacts.each {
					ids << it.objectId
				}
			}

			else{
				def aIds
				if(artifacts){
					aIds = artifacts.collect{it.objectId}.unique()
				}
				println "found unique $itemType artifacts numbering "+ aIds.size()
				ids = getAccessibleIds(user,itemType,aIds)
			}

			if(sharedItems[itemType] == null) {
				log.debug "CACHING ${itemType} $ids"
				sharedItems[itemType] = ids
			}

			return ids
		}

		private getAccessibleIds(user, type,ids) {
				def accessibleIds = []
				def studyNames = this.getSharedItemIds(user.username, StudyDataSource.class.name,null)
				println "my shared studies are $studyNames"
				def studyHQL = "SELECT distinct study FROM StudyDataSource study " + 
				"WHERE study.shortName IN (:studyNames) "
				def studies = []
				studies = StudyDataSource.executeQuery(studyHQL, [studyNames: studyNames])
				if(type == UserList.class.name){
					def artifactHQL = "SELECT distinct list.id FROM UserList list JOIN list.studies studies " + 
					"WHERE studies IN (:studies) "
					accessibleIds = UserList.executeQuery(artifactHQL, [studies: studies])
				}
				if(type == SavedAnalysis.class.name){
					println "sa " + ids
					def artifactHQL = "SELECT distinct analysis.id FROM SavedAnalysis analysis JOIN analysis.studies studies " + 
					"WHERE studies IN (:studies) "
					accessibleIds = SavedAnalysis.executeQuery(artifactHQL, [studies: studies])
				}
				println "found $type accessibleIds " + accessibleIds.size()
				println "retain"
				println accessibleIds.retainAll(ids)
				println "ids " + ids.size()
				return ids
			}
	
	
	private getProtectionGroupsForUser(username){
		def authManager = this.getAuthorizationManager()
		def user = authManager.getUser(username)
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