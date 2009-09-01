class Sample {
	static mapping = {
		table '__STUDY_SCHEMA__.PLIER_CONTENTS'
		version false
		id column:'ID'
		name column: 'BIOSPECIMEN_NAME'
		file column: 'PLIER_NAME'
	}
	String name
	String file
}