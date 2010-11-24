import grails.converters.*
import java.text.*
import java.math.*

@Mixin(ControllerMixin)
class CinController {
	def analysisService
	def savedAnalysisService
	def annotationService
	def userListService
	def htDataService
	
    def index = { 
		if(session.study){
			StudyContext.setStudy(session.study.schemaName)
            if (session.study.hasCopyNumberData()) {
				session.files = htDataService.getCINDataMap()
				session.dataSetType = session.files.keySet()
				//session.df = new ArrayList(session.files.values()).get(0)
				session.df = session.files.values().toArray()[0][1]
				session.cdf = session.files.values().toArray()[0][0]
				log.debug "my ht files for $session.study = $session.files $session.df $session.cdf"
			}
		}
		//loadPatientLists()
		return [diseases:getDiseases(),myStudies:session.myStudies, params:params]
	}
	
	def view = {
		if(isAccessible(params.id)){
			def analysisResult = savedAnalysisService.getSavedAnalysis(params.id)
			StudyContext.setStudy(analysisResult.query["study"])
			loadCurrentStudy()
			session.results = analysisResult.analysis.item
			session.analysis = analysisResult
		}
		else{
			log.debug "user CANNOT access analysis $params.id"
			redirect(controller:'policies', action:'deniedAccess')
		}
		
	}
	
	def submit = {CinCommand cmd ->
			log.debug "cin params : $params"
			log.debug "Command: " + cmd.groups
			log.debug "type : " + cmd.requestType
			log.debug analysisService
			log.debug "baseline group : " + cmd.baselineGroup
			log.debug "groups : " + cmd.groups
			log.debug "study:" + cmd.study 
			log.debug cmd.errors
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			def study = StudyDataSource.findBySchemaName(cmd.study)
			redirect(action:'index',id:study.id)
		} else {
			def tags = []
			tags << "cin"
			
			analysisService.sendRequest(session.id, cmd, tags)
			redirect(controller:'notification')
		}
		
	}
	
	def file = {
		def result = session.results
		try{
			if(params.name){
				byte[] fileBytes
				String chr
				if(params.name.indexOf('chr') >= 0 ) {		
					chr = params.name.substring(11)
					fileBytes = result.cinFiles.get(chr)
				}
				if(params.name.indexOf('heatmap') >= 0)
					//fileBytes = result.heatMapFile
					fileBytes = result.cinFiles.get('heatmap')
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
