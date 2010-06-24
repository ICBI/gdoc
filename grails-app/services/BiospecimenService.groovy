import org.springframework.jdbc.core.JdbcTemplate 
import groovy.text.SimpleTemplateEngine

class BiospecimenService {
	static PAGE_SIZE = 1000
	def sessionFactory
	def jdbcTemplate 
	def queryString = '(select b.biospecimen_id from ${schema}.biospecimen b, common.attribute_type c, ${schema}.biospecimen_attribute_value v ' +
		 			  'where b.biospecimen_id = v.biospecimen_id and v.attribute_type_id = c.attribute_type_id ' +
					  ' and v.value = \'${value}\' and c.short_name = \'${key}\')'
	def rangeQueryString = '(select /*+ index(v,BIOSPECIMEN_ATT_VALUE_INDEX1) */ b.biospecimen_id from ${schema}.biospecimen b, common.attribute_type c, ${schema}.biospecimen_attribute_value v ' +
		 			  	   'where b.biospecimen_id = v.biospecimen_id and v.attribute_type_id = c.attribute_type_id ' +
					  	   ' and c.short_name = \'${key}\' and v.value BETWEEN ${value.min} and ${value.max} )'					
	
    boolean transactional = true
	
	def queryByCriteria(criteria) {
		println "IN BIO"
		println criteria.class
		def engine = new SimpleTemplateEngine()
		def queryTemplate = engine.createTemplate(queryString)
		def rangeQueryTemplate = engine.createTemplate(rangeQueryString)
		def selects = []
		if(!criteria || criteria.size() == 0) {
			return
		}
		criteria.each { entry ->
			def temp =[:]
			temp.key = entry.key
			temp.value = entry.value
			temp.schema = StudyContext.getStudy()
			if(temp.value instanceof java.util.Map) {
				selects << rangeQueryTemplate.make(temp)
			} else {
				selects << queryTemplate.make(temp)
			}
			
		}
		def query = selects.join(" INTERSECT ")
		println query
		def ids = jdbcTemplate.queryForList(query)
		def biospecimenIds = ids.collect { id ->
			return id["BIOSPECIMEN_ID"]
		}
		def biospecimens = []
		def index = 0;
		println "biospecimen ids $biospecimenIds"
		while(index < biospecimenIds.size()) {
			def specimensLeft = biospecimenIds.size() - index
			def tempSpecimens
			if(specimensLeft > PAGE_SIZE) {
				tempSpecimens = Biospecimen.getAll(biospecimenIds.getAt(index..<(index + PAGE_SIZE)))
				biospecimens.addAll(tempSpecimens)
				index += PAGE_SIZE
			} else {
				tempSpecimens = Biospecimen.getAll(biospecimenIds.getAt(index..<biospecimenIds.size()))
				biospecimens.addAll(tempSpecimens)
				index += specimensLeft
			}
		}
		println biospecimens.size()
		return biospecimens.grep { it }
	}
	
	def createBiospecimensForStudy(studyName, biospecimens,biospecimenAndData,auditInfo) {
		def studyDataSource = StudyDataSource.findByShortName(studyName)
		if(!studyDataSource)
			return
		StudyContext.setStudy(studyDataSource.schemaName)
		biospecimens.each {
			loadBiospecimen(it)
		}
		biospecimenAndData.each { biospecimenName, values ->
			addDataValuesToBiospecimen(studyName, biospecimenName, values, auditInfo)
		}
	}
	
	def addDataValuesToBiospecimen(projectName, biospecimenName, values, auditInfo) {
		def studyDataSource = StudyDataSource.findByShortName(projectName)
		if(!studyDataSource)
			return
		StudyContext.setStudy(studyDataSource.schemaName)
		def biospecimen = Biospecimen.findByName(biospecimenName)
		if(!biospecimen)
			return
		values.each { name, value ->
			if(value){
				def type = CommonAttributeType.findByShortName(name)
				if(!type){
					println "Attribute Type ${name} not found.  Unable to load data."
				}
				else{
					def attValue = new BiospecimenValue()
					attValue.commonType = type
					attValue.value = value
					attValue.insertUser = auditInfo.insertUser
					attValue.insertMethod = auditInfo.insertMethod
					attValue.insertDate = auditInfo.insertDate
					attValue.biospecimen = biospecimen
					biospecimen.addToValues(attValue)
					if(!attValue.save(flush:true))
						println attValue.errors
				}
			}else{
				println "no value found for attribute ${name}"
			}
		}
		biospecimen.merge()
	}
	
	
	def loadBiospecimen(params) {
		def studyPatient = StudyPatient.findByDataSourceInternalId(params.patientId)
		def patient  = Patient.get(studyPatient.id)
		def biospecimen = new Biospecimen(params)
		biospecimen.patient = patient
		biospecimen.type = "SAMPLE"
		biospecimen.diseased = true
		biospecimen.attributeTimepointId = 0
		if(!biospecimen.save())
			println biospecimen.errors
		return biospecimen
	}
}
