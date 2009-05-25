class SavedAnalysisController{
	def securityService
	def savedAnalysisService
	def index = {
        if(!params.max) params.max = 10
		def user = GDOCUser.findByLoginName(session.userId)
		def groupAnalysisIds = session.sharedAnalysisIds//securityService.getSharedItemIds(session.userId, SavedAnalysis.class.name)
		def myAnalysis = user.analysis
		def analysisIds = []
		
		if(myAnalysis.metaClass.respondsTo(myAnalysis, "size")) {
				myAnalysis.each{
					analysisIds << it.id.toString()
				}
		} else {
				analysisIds << myAnalysis.id.toString()
		}
		if(groupAnalysisIds.metaClass.respondsTo(groupAnalysisIds, "size")) {
				groupAnalysisIds.removeAll(analysisIds)
		} else {
				groupAnalysisIds.remove(analysisIds)
		}	
		
		//until we modify ui, just add shared analyisis to 'all' analysis
		groupAnalysisIds.each{
			def foundAnalysis = SavedAnalysis.get(it)
			if(foundAnalysis){
				myAnalysis << foundAnalysis
			}
		}
		//lists << sharedLists	
        [ savedAnalysis: myAnalysis ]
    }


	def delete = {
		savedAnalysisService.deleteAnalysis(params.id)
		def user = GDOCUser.findByLoginName(session.userId)
		def groupAnalysisIds = session.sharedAnalysisIds//securityService.getSharedItemIds(session.userId, SavedAnalysis.class.name)
		def myAnalysis = user.analysis
		def analysisIds = []
		
		if(myAnalysis.metaClass.respondsTo(myAnalysis, "size")) {
				myAnalysis.each{
					analysisIds << it.id.toString()
				}
		} else {
				analysisIds << myAnalysis.id.toString()
		}
		if(groupAnalysisIds.metaClass.respondsTo(groupAnalysisIds, "size")) {
				groupAnalysisIds.removeAll(analysisIds)
		} else {
				groupAnalysisIds.remove(analysisIds)
		}	
		
		//until we modify ui, just add shared analyisis to 'all' analysis
		groupAnalysisIds.each{
			def foundAnalysis = SavedAnalysis.get(it)
			if(foundAnalysis){
				myAnalysis << foundAnalysis
			}
		}
		render(template:"/savedAnalysis/savedAnalysisTable",model:[ savedAnalysis: myAnalysis ])
	}
	
}