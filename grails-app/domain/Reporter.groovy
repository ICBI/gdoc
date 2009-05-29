class Reporter {
	static mapping = {
		table 'MARRAY_REPORTER'
		version false
		id column:'marray_reporter_id'
		
	}
	static belongsTo = ArrayDesign
	static hasMany = [arrayDesigns:ArrayDesign]

	String name
	String geneSymbol

}
