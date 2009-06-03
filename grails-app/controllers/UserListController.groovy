class UserListController {
    def securityService
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
		def lists = GDOCUser.findByLoginName(session.userId).lists()
		def listIds = []
		def sharedListIds = securityService.getSharedItemIds(session.userId, UserList.class.name)
		if(lists.metaClass.respondsTo(lists, "size")) {
				lists.each{
					listIds << it.id.toString()
				}
		} else {
				listIds << lists.id.toString()
		}
		if(sharedListIds.metaClass.respondsTo(sharedListIds, "size")) {
				sharedListIds.removeAll(listIds)
		} else {
				sharedListIds.remove(listIds)
		}	
		def sharedLists = []
		//until we modify ui, just add shared lists to 'all' lists
		sharedListIds.each{
			def foundList = UserList.get(it)
			if(foundList){
				lists << foundList
			}
		}
		lists = lists.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}	
        [ userListInstanceList: lists, sharedListInstanceList: sharedLists ]
    }

    def show = {
        def userListInstance = UserList.get( params.id )

        if(!userListInstance) {
            flash.message = "UserList not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ userListInstance : userListInstance ] }
    }


    def deleteList = {
        def userListInstance = UserList.get( params.id )
        if(userListInstance) {
            userListInstance.delete(flush:true)
           	render("")
        }
        else {
            flash.message = "UserList not found with id ${params.id}"
			render(template:"/userList/userListTable",model:[ userListInstanceList: UserList.findAll() ])
        }
    }

	def deleteListItem = {
		println params
        def userListItemInstance = UserListItem.findById(params["id"])
        if(userListItemInstance) {
			def list = UserList.findById(userListItemInstance.list.id)
			list.listItems.remove(userListItemInstance);
			userListItemInstance.delete(flush:true)	
            //flash.message = "UserList item ${params.id} deleted"
			list.save();
			render(template:"/userList/userListDiv",model:[ userListInstance: list, listItems:list.listItems ])
            //render(view:"list",model:[ userListInstanceList: UserList.findAll() ])
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
		if(params['ids']){
			params['ids'].tokenize(",").each{
				userListInstance.addToListItems(new UserListItem(value:it.trim()));
			}
		}
		
		
        if(!userListInstance.hasErrors() && userListInstance.save()) {
				def connection = new UserListConnection(list:userListInstance,user:userListInstance.author,rating:0);
				if(!connection.hasErrors() && connection.save()) {
						if(params["tags"]){
							params['tags'].tokenize(",").each{
								userListInstance.addTag(it);
							}
						}
						if(StudyContext.getStudy()){
						    userListInstance.addTag(StudyContext.getStudy());
						}
				render "$params.name created succesfully"
				}
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
				def connection = new UserListConnection(list:userListInstance,user:userListInstance.author,rating:0);
				if(!connection.hasErrors() && connection.save()) {
					flash.message = "UserList ${userListInstance.id} created"
	            	redirect(action:show,id:userListInstance.id)
				}
	        }
	        else {
				render(view:'create',model:[userListInstance:userListInstance])
	     }
		
	}
	
		/** move to list service **/
	def join = { 
		if(params["listAction"].equals('intersect')){
			println "intersect lists:"
			params["userListId"].each{
				println it
			}
		}
		if(params["listAction"].equals('join')){
			println "join lists:"
			params["userListId"].each{
				println it
			}
		}
		if(params["listAction"].equals('diff')){
			println "diff lists:"
			params["userListId"].each{
				println it
			}
		}
		redirect(action:list,params:params)
	}
	
	def calculateVenn = {
			def list= ['VEGF','EGFR','BRCA1','BRCA1','ER+','ER-','FCR','HGI']
			def list2= ['VEGF','EGFR','BRCA1','SCF7', 'QSR6','HGI']
			assert list.retainAll( list2 )
			    //remove 'b' and 'z', return true because list changed
			assert list == ['VEGF','EGFR','BRCA1','BRCA1','HGI']
			println list
			def listSet = list as Set
			println listSet
		
	}
	

	
}
