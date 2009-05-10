import grails.converters.*


class SavedAnalysis implements Taggable {
	static mapping = {
		table 'SAVED_ANALYSIS'
		jsonData type: 'text'
		type column:'analysis_type'
	}
	static transients = [ "analysis"]
	
	Object analysis
	AnalysisType type
	String jsonData
	GDOCUser author
	
	public Object getAnalysis() {
		if(this.@analysis) {
			return this.@analysis
		}
		if(jsonData) {
			this.@analysis = JSON.parse(jsonData)
			return this.@analysis
		} else {
			return null
		}
	}
	
	public void setAnalysis(Object data) {
		this.@jsonData = data as AnalysisJSON
	}
	
}