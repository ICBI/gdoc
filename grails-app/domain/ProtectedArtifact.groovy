class ProtectedArtifact {
	static mapping = {
			table 'PROTECTED_ARTIFACT'
			version false
			id column:'PROTECTED_ARTIFACT_ID'
			groups joinTable: [name: 'GROUP_ARTIFACT', column: 'COLLABORATION_GROUP_ID', key: 'PROTECTED_ARTIFACT_ID'] 
	}
	static hasMany = [groups:CollaborationGroup]
	static belongsTo = CollaborationGroup
	String name
	String objectId
	String type
	
	

}
