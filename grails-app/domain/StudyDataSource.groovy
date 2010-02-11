class StudyDataSource {

	static mapping = {
		table 'COMMON.DATA_SOURCE'
		version false
		id column:'data_source_id', generator: 'sequence', params: [sequence: 'DATA_SOURCE_SEQUENCE']
		abstractText column: 'abstract'
		pis joinTable: [name:'data_source_pis', key:'data_source_id', column:'contact_id']
		pocs joinTable: [name:'data_source_pocs', key:'data_source_id', column:'contact_id']
	}
	
	static searchable = {
			mapping {
			        abstractText index: 'analyzed'
					schemaName index: 'no'
					pis component: true
					pocs component: true
			        spellCheck "include"
			}
	}

	
	static hasMany = [pis: Contact, pocs: Contact, content: DataSourceContent]
	static fetchMode = [content:"eager", pis: "eager", pocs: "eager"]
	static transients = [ "genomicData"]
	
	String shortName
	String longName
	String abstractText
	String cancerSite
	String schemaName
	String patientIdName
	String integrated
	String overallAccess
	String useInGui
	String insertUser
	Date insertDate
	String insertMethod
	Boolean genomicData
	
	public Boolean getGenomicData() {
		if(this.@genomicData) {
			return this.@genomicData
		}
		if(hasGenomicData()) {
			def tempStudy = StudyContext.getStudy()
			StudyContext.setStudy(this.schemaName)
			def rBinaryFiles = MicroarrayFile.findByNameLike("%.Rda")
			StudyContext.setStudy(tempStudy)
			this.@genomicData = (rBinaryFiles) 
		}
		return this.@genomicData
	}
	
	def hasGenomicData() {
		return content.find {
			it.type == "MICROARRAY"
		}
	}
	
	def hasClinicalData() {
		return content.find {
			it.type == "CLINIC"
		}
	}
}
