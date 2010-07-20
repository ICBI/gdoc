import grails.converters.*
import java.text.*
import java.math.*
import gov.nih.nci.caintegrator.analysis.messaging.HeatMapResult

@Mixin(ControllerMixin)
class HeatMapController {

	def analysisService
	def savedAnalysisService
	def annotationService
	def userListService
	def drugDiscoveryService
	def htDataService
	
    def index = {
		loadPatientLists()
		loadReporterLists()
		loadGeneLists()
		return [diseases:getDiseases(),myStudies:session.myStudies, params:params]
	}
	
	def view = {
		def analysisResult = savedAnalysisService.getSavedAnalysis(params.id)
		StudyContext.setStudy(analysisResult.query["study"])
		session.results = analysisResult.analysis.item
		session.analysis = analysisResult
	}
	
	def drawHeatMap = { HeatMapCommand cmd ->
		log.debug "heatmap params : $params"
		log.debug "type : " + cmd.requestType
		log.debug "patientList : " + cmd.patientList
		log.debug "geneList : " + cmd.geneList
		log.debug "reporterList : " + cmd.reporterList
		log.debug "study:" + cmd.study 
		log.debug cmd.errors
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
			if(cmd.fromComparison && cmd.selectAll) {
				def ids = []
				if(session.results){
					session.results.resultEntries.each{ ccEntry ->
						ids << ccEntry.reporterId
					}
				}
				cmd.reporterIds = "[" + ids.join(',') + "]"
			}
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
		def result = session.results//savedAnalysisService.getSavedAnalysis(params.id)
		//StudyContext.setStudy(result.query["study"])
		//session.results = result.analysis.item
		//session.analysis = result
		try{
			if(params.name){
				byte[] fileBytes
				if(params.name.indexOf('.cdt') > 1)
					fileBytes = result.cdtFile
				if(params.name.indexOf('.gtr') > 1)
					fileBytes = result.gtrFile
				if(params.name.indexOf('.atr') > 1)
					fileBytes = result.atrFile
				if(params.name.indexOf('.jtv') > 1)
					fileBytes = "<DocumentConfig></DocumentConfig>".getBytes()
				response.outputStream << fileBytes
			}
		}catch(java.io.FileNotFoundException fnf){
			log.debug fnf.toString()
			render "File ($params.name) was not found...is the file name correct?"
		} catch (Exception e) {
			e.printStackTrace()
		}
		
	}
	
	
	
}
