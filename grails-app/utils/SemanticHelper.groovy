class SemanticHelper {
	
	static def resolveAttributesForStudy(attributes, study){
		def bundledSemanticCriteria = []
		def resolvedCriteria = [:]
		def resolvedCriteria2 = [:]
		def specimenCriteria = [:]
		def count = 0
		//all this will be refactored to use a properties file, or a better semantic solution
		//------------------------------
		//println "iterate over $attributes in sem. helper"
		attributes.each{ attributeKey, attributeVal ->
			println "resolve $attributeKey , $attributeVal in sem. helper"
			if(attributeVal == 'Relapse'){
				def lessThanAttributeYears = []
				def lessThanAttributeRelapse = []
				def moreThanAttributeYears = []
				switch (study) {
				   case 'CLARKE-LIU': 
							lessThanAttributeYears << [('SURGERY_TO_LR/FU'):[min:0, max:4],('SURGERY_TO_DR/FU'):[min:0, max:4],('SURGERY_TO_RR/FU'):[min:0, max:4]]
							resolvedCriteria['lessThanAttributeYears'] = lessThanAttributeYears
							lessThanAttributeRelapse << [RELAPSE_CODE:1]
							lessThanAttributeRelapse << [RELAPSE_CODE:-1]
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							moreThanAttributeYears << [('SURGERY_TO_LR/FU'):[min:5, max:50],('SURGERY_TO_DR/FU'):[min:5, max:50],('SURGERY_TO_RR/FU'):[min:5, max:50]]
							resolvedCriteria2['moreThanAttributeYears'] = moreThanAttributeYears
							count++
							break;
				   case 'LOI': 
							lessThanAttributeYears << [('SURGERY_TO_RECUR/FU'):[min:0, max:4],('SURGERY_TO_DR/FU'):[min:0, max:4]]
							resolvedCriteria['lessThanAttributeYears'] = lessThanAttributeYears
							lessThanAttributeRelapse << [RECURRENCE_ANY:'YES']
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							moreThanAttributeYears << [('SURGERY_TO_RECUR/FU'):[min:5, max:50],('SURGERY_TO_DR/FU'):[min:5, max:50],RECURRENCE_ANY:'NO']
							resolvedCriteria2['moreThanAttributeYears'] = moreThanAttributeYears
							count++
							break;
				   case 'CRC_PILOT': 
									lessThanAttributeRelapse << [RELAPSE:'YES']
									resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
									lessThanAttributeYears << [('SURGERY_TO_RELAPSE/FU'):[min:0, max:4]]
									resolvedCriteria['lessThanAttributeYears'] = lessThanAttributeYears
									moreThanAttributeYears<< [('SURGERY_TO_RELAPSE/FU'):[min:5, max:50],RELAPSE:'NO']
									resolvedCriteria2['moreThanAttributeYears'] = moreThanAttributeYears
									count++
									break;
				   default: bundledSemanticCriteria = null;
				}
			}else if(attributeKey == 'grade'){
				def grade = []
				switch (study) {
				   case 'LOI':
				   specimenCriteria["ELSTON-ELLIS_GRADE"] = attributeVal
				   count++
				   break;
				   default: bundledSemanticCriteria = null;
				}
			}
			//-----------------------------
			if((resolvedCriteria || resolvedCriteria2) && (count == (attributes.size() - 1))){
				bundledSemanticCriteria << resolvedCriteria
				bundledSemanticCriteria << resolvedCriteria2
				bundledSemanticCriteria << specimenCriteria
			}
			
		}
		return bundledSemanticCriteria
	}
	
	static def determineOutcomeLabel(attribute, study){
		def labels = []
		if(attribute == 'Relapse'){
			switch (study) {
			   case 'CLARKE-LIU': labels = ["Relapse free for less than 5 years", "Relapse free for more than 5 years, or no relapse"]
			   case 'CRC_PILOT': labels = ["Relapse free for less than 5 years", "Relapse free for more than 5 years, or no relapse"]
			   case 'LOI': labels = ["Relapse free for less than 5 years", "Relapse free for more than 5 years, or no relapse"]
			   default: labels = ["Relapse free for less than 5 years", "Relapse free for more than 5 years, or no relapse"]
			}
		}
		if(attribute == 'Mortality'){
			return "Survival"
		}
		return labels
	}
}