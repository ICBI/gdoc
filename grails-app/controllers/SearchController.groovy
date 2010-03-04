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
			def searchResult
			/** TODO - this search returns a motley group of results, which we filter. We need to search based
			on object type, or describe this filter in our query builder...otehrwise, pagination
			becomes a problem. the max is set to 150, this is totally misleading and is needed in the case that
			our result might not ever 'get to' our desired, filtered objects. 
			see here:http://www.grails.org/Searchable+Plugin+-+Methods+-+search
			-KR
			**/
			if(!params.offset){
				searchResult = searchableService.search([result:'searchResult',defaultOperator:"and",offset:0,max:10,order: "asc"],{
							must({
								queryString(params.q+"*")
								polyAlias(params.q)
								})
							must({                                  
									alias("findings") 
							        alias("targets")
									alias("studies")
							   })
				})
			}else{
				searchResult = searchableService.search(params,{
						must({
							queryString(params.q+"*")
							polyAlias(params.q)
							})
						must({                                  
								alias("findings") 
						        alias("targets")
								alias("studies")
						   })
				})
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
			terms << Protein.termFreqs("name")
			terms << searchableService.termFreqs("symbol")
			terms << searchableService.termFreqs("lastName")
			terms << searchableService.termFreqs("title")
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