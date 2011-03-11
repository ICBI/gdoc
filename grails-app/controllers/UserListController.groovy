import grails.converters.*
import grails.orm.PagedResultList

@Mixin(ControllerMixin)
class UserListController {
	private static Integer MAX_LIST_SIZE = 2000
    def securityService
	def userListService
	def exportService
	def annotationService
	def vennService
	def searchableService
	def tagService
	def htDataService
    def index = { redirect(action:list,params:params) }

    def list = {
		log.debug params
		def lists = []
		//removed : 1:"-1 day",7:"-1 week",
		def timePeriods = [hideShared:"Show my Lists",search:"Search my lists",my_gene:"-my gene lists",my_reporter:"-my reporter lists",my_patient:"-my patient lists",30:"-past 30 days",90:"-past 90 days",180:"-past 6 months",all:"Show all Lists (includes shared lists)",gene:"-gene lists",reporter:"-reporter list",patient:"-patient",onlyShared:"Show only shared"]
		def filteredLists = []
		def searchTerm = params.searchTerm
		def pagedLists = []
			if(params.listFilter){
				session.listFilter = params.listFilter
			}
			else if (session.listFilter){
				log.debug "current session list filter is $session.listFilter"
			}else{
				session.listFilter = "all"
			}
			if(params.offset){
				pagedLists = userListService.getPaginatedLists(session.listFilter,session.sharedListIds,params.offset.toInteger(),session.userId,searchTerm)
			}
			else{
				pagedLists = userListService.getPaginatedLists(session.listFilter,session.sharedListIds,0,session.userId,searchTerm)	
			}
		def listSnapShots = []
		listSnapShots = pagedLists["snapshot"]//userListService.getAllListsNoPagination(session.userId,session.sharedListIds)
       [ userListInstanceList: pagedLists["results"], allLists: pagedLists["count"][0], timePeriods: timePeriods, toolsLists:listSnapShots]
    }

	def checkName(name){
		def listName
		if(name){
			listName = name
		}else{
			listName = "list_"+System.currentTimeMillis().toString()
		}
		return listName
	}
	
	def addTag = {
		log.debug params
		if(params.id && params.tag){
			def list = tagService.addTag(UserList.class.name,params.id,params.tag.trim())
			if(list){
				render list.tags
			}
			else{
				render ""
			}
		}
	}
	
	def removeTag = {
		log.debug params
		if(isListAuthor(params.id)){
			if(params.id && params.tag){
				def list = tagService.removeTag(UserList.class.name,params.id,params.tag.trim())
				if(list){
					render list.tags
				}
				else{
					render ""
				}
			}
		}
		else{
			log.debug "user is NOT permitted to remove tag from list"
			redirect(controller:policies,action:deniedAccess)
		}
	}
	
	def venn = {
		log.debug params
		def author = GDOCUser.findByUsername(session.userId)
		def sortedLists = []
		def unitedStudies = []
		def allListsNames = []
		sortedLists = vennService.organizeVennLists(params.ids)
		def tags = userListService.gatherTags(params.ids)
		def tagsString = tags.toString()
		tagsString = tagsString.replace("[","")
		tagsString = tagsString.replace("]","")
		//if this is a patient list, set study
		sortedLists.each{
			allListsNames << it.name
			
		}
		if(tags && tags.contains(Constants.PATIENT_LIST)){
			sortedLists.each{ list ->
				if(list.studies){
					list.studies.each{
						unitedStudies << it.schemaName
					}
				 }
			}
			if(unitedStudies){
				StudyContext.setStudy(unitedStudies[0])
				loadCurrentStudy(); 
			}
		}
		else{
			session.study = null
		}
		def results
		if(sortedLists.size() == 2){
			results = vennService.createIntersection2ListDictionary(sortedLists)
		}
		else if(sortedLists.size() == 3){
			results = vennService.createIntersection3ListDictionary(sortedLists)
		}
		else if(sortedLists.size() == 4){
			results = vennService.createIntersection4ListDictionary(sortedLists)
		}
		session.results = null
		def names = results.dictionary["names"]
		[dictionary:results.dictionary as JSON,compartments:names as JSON,vennNumbers:results.vennData,graphData:results.graphData,allListsNames:allListsNames as JSON,tags: tagsString]
	}
	
	def vennDiagram = {
		log.debug params
		def author = GDOCUser.findByUsername(params.author)
		def vennJSON = vennService.vennDiagram(params.listName,author,params.ids);
		def tags = userListService.gatherTags(params.ids)
		def tagsString = tags.toString()
		tagsString = tagsString.replace("[","")
		tagsString = tagsString.replace("]","")
		def parsedJSON = JSON.parse(vennJSON.toString());
		log.debug vennJSON
		flash.message = null
		def intersectedIds = parsedJSON
		//intersectedIds: intersectedIds, 
		[ vennJSON: vennJSON, tags: tagsString]
	}

	def tools = {
		def ids = []
		def listName = checkName(params.listName)
		if(params.listAction == 'intersect' ||
			params.listAction == 'join' || 
				params.listAction == 'diff'){
			def author = GDOCUser.findByUsername(session.userId)
			def listDup = author.lists.find {
						it.name == listName
			}
			if(listDup) {
				log.debug "List did  not save, $listName already exists as a list"
				flash.error = "List did  not save, $listName already exists in your lists"
				redirect(action:list)
				return
			}
			if(!userListService.validListName(listName)){
				log.debug "List did  not save, invalid characters were found in name, $listName"
				flash.error = "List did  not save, invalid characters were found in name, $listName. Please try again."
				redirect(action:list)
				return
			}
		}
		if (params.userListIds && params.listAction){
			params.userListIds.each{
				ids.add(it)
			}
			def userListInstance
			def tags
			if(params.listAction == 'intersect'){
				userListInstance = userListService.intersectLists(listName,session.userId,ids);
			}else if(params.listAction == 'join'){
				userListInstance = userListService.uniteLists(listName,session.userId,ids);
			}else if(params.listAction == 'diff'){
				userListInstance = userListService.diffLists(listName,session.userId,ids);
			}else if(params.listAction == 'venn'){
					redirect(action:"venn",params:[listName:listName,author:session.userId,ids:ids])
					return
			}
			if(userListInstance){
				flash.message = "UserList ${listName} created"
				session.listFilter = "hideShared"
				session.results = null
				redirect(action:list)
			}else{
				flash.error = "List not created. No items found"
				redirect(action:list)
			}
		}else{
			log.debug "no lists have been selected"
			flash.error = "no lists have been selected"
			redirect(action:list)
		}
	}

    def deleteList = {
		if(isListAuthor(params.id)){
			log.debug "user is permitted to delete list"
			userListService.deleteList(params.id)
			redirect(action:list)
		}
		else{
			log.debug "user is NOT permitted to delete list"
			redirect(controller:'policies',action:'deniedAccess')
		}
    }

	

	def deleteMultipleLists ={
		def message = ""
		if(params.deleteList){
			log.debug "Requesting deletion of: $params.deleteList"
			if(params.deleteList.metaClass.respondsTo(params.deleteList, "max")){
				for(String listIdToBeRemoved : params.deleteList){
					print listIdToBeRemoved + " "
					def userListInstance = UserList.get(listIdToBeRemoved)
			        if(userListInstance) {
			            if(userListInstance.evidence){
							log.debug "could not delete " + userListInstance + ", this link represents a piece of evidence in a G-DOC finding"
							message += " $userListInstance.name could not be deleted because it represents a piece of evidence in a G-DOC finding."
						}
						else if(userListInstance.author.username != session.userId){
							log.debug "did not delete " + userListInstance + ", you are not the author."
							message += "did not delete $userListInstance.id , you are not the author."
						}
						else{
			            	userListInstance.delete(flush:true)
							log.debug "deleted " + userListInstance
							message += " $userListInstance.name has been deleted."
						}
					}
				}
			}else{
				def userListInstance = UserList.get(params.deleteList)
		        if(userListInstance) {
					if(userListInstance.evidence){
						log.debug "could not delete " + userListInstance + ", this link represents a piece of evidence in a G-DOC finding"
						message = "$userListInstance.name could not be deleted because it represents a piece of evidence in a G-DOC finding."
					}
					else if(userListInstance.author.username != session.userId){
						log.debug "did not delete " + userListInstance + ", you are not the author."
						message += "did not delete $userListInstance.id , you are not the author."
					}
					else{
		            	userListInstance.delete(flush:true)
						log.debug "deleted " + userListInstance
						message = "$userListInstance.name has been deleted."
					}
				}
			}
			flash.message = message
			redirect(action:list)
			return
		}else{
			flash.message = "No user list(s) have been selected for deletion"
			redirect(action:list)
			return
		}
		
	}

	def deleteListItem = {
		log.debug params
        def userListItemInstance = UserListItem.findById(params["id"])
        if(userListItemInstance) {
			def list = UserList.findById(userListItemInstance.list.id)
			if(list.author.username == session.userId){
				list.listItems.remove(userListItemInstance);
				userListItemInstance.delete(flush:true)	
	            list.save();
				render(template:"/userList/userListDiv",model:[ userListInstance: list, listItems:list.listItems ])
			}
			else {
	            flash.message = "UserList item not found with id ${params.id} or you are not the author"
				redirect(action:list)
	        }
        }
        
    }

	def getListItems = {
		log.debug params
		def userListInstance = UserList.get( params.id )
		def metadata = userListService.decorateListItems(userListInstance)
       	if(userListInstance) {
			def listItems = userListInstance.listItems
			listItems = listItems.sort{it.value}
			render(template:"/userList/userListDiv",model:[ userListInstance: userListInstance, listItems:listItems, metadata:metadata])
        }
        else {
            flash.message = "UserList not found with id ${params.id}"
			redirect(action:list)
        }
    }


    def saveFromQuery = {
		log.debug params
		def author = GDOCUser.findByUsername(session.userId)
		if(!params["name"]){
			params["name"] = author.username + new Date().getTime();
		}
		
		params["author.id"] = author.id
		def listDup = author.lists.find {
			it.name == params["name"]
		}
		if(listDup) {
			render "List $params.name already exists. Please choose different name and try again."
			return
		}
		if(!userListService.validListName(params["name"])){
			render "List did  not save, invalid characters were found in name, $params.name. Please try again."
			return
		}
		log.debug "save list"
		def ids = new HashSet();
		if(params.selectAll == "true") {
			log.debug "save all ids as Set"
			def tags = params.tags.split(",").toList()
			//if patient list, save all gdocIds straight from result
			if(tags.contains("patient")) {
				session.results.each {
					ids << it.gdocId
				}
				
			}
			
			//if gene symbol list, look up gene symbols from reporters straight from result
			else if(tags.contains("gene")){
				if(session.results){
						session.resultTable.each{ row ->
							ids << row.geneName
						}
				}
			}
			//if reporters list, save reporters straight from result
			else{
				if(session.results){
					session.results.resultEntries.each{ ccEntry ->
						ids << ccEntry.reporterId
					}
				}
			}
			if(ids.size() > MAX_LIST_SIZE) {
				render "List cannot be larger than ${MAX_LIST_SIZE} items. Please select a subset and try again."
				return
			}
		} else if(params['ids']){
			log.debug "just save selected ids as Set"
			params['ids'].tokenize(",").each{
				it = it.replace('[','');
				it = it.replace(']','');
				//if gene symbols list, look up gene symbols from reporters (ids)
				def tags = params.tags.split(",").toList()
				if(tags && tags.contains("gene")){
					log.debug "contains gene"
					if(session.results && session.results.resultEntries){
						session.results.resultEntries.each{ ccEntry ->
							if(it.trim() == ccEntry.reporterId){
								def geneSymbol = annotationService.findGeneForReporter(ccEntry.reporterId)
								if(geneSymbol){
									ids << geneSymbol.trim()
								}
							}
						}
					}else{
						ids << it.trim()
					}
				}else{
					//or just save the value of the id itself, after it has been cleaned up
					ids << it.trim()
				}
				
			}
		}
		def tags = []
		if(params["tags"]){
			params['tags'].tokenize(",").each{
				tags << it
			}
		}
		def userListInstance = userListService.createList(session.userId, params.name, ids, [StudyContext.getStudy()], tags)
        if(userListInstance){
			render "$params.name created succesfully"
		}
		else {
				render "Error creating $params.name list"
        }
    }

	def saveListFromExistingLists = {
		log.debug params
		def author = GDOCUser.findByUsername(session.userId)
		if(!params["name"]){
			params["name"] = author.username + new Date().getTime();
		}
		
		params["author.id"] = author.id
		def listDup = author.lists.find {
			it.name == params["name"]
		}
		if(listDup) {
			render "List $params.name already exists"
			return
		}
		if(!userListService.validListName(params["name"])){
			render "List did  not save, invalid characters were found in name, $params.name. Please try again."
			return
		}
		def ids = new HashSet();
		if(params['ids']){
			params['ids'].tokenize(",").each{
				it = it.replace('[','');
				it = it.replace(']','');
				// just save the value of the id itself, after it has been cleaned up
					ids << it.trim()
			}
		}
		def tags = []
		if(params["tags"]){
			params['tags'].tokenize(",").each{
				tags << it
			}
		}
		def studies = new HashSet<String>()
		if(params["studies"]){
			params['studies'].tokenize(",").each{
				it = it.replace('[','');
				it = it.replace(']','');
				studies << it
			}
		}else{
			studies << StudyContext.getStudy()
		}
		def userListInstance = userListService.createList(session.userId, params.name, ids, studies , tags)
        if(userListInstance) {
				render "$params.name created succesfully"
        } else {
				render "Error creating $params.name list"
        }
	}

	def save = {
		def ids = new HashSet();
		if(params["ids"]){
			params['ids'].each{
				ids << it.trim()
			}
		}
		if(ids.size() > MAX_LIST_SIZE) {
			flash.message = "List cannot be larger than ${MAX_LIST_SIZE} items."
			return
		}
		if(!userListService.validListName(params["name"])){
			render "List did  not save, invalid characters were found in name, $params.name. Please try again."
			return
		}
		def userListInstance = userListService.createList(session.userId, params.name, ids, [StudyContext.getStudy()], [])
		 if(userListInstance) {
			flash.message = "UserList ${userListInstance.name} created"
	            	redirect(action:show,id:userListInstance.id)
	        }
	        else {
				render(view:'create',model:[userListInstance:userListInstance])
	     }
		
	}
	
	def createFromKm = {
		def ids = request.JSON['ids']
		def tags = request.JSON['tags']
		def studies = []
		studies << StudyContext.getStudy()
		def name = request.JSON['name']
		def returnVal = userListService.createList(session.userId, name, ids, studies, tags)
		render returnVal as JSON
	}	
	
	def upload = {
		
	}
	
	def renameList = {
		log.debug params
		def message = ""
		if(params.newNameValue && params.id){
			def author = GDOCUser.findByUsername(session.userId)
			def listDup = author.lists.find {
				it.name == params.newNameValue.trim()
			}
			if(listDup) {
				log.debug "List not saved. $params.newNameValue already exists"
				message = "List not saved. $params.newNameValue already exists."
				render("<span class='errorDetail'>"+message+"</span")
			}
			else if(!userListService.validListName(params.newNameValue)){
				log.debug "List $params.newNameValue contains invalid characters"
				message = "List did not save because invalid characters found in $params.newNameValue. Please try again."
				render("<span class='errorDetail'>"+message+"</span")	
			}else{
				def userListInstance = UserList.get( params.id )
				userListInstance.name = params.newNameValue
				if(userListInstance.save()){
					message = "updated list $params.id to $params.newNameValue"
					render(message)
				}
			}
		}
	}
	
	
	def saveList = {
		//TODO: Validate list
		if(!params.listName){
			log.debug "List needs to be named"
			flash["message"]= "please name this list"
			redirect(action:upload,params:[failure:true])
			return
		}
		if(!request.getFile("file").inputStream.text){
			log.debug "List needs a file associated with it"
			flash["message"]= "please select a file to be uploaded and verify the file contains data"
			redirect(action:upload,params:[failure:true])
			return
		}
		
		if(request.getFile("file").inputStream.text) {
			log.debug "upload list: $params.listName"
			def author = GDOCUser.findByUsername(session.userId)
			def listDup = author.lists.find {
				it.name == params["listName"]
			}
			if(request.getFile("file").getOriginalFilename().lastIndexOf(".txt") == -1){
				log.debug "List $params.listName needs to be formatted as plain-text .txt file"
				flash["message"]= "List $params.listName needs to be formatted as plain-text .txt file"
				redirect(action:upload,params:[failure:true])
				return
			}
			
			
			if(listDup) {
				log.debug "List $params.listName already exists"
				flash["error"]= "List $params.listName already exists"
				redirect(action:upload,params:[failure:true])
				return
			}else if(!userListService.validListName(params["listName"])){
				log.debug "List $params.listName contains invalid characters"
				flash["error"]= "List did not save because invalid characters found in $params.listName. Please try again."
				redirect(action:upload,params:[failure:true])
				return
			}else{
				def userList = new UserList()
				userList.name = params["listName"]
				userList.author = author 
				def items = new HashSet();
				request.getFile("file").inputStream.eachLine { value ->
					//log.debug value
					if(value.trim())
						items << value.trim()
				}
				items.each{ item->
					if (item =~ /^(\w|@|\'|:|\.|\(|\)|\/|-)+$/ && item.length() <= 255)
						userList.addToListItems(new UserListItem(value:item))
				}
				log.debug "size: " + userList.listItems?.size()
				log.debug "listItems: " + userList.listItems
				if(!userList.listItems) {
					flash.error = "List cannot be empty."
					redirect(action:upload,params:[failure:true])
					return
				}
				if(userList.listItems?.size() > MAX_LIST_SIZE) {
					flash.error = "List cannot be larger than ${MAX_LIST_SIZE} items."
					redirect(action:upload,params:[failure:true])
					return
				}
	        	if(!userList.hasErrors() && userList.save()) {

						userList.addTag(params["listType"])
						flash["message"] = "$params.listName uploaded succesfully"
						redirect(action:upload,params:[success:true])
						return

		        } else {
					flash["error"] =  "Error uploading $params.listName list"
					redirect(action:upload,params:[failure:true])
					return
		        }
			}
		}
		//redirect(action:upload)
	}
	
	def export = {
		if(isListAccessible(params.id)){
			log.debug "EXPORTING LIST ${params.id}"
			response.setHeader("Content-disposition", "attachment; filename=${params.id}-export.txt")  
			response.contentType = "text/plain"
			exportService.exportList(response.outputStream, params.id)
		}
		else{
			log.debug "user is NOT permitted to export list"
			redirect(controller:'policies',action:'deniedAccess')
			return
		}
	}
	
	def exportToCytoscape = {
		if(isListAccessible(params.id)){
			log.debug "BUILDING AND EXPORTING TO CYTOSCAPE ${params.id}"
			def cytoscapeFiles = exportService.buildCytoscapeFiles(params.id)
			redirect(controller:"cytoscape",action:"index",params:[sifFile:cytoscapeFiles['sifFile'],edgeAttributeFile:cytoscapeFiles['edgeAttributeFile'],nodeAttributeFile:cytoscapeFiles['nodeAttributeFile'],geneAttributeFile:cytoscapeFiles['geneAttributeFile']])
			return
		}
		else{
			log.debug "user is NOT permitted to export list"
			redirect(controller:'policies',action:'deniedAccess')
			return
		}
	}

	
}
