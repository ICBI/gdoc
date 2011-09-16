class SecurityFilters {
   def securityService
   def springSecurityService
	
   def filters = {
        loginCheck(controller:'*', action:'*') {
		 before = {
			if(params.token && !controllerName.equals('activation')) {
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
						def user = GDOCUser.findByUsername(username)
						if(!user) {
							log.debug "no valid user" 
							redirect(controller:'home')
			                return false
						}
						log.debug "user token authenticated"
						if(session.userId || controllerName.equals('cytoscape')){
							log.debug "session exists or cytoscape, continue"
							return true
						}
						else{
							log.debug "NO user session exists, take to login page"
							redirect(controller:'home', action:'index',params:[desiredPage:controllerName])
			                return false
						}
			  } 
              else if(!springSecurityService.isLoggedIn() && !controllerName.equals('home') 
						&& !controllerName.equals('login') && !controllerName.equals('public')
						 && !controllerName.equals('registration') && !controllerName.equals('activation')
							&& !controllerName.equals('contact')
								&& !controllerName.equals('policies')) {
				  redirect(controller:'home', action:'index')
                  return false
              }
			else if(springSecurityService.isLoggedIn() && 
				(controllerName.equals('admin') || controllerName.equals('protectedArtifact')
					|| controllerName.equals('collaborationGroup') || controllerName.equals('GDOCUser')
						|| controllerName.equals('invitation') || controllerName.equals('membership')
							|| controllerName.equals('userOption') || controllerName.equals('role'))){
				//REFACTOR---isUserGDOCAdmin(currentUser.username)
				 def currentUser = springSecurityService.getPrincipal() 
				//log.debug currentUser.username + " is user an admin?" 
				if(!securityService.isUserGDOCAdmin(currentUser.username)){
					redirect(controller:'home', action:'index')
					log.debug currentUser.username + " is trying to access admin section(s) but is not an Administrator" 
	                return false
				}
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
			after = {
				// Clear out the ThreadLocal context variable before the thread
				// gets returned to the pool
				StudyContext.clear()
			}
		}
		// Added mapping to override searchable controller
		searchableCheck(controller: 'searchable', action:'*') {
			before = {
				redirect(controller:'search')
				return false
			}
		}
		
 	} 
}