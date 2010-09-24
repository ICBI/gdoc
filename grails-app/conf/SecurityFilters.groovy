class SecurityFilters {
   def securityService
	
   def filters = {
        loginCheck(controller:'*', action:'*') {
		 before = {
			if(params.token) {
						String token = new String(params.token.getBytes(), "UTF-8");
						String decryptedToken = EncryptionUtil.decrypt(token);
						String[] info = decryptedToken.split("\\|\\|");
						String username = info[0];
						log.debug "$username accessing gdoc" 
						Long timeRequested = Long.parseLong(info[1]);
						Long currentTime = System.currentTimeMillis();
						Long diff = currentTime - timeRequested;
						Long hours = diff / (60 * 60 * 1000);
						if(hours > 24L) {
							log.debug "time has expired" 
							redirect(controller:'home')
			                return false
						}
						def user = GDOCUser.findByLoginName(username)
						if(!user) {
							log.debug "no valid user" 
							redirect(controller:'home')
			                return false
						}
						log.debug "user token authenticated"
			  } 
              else if(!session.userId && !controllerName.equals('home') 
						&& !controllerName.equals('login')
						 && !controllerName.equals('registration')
							&& !controllerName.equals('contact')
								&& !controllerName.equals('policies')) {
				  redirect(controller:'home', action:'index')
                  return false
              }
			else if(session.userId && controllerName.equals('admin')){
				if(!session.isGdocAdmin){
					redirect(controller:'home', action:'index')
					log.debug "$session.userId tried to access the admin panel but is not a GDOC Administrator" 
	                return false
				}
			}
           }
		}
		listCheck(controller:'userList', action:'*'){
			before = {
				log.debug params
				log.debug session.userId
				if((params["action"].equals("create")|| params["action"].equals("save")
					|| params["_action_Update"] || params["_action_Edit"] ||
						params["_action_Delete"]) && session.userId==null){
					log.debug "user not logged in"
					flash.message = "you must be logged in to create or modify lists"
					redirect(action:'list')
					return false;
				}
				if(params["id"] && (params["_action_Update"] ||
						params["_action_Edit"] || params["_action_Delete"])){
							def list = UserList.findById(params["id"])
							def user = GDOCUser.findByLoginName(session.userId)
							if(user!=null && list!=null){
								 log.debug "user and list are in request"
								if(user.id == list.author.id){
									 log.debug "user is also author"
									return true;
								}
								else{
									 log.debug "user is not the author"
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
				log.debug ""
				return true;
			}
			}
		}
		studyCheck(controller: '*', action:'*') {
			before = {
				if(session.study) {
					StudyContext.setStudy(session.study.schemaName)
				} else {
					//StudyContext.setStudy("EDIN")
				}
			}
		}
		// Added mapping to override searchable controller
		searchableCheck(controller: 'searchable', action:'*') {
			before = {
				redirect(controller:'search')
				return false
			}
		}
		//removed this filter as there may be public compounds / targets
		/**structureRetriever(controller:'moleculeTarget', action:'*') {
			before = {
				if(session.myCollaborationGroups.contains("DDG_COLLAB")){
					//println "allow user $session.userId to Drug Discovery Group information"
					return true
				}
				else{
					println "user $session.userId not in Drug Discovery Group"
					flash.message = "You must be a member of the DDG_COLLAB group to view the requested content. Request access below."
					redirect(controller:'collaborationGroups',params:["unauthorizedGroup":"DDG_COLLAB"])
					return false
				}
			}
		}**/
 	} 
}