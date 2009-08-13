class StudyDataSource {

	static mapping = {
		table 'COMMON.DATA_SOURCE'
		version false
		id column:'data_source_id'
		abstractText column: 'abstract'
		pis joinTable: [name:'data_source_pis', key:'data_source_id', column:'contact_id']
		pocs joinTable: [name:'data_source_pocs', key:'data_source_id', column:'contact_id']
	}
	
	static hasMany = [pis: Contact, pocs: Contact, content: DataSourceContent]
	static fetchMode = [content:"eager"]
	static transients = [ "genomicData"]
	
	String shortName
	String longName
	String abstractText
	String cancerSite
	String schemaName
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
	
	def hasGenomicData() {
		return content.find {
			it.type == "MICROARRAY"
		}
	}
}
