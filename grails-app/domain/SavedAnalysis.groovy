import grails.converters.*


class SavedAnalysis implements Taggable {
	static mapping = {
		table 'SAVED_ANALYSIS'
		jsonData type: 'text'
		type column:'analysis_type'
	}
	static transients = [ "analysis"]
	
	AnalysisType type
	String jsonData
	GDOCUser author
	
	public Object getAnalysis() {
		if(jsonData)
			return JSON.parse(jsonData)
		else 
			return null
	}
	
	public void setAnalysis(Object data) {
		this.@jsonData = data as AnalysisJSON
	}
	
}