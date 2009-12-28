class StructureFile{
	static mapping = {
		table 'DRUG.STRUCTURE_FILE'
	}
	String name
	String relativePath
	Long fileSize
	FileType fileType
	FileFormat fileFormat
	Date dateCreated
	Date lastUpdated
	static constraints = {
		name(nullable:false)
		relativePath(nullable:false)
	}
	

	
}