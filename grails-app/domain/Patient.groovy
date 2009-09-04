class Patient {

	static mapping = {
		table '__STUDY_SCHEMA__.PATIENTS'
		version false
		id column:'patient_id'
		values column:'patient_id'
		biospecimens column:'patient_id'
	}
	static hasMany = [values : DecValue, biospecimens: Biospecimen]
	static fetchMode = [values:"eager", biospecimens: "eager"]
	static transients = ['clinicalData']
	
	//String dataSourceInternalId
	Long gdocId
	Map clinicalData

	public Map getClinicalData() {
		if(!this.@clinicalData) {
			this.@clinicalData = [:]
			values.each { value ->
				if(value.type.vocabulary) {
					def vocab = value.type.vocabs.find { 
						it.term == value.value
					}
					if(vocab) {
						this.@clinicalData[value.type.shortName] = vocab.termMeaning
					}
				} else {
					this.@clinicalData[value.type.shortName] = value.value
				}
			}
		}
		return this.@clinicalData
	}
}
