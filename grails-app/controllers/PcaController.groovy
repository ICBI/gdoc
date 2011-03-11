import grails.converters.*
import java.text.*
import java.math.*

@Mixin(ControllerMixin)
class PcaController {

	def analysisService
	def savedAnalysisService
	def annotationService
	def userListService
	def patientService
	def htDataService
	def idService
	
    def index = {
		if(session.study){
			if(params.baselineGroup && params.groups){
				PcaCommand cmd  = new PcaCommand();
 				def pcagroups = []
			 	pcagroups << params.baselineGroup
				pcagroups << params.groups
				cmd.groups = pcagroups
				cmd.patientCriteria = 'GROUPS'
				flash.cmd = cmd
				flash.message = " Your 2 lists, $cmd.groups have been prepopulated below for pca"
			}
			StudyContext.setStudy(session.study.schemaName)

			session.files = htDataService.getHTDataMap()
			session.dataSetType = session.files.keySet()
			log.debug "my ht files for $session.study = $session.files"
		}
		loadPatientLists()
		loadReporterLists()
		[diseases:getDiseases()]
	}
	
	def submit = { PcaCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			def study = StudyDataSource.findBySchemaName(cmd.study)
			redirect(action:'index',id:study.id)
		} else {
			def datasetType = params.dataSetType.replace(" ","_")
			def tags = []
			tags << "$datasetType"
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
			def author = GDOCUser.findByUsername(session.userId)
			def tempListFound = cleanedGroups.find{ group ->
				if(userListService.listIsTemporary(group,author)){
					return true
				}
			}
			if(tempListFound){
				tags << Constants.TEMPORARY
			}
			
			analysisService.sendRequest(session.id, cmd, tags)
			redirect(controller:'notification')
		}

	}
	
	def view = {
		if(isAccessible(params.id)){
			def analysisResult = savedAnalysisService.getSavedAnalysis(params.id)
			analysisResult.studySchemas().each { study ->
				log.debug "STUDY $study"
				StudyContext.setStudy(study)
				def sds = StudyDataSource.findBySchemaName(study)
				session.study = sds
			}
			StudyContext.setStudy(analysisResult.query["study"])
			loadCurrentStudy()
		}
		else{
			log.debug "user CANNOT access analysis $params.id"
			redirect(controller:'policies', action:'deniedAccess')
		}
	}
	
	def results = {
		def analysisResult = savedAnalysisService.getSavedAnalysis(params.id)
		def results = analysisResult.analysis.item
		def resultEntries = results.resultEntries
		def pcaResults = [:]
		def samples = []
		def sampleHash = [:]
		def sampleIds = []
		def groupHash = [:]
		
		if(analysisResult.query.patientCriteria != 'ALL') {
			analysisResult.query.groups.each {
				idService.getGdocIdsForList(it).each { patientId ->
					groupHash[patientId] = it
				}
			}
		}
		resultEntries.each {
			def sample = [sampleId : it.sampleId, pc1: it.pc1, pc2: it.pc2, pc3: it.pc3]
			samples << sample
			sampleHash[it.sampleId] = sample
			sampleIds << it.sampleId
		}
		def patients = patientService.patientsForSampleIds(sampleIds)
		
		def patientDataHash = [:]
		patients.each { patient ->
			def patientSamples = patient.biospecimens.collect {
				it.name
			}
			patientSamples.each {
				patientDataHash[it] = patient
			}
		}
		
		samples.each {
			def patient = patientDataHash[it.sampleId]
			patient.clinicalDataValues.each {key, value ->
				it[key] = value
			}
			it["patientId"] = patient.gdocId
			if(analysisResult.query.patientCriteria != 'ALL') {
				it["PATIENT_GROUP"] = groupHash[patient.gdocId]
			}
		}
		def dataTypes = []
		analysisResult.studySchemas().each { study ->
			log.debug "STUDY $study"
			StudyContext.setStudy(study)
			def sds = StudyDataSource.findBySchemaName(study)
			session.study = sds
			dataTypes.addAll(AttributeType.findAll())
		}
		def clinicalTypes = []
		dataTypes.each {
			if(it.target == 'PATIENT') {
				def itemType
				def values = []
				if(it.vocabulary) {
					itemType = "vocab"
					it.vocabs.each { vocab ->
						values << vocab.term
					}
					values.sort { it }
				} else if(it.upperRange)  {
					itemType = "range"
					values << it.lowerRange
					values << it.upperRange
				}
				def type = [shortName: it.shortName, longName: it.longName, type: itemType, values: values]
				clinicalTypes << type
			}
		}
		def schemName = StudyContext.getStudy()
		def study = StudyDataSource.findBySchemaName(schemName)
		clinicalTypes.sort { it.longName }
		pcaResults["study"] = study.shortName
		pcaResults["clinicalTypes"] = []
		if(analysisResult.query.patientCriteria != 'ALL') {
			pcaResults["clinicalTypes"] << [shortName: "PATIENT_GROUP", longName: "Patient Group", type: "vocab", values: analysisResult.query.groups]
		}
		pcaResults["clinicalTypes"].addAll(clinicalTypes)
		pcaResults["samples"] = samples
		log.debug pcaResults as JSON
		render pcaResults as JSON
	}
}
