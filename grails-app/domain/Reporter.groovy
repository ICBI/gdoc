class Reporter {
	static mapping = {
		table 'HTARRAY_REPORTER'
		version false
		id column:'HTARRAY_REPORTER_ID', generator: 'sequence', params: [sequence: 'HTARRAY_REPORTER_SEQUENCE']
		arrayDesigns column:'HTARRAY_REPORTER_ID',joinTable:'HTARRAY_REPORTER_LIST'
	}
	static belongsTo = ArrayDesign
	static hasMany = [arrayDesigns:ArrayDesign]

	String name
	String geneSymbol
	String insertUser
	String insertMethod
	Date insertDate
}
