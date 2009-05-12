import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException
import LoginException

class LoginService{
	
	def login(params) throws LoginException{
		def user = GDOCUser.findByLogin_name(params.login_name)
		try{
			AuthenticationManager authenticationManager = SecurityServiceProvider.getAuthenticationManager("gdoc");
			//print authenticationManager.getApplicationContextName();
			boolean loginOK = authenticationManager.login(params.login_name, params.password);
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
				println membership.group.group_name
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