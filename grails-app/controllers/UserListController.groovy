import grails.converters.*

class UserListController {
    def securityService
	def userListService
	def exportService
	def annotationService
    def index = { redirect(action:list,params:params) }

    def list = {
		def lists = []
        lists = userListService.getAllLists(session.userId, session.sharedListIds)
		println lists
		println lists.size()	
        [ userListInstanceList: lists]
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
	
	def vennDiagram = {
		println params
		def author = GDOCUser.findByLoginName(params.author)
		def vennJSON = userListService.vennDiagram(params.listName,author,params.ids);
		def parsedJSON = JSON.parse(vennJSON.toString());
		println vennJSON
		def intersectedIds = parsedJSON
		flash.message = null
		[ vennJSON: vennJSON, intersectedIds: intersectedIds]
	}

	def tools = {
		def ids = []
		def listName = checkName(params.listName)
		if (params.userListIds && params.listAction){
			params.userListIds.each{
				ids.add(it)
			}
			def author = GDOCUser.findByLoginName(session.userId)
			def userListInstance
			if(params.listAction == 'intersect'){
				userListInstance = userListService.intersectLists(listName,author,ids);
			}else if(params.listAction == 'join'){
				userListInstance = userListService.uniteLists(listName,author,ids);
			}else if(params.listAction == 'diff'){
					userListInstance = userListService.diffLists(listName,author,ids);
			}else if(params.listAction == 'venn'){
					redirect(action:"vennDiagram",params:[listName:listName,author:session.userId,ids:ids])
			}
			if(userListInstance){
				if(userListInstance.save(flush:true)){
					flash.message = "UserList ${params.listName} created"
					def lists = userListService.getAllLists(session.userId, session.sharedListIds)
					render(template:"/userList/userListTable",model:[ userListInstanceList: lists ])
				}
				else{
					flash.message = "UserList ${params.listName} was not created"
					def lists = userListService.getAllLists(session.userId, session.sharedListIds)
					render(template:"/userList/userListTable",model:[ userListInstanceList: lists ])
				}
			}else{
				flash.message = "no common items between lists"
				def lists = userListService.getAllLists(session.userId, session.sharedListIds)
				render(template:"/userList/userListTable",model:[ userListInstanceList: lists ])
			}
		}else{
			println "no lists have been selected"
			flash.message = "no lists have been selected"
			def lists = userListService.getAllLists(session.userId, session.sharedListIds)
			render(template:"/userList/userListTable",model:[ userListInstanceList: lists ])
		}
	}

    def deleteList = {
        def userListInstance = UserList.get( params.id )
        if(userListInstance) {
            userListInstance.delete(flush:true)
			println "deleted " + userListInstance
			flash.message = userListInstance.name + " has been deleted"
			def lists = userListService.getAllLists(session.userId, session.sharedListIds)
           	render(template:"/userList/userListTable",model:[ userListInstanceList: lists ])
        }
        else {
            flash.message = "UserList not found with id ${params.id}"
			def lists = userListService.getAllLists(session.userId, session.sharedListIds)
			render(template:"/userList/userListTable",model:[ userListInstanceList: lists ])
        }
    }

	def deleteListItem = {
		println params
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
            render(view:"list",model:[ userListInstanceList: UserList.list( params ) ])
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
		println params
		if(!params["name"]){
			params["name"] = params["author.username"] + new Date().getTime();
		}
		def author = GDOCUser.findByLoginName(params["author.username"])
		params["author.id"] = author.id
		def listDup = author.lists().find {
			it.name == params["name"]
		}
		if(listDup) {
			render "List $params.name already exists"
			return
		}
		def userListInstance = new UserList(params)
		if(params.selectAll == "true") {
			//if patient list, save all gdocIds straight from result
			if(params["tags"].indexOf("patient") > -1) {
				session.results.each {
					userListInstance.addToListItems(new UserListItem(value:it.gdocId));
				}
			}
			//if gene symbol list, look up gene symbols from reporters straight from result
			else if(params["tags"].indexOf("gene") > -1){
				session.results.resultEntries.each{ ccEntry ->
						def geneSymbol = annotationService.findGeneForReporter(ccEntry.reporterId)
						if(geneSymbol){
							userListInstance.addToListItems(new UserListItem(value:geneSymbol));
						}
				}
			}
			//if reporters list, save reporters straight from result
			else{
				session.results.resultEntries.each{ ccEntry ->
					userListInstance.addToListItems(new UserListItem(value:ccEntry.reporterId));
				}
			}
		} else if(params['ids']){
			params['ids'].tokenize(",").each{
				it = it.replace('[','');
				it = it.replace(']','');
				//if gene symbols list, look up gene symbols from reporters (ids)
				if(params["tags"] && params["tags"].indexOf("gene") > -1){
					session.results.resultEntries.each{ ccEntry ->
						if(it.trim() == ccEntry.reporterId){
							def geneSymbol = annotationService.findGeneForReporter(ccEntry.reporterId)
							if(geneSymbol){
								userListInstance.addToListItems(new UserListItem(value:geneSymbol.trim()));
							}
						}
					}
				}else{
					//or just save the value of the id itself, after it has been cleaned up
					userListInstance.addToListItems(new UserListItem(value:it.trim()));
				}
				
			}
		}
		
		
        if(!userListInstance.hasErrors() && userListInstance.save()) {
				
						if(params["tags"]){
							params['tags'].tokenize(",").each{
								userListInstance.addTag(it);
							}
						}
						
				render "$params.name created succesfully"
        }
        else {
				render "Error creating $params.name list"
        }
    }

	def save = {
		def userListInstance = new UserList(params)
		if(params["ids"]){
			params['ids'].each{
				userListInstance.addToListItems(new UserListItem(value:it.trim()));
			}
		}
		 if(!userListInstance.hasErrors() && userListInstance.save()) {
			flash.message = "UserList ${userListInstance.id} created"
	            	redirect(action:show,id:userListInstance.id)
	        }
	        else {
				render(view:'create',model:[userListInstance:userListInstance])
	     }
		
	}
	
		
	
	def upload = {
		
	}
	
	def saveList = {
		//TODO: Validate list
		if(request.getFile("file").inputStream.text) {
			def author = GDOCUser.findByLoginName(session.userId)
			def listDup = author.lists().find {
				it.name == params["listName"]
			}
			if(listDup) {
				flash["message"] "List $params.listName already exists"
				redirect(action:upload)
			}
			def userList = new UserList()
			userList.name = params["listName"]
			userList.author = author
			request.getFile("file").inputStream.eachLine { value ->
				println value
				userList.addToListItems(new UserListItem(value:value.trim()))
			}
        	if(!userList.hasErrors() && userList.save()) {
				
					userList.addTag(params["listType"])
					flash["message"] = "$params.listName uploaded succesfully"
					redirect(action:upload,params:[success:true])
				
	        } else {
				flash["message"] =  "Error uploading $params.listName list"
				redirect(action:upload,params:[failure:true])
	        }
		}
		//redirect(action:upload)
	}
	
	def export = {
		println "EXPORTING LIST ${params.id}"
		response.setHeader("Content-disposition", "attachment; filename=${params.id}-export.txt")  
		response.contentType = "text/plain"
		exportService.exportList(response.outputStream, params.id)
	}
	
}
