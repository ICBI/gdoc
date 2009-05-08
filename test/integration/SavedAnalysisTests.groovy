import grails.converters.*

class SavedAnalysisTests extends GroovyTestCase {
	
	void testSaveAnalysis() {
		def user = new GDOCUser(username:"gdocUser");
		user.save();
		def data = [test: "data"]
		def params = [pvalue: 0.1]
		def analysis = new SavedAnalysis(type:AnalysisType.KM, analysis: data, author:user, queryParams: params)
		analysis.save()
		
		assertEquals(1, SavedAnalysis.findAll().size())
	}
	
	void testLoadAnalysis() {
		def user = new GDOCUser(username:"gdocUser");
		user.save();
		def km = JSON.parse('{"demo_dr_yes":[{"x":0,"y":1,"census":true},{"x":0.8,"y":1,"census":false},{"x":0.8,"y":0.98,"census":false},{"x":1.8,"y":0.98,"census":false},{"x":1.8,"y":0.96000004,"census":false}],"pvalue":3.980287634556118E-135}')
		def params = [pvalue: 0.1]
		def analysis = new SavedAnalysis(type:AnalysisType.KM, analysis: km , author:user, queryParams: params)
		analysis.save()
		
		def shared = SavedAnalysis.findByType(AnalysisType.KM)
		
		assertNotNull(shared)
		assertNotNull(shared.analysis)
		assertEquals(5, shared.analysis["demo_dr_yes"].size())
		assertNotNull(shared.queryParams)
		assertEquals(0.1, shared.queryParams["pvalue"])
	}
}