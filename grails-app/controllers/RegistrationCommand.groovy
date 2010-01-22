class RegistrationCommand {

	String netId
	String department
	
	
	static constraints = {
		netId(blank:false)
	}
	
}