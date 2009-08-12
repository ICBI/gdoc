class KmAttribute implements Serializable {
	
	static mapping = {
		table '__STUDY_SCHEMA__.KM_ATTRIBUTES'
		version false
		id composite: ['attribute', 'censorAttribute']
		attribute column: 'km_attribute_short'
		censorAttribute column: 'censor_attribute_short'
		attributeDescription column: 'km_attribute'
		censorDescription column: 'censor_attribute'
		censorValue column: 'censor_value'
	}
    static constraints = {
    }

	String attribute
	String censorAttribute
	String attributeDescription
	String censorDescription
	String censorValue
}
