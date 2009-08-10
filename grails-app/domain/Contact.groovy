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

	String firstName
	String lastName
	String suffix
	String email
}
