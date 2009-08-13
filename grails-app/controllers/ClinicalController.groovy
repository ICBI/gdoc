import grails.converters.*

class ClinicalController {

	def clinicalService
	def searchResults
	
    def index = { 
		if(params.id) {
			def currStudy = StudyDataSource.get(params.id)
			session.study = currStudy
			StudyContext.setStudy(session.study.schemaName)
		}
		session.dataTypes = AttributeType.findAll().sort { it.longName }
	}
	
	def search = {
		def criteria = QueryBuilder.build(params, "clinical_", session.dataTypes)
		println criteria
		searchResults = clinicalService.queryByCriteria(criteria)
		def columns = []
		columns << [index: "id", name: "GDOC ID", sortable: true, width: '70']
		columns << [index: "dataSourceInternalId", name: "PATIENT ID", sortable: true, width: '70']
		def columnNames = []
		println searchResults
		searchResults.each { patient ->
			patient.clinicalData.each { key, value ->
				if(!columnNames.contains(key)) {
					columnNames << key
				}
			}
		}
		columnNames.sort()
		columnNames.each {
			def column = [:]
			column["index"] = it
			column["name"] = it
			column["width"] = '70'
			column["sortable"] = true
			columns << column
		}
		session.columnJson = columns as JSON
		def sortedColumns = ["GDOC ID", "PATIENT ID"]
		columnNames.sort()
		sortedColumns.addAll(columnNames)
		session.results = searchResults
		session.columns = sortedColumns
		session.columnNames = sortedColumns as JSON
	}
	
	def view = {
		searchResults = session.results
		def columns = session.columns
		def results = []
		def rows = params.rows.toInteger()
		def currPage = params.page.toInteger()
		def startIndex = ((currPage - 1) * rows)
		def endIndex = (currPage * rows)
		def sortColumn = params.sidx
		if(endIndex > searchResults.size()) {
			endIndex = searchResults.size()
		}
		def sortedResults = searchResults.sort { r1, r2 ->
			def val1 
			def val2
			if(sortColumn == "id" || sortColumn == "dataSourceInternalId") {
				val1 = r1[sortColumn]
				val2 = r2[sortColumn]
			} else {
				val1 = r1.clinicalData[sortColumn]
				val2 = r2.clinicalData[sortColumn]
				if(val1 && val1.isDouble()) {
					val1 = r1.clinicalData[sortColumn].toDouble()
				} 
				if(val2 && val2.isDouble()) {
					val2 = r2.clinicalData[sortColumn].toDouble()
				}
				
			}
			
			def comparison
			if(val1 == val2) {
				return 0
			}
			if(params.sord != 'asc') {
				if(val2 == null) {
					return -1
				} else if (val1 == null) {
					return 1
				}
				comparison =  val2.compareTo(val1)
			} else {
				if(val1 == null) {
					return -1
				} else if(val2 == null) {
					return 1
				}
				comparison =  val1.compareTo(val2)
			}
			return comparison
		}
		session.results = sortedResults
		sortedResults.getAt(startIndex..<endIndex).each { patient ->
			def cells = []
			cells << patient.gdocId
			cells << patient.dataSourceInternalId
			columns.each { item ->
				if(item != "GDOC ID" && item != "PATIENT ID")
					cells << patient.clinicalData[item]
			}
			results << [id: patient.gdocId, cell: cells]
		}
		def jsonObject = [
			page: currPage,
			total: Math.ceil(searchResults.size() / rows),
			records: searchResults.size(),
			rows:results
		]
		render jsonObject as JSON
	}
}
