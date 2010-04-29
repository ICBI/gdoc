class Contact {

	static mapping = {
		table 'COMMON.CONTACT'
		version false
		id column:'contact_id', generator: 'sequence', params: [sequence: 'CONTACT_SEQUENCE']
		firstName column: 'first_name'
		lastName column: 'last_name'
		//studies joinTable:[name:"data_source_contact", key:'contact_id', column:'data_source_id']
	}
    static constraints = {
    }
	
	
	static searchable = {
		mapping {
				alias "contacts"
		        firstName index: 'no'
				suffix index: 'no'
				email index: 'no'
		        spellCheck "include"
				//studies component: true
		}
	}
	//static hasMany = [studies: StudyDataSource]
	//static fetchMode = [studies: "eager"]
	
	String firstName
	String lastName
	String suffix
	String email
	String netid
	String notes
	String insertUser
	Date insertDate
	String insertMethod
}
