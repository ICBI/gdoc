class Reporter {
	static mapping = {
		table 'HTARRAY_REPORTER'
		version false
		id column:'HTARRAY_REPORTER_ID'
		arrayDesigns column:'HTARRAY_REPORTER',joinTable:'HTARRAY_REPORTER_LIST'
	}
	static belongsTo = ArrayDesign
	static hasMany = [arrayDesigns:ArrayDesign]

	String name
	String geneSymbol

}
