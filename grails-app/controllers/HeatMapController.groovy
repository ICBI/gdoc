import grails.converters.*
import java.text.*
import java.math.*
import gov.nih.nci.caintegrator.analysis.messaging.HeatMapResult

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
			def reporterLists = []
			lists.each { item ->
				if(item.tags.contains("reporter"))
					reporterLists << item
			}
			session.reporterLists = reporterLists
			session.patientLists = patientLists
		}
		
		def diseases = session.myStudies.collect{it.cancerSite}
		diseases.remove("N/A")
		def myStudies = session.myStudies
		return [diseases:diseases as Set,myStudies:myStudies, params:params]
	}
	
	def view = {
		def analysisResult = savedAnalysisService.getSavedAnalysis(params.id)
		StudyContext.setStudy(analysisResult.query["study"])
		session.results = analysisResult.analysis.item
		session.analysis = analysisResult
	}
	
	def drawHeatMap = { HeatMapCommand cmd ->
		println "heatmap params : $params"
		println "type : " + cmd.requestType
		println "patientList : " + cmd.patientList
		println "geneList : " + cmd.geneList
		println "reporterList : " + cmd.reporterList
		println "study:" + cmd.study 
		println cmd.errors
		if(!session.study)
			StudyContext.setStudy(cmd.study)
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
			analysisService.sendRequest(session.id, cmd, tags)
			redirect(controller:'notification')
		}

	}
	
	def selectDataType = {
		if(!session.files[params.dataType])
			render g.select(optionKey: 'name', optionValue: 'description', noSelection: ['': 'Select Data Type First'], id: 'dataFile', name: "dataFile")
		else
			render g.select(optionKey: 'name', optionValue: 'description', from: session.files[params.dataType], id: 'dataFile', name: "dataFile")
	}
	
	def file = {
		def result = savedAnalysisService.getSavedAnalysis(params.id)//session.results
		StudyContext.setStudy(result.query["study"])
		session.results = result.analysis.item
		session.analysis = result
		try{
			if(params.name){
				byte[] fileBytes
				if(params.name.indexOf('.cdt') > 1)
					fileBytes = result.analysis.item.cdtFile
				if(params.name.indexOf('.gtr') > 1)
					fileBytes = result.analysis.item.gtrFile
				if(params.name.indexOf('.atr') > 1)
					fileBytes = result.analysis.item.atrFile
				response.outputStream << fileBytes
			}
		}catch(java.io.FileNotFoundException fnf){
			println fnf.toString()
			render "File ($params.name) was not found...is the file name correct?"
		} catch (Exception e) {
			e.printStackTrace()
		}
		
	}
	
	
	
}
