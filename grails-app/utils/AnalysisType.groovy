public enum AnalysisType {
	KM,
	CLASS_COMPARISON, 
	GENE_EXPRESSION;

	public String value() {
		return name();
	}

	public static AnalysisType fromValue(String v) {
		return valueOf(v);
	}
	
}