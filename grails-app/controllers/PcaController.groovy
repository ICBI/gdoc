import grails.converters.*
import java.text.*
import java.math.*


class PcaController {

	def analysisService
	def savedAnalysisService
	def annotationService
	def userListService
	def patientService
	
    def index = {
		session.study = StudyDataSource.get(params.id)
		StudyContext.setStudy(session.study.schemaName)
		def lists = userListService.getAllLists(session.userId,session.sharedListIds)
		def geneLists = []
		def patientLists = []
		def reporterLists = []
		lists.each { item ->
			if((item.tags.contains("patient") && item.schemaNames().contains(StudyContext.getStudy())))
				patientLists << item
			if((item.tags.contains("reporter") && item.schemaNames().contains(StudyContext.getStudy())))
				reporterLists << item
			if((item.tags.contains("reporter") && item.schemaNames().contains(StudyContext.getStudy())))
				geneLists << item
		}
		session.patientLists = patientLists.sort { it.name }
		session.reporterLists = reporterLists
		session.geneLists = geneLists
		session.files = MicroarrayFile.findByNameLike('%.Rda')
	}
	
	def submit = { PcaCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			def study = StudyDataSource.findBySchemaName(cmd.study)
			redirect(action:'index',id:study.id)
		} else {
			analysisService.sendRequest(session.id, cmd)
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
			println "STUDY $study"
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
			it["patientId"] = patient.id
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
		pcaResults["clinicalTypes"] = clinicalTypes
		pcaResults["samples"] = samples
		println pcaResults as JSON
		render pcaResults as JSON
	}
}
