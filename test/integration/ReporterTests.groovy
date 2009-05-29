class ReporterTests extends GroovyTestCase {

    void testFindReporterByGene() {
		def criteria = Reporter.createCriteria()
		def reporters = criteria.list{
			eq("geneSymbol", "EGFR")
		}
		reporters.each {
			println it.name
		}
    }
}
