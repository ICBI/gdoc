public enum AnalysisType {
	KM,
	CLASS_COMPARISON;

	public String value() {
		return name();
	}

	public static AnalysisType fromValue(String v) {
		return valueOf(v);
	}
	
}