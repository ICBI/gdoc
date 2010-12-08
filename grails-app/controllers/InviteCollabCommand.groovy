class InviteCollabCommand {

	String collaborationGroupName
	String[] users
	
	static constraints = {
		users(validator: { val, obj ->
			if(!val || val[0] == "") {
				return "custom.size"
			}
			else{
				return true
			}
			
		})
		collaborationGroupName(blank:false)
	}
	
}