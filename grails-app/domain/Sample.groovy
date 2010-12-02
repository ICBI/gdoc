class Sample {
	static mapping = {
		table '__STUDY_SCHEMA__.HT_FILE_CONTENTS'
		version false
		id column:'ID'
		name column: 'BIOSPECIMEN_NAME'
		file column: 'FILE_NAME'
		design column: 'DESIGN_ID'
	}
	String name
	String file
	static belongsTo = Biospecimen
	Biospecimen biospecimen
	String designType
	ArrayDesign design
}