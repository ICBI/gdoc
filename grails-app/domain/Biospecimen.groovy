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
		attributeTimepointId column: 'ATTRIBUTE_TIMEPOINT_ID'
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
	Integer attributeTimepointId
	Map biospecimenData
	
	public Map getBiospecimenData() {
		if(!this.@biospecimenData) {
			this.@biospecimenData = [:]
			values.each { value ->
				if(value.type.vocabulary) {
					def vocab = value.type.vocabs.find { 
						it.term == value.value
					}
					if(vocab) {
						this.@biospecimenData[value.type.shortName] = vocab.termMeaning
					}
				} else {
					this.@biospecimenData[value.type.shortName] = value.value
				}
			}
		}
		return this.@biospecimenData
	}
}
