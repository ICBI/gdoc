import grails.converters.*

class CollaborationGroupsController {
	def collaborationGroupService
	def securityService
	def invitationService
	def userListService
	def quickStartService
	def savedAnalysisService
	def searchResults
	
	def reloadMembershipAndStudyData(){
			def studyNames = securityService.getSharedItemIds(session.userId, StudyDataSource.class.name,true)
			def myStudies = []
		
			studyNames.each{
				def foundStudy = StudyDataSource.findByShortName(it)
				if(foundStudy){
					myStudies << foundStudy
				}
			}
			session.myStudies = []
			session.myStudies = myStudies
			def myCollaborationGroups = []
			myCollaborationGroups = securityService.getCollaborationGroups(session.userId)
			def sharedListIds = []
			sharedListIds = userListService.getSharedListIds(session.userId,true)
			session.sharedListIds = sharedListIds
			//get shared anaylysis and places them in session scope
			def sharedAnalysisIds = []
			sharedAnalysisIds = savedAnalysisService.getSharedAnalysisIds(session.userId,true)
			session.sharedAnalysisIds = sharedAnalysisIds
			session.dataAvailability = quickStartService.getMyDataAvailability(session.myStudies)
			session.myCollaborationGroups = myCollaborationGroups
			log.debug "reloaded all membership data"
	}
	
	def index = {
		def memberships = []
		def managedMemberships =  []
		def otherMemberships =  []
		def allMemberships = []
		def gdocUsers = []
		gdocUsers = GDOCUser.list()
		if (gdocUsers){
			def myself = gdocUsers.find{
					it.loginName == session.userId
			}
			if(myself){
					//log.debug "remove $myself"
					gdocUsers.remove(myself)
			}
		gdocUsers = gdocUsers.sort{it.lastName}
		}
			
		
		memberships = collaborationGroupService.getUserMemberships(session.userId)
		managedMemberships = memberships[0]
		
		otherMemberships = memberships[1]
		
		allMemberships = memberships[2]
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
		def invitations = []
		invitations = invitationService.findAllInvitationsForUser(session.userId)
		session.invitations = invitations
		def manMem = []
		manMem = managedMemberships.collect{it.collaborationGroup}
		if(manMem){
			manMem = manMem as Set
			manMem = manMem as List
			manMem.sort{it.name}
			session.managedMemberships	= manMem
		}
			
		def otherMem = []
		otherMem = otherMemberships.collect{it.collaborationGroup}
		if(otherMem){
			otherMem = otherMem as Set
			otherMem = otherMem as List
			otherMem.sort{it.name}	
		}
			
		def allMem = []
		allMem = allMemberships.collect{it.collaborationGroup}
		if(allMem){
			allMem = allMem as Set
			allMem = allMem as List
			allMem.sort{it.name}	
		}
			
		otherMem.sort{it.name}
		log.debug otherMem
		[gdocUsers:gdocUsers,managedMemberships:manMem,otherMemberships:otherMem,allMemberships:allMem]
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
					session.myCollaborationGroups << cmd.collaborationGroupName
					session.managedMemberships << cmd.collaborationGroupName
					redirect(action:"index")
				}
			}catch(DuplicateCollaborationGroupException de){
				log.debug "DUPLICATE! " + de
				flash.message = cmd.collaborationGroupName + " already exists as a collaboration group."
				redirect(action:"index")
			}
		}
	}
	
	/**
	A collab manager invites users to join a group, thus making him the requestor and they the invitees. They
	would then act as the the invitee in confirm access to a group. Works opposite if they 'wish' to be added
	to a group.
	**/
	def inviteUsers = {InviteCollabCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'showUsers')
			return
		} else{
			flash['cmd'] = cmd
			def existingUsers = []
			existingUsers = collaborationGroupService.getExistingUsers(cmd.users,cmd.collaborationGroupName)
			if(existingUsers){
				def exUserString = ""
				existingUsers.each{ u ->
					exUserString += u + " ,"
				}
				log.debug "$exUserString already exist(s) in the group" + cmd.collaborationGroupName 
				flash.message = "$exUserString already exists in the " + cmd.collaborationGroupName  + " group. No users added to group."
				redirect(uri:"/collaborationGroups/showUsers")
				return;
			}
			else{
				def usrs = []
				if(cmd.users[0] == 'allUsers'){
					session.uresults.each {
						usrs << it.loginName
					}
					//flash.message = "An invitation has been sent to all users to join the " + cmd.collaborationGroupName + " collaboration group."
				}else{
					usrs = cmd.users[0].split(",")
					//flash.message = "An invitation has been sent to $usrs to join the " + cmd.collaborationGroupName + " collaboration group."
				}
				def manager = securityService.findCollaborationManager(cmd.collaborationGroupName)
				def inv
				if(manager && (manager.loginName == session.userId)){
					usrs.each{ user ->
						inv = invitationService.findSimilarRequest(manager.loginName,user,cmd.collaborationGroupName)
						if(inv){
							flash.error = "A similar invitation exists for $user invited. No invitations sent... please try again."
							log.debug "A similar invitation exists for $user invited."
						}
					}
					if(!inv){
						usrs.each{ user ->
							if(invitationService.requestAccess(manager.loginName,user,cmd.collaborationGroupName))
							log.debug session.userId + " invited user $user to " + cmd.collaborationGroupName
							flash.message = session.userId + " invited user(s) to " + cmd.collaborationGroupName
						}
					}
				}
				redirect(action:"showUsers")
				return;
			}
		}
		redirect(action:"showUsers")
		return;
	}
	
	
	def showUsers = {
		log.debug params.userId
		def users
		if(params.userId){
			 users = GDOCUser.createCriteria().list()
				{
					projections{
						property('loginName')
						property('firstName')
						property('lastName')
						//property('email')
						property('organization')
					}
					and{
						'order'("loginName", "asc")
					}
					or {
						ilike("loginName", "%"+params.userId+"%")
						ilike("lastName", params.userId)
					}
				}
		}else{
			users = GDOCUser.createCriteria().list()
				{
					projections{
						property('loginName')
						property('firstName')
						property('lastName')
						//property('email')
						property('organization')
					}
					and{
						'order'("loginName", "asc")
					}
				}
		}
		def columns = []
		columns << [index: "loginName", name: "User Id", sortable: true, width: '70']
		def columnNames = ["firstName","lastName","organization"]
		def userListings = []
		users.each{
			def userMap = [:]
			userMap["loginName"] = it[0]
			userMap["firstName"] = it[1]
			userMap["lastName"] = it[2]
			//userMap["email"] = it[3]
			userMap["organization"] = it[3]
			userListings << userMap
		}
		columnNames.each {
			def column = [:]
			column["index"] = it
			column["name"] = it
			column["width"] = '160'
			column["sortable"] = true
			columns << column
		}
		session.ucolumnJson = columns as JSON
		def sortedColumns = ["User ID"]//, "PATIENT ID"]
		sortedColumns.addAll(columnNames)
		session.uresults = userListings
		session.ucolumns = sortedColumns
		session.ucolumnNames = sortedColumns as JSON
	}
	
	def viewUsers = {
		searchResults = session.uresults
		def columns = session.ucolumns
		def results = []
		def rows = params.rows.toInteger()
		def currPage = params.page.toInteger()
		def startIndex = ((currPage - 1) * rows)
		def endIndex = (currPage * rows)
		def sortColumn = params.sidx
		if(endIndex > searchResults.size()) {
			endIndex = searchResults.size()
		}
		def sortedResults = searchResults.sort { r1, r2 ->
			def val1 
			def val2
			val1 = r1[sortColumn]
			val2 = r2[sortColumn]
			def comparison
			if(val1 == val2) {
				return 0
			}
			if(params.sord != 'asc') {
				if(val2 == null) {
					return -1
				} else if (val1 == null) {
					return 1
				}
				comparison =  val2.compareTo(val1)
			} else {
				if(val1 == null) {
					return -1
				} else if(val2 == null) {
					return 1
				}
				comparison =  val1.compareTo(val2)
			}
			return comparison
		}
		session.uresults = sortedResults
		sortedResults.getAt(startIndex..<endIndex).each { user ->
			def cells = []
			cells << user.loginName
			cells << user.firstName
			cells << user.lastName
			//cells << user.email
			cells << user.organization
			results << [id: user.loginName, cell: cells]
		}
		//log.debug "results rows:" + results
		def jsonObject = [
			page: currPage,
			total: Math.ceil(searchResults.size() / rows),
			records: searchResults.size(),
			rows:results
		]
		render jsonObject as JSON
	}
	
	/**requests access to a collaboration group**/
	def requestAccess = {
		if(params.collaborationGroupName){
			def manager = securityService.findCollaborationManager(params.collaborationGroupName)
			if(invitationService.requestAccess(session.userId,manager.loginName,params.collaborationGroupName)){
				log.debug session.userId + " is requesting access to " + params.collaborationGroupName 
				flash.message = "An access request has been sent to join the " + params.collaborationGroupName + " collaboration group."
				redirect(action:"index")
			}
		}else{
			flash.message = "No collaboration group specified. Try again."
			redirect(action:"index")
		}
	}
	
	/*
	deletes a user or users from a collaboration group
	*/
	def deleteUsersFromGroup = {DeleteCollabUserCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		} else{
			flash['cmd'] = cmd
			log.debug("request deletion of $cmd.users from $cmd.collaborationGroupName")
			def permitted = false
			if(securityService.isUserGroupManager(session.userId, cmd.collaborationGroupName)){
				permitted = true
			}
			if(cmd.users.size() == 1 && cmd.users[0] == session.userId){
				permitted = true
			}
			
			if(permitted){
				log.debug("permission granted to delete $cmd.users from $cmd.collaborationGroupName")
				def manager = securityService.findCollaborationManager(cmd.collaborationGroupName)
				def delString = ""
				cmd.users.each{ user ->
					invitationService.revokeAccess(manager.loginName, user, cmd.collaborationGroupName)
					log.debug "$user has been removed from " + cmd.collaborationGroupName 
					delString += user + ", "
				}
				flash.message = delString + " has been marked for removal from " + cmd.collaborationGroupName + ". Subsequent logins will prevent user from accessing this group"
				reloadMembershipAndStudyData()
				redirect(action:'index')
			}
			else{
				log.debug "user CANNOT delete users from the $cmd.collaborationGroupName"
				redirect(controller:'policies', action:'deniedAccess')
			}
		}
	}
	
	
	//grants some user access to a group
	def grantAccess = { 
		if(params.id && params.user && params.group){
			if(invitationService.confirmAccess(session.userId, params.id))
				flash.message = "$params.user user has been added to the $params.group"
			else
				flash.message = "$params.user user has NOT been added to the $params.group"
		}
		reloadMembershipAndStudyData()
		redirect(action:index)
	}
	
	//accepts an invitation to join a group
	def addUser = {
		log.debug params.id
		if(params.user && params.id && params.group){
			if(invitationService.acceptAccess(params.user,params.id))
				flash.message = "$params.user user has been added to the $params.group"
			else
				flash.message = "$params.user user has NOT been added to the $params.group"
		}
		reloadMembershipAndStudyData()
		redirect(action:index)
	}
	
	//either rejects some user access to a group, or rejects an invitation to join a group
	def rejectInvite = {
		log.debug params.id
		if(params.user && params.id && params.group){
			if(invitationService.rejectAccess(params.id))
				flash.message = "$params.user user will not be joining the $params.group group at this time"
			else
				flash.message = "$params.user user has NOT been rejected access to the $params.group"
		}
		redirect(action:index)
	}
	
}