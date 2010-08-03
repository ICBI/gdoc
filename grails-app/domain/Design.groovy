class Design {
	static mapping = {
		table 'ALL_DESIGNS'
		version false
		id column:'HTARRAY_DESIGN_ID'
		arrayType column: 'ARRAY_TYPE'
	}
	String arrayType
	String platform
}
