import grails.converters.*
import java.text.*
import java.math.*


class HeatMapController {

	def analysisService
	def savedAnalysisService
	def annotationService
	def userListService
	def drugDiscoveryService
	def htDataService
	
    def index = {
	    if(session.study){
			StudyContext.setStudy(session.study.schemaName)
			def lists = userListService.getAllLists(session.userId,session.sharedListIds)
			def patientLists = lists.findAll { item ->
				(item.tags.contains("patient") && item.schemaNames().contains(StudyContext.getStudy()))
			}
			session.patientLists = patientLists
		}
		
		def diseases = session.myStudies.collect{it.cancerSite}
		diseases.remove("N/A")
		def myStudies = session.myStudies
		return [diseases:diseases as Set,myStudies:myStudies, params:params]
	}
	
	
	def drawHeatMap = { HeatMapCommand cmd ->
		println "heatmap params : $params"
		println "type : " + cmd.requestType
		println "patientList : " + cmd.patientList
		println "geneList : " + cmd.geneList
		println "reporterList : " + cmd.reporterList
		println "study:" + cmd.study 
		println cmd.errors
		def tags = []
		tags << "heatMap"
		
		def author = GDOCUser.findByLoginName(session.userId)
		def list1IsTemp = userListService.listIsTemporary(cmd.patientList,author)
		if(list1IsTemp){
			tags << Constants.TEMPORARY
		}
		
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			def study = StudyDataSource.findBySchemaName(cmd.study)
			redirect(action:'index',id:study.id)
		} else {
			//analysisService.sendRequest(session.id, cmd, tags)
			redirect(controller:'savedAnalysis')
		}

	}
	
	
	
	
}
