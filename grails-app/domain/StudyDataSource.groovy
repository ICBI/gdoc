class StudyDataSource {

	static mapping = {
		table 'COMMON.DATA_SOURCE'
		version false
		id column:'data_source_id'
		abstractText column: 'abstract'
	}
	static transients = [ "genomicData"]
	
	String shortName
	String longName
	String abstractText
	String cancerSite
	String piLastName
	String piFirstName
	String piNameSuffix
	String contactLastName
	String contactFirstName
	String schemaName
	String contact_email
	Boolean genomicData
	
	public Boolean getGenomicData() {
		if(this.@genomicData) {
			return this.@genomicData
		}
		def tempStudy = StudyContext.getStudy()
		StudyContext.setStudy(this.schemaName)
		def rBinaryFiles = MicroarrayFile.findByNameLike("%.Rda")
		StudyContext.setStudy(tempStudy)
		this.@genomicData = (rBinaryFiles) 
		return this.@genomicData
	}
}
