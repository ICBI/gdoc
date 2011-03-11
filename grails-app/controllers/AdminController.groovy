import grails.converters.*

class AdminController {
	def quickStartService
	def cleanupService
	def securityService
	def searchResults
	
	def index = {
		def dataAvail = servletContext.getAttribute("dataAvailability")
		def loadedStudies = []
		dataAvail['dataAvailability'].each{elm ->
			def studyName = elm.find{ key, value ->
				if(key == 'STUDY'){
					//log.debug "load $value"
					loadedStudies << value
				}
			}
		}
		if(loadedStudies)
		loadedStudies.sort()
		[loadedStudies:loadedStudies]
	}
	
	def reload = {
		log.debug("reload available data")
	    quickStartService.loadDataAvailability()
		def da = quickStartService.getDataAvailability()
		servletContext.setAttribute("dataAvailability", da)
		session.dataAvailability = quickStartService.getMyDataAvailability(session.myStudies)
		log.debug("finished reloading data")
		redirect(action:index)
		return 
	}
	
	def searchUsers = {
		def users
		if(params.userId){
			 users = GDOCUser.createCriteria().list()
				{
					projections{
						property('id')
						property('username')
						property('firstName')
						property('lastName')
						property('email')
						property('organization')
					}
					and{
						'order'("username", "asc")
						ne("username","CSM")
					}
					or {
						eq("username", params.userId)
					}
				}
		}else{
			users = GDOCUser.createCriteria().list()
				{
					projections{
						property('id')
						property('username')
						property('firstName')
						property('lastName')
						property('email')
						property('organization')
					}
					and{
						'order'("username", "asc")
						ne("username","CSM")
					}
				}
		}
		def columns = []
		columns << [index: "id", name: "User ID", sortable: true, width: '70']
		//columns << [index: "dataSourceInternalId", name: "PATIENT ID", sortable: true, width: '70']
		def columnNames = ["username","firstName","lastName","email","organization"]
		def userListings = []
		users.each{
			def userMap = [:]
			userMap["id"] = it[0]
			userMap["username"] = it[1]
			userMap["firstName"] = it[2]
			userMap["lastName"] = it[3]
			userMap["email"] = it[4]
			userMap["organization"] = it[5]
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
			cells << user.id
			cells << user.username
			cells << user.firstName
			cells << user.lastName
			cells << user.email
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
	
	def searchGroups = {
		def groups
		groups = CollaborationGroup.createCriteria().list()
				{
					and{
						'order'("name", "desc")
					}
				}
		def columns = []
		columns << [index: "id", name: "Group ID", sortable: true, width: '70']
		def columnNames = ["name","description"]
		def groupsListings = []
		groups.each{
			def groupsMap = [:]
			groupsMap["id"] = it.id
			groupsMap["name"] = it.name
			groupsMap["description"] = it.description
			groupsListings << groupsMap
		}
		columnNames.each {
			def column = [:]
			column["index"] = it
			column["name"] = it
			column["width"] = '260'
			column["sortable"] = true
			columns << column
		}
		session.ucolumnJson = columns as JSON
		def sortedColumns = ["Group ID"]
		sortedColumns.addAll(columnNames)
		session.uresults = groupsListings
		session.ucolumns = sortedColumns
		session.ucolumnNames = sortedColumns as JSON
	}
	
	def viewGroups = {
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
		sortedResults.getAt(startIndex..<endIndex).each { group ->
			def cells = []
			cells << group.id
			cells << group.name
			cells << group.description
			results << [id: group.id, cell: cells]
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
	
	def pendingInvites = {
			def invites = []
				 invites = Invitation.createCriteria().list()
					{
						projections {
							property('id')
							group {
								property('name')
							}
							property('status')
							property('dateCreated')
							requestor {
								property('username')
							}
							invitee {
								property('username')
							}
						}
						and{
							'order'("dateCreated", "asc")
							eq("status",InviteStatus.PENDING)
						}
					}
					def columns = []
					columns << [index: "id", name: "Invite ID", sortable: true, width: '70']
					//columns << [index: "dataSourceInternalId", name: "PATIENT ID", sortable: true, width: '70']
					def columnNames = ["group","status","dateCreated","requestor","invitee"]
					def invitesListings = []
					invites.each{
						def invitesMap = [:]
						invitesMap["id"] = it[0]
						invitesMap["group"] = it[1]
						invitesMap["status"] = it[2]
						invitesMap["dateCreated"] = it[3]
						invitesMap["requestor"] = it[4]
						invitesMap["invitee"] = it[5]
						invitesListings << invitesMap
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
					def sortedColumns = ["Invite ID"]//, "PATIENT ID"]
					sortedColumns.addAll(columnNames)
					session.uresults = invitesListings
					session.ucolumns = sortedColumns
					session.ucolumnNames = sortedColumns as JSON
			}
			
	def viewInvites = {
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
				sortedResults.getAt(startIndex..<endIndex).each { invite ->
					def cells = []
					cells << invite.id
					cells << invite.group
					cells << invite.status.toString()
					cells << invite.dateCreated
					cells << invite.requestor
					cells << invite.invitee
					results << [id: invite.id, cell: cells]
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
	
	def showGroup = {
				if(params.group){
					log.debug "find group $params.group"
					def id = params.group.toLong()
					def group = CollaborationGroup.get(id)
					def members = group.users?.collect{it.username}
					[group:group,members:members]
				}
	}

	def deleteGroup = {
				if(params.group){
					log.debug "requesting deletion of group $params.group"
					def id = params.group.toLong()
					def group = CollaborationGroup.get(id)
					def groupName = group.name
					def manager = securityService.findCollaborationManager(groupName)
					if(manager){
						if(securityService.deleteCollaborationGroup(manager.username,groupName)){
							log.debug "collaboration group, $groupName has been deleted"
							flash.message = "collaboration group, $groupName has been deleted"
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
						log.debug "collaboration group, $groupName has NOT been deleted, no manager found"
						flash.error = "collaboration group, $groupName has NOT been deleted, no manager found"
						redirect(action:index)
						return
					}
					
				}
	}
	
	
	def showUser = {
		if(params.user){
			log.debug "find user $params.user"
			def id = params.user.toLong()
			def user = GDOCUser.get(id)
			String listHQL = "SELECT count(distinct list.id) FROM UserList list JOIN list.author author " + 
			"WHERE author.username = :username "
			def listCount = UserList.executeQuery(listHQL,[username:user.username])
			String analysisHQL = "SELECT count(distinct analysis.id) FROM SavedAnalysis analysis JOIN analysis.author author " + 
			"WHERE author.username = :username "
			def analysisCount = SavedAnalysis.executeQuery(analysisHQL,[username:user.username])
			def isGdocAdmin = securityService.isUserGDOCAdmin(user.username)
			[user:user,listCount:listCount,analysisCount:analysisCount,isGdocAdmin:isGdocAdmin]
		}
	}
	
	def deleteUser = {
		if(params.user){
			log.debug "requesting deletion of user $params.user"
			def id = params.user.toLong()
			if(securityService.removeUser(id)){
				flash.message = "Deleted user $params.user"
				redirect(action:index)
				return
			}	
		}
	}
	
	
}