import grails.converters.*

class HomeController {
	def feedService
	def feedMap
	def summaryService
	def quickStartService
	
    def index = { 
		if(session.userId){
			redirect(controller:'workflows')
			return
		}
		//get LCCC feed
	//	feedMap = feedService.getFeed()
		
		//session.patientSummary = summaryService.patientSummary()
		//session.studySummary = summaryService.studySummary()
		
		//get patient counts for each study
		session.studyCounts = summaryService.patientCounts()
		def studies = StudyDataSource.list();
		
		def da = quickStartService.getMyDataAvailability(studies)
		def diseaseBreakdown = [:]
		def dataBreakdown = [:]
		def totalPatient = 0
		def totalStudies = 0
		def totalData = new HashSet()
		if(da["dataAvailability"])
			totalStudies = da["dataAvailability"].size()
		da["dataAvailability"].each{ study ->
			def disease = study["CANCER"]
			//println "disease: " + disease
			study.each{ key,value ->
				if(!diseaseBreakdown[disease]){
					diseaseBreakdown[disease] = [:]
					diseaseBreakdown[disease]["availableData"] = new HashSet()
				}
					if(key == 'CANCER'){
					if(diseaseBreakdown[disease]["studyNumber"]){
						diseaseBreakdown[disease]["studyNumber"] += 1
						//println "add another $disease study: $study.STUDY"
					}else{
						diseaseBreakdown[disease]["studyNumber"] = 1
					}
					}
					if(key == 'CLINICAL'){
						if(diseaseBreakdown[disease]["patientNumber"]){
								diseaseBreakdown[disease]["patientNumber"] += value
						}else{
								diseaseBreakdown[disease]["patientNumber"] = value
						}
						totalPatient += value
					}
					
				
				if(key != "STUDY" &&  key != "CANCER"){
					if(value > 0){
						//println  "$disease has $key available"
						def nameAndImage = [:]
						def image = key.replace(" ","_")+"_icon.gif" 
						nameAndImage[key] = image
						diseaseBreakdown[disease]["availableData"] << nameAndImage
						if(dataBreakdown[key]){
							dataBreakdown[key] += 1
						}
						else dataBreakdown[key] = 1
						totalData << nameAndImage
					}
				}
			}
		}
		diseaseBreakdown['<i>TOTAL</i>'] = [:]
		diseaseBreakdown['<i>TOTAL</i>']['patientNumber'] = totalPatient
		diseaseBreakdown['<i>TOTAL</i>']['studyNumber'] = totalStudies
		diseaseBreakdown['<i>TOTAL</i>']['availableData'] = totalData
		//println diseaseBreakdown
		//println dataBreakdown
		
		//get anatomic sources
		def sampleSummary = summaryService.sampleSummary()
		if(sampleSummary instanceof Map){
			session.sampleSummary = sampleSummary
			session.anatomicSourceValues = summaryService.anatomicSources(sampleSummary)
		}
		
		[diseaseBreakdown:diseaseBreakdown, dataBreakdown:dataBreakdown]
	}
	
	def workflows = {
		
		
	}
	
}
