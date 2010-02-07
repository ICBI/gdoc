class Contact {

	static mapping = {
		table 'COMMON.CONTACT'
		version false
		id column:'contact_id'
		firstName column: 'first_name'
		lastName column: 'last_name'
	}
    static constraints = {
    }
	
	static searchable = {
		mapping {
		        firstName index: 'no'
				suffix index: 'no'
				email index: 'no'
		        spellCheck "include"
		}
	}

	String firstName
	String lastName
	String suffix
	String email
}
