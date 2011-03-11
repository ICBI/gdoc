import grails.converters.*
import org.springframework.util.ClassUtils
import org.compass.core.engine.SearchEngineQueryParseException
import java.util.regex.Matcher
import java.util.regex.Pattern

class SearchController {
	def searchableService
	
	def index = {
		 def invalidChars = ['*','?','~','[',']','"','+','-','<','>']
		 if (!(params.q?.trim()) || invalidChars.contains(params.q)) { 
			log.debug "no params or invalid params"
			//flash.message = "no params or invalid params"
			return [:] 
		 } 
		else{
			try { 
			def tbdResults = []
			def suggs = []
			log.debug "search string = $params.q" + "*" 
			def searchResult
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
			
			log.debug "got results=" + searchResult
			if(!searchResult.results){
				suggs = gatherTermFreqs(params.q)
				if(suggs.size()>=5){
					suggs = suggs.getAt(0..5)
				}
			}
			return [searchResult:searchResult,suggs:suggs] 
		 } catch (SearchEngineQueryParseException ex) { 
			return [parseException: true] 
		 } 	catch (RuntimeException ex) { 
				return [parseException: true] 
		}
		}

	}
	
	
	def relevantTerms = {
		log.debug params
		def searchResult = []
		if (!params.q?.trim()) { 
			render ""
		}else{
			 try { 
				searchResult = gatherTermFreqs(params.q)
				render searchResult as JSON
			 } catch (SearchEngineQueryParseException ex) { 
				log.debug ex
				render ""
			 }
		}
		
	}
	
	def userAutocomplete = {
		log.debug params
		def searchResult = []
		if (!params.q?.trim()) { 
			render ""
		}else{
			 try { 
				def terms = []
				terms << GDOCUser.termFreqs("firstName")
				terms << GDOCUser.termFreqs("lastName")
				terms << GDOCUser.termFreqs("username")
				terms.flatten().each{
					if(it.term.contains(params.q.trim()))
						searchResult << it.term
				}
				log.debug searchResult
				render searchResult as JSON
			 } catch (SearchEngineQueryParseException ex) { 
				log.debug ex
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
			terms << GeneAlias.termFreqs("symbol",size:30000)
			terms << searchableService.termFreqs("lastName")
			terms << searchableService.termFreqs("title")
			terms.flatten().each{
				if(it.term.contains(query.trim()))
					searchResult << it.term
			}
			//log.debug searchResult
			return searchResult
		 } catch (SearchEngineQueryParseException ex) { 
			log.debug ex
			return []
		 }
	}

}