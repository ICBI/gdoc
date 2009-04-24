class SecurityFilters {
   def filters = {
        loginCheck(controller:'*', action:'*') {
           before = {
              if(!session.userId && !controllerName.equals('home') && !controllerName.equals('login')) {
                  redirect(controller:'home')
                  return false
               }
           }
		}
		listCheck(controller:'userList', action:'*'){
			before = {
				println params
				println session.userId
				if((params["action"].equals("create")|| params["action"].equals("save")
					|| params["_action_Update"] || params["_action_Edit"] ||
						params["_action_Delete"]) && session.userId==null){
					println "user not logged in"
					flash.message = "you must be logged in to create or modify lists"
					redirect(action:'list')
					return false;
				}
				if(params["id"] && (params["_action_Update"] ||
						params["_action_Edit"] || params["_action_Delete"])){
							def list = UserList.findById(params["id"])
							def user = GDOCUser.findByUsername(session.userId)
							if(user!=null && list!=null){
								 println "user and list are in request"
								if(user.id == list.author.id){
									 println "user is also author"
									return true;
								}
								else{
									 println "user is not the author"
									flash.message = "you must be the author of this list to perform this action."
									redirect(action:'list')
									return false;
								}
							}else{
								flash.message = "the user list or the user cannot be found"
								redirect(action:'list')
								return false;
							}
					
				}
			    else{
				println "not modifying list"
				return true;
			}
			}
		}
		studyCheck(controller: '*', action:'*') {
			before = {
				println "in studycheck"
				if(session.study) {
					StudyContext.setStudy(session.study.schemaName)
				} else {
					StudyContext.setStudy("EDIN")
				}
			}
		}
 	} 
}