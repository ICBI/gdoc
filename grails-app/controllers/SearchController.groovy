import grails.converters.*
import org.springframework.util.ClassUtils
import org.compass.core.engine.SearchEngineQueryParseException

class SearchController {
	def searchableService
	
	def index = {
		
		 if (!params.q?.trim()) { 
			println "no params"
			return [:] 
		 } 
		else{
			try { 
			def tbdResults = []
			def cleaned = []
			println "search string = $params.q"
			def searchResult = searchableService.search([result:"searchResult",max:50],{
					queryString(params.q+"*")
			})
			println searchResult.class
			if(searchResult){
				searchResult.results.each{
					def resultClass = ClassUtils.getShortName(it.getClass())
					if(resultClass != "MoleculeTarget" && resultClass != "StudyDataSource"){ 
						println "$resultClass is not a MoleculeTarget or StudyDataSource"
						tbdResults << it
					}
				}
				
				searchResult.results.removeAll(tbdResults)
				searchResult.total = searchResult.results.size()
			}
			
			println searchResult
			return [searchResult:searchResult] 
		 } catch (SearchEngineQueryParseException ex) { 
			return [parseException: true] 
		 } 
		}
	}
	
	
	def relevantTerms = {
		println params
		def searchResult = []
		if (!params.q?.trim()) { 
			render ""
		}else{
			 try { 
				def terms = []
				terms << searchableService.termFreqs("longName")
				terms << searchableService.termFreqs("shortName")
				terms << searchableService.termFreqs("cancerSite")
				terms << searchableService.termFreqs("abstractText")
				terms << searchableService.termFreqs("name")
				terms << searchableService.termFreqs("symbol")
				terms.flatten().each{
					if(it.term.contains(params.q?.trim()))
						searchResult << it.term
				}
				render searchResult as JSON
			 } catch (SearchEngineQueryParseException ex) { 
				println ex
				render ""
			 }
		}
		
	}

}