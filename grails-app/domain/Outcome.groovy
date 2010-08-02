class Outcome {
	static mapping = {
		table 'COMMON.OUTCOME_DATA'
		version false
		id column:'ID', generator: 'sequence', params: [sequence: 'OUTCOME_DATA_SEQUENCE']
		patientId column:'PATIENT_ID'
		studyName column:'STUDY_NAME'
		outcomeType column:'OUTCOME_TYPE'
		outcomeDescription column:'OUTCOME_DESCRIPTION'
	}
	
	
	String studyName
	Long patientId
	String outcomeType
	String outcomeDescription
	
	
}
