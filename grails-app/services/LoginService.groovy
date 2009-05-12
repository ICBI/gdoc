import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException
import LoginException

class LoginService{
	
	def login(params) throws LoginException{
		def user = GDOCUser.findByLoginName(params.loginName)
		try{
			AuthenticationManager authenticationManager = SecurityServiceProvider.getAuthenticationManager("gdoc");
			//print authenticationManager.getApplicationContextName();
			boolean loginOK = authenticationManager.login(params.loginName, params.password);
			if (loginOK){
				System.out.println("SUCESSFUL LOGIN");
			}
			else {
				System.out.println("ERROR IN LOGIN");
				throw new LoginException("error in authentication");
			}
			}catch (CSException cse){
				System.out.println("ERROR IN LOGIN -- CS Exception");
				cse.printStackTrace(System.out);
				throw new LoginException("error in authentication");
		}
		
		try {
			AuthorizationManager authorizationManager = SecurityServiceProvider.getAuthorizationManager("gdoc");
			print "got user " + authorizationManager + " from csm, ready to authorize "
			Set memberships = user.getGroups();
			memberships.each{ membership ->
				println membership.group.groupName
			}
		}catch ( CSException cse){
			System.out.println("ERROR IN AUTHORIZATION ");
			cse.printStackTrace(System.out);
			throw new LoginException("error in authorization");
		}
		
		return user
		
	}
	
	def logout(session){
		session.invalidate()
	}
	
}