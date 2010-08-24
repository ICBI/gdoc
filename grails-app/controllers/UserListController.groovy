import grails.converters.*
import grails.orm.PagedResultList


class UserListController {
	private static Integer MAX_LIST_SIZE = 2000
    def securityService
	def userListService
	def exportService
	def annotationService
	def tagService
    def index = { redirect(action:list,params:params) }

    def list = {
		log.debug params
		def lists = []
		def timePeriods = [1:"1 day",7:"1 week",30:"past 30 days",90:"past 90 days",hideShared:"show my lists only",all:"show all"]
		def filteredLists = []
		def pagedLists = []
        lists = userListService.getAllLists(session.userId, session.sharedListIds)
		if(lists){
			def ids = lists.collect{it.id}
			def filteredListIds = []

			if(params.listFilter){
				if(params.listFilter == 'all'){
					session.listFilter = "all"
					filteredLists = lists
					filteredListIds = filteredLists.collect{it.id}
				}
				else{
					session.listFilter = params.listFilter
					filteredLists = userListService.filterLists(params.listFilter,lists,session.userId)
					filteredListIds = filteredLists.collect{it.id}
				}
			}
			else if(session.listFilter){
				filteredLists = userListService.filterLists(session.listFilter,lists,session.userId)
				filteredListIds = filteredLists.collect{it.id}
			}
			else{
				session.listFilter = "all"
				//filteredLists = userListService.filterLists(session.listFilter,lists)
				filteredListIds = ids
			}

			if(params.offset){
				pagedLists = userListService.getPaginatedLists(filteredListIds,params.offset.toInteger())
			}
			else{
				pagedLists = userListService.getPaginatedLists(filteredListIds,0)	
			}
		}
		
       [ userListInstanceList: pagedLists, allLists: lists, timePeriods: timePeriods]
    }

    def show = {
        def userListInstance = UserList.get( params.id )

        if(!userListInstance) {
            flash.message = "UserList not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ userListInstance : userListInstance ] }
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
	
	def vennDiagram = {
		log.debug params
		def author = GDOCUser.findByLoginName(params.author)
		def vennJSON = userListService.vennDiagram(params.listName,author,params.ids);
		def tags = userListService.gatherTags(params.ids)
		def tagsString = tags.toString()
		tagsString = tagsString.replace("[","")
		tagsString = tagsString.replace("]","")
		def parsedJSON = JSON.parse(vennJSON.toString());
		log.debug vennJSON
		flash.message = null
		def intersectedIds = parsedJSON
		[ vennJSON: vennJSON, intersectedIds: intersectedIds, tags: tagsString]
	}

	def tools = {
		def ids = []
		def listName = checkName(params.listName)
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
					redirect(action:"vennDiagram",params:[listName:listName,author:session.userId,ids:ids])
					return
			}
			if(userListInstance){
				flash.message = "UserList ${listName} created"
				def lists = userListService.getAllLists(session.userId, session.sharedListIds)
				def filteredLists = []
				if(session.listFilter){
					filteredLists = userListService.filterLists(session.listFilter,lists,session.userId)
				}
				redirect(action:list)
				//render(template:"/userList/userListTable",model:[ userListInstanceList: filteredLists ])
			}else{
				flash.message = "no items present in resulting list"
				def lists = userListService.getAllLists(session.userId, session.sharedListIds)
				def filteredLists = []
				if(session.listFilter){
					filteredLists = userListService.filterLists(session.listFilter,lists,session.userId)
				}
				redirect(action:list)
				//render(template:"/userList/userListTable",model:[ userListInstanceList: filteredLists ])
			}
		}else{
			log.debug "no lists have been selected"
			flash.message = "no lists have been selected"
			def lists = userListService.getAllLists(session.userId, session.sharedListIds)
			def filteredLists = []
			if(session.listFilter){
				filteredLists = userListService.filterLists(session.listFilter,lists,session.userId)
			}
			redirect(action:list)
			//render(template:"/userList/userListTable",model:[ userListInstanceList: filteredLists ])
		}
	}

    def deleteList = {
        def userListInstance = UserList.get( params.id )
        if(userListInstance) {
            userListInstance.delete(flush:true)
			log.debug "deleted " + userListInstance
			flash.message = userListInstance.name + " has been deleted"
			def lists = userListService.getAllLists(session.userId, session.sharedListIds)
			def filteredLists = []
			if(session.listFilter){
				filteredLists = userListService.filterLists(session.listFilter,lists,session.userId)
			}
           	render(template:"/userList/userListTable",model:[ userListInstanceList: filteredLists ])
        }
        else {
            flash.message = "UserList not found with id ${params.id}"
			def lists = userListService.getAllLists(session.userId, session.sharedListIds)
			def filteredLists = []
			if(session.listFilter){
				filteredLists = userListService.filterLists(session.listFilter,lists,session.userId)
			}
			render(template:"/userList/userListTable",model:[ userListInstanceList: filteredLists ])
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
						}else{
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
					}else{
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
			list.listItems.remove(userListItemInstance);
			userListItemInstance.delete(flush:true)	
            list.save();
			render(template:"/userList/userListDiv",model:[ userListInstance: list, listItems:list.listItems ])
        }
        else {
            flash.message = "UserList item not found with id ${params.id}"
			def lists = userListService.getAllLists(session.userId, session.sharedListIds)
			def filteredLists = []
			if(session.listFilter){
				filteredLists = userListService.filterLists(session.listFilter,lists,session.userId)
			}
            render(view:"list",model:[ userListInstanceList: filteredLists ])
        }
    }

	def getListItems = {
		log.debug params
		def userListInstance = UserList.get( params.id )
		def metadata = userListService.decorateListItems(userListInstance)
       	if(userListInstance) {
			def listItems = userListInstance.listItems
			render(template:"/userList/userListDiv",model:[ userListInstance: userListInstance, listItems:listItems, metadata:metadata])
        }
        else {
            flash.message = "UserList not found with id ${params.id}"
			def lists = userListService.getAllLists(session.userId, session.sharedListIds)
			def filteredLists = []
			if(session.listFilter){
				filteredLists = userListService.filterLists(session.listFilter,lists,session.userId)
			}
            render(view:"list",model:[ userListInstanceList: filteredLists ])
        }
    }

    def edit = {
        def userListInstance = UserList.get( params.id )

        if(!userListInstance) {
            flash.message = "UserList not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ userListInstance : userListInstance ]
        }
    }

    def update = {
        def userListInstance = UserList.get( params.id )
        if(userListInstance) {
            userListInstance.properties = params
            if(!userListInstance.hasErrors() && userListInstance.save()) {
                flash.message = "UserList ${params.id} updated"
                redirect(action:show,id:userListInstance.id)
            }
            else {
                render(view:'edit',model:[userListInstance:userListInstance])
            }
        }
        else {
            flash.message = "UserList not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def userListInstance = new UserList()
        def userInstance = GDOCUser.findByLoginName(session.userId)
        userListInstance.properties = params
        return ['userListInstance':userListInstance,"userInstance":userInstance]
    }


    def saveFromQuery = {
		log.debug params
		def author = GDOCUser.findByLoginName(session.userId)
		if(!params["name"]){
			params["name"] = author.loginName + new Date().getTime();
		}
		
		params["author.id"] = author.id
		def listDup = author.lists().find {
			it.name == params["name"]
		}
		if(listDup) {
			render "List $params.name already exists"
			return
		}
		log.debug "save list"
		def ids = []
		if(params.selectAll == "true") {
			log.debug "save all ids"
			//if patient list, save all gdocIds straight from result
			if(params["tags"].indexOf("patient") > -1) {
				session.results.each {
					ids << it.gdocId
				}
				
			}
			//if gene symbol list, look up gene symbols from reporters straight from result
			else if(params["tags"].indexOf("gene") > -1){
				if(session.results){
						session.results.resultEntries.each{ ccEntry ->
							def geneSymbol = annotationService.findGeneForReporter(ccEntry.reporterId)
							if(geneSymbol){
								ids << geneSymbol
							}
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
			if(ids.size > MAX_LIST_SIZE) {
				render "List cannot be larger than ${MAX_LIST_SIZE} items."
				return
			}
		} else if(params['ids']){
			log.debug "just save selected ids"
			params['ids'].tokenize(",").each{
				it = it.replace('[','');
				it = it.replace(']','');
				//if gene symbols list, look up gene symbols from reporters (ids)
				if(params["tags"] && params["tags"].indexOf("gene") > -1){
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
        if(userListInstance) {
				render "$params.name created succesfully"
        } else {
				render "Error creating $params.name list"
        }
    }

	def saveListFromExistingLists = {
		log.debug params
		def author = GDOCUser.findByLoginName(session.userId)
		if(!params["name"]){
			params["name"] = author.loginName + new Date().getTime();
		}
		
		params["author.id"] = author.id
		def listDup = author.lists().find {
			it.name == params["name"]
		}
		if(listDup) {
			render "List $params.name already exists"
			return
		}
		def ids = []
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
		def ids = []
		if(params["ids"]){
			params['ids'].each{
				ids << it.trim()
			}
		}
		if(ids.size > MAX_LIST_SIZE) {
			flash.message = "List cannot be larger than ${MAX_LIST_SIZE} items."
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
			def author = GDOCUser.findByLoginName(session.userId)
			def listDup = author.lists().find {
				it.name == params.newNameValue.trim()
			}
			if(listDup) {
				log.debug "List $params.newNameValue already exists"
				message = "List $params.newNameValue already exists"
				render(message)
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
			def author = GDOCUser.findByLoginName(session.userId)
			def listDup = author.lists().find {
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
				flash["message"]= "List $params.listName already exists"
				redirect(action:upload,params:[failure:true])
				return
			}else{
				def userList = new UserList()
				userList.name = params["listName"]
				userList.author = author 
				request.getFile("file").inputStream.eachLine { value ->
					//log.debug value
					if(value.trim())
						userList.addToListItems(new UserListItem(value:value.trim()))
				}
				if(userList.listItems.size() > MAX_LIST_SIZE) {
					flash.message = "List cannot be larger than ${MAX_LIST_SIZE} items."
					redirect(action:upload,params:[failure:true])
					return
				}
	        	if(!userList.hasErrors() && userList.save()) {

						userList.addTag(params["listType"])
						flash["message"] = "$params.listName uploaded succesfully"
						redirect(action:upload,params:[success:true])
						return

		        } else {
					flash["message"] =  "Error uploading $params.listName list"
					redirect(action:upload,params:[failure:true])
					return
		        }
			}
		}
		//redirect(action:upload)
	}
	
	def export = {
		log.debug "EXPORTING LIST ${params.id}"
		response.setHeader("Content-disposition", "attachment; filename=${params.id}-export.txt")  
		response.contentType = "text/plain"
		exportService.exportList(response.outputStream, params.id)
	}
	
	def exportToCytoscape = {
		log.debug "BUILDING AND EXPORTING TO CYTOSCAPE ${params.id}"
		def cytoscapeFiles = exportService.buildCytoscapeFiles(params.id)
		redirect(controller:"cytoscape",action:"index",params:[sifFile:cytoscapeFiles['sifFile'],edgeAttributeFile:cytoscapeFiles['edgeAttributeFile'],nodeAttributeFile:cytoscapeFiles['nodeAttributeFile'],geneAttributeFile:cytoscapeFiles['geneAttributeFile']])
		return
	}
	
}
