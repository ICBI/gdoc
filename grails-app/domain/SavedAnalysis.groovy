import grails.converters.*

class SavedAnalysis implements Taggable {
	static mapping = {
		table 'SAVED_ANALYSIS'
		jsonData type:'text'
		queryParameters type:'text'
	}
	static transients = [ "analysis", "queryParams" ]
	
	AnalysisType type
	String jsonData
	String queryParameters
	GDOCUser author
	
	public Object getAnalysis() {
		if(jsonData)
			return JSON.parse(jsonData)
		else 
			return null
	}
	
	public void setAnalysis(Object data) {
		this.@jsonData = data as JSON
	}
	
	public Object getQueryParams() {
		if(queryParameters)
			return JSON.parse(queryParameters)
		else 
			return null
	}
	
	public void setQueryParams(Object data) {
		this.@queryParameters = data as JSON
	}
}