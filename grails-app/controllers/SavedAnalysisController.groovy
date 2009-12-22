import grails.converters.*

class SavedAnalysisController{
	def securityService
	def savedAnalysisService
	def tagService
	
	def index = {
		def myAnalyses = []
		def timePeriods = [1:"1 day",7:"1 week",30:"past 30 days",90:"past 90 days",all:"show all"]
		def filteredAnalysis = []
		
		myAnalyses = savedAnalysisService.getAllSavedAnalysis(session.userId,session.sharedAnalysisIds)
		
		if(params.analysisFilter){
			if(params.analysisFilter == 'all'){
				session.analysisFilter = "all"
				filteredAnalysis = myAnalyses
				return [ savedAnalysis: filteredAnalysis, timePeriods: timePeriods]
			}
			else{
				session.analysisFilter = params.analysisFilter
				filteredAnalysis = savedAnalysisService.filterAnalysis(params.analysisFilter,myAnalyses)
				//println params.analysisFilter
				//println filteredAnalysis.size()
			}
		}
		else if(session.analysisFilter){
			filteredAnalysis = savedAnalysisService.filterAnalysis(session.analysisFilter,myAnalyses)
		}
		else{
			session.analysisFilter = "30"
			filteredAnalysis = savedAnalysisService.filterAnalysis(session.analysisFilter,myAnalyses)
		}
        [ savedAnalysis: filteredAnalysis, timePeriods: timePeriods]
    }

	def delete = {
		savedAnalysisService.deleteAnalysis(params.id)
		def myAnalyses = []
		myAnalyses = savedAnalysisService.getAllSavedAnalysis(session.userId,session.sharedAnalysisIds)
		def filteredAnalysis = []
		if(session.analysisFilter){
			filteredAnalysis = savedAnalysisService.filterAnalysis(session.analysisFilter,myAnalyses)
		}
		render(template:"/savedAnalysis/savedAnalysisTable",model:[ savedAnalysis: filteredAnalysis ])
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
				} 	else{
							savedAttempt["result"] = "Analysis not Saved -- may have already been saved."
							println savedAttempt
							render savedAttempt as JSON
					}
			}else{
					savedAttempt["result"] = "Analysis not Saved -- may have already been saved."
					println savedAttempt
					render savedAttempt as JSON
			}
	}
	
	def addTag = {
		println params
		if(params.id && params.tag){
			def list = tagService.addTag(SavedAnalysis.class.name,params.id,params.tag.trim())
			if(list){
				render list.tags
			}
			else{
				render ""
			}
		}
	}
	
	def removeTag = {
		println params
		if(params.id && params.tag){
			def analysis = tagService.removeTag(SavedAnalysis.class.name,params.id,params.tag.trim())
			if(analysis){
				render analysis.tags
			}
			else{
				render ""
			}
		}
	}
	
}