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
				} else if (value.metaClass.respondsTo(value, 'join')) {
					if(value[0] || value[1]) {
						def minMax = [:]
						minMax["min"] = value[0]
						minMax["max"] = value[1]
						if(!value[0]) {
							minMax["min"] = Integer.MIN_VALUE
						} else if(!value[1]) {
							minMax["max"] = Integer.MAX_VALUE
						}
						criteria[key.replace(formKey, "")] = minMax
					}
				} else {
					value = value.replace("'", "")
					criteria[key.replace(formKey, "")] = value
				}
			}
		}
		return criteria
	}

}