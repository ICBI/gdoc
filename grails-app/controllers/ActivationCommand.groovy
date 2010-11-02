import java.util.regex.Matcher
import java.util.regex.Pattern

class ActivationCommand {

	String userId
	String password
	String firstName
	String lastName
	String organization
	
	
	static constraints = {
		userId(email:true, blank:false)
		password(size:8..15,blank:false, matches:/^.*(?=.{8,})(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%*&+()]).*$/)
		firstName(size:1..20,blank:false)
		lastName(size:1..20,blank:false)
		organization(blank:false)
	}
	
}