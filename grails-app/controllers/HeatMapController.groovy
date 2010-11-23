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
	def idService
	def colors = ["#FF0000", "#00FF00", "#0000FF", "#FFFF00"]
	
    def index = {
		loadPatientLists()
		loadReporterLists()
		loadGeneLists()
		return [diseases:getDiseases(),myStudies:session.myStudies, params:params]
	}
	
	def view = {
		if(isAccessible(params.id)){
			def analysisResult = savedAnalysisService.getSavedAnalysis(params.id)
			StudyContext.setStudy(analysisResult.query["study"])
			loadCurrentStudy()
			session.results = analysisResult.analysis.item
			session.analysis = analysisResult
			session.groupLegend = null
			if(analysisResult.query.patientList != 'ALL') {
				def groupLegend = [:]
				analysisResult.query.groups.eachWithIndex { group, number ->
					groupLegend[group] = colors[number] 
				}
				session.groupLegend = groupLegend
			}
		}
		else{
			log.debug "user CANNOT access analysis $params.id"
			redirect(controller:'policies', action:'deniedAccess')
		}
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
			def cleanedGroups = []
			cmd.groups.each{ g ->
				if(g){
					g.tokenize(",").each{
						it = it.replace('[','');
						it = it.replace(']','');
						cleanedGroups << it
					}
				}		
			}
			cmd.groups = cleanedGroups
			if(cmd.reporterIds) {
				def numberReporters = cmd.reporterIds.split(',').size()
				if(numberReporters > HeatMapCommand.MAX_REPORTERS || numberReporters < 2) {
					flash['reporterError'] = "You must select more than 2 reporters and less than 5000 reporters for heat map."
					redirect(action:'view', controller: 'analysis',id:session.analysis.id)
					return
				}
			}
			
			analysisService.sendRequest(session.id, cmd, tags)
			redirect(controller:'notification')
		}

	}
	
	def file = {
		def result = session.results
		try{
			if(params.name){
				byte[] fileBytes
				if(params.name.indexOf('.cdt') > 1) {
					def groups = session.analysis.query.groups
					if(session.analysis.query.patientList != 'ALL' && groups && groups.size() > 0) {
						def idColorMap = [:]
						groups.each { group ->
							def samples = idService.samplesForListName(group)
							samples.each { sample ->
								idColorMap[sample] = session.groupLegend[group]
							}
						}
						byte[] bytes  = result.cdtFile
						InputStream inputStream = new ByteArrayInputStream(bytes);
						def input  = new BufferedReader(new InputStreamReader(inputStream))
						def byteOut = new ByteArrayOutputStream() 
						def columns
						def colors
						input.eachLine { line, number ->
							if(number == 1) {
								columns = line.split("\t")
								colors = new String[columns.length]
							}
							if(number == 3) {
								colors[0] = "BGCOLOR"
								(4..<columns.length).each {
									colors[it] = idColorMap[columns[it]]
								}
								def colorLine = colors.join('\t') + "\n"
								byteOut << colorLine.getBytes()
							}
							def temp = line + "\n"
							byteOut << temp.getBytes()
						}
						fileBytes = byteOut.toByteArray()
					} else {
						fileBytes = result.cdtFile
					}
				}
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
