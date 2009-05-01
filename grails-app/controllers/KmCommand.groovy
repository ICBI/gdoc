class KmCommand {

	List groups
	String endpoint
	
	static constraints = {
		groups(validator: { val, obj ->
			if(!val) {
				return "custom.size"
			}
			return true
		})
		endpoint(blank:false)
	}
	
}