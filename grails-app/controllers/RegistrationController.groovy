import grails.converters.*


class RegistrationController {

	def securityService
	
    def index = {
		def departmentList = ["Oncology","Pathology","Radiation Medicine","Biostatistics","Bioinformatics",
		"Biomathmatics","Student"]
		[departmentList:departmentList]
	}
	
	def register = { RegistrationCommand cmd ->
		log.debug "netId : " + cmd.netId
		log.debug "department:" + cmd.department 
		log.debug cmd.errors
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			//log.debug cmd.errors
			redirect(action:'index')
		} else {
			log.debug "no errors, begin registration"
			flash['cmd'] = cmd
			//check if user already exists
			def existingUser = GDOCUser.findByLoginName(cmd.netId)
			if(existingUser){
				log.debug cmd.netId + " already exists as a user in the G-DOC system. Use Net-Id credentials to login above"
				flash.message = cmd.netId + " already exists as a user in the G-DOC system. Use Net-Id credentials to login above"
				redirect(action:'index')
				return
			}else{
				//if user doesn't exist, validate the netID
				def newUser = securityService.validateNetId(cmd.netId.trim(), cmd.department)
				if(newUser){
					//check to make sure user has required fields
					if(newUser.getLoginName() && newUser.getFirstName() && newUser.getLastName()){
						//if user's netId is valid, add user to G-DOC system
						if(securityService.createUser(newUser)){
							//add to PUBLIC collab group
							def managerPublic = securityService.findCollaborationManager("PUBLIC")
							securityService.addUserToCollaborationGroup(managerPublic.loginName, newUser.getLoginName(), "PUBLIC")
							flash.message = "Your account has been created in G-DOC!" +  
							" Please login above with your NET ID credentials. " + 
							"Your current permissions allow you to view public data sets. Once logged in you may gain access to " +
							"other data sets by emailing the POC directly from the 'Study DataSource' page. You can also request " + 
							"access to the study group via the 'Collaboration Groups' page."
							redirect(controller:'home', action:'index')
							return
						}else{
							log.debug "system error adding the user to G-DOC"
							flash.message = "We're sorry, there was a system error adding the user to G-DOC. Please try again."
							redirect(action:'index')
							return
						}
					}else{
						log.debug "user has NET ID, but missing a required field (loginName, firstName or lastName)"
						flash.message = "There was a system error adding the user to G-DOC. The user may be missing a first and/or last name in the system."
						redirect(action:'index')
						return
					}
				}else{
					log.debug cmd.netId + " is an invalid Net-Id. Contact Georgetown University Administration to obtain Net-Id."
					flash.message = cmd.netId + " is an invalid Net-Id. Contact Georgetown University Administration to obtain Net-Id."
					redirect(action:'index')
					return
				}
			}
			
			redirect(action:'index')
		}

	}
	
}
