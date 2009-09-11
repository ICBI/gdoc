import grails.converters.JSON

class SummaryService {
	def jdbcTemplate 
	def middlewareService
	
	def patientSummary() {
		return jdbcTemplate.queryForInt("select count(distinct(gdoc_id)) from common.patient_data_source")
	}
	
	def studySummary() {
		return StudyDataSource.count()
	}
	
	def sampleSummary() {
		return middlewareService.loadResource("Sample", null, null)
	}
	
	def patientCounts(){
		def studyList = StudyDataSource.findAll()
		def studyCounts =[:]
		studyList.each{
			if(it.schemaName != 'PREOP'){
					StudyContext.setStudy(it.schemaName)
					def query = "select count(p) from Patient p"
					def patients = Patient.executeQuery(query)
					if(patients){
						//println patients[0]
						studyCounts[it.schemaName] = patients[0]
					}
			}
		}
		return studyCounts
	}
	
	def anatomicSources(sampleSummary){
		def anatomicSourcesList = [];
		def anatomicSources = [];
		sampleSummary.each{
				it.each{ ds ->
					sampleSummary[ds.key].each{ attr->
						if(attr.key == 'anatomicSource'){
							sampleSummary[ds.key].anatomicSource.each{vv ->
								def match = anatomicSourcesList.find{ en ->
									en.key == vv.entrySet().toArray()[0].key
								}
								if(match){
									def t = anatomicSourcesList.indexOf(match)
									def f = anatomicSourcesList.get(t)
									f.value = f.value +  vv.entrySet().toArray()[0].value
								}else{
									anatomicSourcesList.addAll(vv.entrySet())
								}
							}
						}
					}
				}
		}
			
			def totalSamples = 0
			
			anatomicSourcesList.each{
				totalSamples += it.value
			}
			
			def totalSampleCount=[:]
			totalSampleCount["Total"] = totalSamples
			anatomicSourcesList.addAll(totalSampleCount.entrySet())
		
			anatomicSources = anatomicSourcesList.sort{ a,b-> 
 						java.util.Map.Entry<K, V> e1 = ( java.util.Map.Entry<K, V> ) a;
					    java.util.Map.Entry<K, V> e2 = ( java.util.Map.Entry<K, V> ) b;
						return  e2.getValue().equals(e1.getValue())? 0:e2.getValue()<e1.getValue()? -1: 1		
			}
		return anatomicSources
	}
	
}