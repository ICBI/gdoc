class SemanticHelper {
	
	static def resolveAttributesForStudy(attributes, study){
		def bundledSemanticCriteria = []
		def resolvedCriteria = [:]
		def resolvedCriteria2 = [:]
		def specimenCriteria = [:]
		def count = 0
		/** @TODO: refactor
		1)run canned query (or access view) and utilze existing clinicalService to add criteria onto precanned outcome of patientIds
		2)JUST refactor SemanticHelper to resolve come outcomes and outcomesTimes 
		**/
		attributes.each{ attributeKey, attributeVal ->
			println "resolve $attributeKey , $attributeVal in sem. helper"
			if(attributeVal == 'Relapse'){
				def lessThanAttributeYears = []
				def lessThanAttributeRelapse = []
				def moreThanAttributeYears = []
				switch (study) {
				   case 'CLARKE-LIU': 
							//lessThanAttributeYears << [('SURGERY_TO_LR/FU'):[min:0, max:4],('SURGERY_TO_DR/FU'):[min:0, max:4],('SURGERY_TO_RR/FU'):[min:0, max:4]]
							lessThanAttributeYears << [('SURGERY_TO_DR/FU'):[min:0, max:4]]
							resolvedCriteria['lessThanAttributeYears'] = lessThanAttributeYears
							lessThanAttributeRelapse << [RELAPSE_CODE:1]
							//lessThanAttributeRelapse << [RELAPSE_CODE:-1]
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							//moreThanAttributeYears << [('SURGERY_TO_LR/FU'):[min:5, max:50],('SURGERY_TO_DR/FU'):[min:5, max:50],('SURGERY_TO_RR/FU'):[min:5, max:50]]
							moreThanAttributeYears << [('SURGERY_TO_DR/FU'):[min:5, max:50]]
							resolvedCriteria2['moreThanAttributeYears'] = moreThanAttributeYears
							count++
							break;
				   case 'LOI': 
							//lessThanAttributeYears << [('SURGERY_TO_RECUR/FU'):[min:0, max:4],('SURGERY_TO_DR/FU'):[min:0, max:4]]
							lessThanAttributeYears << [('SURGERY_TO_RECUR/FU'):[min:0, max:4]]
							resolvedCriteria['lessThanAttributeYears'] = lessThanAttributeYears
							lessThanAttributeRelapse << [RECURRENCE_ANY:'YES']
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							//moreThanAttributeYears << [('SURGERY_TO_RECUR/FU'):[min:5, max:50],('SURGERY_TO_DR/FU'):[min:5, max:50],RECURRENCE_ANY:'NO']
							moreThanAttributeYears << [('SURGERY_TO_RECUR/FU'):[min:5, max:50],RECURRENCE_ANY:'NO']
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
					case 'WANG':
							lessThanAttributeRelapse << [RELAPSE:'YES']
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							moreThanAttributeYears<< [RELAPSE:'NO']
							resolvedCriteria2['moreThanAttributeYears'] = moreThanAttributeYears
							count++
							break;
					case 'ZHOU':
							lessThanAttributeRelapse << [RELAPSE:'YES']
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							moreThanAttributeYears<< [RELAPSE:'NO']
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
			else if(attributeKey == 'er'){
				def erStatus = []
				def erVal
				switch (study) {
					case 'LOI':
						if(attributeVal == 'Positive') 
							erVal = 'POSITIVE'
						else erVal = 'NEGATIVE'
					 	erStatus << [ER_STATUS_PATIENT:erVal]
						resolvedCriteria['erStatus'] = erStatus
						resolvedCriteria2['erStatus'] = erStatus
						count++
						break;
					case 'WANG':
						if(attributeVal == 'Positive') 
							erVal = 'POSITIVE'
						else erVal = 'NEGATIVE'
						erStatus << [ER_STATUS:erVal]
						resolvedCriteria['erStatus'] = erStatus
						resolvedCriteria2['erStatus'] = erStatus
						count++
						break;
					case 'ZHOU':
						if(attributeVal == 'Positive') 
							erVal = 'POSITIVE'
						else erVal = 'NEGATIVE'
						erStatus << [ER_STATUS:erVal]
						resolvedCriteria['erStatus'] = erStatus
						resolvedCriteria2['erStatus'] = erStatus
						count++
						break;
				}
			}
			else if(attributeVal == 'Mortality'){
				def lessThanAttributeYears = []
				def lessThanAttributeRelapse = []
				def moreThanAttributeYears = []
				switch (study) {
				   case 'CLARKE-LIU': 
							lessThanAttributeYears << [('SURGERY_TO_DEATH/FU'):[min:0, max:4]]
							resolvedCriteria['lessThanAttributeYears'] = lessThanAttributeYears
							lessThanAttributeRelapse << [BREASTCANCER_DEATH:'YES']
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							moreThanAttributeYears << [('SURGERY_TO_DEATH/FU'):[min:5, max:50]]
							resolvedCriteria2['moreThanAttributeYears'] = moreThanAttributeYears
							count++
							break;
				   case 'CRC_PILOT': 
							lessThanAttributeRelapse << [VITAL_STATUS:'DEAD']
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							moreThanAttributeYears<< [VITAL_STATUS:'ALIVE']
							resolvedCriteria2['moreThanAttributeYears'] = moreThanAttributeYears
							count++
							break;
					case 'FCR':
							lessThanAttributeYears << [('SURGERY_TO_DEATH/FU__FCR'):[min:0, max:4]]
							resolvedCriteria['lessThanAttributeYears'] = lessThanAttributeYears
							lessThanAttributeRelapse << [VITAL_STATUS:'DEAD']
							resolvedCriteria['lessThanAttributeRelapse'] = lessThanAttributeRelapse
							moreThanAttributeYears << [('SURGERY_TO_DEATH/FU__FCR'):[min:5, max:50]]
							//moreThanAttributeYears << [VITAL_STATUS:'DEAD']
							moreThanAttributeYears << [VITAL_STATUS:'ALIVE']
							resolvedCriteria2['moreThanAttributeYears'] = moreThanAttributeYears
							count++
							break;
				   default: bundledSemanticCriteria = null;
				}
			}
			//-----------------------------
			if((resolvedCriteria || resolvedCriteria2) && (count == (attributes.size()))){
				bundledSemanticCriteria << resolvedCriteria
				bundledSemanticCriteria << resolvedCriteria2
				bundledSemanticCriteria << specimenCriteria
			}
			
		}
		return bundledSemanticCriteria
	}
	
	static def determineStudyDataLabel(attribute, study){
		def labels = []
		if(attribute == 'Mortality'){
			switch (study) {
			   case 'CLARKE-LIU': labels = ["Survival:less than 5 years", "Survival:more than 5 years"]; break;
			   case 'CRC_PILOT': labels = ["Dead", "Alive"]; break;
			   case 'FCR': labels = ["Survival:less than 5 years", "Survival:more than 5 years"]; break;
			   default: labels = ["Dead", "Alive"]
			}
		}
		if(attribute == 'Relapse'){
			switch (study) {
			   case 'CLARKE-LIU': labels = ["Had distal recurrence less than 5 years", "No distal recurrence for more than 5 years, or no relapse"]; break;
			   case 'CRC_PILOT': labels = ["Relapse free for less than 5 years", "Relapse free for more than 5 years, or no relapse"]; break;
			   case 'LOI': labels = ["Had relapse in less than 5 years", "Relapse free for more than 5 years, or no relapse"]; break;
			   case 'WANG': labels = ["Relapse", "No Relapse"]; break;
			   case 'ZHOU': labels = ["Relapse", "No Relapse"]; break;
			   default: labels = ["Relapse", "No Relapse"]
			}
		}
		return labels
	}
	
	static def determineOutcomeLabel(attribute){
		def labels = []
		switch (attribute) {
			case 'Relapse': labels = ["No relapse within predetermined period OR no relapse at all","Had relapse"]
							break;
			case 'Mortality': labels = ["Survival past predetermined period","No Survival"]
							  break;
			default: labels = ["No outcome defined", "No outcome defined"]
		}
		return labels
	}
}