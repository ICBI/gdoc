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
			def suggs = []
			println "search string = $params.q" + "*" 
			def searchResult = searchableService.search(result:'searchResult',max:150){
					queryString(params.q+"*")
			}
			
			if(searchResult){
				searchResult.results.each{
					def resultClass = ClassUtils.getShortName(it.getClass())
					if(resultClass != "MoleculeTarget" && resultClass != "StudyDataSource"){ 
						//println "$resultClass is not a MoleculeTarget or StudyDataSource"
						tbdResults << it
					}
				}
				println "now remove all results that aren't our desired domain objects"
				searchResult.results.removeAll(tbdResults)
				searchResult.total = searchResult.results.size()
			}
			
			println searchResult
			if(!searchResult.results){
				suggs = gatherTermFreqs(params.q)
				if(suggs.size()>=5){
					suggs = suggs.getAt(0..5)
				}
			}
			return [searchResult:searchResult,suggs:suggs] 
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
				searchResult = gatherTermFreqs(params.q)
				render searchResult as JSON
			 } catch (SearchEngineQueryParseException ex) { 
				println ex
				render ""
			 }
		}
		
	}
	
	def gatherTermFreqs(query){
		def searchResult = []
		try { 
			def terms = []
			terms << searchableService.termFreqs("longName")
			terms << searchableService.termFreqs("shortName")
			terms << searchableService.termFreqs("cancerSite")
			terms << searchableService.termFreqs("abstractText")
			terms << searchableService.termFreqs("name")
			terms << searchableService.termFreqs("symbol")
			terms << searchableService.termFreqs("lastName")
			terms.flatten().each{
				if(it.term.contains(query.trim()))
					searchResult << it.term
			}
			return searchResult
		 } catch (SearchEngineQueryParseException ex) { 
			println ex
			return []
		 }
	}

}