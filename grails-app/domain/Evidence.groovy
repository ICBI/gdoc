class Evidence {
	static mapping = {
		table 'COMMON.EVIDENCE'
		version false
		id column:'EVIDENCE_ID'
	}
	
	static searchable = {
		alias "evidence"
	    finding component: true
		userList component: true
		savedAnalysis component: true
		relatedFinding component: true
	}
	
	
	Finding finding
	UserList userList
	SavedAnalysis savedAnalysis
	Finding relatedFinding
	
	static belongsTo = [userList:UserList,finding:Finding,savedAnalysis:SavedAnalysis,relatedFinding:Finding]
	
	String description
	Date dateCreated
	Date lastUpdated
	
	
}
