class SemanticHelper {
	
	static def resolveAttributeForStudy(attribute, study){
		def resolvedAttribute = []
		//all this will be refactored to use a properties file, or a better semantic solution
		//------------------------------
		if(attribute == 'Relapse'){
			switch (study) {
			   case 'CLARKE-LIU': 
						resolvedAttribute << [('SURGERY_TO_LR/FU'):[min:0, max:4]]
						resolvedAttribute << [('SURGERY_TO_LR/FU'):[min:5, max:50]]
						break;
			   case 'LOI': 
						resolvedAttribute << [('SURGERY_TO_RECUR/FU'):[min:0, max:4]]
						resolvedAttribute << [('SURGERY_TO_RECUR/FU'):[min:5, max:50]]
						break;
			   case 'CRC_PILOT': resolvedAttribute << [('SURGERY_TO_RELAPSE/FU'):[min:0, max:4]]
								 resolvedAttribute << [('SURGERY_TO_RELAPSE/FU'):[min:5, max:50]]
								 break;
			   default: resolvedAttribute = null;
			}
		}
		//-----------------------------
		return resolvedAttribute
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