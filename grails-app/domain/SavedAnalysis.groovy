import grails.converters.*


class SavedAnalysis implements Taggable {
	static mapping = {
		table 'SAVED_ANALYSIS'
		analysisData type: 'text'
		queryData type: 'text'
		type column:'analysis_type'
	}
	static transients = [ "analysis", "query"]
	
	Object analysis
	Object query
	AnalysisType type
	String analysisData
	String queryData
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
		this.@analysisData = data as AnalysisJSON
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
		this.@queryData = data as AnalysisJSON
	}
	
	def reloadData = {
		this.@analysis = JSON.parse(analysisData)
	}
	
}