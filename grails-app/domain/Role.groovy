class Role {
	static mapping = {
			table 'CSM_ROLE'
			version false
			id column:'ROLE_ID'
			name column:'ROLE_NAME'
	}
	
	String name
}