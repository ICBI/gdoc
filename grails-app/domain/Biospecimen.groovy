class Biospecimen {

	static mapping = {
		table '__STUDY_SCHEMA__.BIOSPECIMEN'
		version false
		id column:'biospecimen_id', generator: 'sequence', params: [sequence: '__STUDY_SCHEMA__.BIOSPECIMEN_SEQUENCE']
		type column: 'class'
		patient column:'patient_id'
		values column:'biospecimen_id'
		insertUser column: 'INSERT_USER'
		insertMethod column: 'INSERT_METHOD'
		insertDate column: 'INSERT_DATE'
	}
	static transients = ['biospecimenData']
	
	static hasMany = [values : BiospecimenValue, reductionAnalyses: ReductionAnalysis]
	
	String type
	String name
	String insertUser
	String insertMethod
	Date insertDate
	Patient patient
	Boolean diseased
	Map biospecimenData
	
	public Map getBiospecimenData() {
		if(!this.@biospecimenData) {
			this.@biospecimenData = [:]
			values.each { value ->
				if(value.commonType.vocabulary) {
					def vocab = value.commonType.vocabs.find { 
						it.term == value.value
					}
					if(vocab) {
						this.@biospecimenData[value.commonType.shortName] = vocab.termMeaning
					}
				} else {
					this.@biospecimenData[value.commonType.shortName] = value.value
				}
			}
		}
		return this.@biospecimenData
	}
}
