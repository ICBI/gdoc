class Reporter {
	static mapping = {
		table 'MARRAY_REPORTER'
		version false
		id column:'marray_reporter_id'
		arrayDesigns column:'MARRAY_REPORTER_ID',joinTable:'MARRAY_REPORTER_LIST'
	}
	static belongsTo = ArrayDesign
	static hasMany = [arrayDesigns:ArrayDesign]

	String name
	String geneSymbol

}
