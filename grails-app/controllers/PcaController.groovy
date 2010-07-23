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
	
    def index = {
		if(session.study){
			session.files = htDataService.getHTDataMap()
		}
		
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
			def author = GDOCUser.findByLoginName(session.userId)
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
		def analysisResult = savedAnalysisService.getSavedAnalysis(params.id)
		def results = analysisResult.analysis.item
		
		session.results = results
		session.analysis = analysisResult
		def attributes = []
		analysisResult.studySchemas().each { study ->
			log.debug "STUDY $study"
			StudyContext.setStudy(study)
			def sds = StudyDataSource.findBySchemaName(study)
			session.study = sds
			attributes.addAll(AttributeType.findAll())
		}
		session.dataTypes = attributes
	}
	
	def results = {
		def results = session.results
		def resultEntries = results.resultEntries
		def pcaResults = [:]
		def samples = []
		def sampleHash = [:]
		def sampleIds = []
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
			patient.clinicalData.each {key, value ->
				it[key] = value
			}
			it["patientId"] = patient.gdocId
		}
		
		def clinicalTypes = []
		session.dataTypes.each {
			def itemType
			def values = []
			if(it.vocabulary) {
				itemType = "vocab"
				it.vocabs.each { vocab ->
					values << vocab.term
				}
			} else if(it.upperRange)  {
				itemType = "range"
				values << it.lowerRange
				values << it.upperRange
			}
			def type = [shortName: it.shortName, longName: it.longName, type: itemType, values: values]
			clinicalTypes << type
		}
		def schemName = StudyContext.getStudy()
		def study = StudyDataSource.findBySchemaName(schemName)
		pcaResults["study"] = study.shortName
		pcaResults["clinicalTypes"] = clinicalTypes
		pcaResults["samples"] = samples
		log.debug pcaResults as JSON
		render pcaResults as JSON
	}
}
