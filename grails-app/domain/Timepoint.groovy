class Timepoint {
	
	static mapping = {
		table '__STUDY_SCHEMA__.ATTRIBUTE_TIMEPOINT'
		version false
		id column:'ATTRIBUTE_TIMEPOINT_ID'
		seriesId column: 'SERIES_ID'
		timepointId column: 'TIMEPOINT'
	}
	
	String seriesId
	String timepointId
	String tag
}
