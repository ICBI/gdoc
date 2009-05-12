class UserListController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
        [ userListInstanceList: UserList.list( params ) ]
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
            //flash.message = "UserList ${params.id} deleted"
			//redirect(uri:"/userList/list")
			render("")
			//render(template:"/userList/userListTable",model:[ userListInstanceList: UserList.findAll()])
            //render(view:li, model:[ userListInstanceList: UserList.findAll() ])
        }
        else {
            flash.message = "UserList not found with id ${params.id}"
			render(template:"/userList/userListTable",model:[ userListInstanceList: UserList.findAll() ])
            //redirect(action:list, model:[ userListInstanceList: UserList.findAll() ])
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
}
