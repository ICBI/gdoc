class Patient {

	static mapping = {
		table '__STUDY_SCHEMA__.PATIENTS'
		version false
		id column:'patient_id'
		values column:'patient_id'
		biospecimens column:'patient_id'
	}
	static hasMany = [values : AttributeValue, biospecimens: Biospecimen]
	static fetchMode = [values:"eager", biospecimens: "eager"]
	static transients = ['clinicalData', 'clinicalDataValues']
	
	String dataSourceInternalId
	Long gdocId
	Map clinicalData
	Map clinicalDataValues
	
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
	
	public Map getClinicalDataValues() {
		if(!this.@clinicalDataValues) {
			this.@clinicalDataValues = [:]
			values.each { value ->
				this.@clinicalDataValues[value.type.shortName] = value.value
			}
		}
		return this.@clinicalDataValues
	}
}
