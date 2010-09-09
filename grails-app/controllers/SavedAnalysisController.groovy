import grails.converters.*

class SavedAnalysisController{
	def securityService
	def savedAnalysisService
	def tagService
	
	def index = {
		def myAnalyses = []
		def timePeriods = [1:"1 day",7:"1 week",30:"past 30 days",90:"past 90 days",180:"past 6 months",hideShared:"hide shared lists",all:"show all"]
		def filteredAnalysis = []
		def pagedAnalyses = [:]
		
		
		if(params.analysisFilter){
			session.analysisFilter = params.analysisFilter
			//myAnalyses = savedAnalysisService.getFilteredAnalysis(params.analysisFilter,session.userId,session.sharedAnalysisIds)
		}
		else if(session.analysisFilter){
			log.debug "current session analysis filter is $session.analysisFilter"
		}
		else{
			session.analysisFilter = "hideShared"
			//myAnalyses = savedAnalysisService.getFilteredAnalysis("hideShared",session.userId,session.sharedAnalysisIds)
		}
		if(params.offset){
			pagedAnalyses = savedAnalysisService.getPaginatedAnalyses(session.analysisFilter,session.sharedAnalysisIds,params.offset.toInteger(),session.userId)
		}
		else{
			pagedAnalyses = savedAnalysisService.getPaginatedAnalyses(session.analysisFilter,session.sharedAnalysisIds,0,session.userId)	
		}
		/**pagedAnalyses["result"].each{
			def sAnalysesMap = [:]
			sAnalysesMap["id"] = it[0]
			sAnalysesMap["type"] = it[1]
			sAnalysesMap["status"] = it[2]
			sAnalysesMap["dateCreated"] = it[3]
			sAnalysesMap["author"] = it[4]
			sAnalysesMap["studies"] = it[5]
			filteredAnalysis << sAnalysesMap
		}
		remove special cases
		if(myAnalyses){
			def filteredAnalysisIds = myAnalyses.collect{it.id}
			/**def specialCases = []
			myAnalyses.each{ analysis ->
				if((analysis.type == AnalysisType.KM_GENE_EXPRESSION) && (analysis.query.geAnalysisId.toString() == 'null')){
					specialCases << analysis
				}
				if(savedAnalysisService.analysisIsTemporary(analysis.id)){
					//if(!session.tempAnalyses?.contains(it.id)){
					session.tempAnalyses << analysis.id
					//}
				}
			}
			log.debug "remove special case analyses $specialCases"
			if(specialCases)
				myAnalyses.removeAll(specialCases)

			//def ids = myAnalyses.collect{it.id}
			def filteredAnalysisIds = myAnalyses.collect{it.id}
			/**
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


			
		}**/
		log.debug "the count is " + pagedAnalyses.totalCount
        [ savedAnalysis: pagedAnalyses, allAnalysesSize:pagedAnalyses.totalCount, timePeriods: timePeriods]
    }

	def delete = {
		savedAnalysisService.deleteAnalysis(params.id)
		def myAnalyses = []
		/**myAnalyses = savedAnalysisService.getAllSavedAnalysis(session.userId,session.sharedAnalysisIds)
		def filteredAnalysis = []
		if(session.analysisFilter){
			filteredAnalysis = savedAnalysisService.filterAnalysis(session.analysisFilter,myAnalyses,session.userId)
		}
		render(template:"/savedAnalysis/savedAnalysisTable",model:[ savedAnalysis: filteredAnalysis ])**/
		redirect(action:index)
		return
	}
	
	def deleteMultipleAnalyses ={
		def message = ""
		if(params.deleteAnalyses){
			log.debug "Requesting deletion of: $params.deleteAnalyses"
			if(params.deleteAnalyses.metaClass.respondsTo(params.deleteAnalyses, "max")){
				params.deleteAnalyses.each{ analysisIdToBeRemoved ->
					print analysisIdToBeRemoved + " "
					def analysis = SavedAnalysis.get(analysisIdToBeRemoved)
			        if(analysis) {
			            if(analysis.evidence){
							log.debug "could not delete " + analysis + ", this link represents a piece of evidence in a G-DOC finding"
							message += "analysis $analysis.id could not be deleted because represented as a piece of evidence in a G-DOC finding."
						}else{
			            	analysis.delete(flush:true)
							log.debug "deleted " + analysis
							message += "analysis $analysis.id has been deleted."
						}
					}
				}
			}else{
				def analysis = SavedAnalysis.get(params.deleteAnalyses)
		        if(analysis) {
		             if(analysis.evidence){
							log.debug "could not delete " + analysis + ", this link represents a piece of evidence in a G-DOC finding"
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
			log.debug ("THE RESULT:")
			log.debug params.resultData
			def savedAttempt = [:]
			log.debug "session command" + session.command
			if(session.command != null){
			log.debug ("THE COMMAND PARAMS:")
				if(savedAnalysisService.saveAnalysisResult(session.userId, params.resultData,session.command,null)){
					log.debug ("saved analysis")
					savedAttempt["result"] = "Analysis Saved"
					render savedAttempt as JSON		
				} 	else{
							savedAttempt["result"] = "Analysis not Saved -- may have already been saved."
							log.debug savedAttempt
							render savedAttempt as JSON
					}
			}else{
					savedAttempt["result"] = "Analysis not Saved -- may have already been saved."
					log.debug savedAttempt
					render savedAttempt as JSON
			}
	}
	
	def addTag = {
		log.debug params
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
		log.debug params
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