
class RegistrationPublicCommand {

	String userId
	
	
	static constraints = {
		userId(email:true, blank:false, validator: {val, obj ->
			if(val) {
				def emailArray = val.tokenize("@");
				if(emailArray[1] == "georgetown.edu"){
					return "custom.gtown"
				}
			}
		})
	}
	
}