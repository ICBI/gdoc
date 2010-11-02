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
		def da = quickStartService.getDataAvailability()
		servletContext.setAttribute("dataAvailability", da)
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
						property('loginName')
						property('firstName')
						property('lastName')
						property('email')
						property('organization')
					}
					and{
						'order'("loginName", "asc")
					}
					or {
						eq("loginName", params.userId)
					}
				}
		}else{
			users = GDOCUser.createCriteria().list()
				{
					projections{
						property('id')
						property('loginName')
						property('firstName')
						property('lastName')
						property('email')
						property('organization')
					}
					and{
						'order'("loginName", "asc")
					}
				}
		}
		def columns = []
		columns << [index: "id", name: "User ID", sortable: true, width: '70']
		//columns << [index: "dataSourceInternalId", name: "PATIENT ID", sortable: true, width: '70']
		def columnNames = ["loginName","firstName","lastName","email","organization"]
		def userListings = []
		users.each{
			def userMap = [:]
			userMap["id"] = it[0]
			userMap["loginName"] = it[1]
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
			cells << user.loginName
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
	
	def showUser = {
		if(params.user){
			log.debug "find user $params.user"
			def id = params.user.toLong()
			def user = GDOCUser.get(id)
			String listHQL = "SELECT count(distinct list.id) FROM UserList list JOIN list.author author " + 
			"WHERE author.loginName = :loginName "
			def listCount = UserList.executeQuery(listHQL,[loginName:user.loginName])
			String analysisHQL = "SELECT count(distinct analysis.id) FROM SavedAnalysis analysis JOIN analysis.author author " + 
			"WHERE author.loginName = :loginName "
			def analysisCount = SavedAnalysis.executeQuery(analysisHQL,[loginName:user.loginName])
			def isGdocAdmin = securityService.isUserGDOCAdmin(user.loginName)
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