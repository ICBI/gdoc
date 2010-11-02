import java.util.regex.Matcher
import java.util.regex.Pattern

class ResetPasswordCommand {

	String userId
	String password
	
	
	static constraints = {
		userId(email:true, blank:false)
		password(size:8..15,blank:false, matches:/^.*(?=.{8,})(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%*&+()]).*$/)
	}
	
}