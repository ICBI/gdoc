class Biospecimen {

	static mapping = {
		table '__STUDY_SCHEMA__.BIOSPECIMEN'
		version false
		id column:'biospecimen_id'
		type column: 'class'
		patient column:'patient_id'
		values column:'biospecimen_id'
		
	}
	static transients = ['biospecimenData']
	
	static hasMany = [values : BiospecimenValue]
	
	String type
	String name
	Patient patient
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
