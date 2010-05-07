class SemanticHelper {
	
	static def resolveAttributesForStudy(attributes, study){
		def bundledSemanticCriteria = []
		def resolvedCriteria = [:]
		def resolvedCriteria2 = [:]
		//all this will be refactored to use a properties file, or a better semantic solution
		//------------------------------
		//println "iterate over $attributes in sem. helper"
		attributes.each{ attributeKey, attributeVal ->
			//println "resolve $attributeVal in sem. helper"
			if(attributeVal == 'Relapse'){
				switch (study) {
				   case 'CLARKE-LIU': 
							def lessThanAttributeYears = []
							lessThanAttributeYears << [('SURGERY_TO_LR/FU'):[min:0, max:4]]
							lessThanAttributeYears << [('SURGERY_TO_DR/FU'):[min:0, max:4]]
							lessThanAttributeYears << [('SURGERY_TO_RR/FU'):[min:0, max:4]]
							resolvedCriteria['lessThanAttributeYears'] = lessThanAttributeYears
							def lessThanAttributeRelapse = []
							lessThanAttributeRelapse << [RELAPSE_CODE:1]
							lessThanAttributeRelapse << [RELAPSE_CODE:-1]
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							def moreThanAttributeYears = []
							moreThanAttributeYears << [('SURGERY_TO_LR/FU'):[min:5, max:50]]
							moreThanAttributeYears << [('SURGERY_TO_DR/FU'):[min:5, max:50]]
							moreThanAttributeYears << [('SURGERY_TO_RR/FU'):[min:5, max:50]]
							resolvedCriteria2['moreThanAttributeYears'] = moreThanAttributeYears
							def moreThanAttributeRelapse = []
							moreThanAttributeRelapse << [RELAPSE_CODE:1]
							moreThanAttributeRelapse << [RELAPSE_CODE:-1]
							resolvedCriteria2['moreThanAttributeRelapse'] = moreThanAttributeRelapse
							
							//println "add criteria in semantic helper"
							bundledSemanticCriteria << resolvedCriteria
							bundledSemanticCriteria << resolvedCriteria2
							break;
				   case 'LOI': 
							def lessThanAttributeYears = []
							lessThanAttributeYears << [('SURGERY_TO_RECUR/FU'):[min:0, max:4]]
							lessThanAttributeYears << [('SURGERY_TO_DR/FU'):[min:0, max:4]]
							resolvedCriteria['lessThanAttributeYears'] = lessThanAttributeYears
							def lessThanAttributeRelapse = []
							lessThanAttributeRelapse << [RECURRENCE_ANY:'YES']
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							def moreThanAttributeYears = []
							moreThanAttributeYears << [('SURGERY_TO_RECUR/FU'):[min:5, max:50]]
							moreThanAttributeYears << [('SURGERY_TO_DR/FU'):[min:5, max:50]]
							resolvedCriteria2['moreThanAttributeYears'] = moreThanAttributeYears
							def moreThanAttributeRelapse = []
							moreThanAttributeRelapse << [RECURRENCE_ANY:'YES']
							resolvedCriteria2['moreThanAttributeRelapse'] = moreThanAttributeRelapse
							//println "add criteria in semantic helper"
							bundledSemanticCriteria << resolvedCriteria
							bundledSemanticCriteria << resolvedCriteria2
							break;
				   /**case 'CRC_PILOT': resolvedAttribute << [('SURGERY_TO_RELAPSE/FU'):[min:0, max:4]]
									 resolvedAttribute << [('SURGERY_TO_RELAPSE/FU'):[min:5, max:50]]
									 break;**/
				   default: bundledSemanticCriteria = null;
				}
			}
			//-----------------------------
		}
		return bundledSemanticCriteria
	}
	
	static def determineOutcomeLabel(attribute){
		if(attribute == 'Relapse'){
			return "Relapse-free"
		}
		if(attribute == 'Mortality'){
			return "Survival"
		}
	}
}