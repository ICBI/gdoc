class ArrayDesign {
	static mapping = {
		table 'HTARRAY_DESIGN'
		version false
		id column:'HTARRAY_DESIGN_ID'
		reporters joinTable:[name:'HTARRAY_REPORTER_LIST', key:'HTARRAY_DESIGN_ID', column:'HTARRAY_REPORTER_ID']
	}
	static fetchMode = [reporters:'eager']
	static hasMany = [reporters:Reporter]

	String platform
	String arrayType
}
