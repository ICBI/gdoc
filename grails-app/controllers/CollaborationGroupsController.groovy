import grails.converters.*
import java.net.URLEncoder
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class CollaborationGroupsController {
	def collaborationGroupService
	def securityService
	def invitationService
	def userListService
	def quickStartService
	def savedAnalysisService
	def searchResults
	def mailService
	
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
					it.username == session.userId
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
					session.myCollaborationGroups << pg.name
					if(session.managedMemberships)
					 	session.managedMemberships << pg.name
					else{
						session.managedMemberships = []
						session.managedMemberships << pg.name
					}
						
					redirect(action:"index")
				}
			}catch(DuplicateCollaborationGroupException de){
				log.debug "DUPLICATE! " + de
				flash.message = cmd.collaborationGroupName + " already exists as a collaboration group."
				redirect(action:"index")
			}
		}
	}
	
	def deleteGroup = {
				if(params.group){
					log.debug "requesting deletion of group $params.group"
					def id = params.group.toLong()
					def group = CollaborationGroup.get(id)
					def groupName = group?.name
					def manager
					if (groupName) 
						manager = securityService.findCollaborationManager(groupName)
					if(manager && (manager.username == session.userId)){
						if(securityService.deleteCollaborationGroup(session.userId,groupName)){
							log.debug "collaboration group, $groupName has been deleted"
							flash.message = "collaboration group, $groupName has been deleted"
							reloadMembershipAndStudyData()
							redirect(action:index)
							return
						}
						else{
							log.debug "collaboration group, $groupName has NOT been deleted"
							flash.error = "collaboration group, $groupName has NOT been deleted"
							redirect(action:index)
							return
						}
					}
					else{
						log.debug "user is NOT permitted to delete list"
						redirect(controller:'policies',action:'deniedAccess')
						return
					}
					
				}
	}
	
	def buildUserNameForInvite(user){
		if(user.firstName && user.lastName){
			return user.firstName + " " + user.lastName
		}
		else{
			return user.username
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
			def usrs = []
			if(cmd.users == 'allUsers'){
				log.debug "$session.userId attempted to invite all users, return false"
				flash.error = "You are not permitted to invite all users at one time to this group. No users added to group, please make a selection of 10 users or less."
				redirect(uri:"/collaborationGroups/showUsers")
				return;
			}
			else{
				usrs = cmd.users.tokenize(",")
				if(usrs.size() == 0 ){
					log.debug "$session.userId attempted to invite 0 users, return false"
					flash.error = "You must slect at least one user."
					redirect(uri:"/collaborationGroups/showUsers")
					return;
				}
				if(usrs.size() > 10){
					log.debug "$session.userId attempted to invite more than 10 users, return false"
					flash.error = "You are not permitted to invite more than 10 users at a time, to this group. No users added to group, please make a selection of 10 users or less."
					redirect(uri:"/collaborationGroups/showUsers")
					return;
				}
				log.debug "$session.userId attempting to invite $usrs"
				def gdocUsers = []
				def usernames = []
				usrs.each{ 
					def id = it.toLong()
					def gu = GDOCUser.get(id)
					gdocUsers << gu
					usernames << gu.username
				}
				def existingUsers = []
				existingUsers = collaborationGroupService.getExistingUsers(usernames,cmd.collaborationGroupName)
				if(existingUsers){
					def exUserString = ""
					existingUsers.each{ u ->
						exUserString += u + " ,"
					}
					log.debug "$exUserString already exist(s) in the group" + cmd.collaborationGroupName 
					flash.error = "$exUserString already exists in the " + cmd.collaborationGroupName  + " group. No users added to group."
					redirect(uri:"/collaborationGroups/showUsers")
					return;
				}
				else{
					def manager = securityService.findCollaborationManager(cmd.collaborationGroupName)
					def inv
					def exists = false
					if(manager && (manager.username == session.userId)){
						usernames.each{ u ->
							inv = invitationService.findSimilarRequest(manager.username,u,cmd.collaborationGroupName)
							if(inv){
								flash.error = "A similar invitation exists for $u invited. No invitations sent... please try again."
								log.debug "A similar invitation exists for $u invited, do not send invites."
								exists = true
							}
						}
						if(!exists){
							gdocUsers.each{ user ->
								if(invitationService.requestAccess(manager.username,user.username,cmd.collaborationGroupName))
								log.debug session.userId + " invited user $user.username to " + cmd.collaborationGroupName
								flash.message = session.userId + " invited user(s) to " + cmd.collaborationGroupName
								def managerName = buildUserNameForInvite(manager)
								def th = Thread.start {
								    	if(user.email){
											def subject = "$managerName has invited you to join the group, $cmd.collaborationGroupName"
											sendEmail(user,subject)
										}
										else{
											log.debug "no email address was listed for $user.email account, invitation will only be seen on login."
											redirect(action:"showUsers")
											return
										}
								}

							}
						}
						else{
							redirect(action:"showUsers")
							return;
						}
					}
					redirect(action:"showUsers")
					return;
				}
			}
			redirect(action:"showUsers")
			return;
		}
	}
	
	
	def showUsers = {
		def users
		if(params.userId){
			 users = GDOCUser.createCriteria().list()
				{
					projections{
						property('id')
						property('lastName')
						property('firstName')
						property('organization')
					}
					and{
						'order'("username", "asc")
						ne("username","CSM")
					}
					or {
						ilike("username", "%"+params.userId+"%")
						ilike("lastName", params.userId)
					}
				}
		}else{
			users = GDOCUser.createCriteria().list()
				{
					projections{
						property('id')
						property('lastName')
						property('firstName')
						property('organization')
					}
					and{
						'order'("lastName", "asc")
						ne("username","CSM")
					}
				}
		}
		def columns = []
		columns << [index: "id", name: "User ID", sortable: true, width:'0',resizable:false]
		def columnNames = ["lastName","firstName","organization"]
		def userListings = []
		users.each{
			def userMap = [:]
			userMap["id"] = it[0]
			userMap["lastName"] = it[1]
			userMap["firstName"] = it[2]
			userMap["organization"] = it[3]
			userListings << userMap
		}
		columnNames.each {
			def column = [:]
			column["index"] = it
			column["name"] = it
			column["width"] = '170'
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
			cells << user.id
			cells << user.lastName
			cells << user.firstName
			cells << user.organization
			results << [id: user.id, cell: cells]
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
			if(invitationService.requestAccess(session.userId,manager.username,params.collaborationGroupName)){
				log.debug session.userId + " is requesting access to " + params.collaborationGroupName 
				def sessUser = GDOCUser.findByUsername(session.userId)
				def userName = buildUserNameForInvite(sessUser)
				if(manager.email){
					def subject = "$userName has requested access to join your group, $params.collaborationGroupName"
					def th = Thread.start {
						sendEmail(manager,subject)
					}
				}
				flash.message = "An access request has been created to join the " + params.collaborationGroupName + " collaboration group."
				redirect(action:"index")
			}
		}else{
			flash.message = "No collaboration group specified. Try again."
			redirect(action:"index")
		}
	}
	
	def sendEmail(sendTo,subjectText){
		def baseUrl = CH.config.grails.serverURL
		def token = sendTo.username + "||" + System.currentTimeMillis()
		def collabUrl = baseUrl+"/gdoc/collaborationGroups?token=" + URLEncoder.encode(EncryptionUtil.encrypt(token), "UTF-8")
		mailService.sendMail{
			to sendTo.email
			from "gdoc-help@georgetown.edu"
			subject "$subjectText"
			body "To view this request, click this link  (or paste in a browser):\n"+collabUrl+"\n.*Note: This link will expire within 24 hours*"
		}
		log.debug "email has been sent to $sendTo.email account"	
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
					invitationService.revokeAccess(manager.username, user, cmd.collaborationGroupName)
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