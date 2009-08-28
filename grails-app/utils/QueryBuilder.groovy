class QueryBuilder {

	static def build = { params, formKey, dataTypes ->
		def criteria = [:]
		params.each { key, value ->
			if(key.contains(formKey) && value) {
				if(key.contains("range_")) {
					def minMax = [:]
					minMax["min"] = value.split(" - ")[0].toInteger()
					minMax["max"] = value.split(" - ")[1].toInteger()
					def attrName = key.substring(key.indexOf("range_") + 6)
					def range = dataTypes.find {
						it.shortName == attrName
					}
					if(minMax["min"] != range.lowerRange.toInteger() || minMax["max"] != range.upperRange.toInteger()) {
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