class QueryBuilder {

	static def build = { params, formKey ->
		def criteria = [:]
		params.each { key, value ->
			if(key.contains(formKey) && value) {
				if(key.contains("range_")) {
					def minMax = [:]
					minMax["min"] = value.split(" - ")[0].toInteger()
					minMax["max"] = value.split(" - ")[1].toInteger()
					if(minMax["min"] != 0 || minMax["max"] != 50) {
						criteria[key.replace(formKey + "range_", "")] = minMax
					}
				} else {
					criteria[key.replace(formKey, "")] = value
				}
			}
		}
		return criteria
	}

}