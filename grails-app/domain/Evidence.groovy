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
	}
	
	Finding finding
	UserList userList
	SavedAnalysis savedAnalysis
	Finding relatedFinding
	String description
	Date dateCreated
	Date lastUpdated
	
	
}
