class CinCommand {
	
	String study
	String dataFile
	String cytobandsDataFile
	String cytobandsAnnotationFile = "hg18_annot.Rda"
	String baselineGroup
	String groups
	static AnalysisType requestType = (AnalysisType.CIN)
	def userListService
	def idService
	
	static constraints = {
		dataFile(blank:false)
		cytobandsDataFile(blank:false)
		baselineGroup(blank:false, validator: {val, obj ->
			if(obj.idService.getGdocIdsForList(val).size() <= 2)
				return "custom.size"
		})
		groups(blank:false, validator: {val, obj ->
			if(obj.baselineGroup && obj.userListService.doListsOverlap(obj.baselineGroup, val))
				return "custom.overlap"
			if(obj.idService.getGdocIdsForList(val).size() <= 2)
				return "custom.size"
		})
	}
	
}