class Patient {

	static mapping = {
		table '__STUDY_SCHEMA__.PATIENT'
		version false
		id column:'patient_id'
		values column:'patient_id'
		
	}
	static hasMany = [values : DecValue]
	static fetchMode = [values:"eager"]
	static transients = ['clinicalData']
	
	Long dataSourceInternalId
	Map clinicalData

	public Map getClinicalData() {
		if(!this.@clinicalData) {
			this.@clinicalData = [:]
			values.each { value ->
				if(value.type.vocabulary) {
					def vocab = value.type.vocabs.find { 
						it.term == value.value
					}
					this.@clinicalData[value.type.shortName] = vocab.termMeaning
				} else {
					this.@clinicalData[value.type.shortName] = value.value
				}
			}
		}
		return this.@clinicalData
	}
}
