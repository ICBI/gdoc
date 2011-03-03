import java.util.regex.Matcher
import java.util.regex.Pattern

class ActivationCommand {

	String userId
	String password
	String passwordConfirm
	String firstName
	String lastName
	String title
	String organization
	String reason
	
	static constraints = {
		userId(email:true, blank:false)
		password(size:8..15,blank:false, matches:/^.*(?=.{8,})(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%*&+()]).*$/)
		passwordConfirm(blank: false, validator: {val, obj ->
			if(obj.passwordConfirm != obj.password) 
				return "custom.passwordMatch"
		})
		firstName(size:1..20,blank:false)
		lastName(size:1..20,blank:false)
		title(blank:false)
		organization(blank:false)
		reason(size: 1..255, blank:false)
	}
	
}