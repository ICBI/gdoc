class Role {
	static mapping = {
			table 'GDOC_ROLE'
			version false
			id column:'ROLE_ID'
			name column:'NAME'
	}
	
	String name
}