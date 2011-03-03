class UserOption {
    static mapping = {
		table 'USER_OPTION'
		version false
		id column:'user_option_id'
		type column: 'option_type'
		value column: 'option_value'
	}
	
	static belongsTo = [user:GDOCUser]
	UserOptionType type
	String value
}