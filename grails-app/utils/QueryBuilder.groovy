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
					criteria[key.replace(formKey, "")] = value
				}
			}
		}
		return criteria
	}
	
	static def buildSparql = { params ->
		def sparqlString = "SELECT ?gid ?study ?t ?v WHERE { ?patient rdf:type gdoc:GDOCSubject . ";
		params.keySet().removeAll( ['search','errors', 'action', 'controller'] as Set )
		params.each { key, value ->
				if(key.contains("range_")) {
					def minMax = [:]
					minMax["min"] = value.split(" - ")[0]
					minMax["max"] = value.split(" - ")[1]
					def attrName = key.substring(key.indexOf("range_") + 6)
					def keyVar = attrName.replace(":", "_");
					def attrVar = keyVar + "_attr";
					def attrValue = keyVar + "_value";
					def paramClause = " { ?patient" + " gdoc:Has_Clinical_Attribute ?"+ attrVar +
											" . ?"+attrVar +" a " + attrName + " . ?"+ attrVar + 
											" gdoc:Clinical_Value ?"+ attrValue + 
											" . FILTER (?" + attrValue + ">='" + minMax["min"] + "' && " +
													  	"?" + attrValue + "<='" + minMax["max"] + "' ) } . ";
					sparqlString += paramClause;
					/**def range = dataTypes.find {
						it.shortName == attrName
					}
					if(minMax["min"] != range.lowerRange.toInteger() || minMax["max"] != range.upperRange.toInteger()) {
						criteria[key.replace(formKey + "range_", "")] = minMax
					}**/
				} /**else if (value.metaClass.respondsTo(value, 'join')) {
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
				}**/ else {
					def keyVar = key.replace(":", "_");
					def attrVar = keyVar + "_attr";
					def attrValue = keyVar + "_value";
					def paramClause = " { ?patient" + " gdoc:Has_Clinical_Attribute ?"+ attrVar +
					" . ?"+attrVar +" a " + key + " . ?"+ attrVar + 
					" gdoc:Clinical_Value ?"+ attrValue + 
					" . FILTER (?" + attrValue + "='" + value + "') } . ";
					sparqlString += paramClause;
				}
		}
		sparqlString += " ?patient gdoc:Participated_In ?study . ?study nci:Display_Name ?studyName . "
		sparqlString += " ?patient gdoc:Has_Clinical_Attribute ?a . " +
			"?a gdoc:Clinical_Value ?v . " +
			"?a rdf:type ?t . ?patient gdoc:GDOC_ID ?gid }";
		return sparqlString
	}
	
	static def getAllAttributesSparql(){
		private static String attrQuerySelect = "SELECT DISTINCT ?type WHERE {?p gdoc:Has_Clinical_Attribute ?att . ?att rdf:type ?type }";
		return attrQuerySelect
	}
	
	static def getAllStudiesSparql(){
		private static String studySelect = "SELECT DISTINCT ?study WHERE { ?patient gdoc:Participated_In ?study . ?study nci:Display_Name ?studyName }";
	}

}