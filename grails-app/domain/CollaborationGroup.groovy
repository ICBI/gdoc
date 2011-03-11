class CollaborationGroup {
	static mapping = {
			table 'COLLABORATION_GROUP'
			version false
			id column:'COLLABORATION_GROUP_ID'
			name column:'NAME'
			description column: 'DESCRIPTION'
			artifacts joinTable: [name: 'GROUP_ARTIFACT', column: 'PROTECTED_ARTIFACT_ID', key: 'COLLABORATION_GROUP_ID']
	}
	
	String name
	String description
	static hasMany = [artifacts:ProtectedArtifact,memberships:Membership,invitations:Invitation]
	static constraints = {
		name(nullable:false)
	}
	
	static searchable = {
		alias "collaborationGroup"
	}
	static transients = ['users']
	
	public Object getUsers() {
		if(this.@memberships) {
			def users = []
			users = this.@memberships.collect{it.user}
			return users as Set
		}
	}
	
	public void setName(String s){ 
	        name = s?.toUpperCase() 
 	}
	
	public String getName(){
		return name.toUpperCase() 
	}
	
}