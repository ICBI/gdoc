import java.net.URLEncoder
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class ActivationController {
	def securityService
	
    def newAccount = {
		log.debug "preparing to create a new account"
		if(params.token) {
			String token = new String(params.token.getBytes(), "UTF-8");
			def validRequestor = securityService.validateToken(token)
				if(!validRequestor){
					log.debug "invalid token"
					flash.error = "This request has expired and is no longer valid"
					redirect(controller:'home',action:"index")
				}else{
					log.debug "user token authenticated"
					[userId:validRequestor]
				}		
		  }
		else{
			redirect(controller:'policies',action:"deniedAccess")
			return
		}
		
	}
	
	
	def reset = {
		log.debug "preparing to reset an existing account"
		if(params.token) {
			String token = new String(params.token.getBytes(), "UTF-8");
			def validRequestor = securityService.validateToken(token)
				if(!validRequestor){
					log.debug "invalid token"
					flash.message = "This request for a new account is has expired and is no longer valid"
					redirect(controller:'home',action:"index")
				}else{
					log.debug "user token authenticated"
					[userId:validRequestor]
				}		
		  }
		else{
			redirect(controller:'policies',action:"deniedAccess")
			return
		}
	}
	
	def resetPassword = {
		ResetPasswordCommand cmd ->
		log.debug "userId : " + cmd.userId
		log.debug "password : " + cmd.password
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			log.debug cmd.errors
			def baseUrl = CH.config.grails.serverURL
			def token = cmd.userId + "||" + System.currentTimeMillis()
			def resetUrl = baseUrl+"/gdoc/activation/reset?token=" + URLEncoder.encode(EncryptionUtil.encrypt(token), "UTF-8")
			redirect(url:resetUrl)
			return
		}
		else{
			flash['cmd'] = cmd
			log.debug "resetting password"
			if(securityService.changeUserPassword(cmd.userId,cmd.password)){
				def u = GDOCUser.findByLoginName(cmd.userId)
				if(session.userId){
					flash.message = "You have successfully changed your password."
					log.debug "$cmd.userId successfully changed password."
					redirect(controller:'workflows',action:"index")
					return
				}else{
					flash.message = "You have successfully changed your password."
					log.debug "$cmd.userId successfully changed password."
					redirect(controller:'home',action:"index")
					return
				}
				
			}else{
				log.debug "$cmd.userId password has NOT been reset"
				flash.message = "$cmd.userId password has NOT been reset. Please try again or contact the help desk"
				redirect(controller:'home',action:"index")
			}
		}
	}
	
	
	def activateAccount = {
		ActivationCommand cmd ->
		log.debug "userId : " + cmd.userId
		log.debug "password : " + cmd.password
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			log.debug cmd.errors
			def baseUrl = CH.config.grails.serverURL
			def token = cmd.userId + "||" + System.currentTimeMillis()
			def activateUrl = baseUrl+"/gdoc/activation/newAccount?token=" + URLEncoder.encode(EncryptionUtil.encrypt(token), "UTF-8")
			redirect(url:activateUrl)
			return
		}
		else{
			flash['cmd'] = cmd
			def newUser
			def existingUser = GDOCUser.findByLoginName(cmd.userId)
			if(existingUser){
				log.debug "user already exists"
				flash.message = "This user already exists in the system"
				redirect(controller:'home',action:"index")
			}
			log.debug "now add the user to system with public access"
			try{
				newUser = securityService.populateNewUserAttributes(cmd.userId,cmd.password,cmd.firstName,cmd.lastName,cmd.userId,cmd.organization)
			}catch (SecurityException se){
				log.debug "user not added " + se
				flash.message = "There was a problem adding user to the system. Please contact G-DOC help desk."
				redirect(controller:'home',action:"index")
			}
			if(securityService.createUser(newUser)){
				//add to PUBLIC collab group
				def managerPublic = securityService.findCollaborationManager("PUBLIC")
				securityService.addUserToCollaborationGroup(managerPublic.loginName, newUser.getLoginName(), "PUBLIC")
			}
			flash.message = "Welcome ... your account has been created in G-DOC!" +  
			" Please login above with your credentials. " + 
			"Your current permissions allow you to view public data sets. Once logged in you may gain access to " +
			"other data sets by emailing the POC directly from the 'Study DataSource' page. You can also request " + 
			"access to the study group via the 'Collaboration Groups' page."
			redirect(controller:'home', action:'index')
			return
		}
	}
	
}
