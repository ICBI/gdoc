class Biospecimen {

	static mapping = {
		table '__STUDY_SCHEMA__.BIOSPECIMEN'
		version false
		id column:'biospecimen_id'
		type column: 'class'
		patient column:'patient_id'
	}
	
	String type
	String name
	Patient patient
}
