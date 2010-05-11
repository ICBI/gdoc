import org.springframework.jdbc.core.JdbcTemplate 
import groovy.text.SimpleTemplateEngine

class BiospecimenService {
	static PAGE_SIZE = 1000
	def sessionFactory
	def jdbcTemplate 
	def queryString = '(select b.biospecimen_id from ${schema}.biospecimen b, common.attribute_type c, ${schema}.biospecimen_attribute_value v ' +
		 			  'where b.biospecimen_id = v.biospecimen_id and v.attribute_type_id = c.attribute_type_id ' +
					  ' and v.value = \'${value}\' and c.short_name = \'${key}\')'
	def rangeQueryString = '(select b.biospecimen_id from ${schema}.biospecimen b, common.attribute_type c, ${schema}.biospecimen_attribute_value v ' +
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
}
