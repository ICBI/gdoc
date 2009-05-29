class ArrayDesign {
	static mapping = {
		table 'MARRAY_DESIGN'
		version false
		id column:'marray_design_id'
		reporters joinTable:[name:'MARRAY_REPORTER_LIST', key:'marray_design_id', column:'marray_reporter_id']
	}
	static hasMany = [reporters:Reporter]

	String platform
}
