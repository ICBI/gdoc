class CreateCollabCommand {

	String collaborationGroupName
	String description
	
	static constraints = {
		collaborationGroupName(blank:false)
	}
	
}