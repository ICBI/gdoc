import grails.converters.*
import java.text.*
import java.math.*


class PcaController {

	def analysisService
	def savedAnalysisService
	def annotationService
	def userListService
	
    def index = {
		session.study = StudyDataSource.get(params.id)
		StudyContext.setStudy(session.study.schemaName)
		def lists = userListService.getAllLists(session.userId,session.sharedListIds)
		def patientLists = lists.findAll { item ->
			(item.tags.contains("patient") && item.schemaNames().contains(StudyContext.getStudy()))
		}
		session.patientLists = patientLists.sort { it.name }
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
		//def analysisResult = savedAnalysisService.getSavedAnalysis(params.id)
		def attributes = []
		//analysisResult.studyNames.each { study ->
		["LOI"].each { study ->
			StudyContext.setStudy(study)
			attributes.addAll(AttributeType.findAll())
		}
		session.dataTypes = attributes
	}
	
	def results = {
		view()
		def pcaResults = [:]
		def samples = [:]
		def sampleGroup1 = []
		def sampleGroup2 = []
		def sampleGroup3 = []
		def test = true
		(1..100).each {
			def dr = 'YES'
			if(test)
				dr = 'YES'
			else 
				dr = 'NO'
			test = !test
			def sample = [sampleId : it, pc1: it, pc2: it, pc3: it, DR: dr, AGE: it, 'SURGERY_TO_RECUR/FU': (it / 2), 'SURGERY_TO_DR/FU_DAYS': (it * 100)]
			if((it % 3) == 0)
				sampleGroup3 << sample
			else if((it % 3) == 1)
				sampleGroup2 << sample
			else 
				sampleGroup1 << sample
		}
		samples["pc1pc2"] = sampleGroup1
		samples["pc1pc3"] = sampleGroup2
		samples["pc2pc3"] = sampleGroup3
		
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
		render pcaResults as JSON
	}
}
