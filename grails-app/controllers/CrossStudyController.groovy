import grails.converters.*

class CrossStudyController {
	def clinicalService
	
	def index = {
		def searchable = clinicalService.retrieveSearchableAttributes()
		def attrbs = []
		searchable.results.bindings.each{ binding ->
				binding.each{ attribute ->
					attrbs << attribute.value.value.split('#')[1]
				}
			}
		 attrbs << "Study"
		 session.myAtts = attrbs.sort()
		 [myAtts: attrbs.sort()]
	}
	
	def searchAcrossStudies = {
		//params["range_nci:AGE"] = "50 - 90"
		params["nci:DR"] = "YES"
		log.debug params
		def sparql = QueryBuilder.buildSparql(params)
		def results = clinicalService.queryAcrossStudies(sparql)
		log.debug results
		[results:results]
	}
}