import grails.converters.*

class SavedAnalysisTests extends GroovyTestCase {
	
	void testNothing() {
		
	}
/*	def securityService
	
	void testGetAllSavedAnalysis(){
		def user = GDOCUser.findByUsername("kmr75")
		//def groupAnalysisIds = securityService.getSharedItemIds("kmr75", SavedAnalysis.class.name,true)
		def notifications = []
		notifications = user.analysis
		def today = new Date()
		notifications.each{ n ->
			//println today.minus(n.dateCreated)
		}
		println "All notifications:" + notifications.size()
		
	}
	
	void testSaveAnalysis() {
		def user = GDOCUser.findByUsername("gdocUser");
		def data = [test: "data"]
		def params = [pvalue: 0.1]
		def analysis = new SavedAnalysis(type:AnalysisType.KM_GENE_EXPRESSION, analysis: data, query: params, author:user)
		analysis.save()
		
		assertEquals(1, SavedAnalysis.findAll().size())
	}
	
	void testLoadAnalysis() {
		def user = GDOCUser.findByUsername("gdocUser");
		def km = JSON.parse('{"demo_dr_yes":[{"x":0,"y":1,"census":true},{"x":0.8,"y":1,"census":false},{"x":0.8,"y":0.98,"census":false},{"x":1.8,"y":0.98,"census":false},{"x":1.8,"y":0.96000004,"census":false}],"pvalue":3.980287634556118E-135}')
		def params = [pvalue: 0.1]
		def analysis = new SavedAnalysis(type:AnalysisType.KM_GENE_EXPRESSION, analysis: km , query: params, author:user)
		analysis.save()
		
		def shared = SavedAnalysis.findByType(AnalysisType.KM_GENE_EXPRESSION)
		
		assertNotNull(shared)
		assertNotNull(shared.analysis)
		assertEquals(5, shared.analysis["demo_dr_yes"].size())
	}
	
	void testDeleteAnalysis() {
		def user = GDOCUser.findByUsername("gdocUser");
		def data = [test: "data"]
		def params = [pvalue: 0.1]
		def analysis = new SavedAnalysis(type:AnalysisType.KM_GENE_EXPRESSION, analysis: data, query: params, author:user)
		analysis.save(flush:true)
		
		analysis.delete(flush:true)
		
		assertEquals(0, SavedAnalysis.findAll().size())
	}
	
	void testUpdateAnalysis() {
		def user = GDOCUser.findByUsername("gdocUser");
		def data = [test: "data"]
		def params = [pvalue: 0.1]
		def analysis = new SavedAnalysis(type:AnalysisType.KM_GENE_EXPRESSION, analysis: data, query: params, author:user)
		analysis.save(flush:true)
		
		def km = JSON.parse('{"demo_dr_yes":[{"x":0,"y":1,"census":true},{"x":0.8,"y":1,"census":false},{"x":0.8,"y":0.98,"census":false},{"x":1.8,"y":0.98,"census":false},{"x":1.8,"y":0.96000004,"census":false}],"pvalue":3.980287634556118E-135}')
		analysis.analysis = km
		analysis.save(flush:true)
		
		def shared = analysis
		
		assertNotNull(shared)
		assertNotNull(shared.analysis)
		assertNotNull(shared.query)
		assertEquals(5, shared.analysis["demo_dr_yes"].size())
	}*/
}