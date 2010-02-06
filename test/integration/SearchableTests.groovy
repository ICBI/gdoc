import org.compass.core.engine.SearchEngineQueryParseException
import org.springframework.util.ClassUtils
import org.codehaus.groovy.grails.plugins.searchable.SearchableUtils
import org.codehaus.groovy.grails.plugins.searchable.lucene.LuceneUtils
import org.codehaus.groovy.grails.plugins.searchable.util.StringQueryUtils
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.queryParser.QueryParser
import org.apache.lucene.search.Query
import org.compass.core.*
import org.compass.core.support.search.*
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Hits
import org.apache.lucene.document.Document


class SearchableTests extends GroovyTestCase {
	def searchableService

	void testSearchStemming(){
		//println "begin indexing..."
		//searchableService.reindex()
		//println "finished indexing."
		def searchResult = []
		searchResult = StudyDataSource.search{
				queryString("b*")
		}
		if(searchResult.results){
			searchResult.results.each{
				def resultClass = ClassUtils.getShortName(it.getClass())
				if(resultClass == "MoleculeTarget"){ 
					println "$it.molecule.name is a target of: $it.protein.name"
					
				}
				if(resultClass == "StudyDataSource"){
					println "$it.longName was found with id $it.id"
				}
			}
		}
	}

	void testSearchIndex(){
		try { 
			def terms = []
			terms << searchableService.termFreqs("longName")
			terms << searchableService.termFreqs("shortName")
			terms << searchableService.termFreqs("cancerSite")
			terms << searchableService.termFreqs("abstractText")
			terms << searchableService.termFreqs("name")
			terms << searchableService.termFreqs("symbol",size:50)
			terms.flatten().each{
				println "$it.term of $it.propertyName was referenced $it.freq times"
			}
			
			
			/**def comp = searchableService.compass
			def lastResult =""
			QueryParser parser = new QueryParser("cancerSite", new StandardAnalyzer()); 
			IndexSearcher indexSearcher = new IndexSearcher("/Users/kmr75/.grails/projects/gdoc/searchable-index/test/index/studydatasource");
			Query query = parser.parse("b*");
			Hits hits = indexSearcher.search(query);
			int hitCount = hits.length();
			println hitCount
			for (int i = 0; (i < hitCount && i < 10); i++) {
				Document doc = hits.doc(i);
				System.out.println(doc.getValues("all"));
				//indexSearcher.close();
				//lastResult=doc.get("title").toString();
			}**/
			
			/**
			CompassSession compassSession = comp.openSession();
			CompassQueryBuilder queryBuilder = compassSession.queryBuilder();
			CompassQuery compassQuery = queryBuilder.queryString("br*").toQuery();
			CompassSearchHelper searchHelper = new CompassSearchHelper(comp, 6);
			CompassSearchCommand searchCommand = new CompassSearchCommand( compassQuery, 1 );
			//compassQuery.setAliases("productLocal");
			CompassTransaction transaction = compassSession.beginTransaction();
			CompassSearchResults csr = searchHelper.search(searchCommand);
			CompassHit[] hits = csr.getHits();
			hits.each{
				println "this hit:" + it.getResource().getProperties()
			}
			/**def searchResult = searchableService.search(result: 'every',{queryString("c*")})//LuceneUtils.termsForQueryString('bre*',analyzer) //
			println searchResult.class
			if(searchResult){
				searchResult.each{ result ->
					println result.class
					//def className = ClassUtils.getShortName(result.getClass())
					//def fooDomain = new DefaultGrailsDomainClass( StudyDataSource.class )
					//def obj = SearchableUtils.getSearchablePropertyValue(fooDomain)
					//println obj.class
					//println "$result found"
				}
			}**/
		 } catch (SearchEngineQueryParseException ex) { 
		 	println ex
		 }
	}
	


}
