import grails.converters.*
import java.text.*
import java.math.*
import gov.nih.nci.caintegrator.analysis.messaging.HeatMapResult
import org.codehaus.groovy.grails.web.json.JSONObject

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
		if(session.study) {
			if(params.baselineGroup && params.groups){
				HeatMapCommand cmd  = new HeatMapCommand();
 				def hmgroups = []
			 	hmgroups << params.baselineGroup
				hmgroups << params.groups
				cmd.groups = hmgroups
				cmd.patientList = 'GROUPS'
				flash.cmd = cmd
				flash.message = " Your 2 lists, $cmd.groups have been prepopulated below for heatmap viewer"
			}
			StudyContext.setStudy(session.study.schemaName)
			session.files = htDataService.getHTDataMap()
			session.dataSetType = session.files.keySet()
		}
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
			[tags:analysisResult.tags]
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
		log.debug "dataSetType" + cmd.dataSetType
		log.debug cmd.errors
		if(!session.study)
			StudyContext.setStudy(cmd.study)
		def tags = []
		tags << "heatMap"
			if(cmd.dataSetType){
				cmd.dataSetType.tokenize(",").each{
					it = it.replace('[','');
					it = it.replace(']','');
					tags << it.trim()
				}
			}		
		
		def author = GDOCUser.findByUsername(session.userId)
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
					def colorPatients = false
					def dataRow = 3
					def groups = session.analysis.query.groups
					def idColorMap = [:]
					if(session.analysis.query.patientList != 'ALL' && groups && groups.size() > 0) {
						dataRow = 4
						colorPatients = true
						groups.each { group ->
							def samples = idService.samplesForListName(group)
							samples.each { sample ->
								idColorMap[sample] = session.groupLegend[group]
							}
						}
					}	
					def reporters
					if(session.analysis.query.reporterIds != JSONObject.NULL) {
						def reporterIds = cleanIds(session.analysis.query.reporterIds)
						reporters = annotationService.findReportersByName(reporterIds)
					} else {
						log.debug "QUERY ${session.analysis.query}"
						if(session.analysis.query.reporterList && session.analysis.query.reporterList != JSONObject.NULL) {
							reporters = annotationService.getReportersForReporterList(session.analysis.query.reporterList)
						} else {
							reporters = annotationService.getReportersForGeneList(session.analysis.query.geneList)
						}
					}
					def repIds = reporters.collect {
						it.name
					}
					def reporterMap = annotationService.findAllGenesForReporters(repIds, session.analysis.query.dataFile)
					log.debug "REPORTERS ${reporterMap}"
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
						//Ignore clustering information
						if(number == 2) {
							return
						}
						if(colorPatients && (number == 3)) {
							colors[0] = "BGCOLOR"
							(4..<columns.length).each {
								colors[it] = idColorMap[columns[it]]
							}
							def colorLine = colors.join('\t') + "\n"
							byteOut << colorLine.getBytes()
						}
						if(number >= dataRow) {
							def tempLine = line.split("\t")
							if(reporterMap[tempLine[1]] && (reporterMap[tempLine[1]]!=tempLine[1])){
								tempLine[2] = reporterMap[tempLine[1]]
							}
							else{
								tempLine[2] = ""
							}
							line = tempLine.join("\t")
						}
						def temp = line + "\n"
						byteOut << temp.getBytes()
					}
					fileBytes = byteOut.toByteArray()
				}
				if(params.name.indexOf('.gtr') > 1)
					fileBytes = result.gtrFile
				if(params.name.indexOf('.atr') > 1)
					fileBytes = ''
				if(params.name.indexOf('.jtv') > 1)
					fileBytes = '<DocumentConfig><UrlExtractor/><ArrayUrlExtractor/><MainView><ColorExtractor><ColorSet/></ColorExtractor><ArrayDrawer/><GlobalXMap><FixedMap type="Fixed"/><FillMap type="Fill"/><NullMap type="Null"/></GlobalXMap><GlobalYMap><FixedMap type="Fixed"/><FillMap type="Fill"/><NullMap type="Null"/></GlobalYMap><ZoomXMap><FixedMap type="Fixed"/><FillMap type="Fill"/><NullMap type="Null"/></ZoomXMap><ZoomYMap><FixedMap type="Fixed"/><FillMap type="Fill"/><NullMap type="Null"/></ZoomYMap><TextView><TextView><GeneSummary/></TextView><TextView><GeneSummary/></TextView><TextView><GeneSummary/></TextView><TextView><GeneSummary included="1,2"/></TextView><Selection index="1"/><Selection index="2"/><Dividers Position0="79" Position1="50" Position2="50"/></TextView></MainView><GeneListMaker/></DocumentConfig>'.getBytes()
				response.outputStream << fileBytes
			}
		}catch(java.io.FileNotFoundException fnf){
			log.debug fnf.toString()
			render "File ($params.name) was not found...is the file name correct?"
		} catch (Exception e) {
			e.printStackTrace()
		}
		
	}
	
	
	private List cleanIds(ids) {
		def cleanedIds = []
		if(ids){
			ids.tokenize(",").each{
				it = it.replace('[','');
				it = it.replace(']','');
				cleanedIds << it
			}
		}
		return cleanedIds		
	}
}
