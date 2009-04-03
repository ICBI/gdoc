class QueryBuilder {

	static def build = { params, formKey ->
		def criteria = [:]
		StudyContext.setStudy("EDIN")
		params.each { key, value ->
			if(key.contains(formKey) && value) {
				criteria[key.replace(formKey, "")] = value
			}
		}
		return criteria
	}

}