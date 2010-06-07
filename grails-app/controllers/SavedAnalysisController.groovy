import grails.converters.*

class SavedAnalysisController{
	def securityService
	def savedAnalysisService
	def tagService
	
	def index = {
		def myAnalyses = []
		def timePeriods = [1:"1 day",7:"1 week",30:"past 30 days",90:"past 90 days",hideShared:"hide shared lists",all:"show all"]
		def filteredAnalysis = []
		def pagedAnalyses
		myAnalyses = savedAnalysisService.getAllSavedAnalysis(session.userId,session.sharedAnalysisIds)
		
		//remove special cases
		def specialCases = []
		myAnalyses.each{ analysis ->
			if((analysis.type == AnalysisType.KM_GENE_EXPRESSION) && (analysis.query.geAnalysisId.toString())){
				specialCases << analysis
			}
		}
		println "remove special case analyses $specialCases"
		myAnalyses.removeAll(specialCases)
		
		def ids = myAnalyses.collect{it.id}
		def filteredAnalysisIds = []
		
		if(params.analysisFilter){
			if(params.analysisFilter == 'all'){
				session.analysisFilter = "all"
				filteredAnalysis = myAnalyses
				filteredAnalysisIds = ids
			}
			else{
				session.analysisFilter = params.analysisFilter
				filteredAnalysis = savedAnalysisService.filterAnalysis(params.analysisFilter,myAnalyses,session.userId)
				filteredAnalysisIds = filteredAnalysis.collect{it.id}
			}
		}
		else if(session.analysisFilter){
			filteredAnalysis = savedAnalysisService.filterAnalysis(session.analysisFilter,myAnalyses,session.userId)
			filteredAnalysisIds = filteredAnalysis.collect{it.id}
		}
		else{
			session.analysisFilter = "all"
			filteredAnalysisIds = ids
		}
		
		
		if(params.offset){
			pagedAnalyses = savedAnalysisService.getPaginatedAnalyses(filteredAnalysisIds,params.offset.toInteger())
		}
		else{
			pagedAnalyses = savedAnalysisService.getPaginatedAnalyses(filteredAnalysisIds,0)	
		}
        [ savedAnalysis: pagedAnalyses, allAnalysesSize:myAnalyses.size(), timePeriods: timePeriods]
    }

	def delete = {
		savedAnalysisService.deleteAnalysis(params.id)
		def myAnalyses = []
		myAnalyses = savedAnalysisService.getAllSavedAnalysis(session.userId,session.sharedAnalysisIds)
		def filteredAnalysis = []
		if(session.analysisFilter){
			filteredAnalysis = savedAnalysisService.filterAnalysis(session.analysisFilter,myAnalyses,session.userId)
		}
		render(template:"/savedAnalysis/savedAnalysisTable",model:[ savedAnalysis: filteredAnalysis ])
	}
	
	def deleteMultipleAnalyses ={
		def message = ""
		if(params.deleteAnalyses){
			println "Requesting deletion of: $params.deleteAnalyses"
			if(params.deleteAnalyses.metaClass.respondsTo(params.deleteAnalyses, "max")){
				params.deleteAnalyses.each{ analysisIdToBeRemoved ->
					print analysisIdToBeRemoved + " "
					def analysis = SavedAnalysis.get(analysisIdToBeRemoved)
			        if(analysis) {
			            if(analysis.evidence){
							println "could not delete " + analysis + ", this link represents a piece of evidence in a G-DOC finding"
							message += "analysis $analysis.id could not be deleted because represented as a piece of evidence in a G-DOC finding."
						}else{
			            	analysis.delete(flush:true)
							println "deleted " + analysis
							message += "analysis $analysis.id has been deleted."
						}
					}
				}
			}else{
				def analysis = SavedAnalysis.get(params.deleteAnalyses)
		        if(analysis) {
		             if(analysis.evidence){
							println "could not delete " + analysis + ", this link represents a piece of evidence in a G-DOC finding"
							message += "analysis $analysis.id could not be deleted because represented as a piece of evidence in a G-DOC finding."
						}else{
			            	analysis.delete(flush:true)
							message += "analysis $analysis.id has been deleted."
						}
				}
			}
			flash.message = message
			redirect(action:index)
			return
		}else{
			flash.message = "No analyses have been selected for deletion"
			redirect(action:index)
			return
		}
	}
	
	//TODO - decide if we always want to auto-save KM plots. Right now, we do not. They must implicitly call 'save'.
	def save = {
			println ("THE RESULT:")
			println params.resultData
			def savedAttempt = [:]
			println "session command" + session.command
			if(session.command != null){
			println ("THE COMMAND PARAMS:")
				if(savedAnalysisService.saveAnalysisResult(session.userId, params.resultData,session.command,null)){
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