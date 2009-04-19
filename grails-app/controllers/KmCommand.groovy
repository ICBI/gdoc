class KmCommand {

	List groups
	String survivalData
	
	static constraints = {
		groups(validator: { val, obj ->
			if(!val) {
				return "custom.size"
			}
			if(val.size() != 2) {
				return "custom.size"
			}
			val.each {list ->
				println val
			}
			return true
		})
	}
	
}