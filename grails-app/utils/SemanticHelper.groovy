class SemanticHelper {
	
	static def determineOutcomeLabel(attribute){
		def labels = []
		switch (attribute) {
			case 'Relapse': labels = ["No relapse within predetermined period OR no relapse at all","Had relapse"]
							break;
			case 'Mortality': labels = ["Survival past predetermined period","No Survival"]
							  break;
			case 'Tumor vs. Non-Tumor': labels = ["Non-Tumor","Tumor"]
								break;
			case 'Metastasis': labels = ["No metastasis within predetermined period","Had metastasis"]
							  break;
			default: labels = ["No outcome defined", "No outcome defined"]
		}
		return labels
	}
	
	static def determineOutcomeQuery(attribute){
		def labels = []
		switch (attribute) {
			case 'Relapse': labels = ["Relapse","No Relapse"]
							break;
			case 'Metastasis': labels = ["Metastasis","No Metastasis"]
							break;
			case 'Tumor vs. Non-Tumor': labels = ["Tumor","Non-Tumor"]
							break;
			case 'Mortality': labels = ["Mortality","No Mortality"]
							  break;
			default: labels = ["No outcome defined", "No outcome defined"]
		}
		return labels
	}
}