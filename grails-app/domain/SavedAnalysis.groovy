import grails.converters.*


class SavedAnalysis implements Taggable {
	static mapping = {
		table 'SAVED_ANALYSIS'
		analysisData type: 'text'
		queryData type: 'text'
		type column:'analysis_type'
		
	}
	
	static constraints = {
		taskId nullable: true
	}
	
	static transients = [ "analysis", "query"]
	
	Object analysis
	Object query
	AnalysisType type
	String analysisData
	String queryData
	String status
	String taskId
	GDOCUser author
	Date dateCreated
	Date lastUpdated
	
	public Object getAnalysis() {
		if(this.@analysis) {
			return this.@analysis
		}
		if(analysisData) {
			this.@analysis = JSON.parse(analysisData)
			return this.@analysis
		} else {
			return null
		}
	}
	
	public void setAnalysis(Object data) {
		def temp = data as JSON
		println "TEMP: $temp"
		this.@analysisData = data as JSON
	}
	
	public Object getQuery() {
		if(this.@query) {
			return this.@query
		}
		if(queryData) {
			this.@query = JSON.parse(queryData)
			return this.@query
		} else {
			return null
		}
	}
	
	public void setQuery(Object data) {
		this.@queryData = data as JSON
	}
	
	def reloadData = {
		this.@analysis = JSON.parse(analysisData)
	}
	
}