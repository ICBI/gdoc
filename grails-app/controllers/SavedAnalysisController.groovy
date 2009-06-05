import grails.converters.*

class SavedAnalysisController{
	def securityService
	def savedAnalysisService
	def index = {
        if(!params.max) params.max = 10
		def user = GDOCUser.findByLoginName(session.userId)
		def groupAnalysisIds = securityService.getSharedItemIds(session.userId, SavedAnalysis.class.name)
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
		myAnalysis = myAnalysis.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}
        [ savedAnalysis: myAnalysis ]
    }

	//TODO refactor to re-use the code in this
	def delete = {
		savedAnalysisService.deleteAnalysis(params.id)
		def user = GDOCUser.findByLoginName(session.userId)
		def groupAnalysisIds = securityService.getSharedItemIds(session.userId, SavedAnalysis.class.name)
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
		myAnalysis = myAnalysis.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}
		render(template:"/savedAnalysis/savedAnalysisTable",model:[ savedAnalysis: myAnalysis ])
	}
	
	//TODO - decide if we always want to auto-save KM plots. Right now, we do not. They must implicitly call 'save'.
	def save = {
			println ("THE RESULT:")
			println params.resultData
			def savedAttempt = [:]
			println "session command" + session.command
			if(session.command != null){
			println ("THE COMMAND PARAMS:")
				if(savedAnalysisService.saveAnalysisResult(session.userId, params.resultData,session.command)){
					println ("saved analysis")
					savedAttempt["result"] = "Analysis Saved"
					render savedAttempt as JSON		
				}
			}
	}
	
}