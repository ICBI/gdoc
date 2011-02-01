public enum DataType {
	CLINICAL('CLINICAL', 'clinical'),
	GENE_EXPRESSION('GENE_EXPRESSION', 'gene_expression'),
	COPY_NUMBER('COPY_NUMBER', 'copy_number'),
	MICRORNA('microRNA', 'microrna'),
	METABOLOMICS('METABOLOMICS', 'metabolomics')
	
	String name;
	String tag;
	
	DataType(String name, String tag) {
		this.name = name
		this.tag = tag
	}
	
	public String value() {
		return name()
	}
	
	public String tag() {
		return this.tag
	}
	
}