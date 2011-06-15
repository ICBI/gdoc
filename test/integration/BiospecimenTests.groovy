class BiospecimenTests extends GroovyTestCase {
	def jdbcTemplate

    void testBiospecimenValues() {
		StudyContext.setStudy("LOI")
		def specimens = Biospecimen.findAll()
		specimens.each { specimen ->
			assertNotNull(specimen.biospecimenData)
			specimen.biospecimenData.each {
				println it
			}
		}
		
    }

	void testSamples(){
		StudyContext.setStudy("INDIVDEMO")
		def study = "INDIVDEMO"
		def specWith = []
		println "now $specWith"
		specWith = Sample.findAllByDesignType('METABOLOMICS')
		def sids = []
		sids = specWith.collect{it.id}
		def sidsString = sids.toString().replace("[","")
		sidsString = sidsString.replace("]","")
		println "after $sids"
		println sidsString
		//get biospecsl("from Sample as s where s.id in (:p)", [p:plist])
		def query = "select s.biospecimen_id from " + study + ".HT_FILE_CONTENTS s where s.id in ("+sidsString+")"
		def bsWith = jdbcTemplate.queryForList(query)
		println bsWith
		specWith = []
	}
}
