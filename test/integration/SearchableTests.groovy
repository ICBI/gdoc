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
	def annotationService
	
	
	void testSearchStemming(){
		//println "begin indexing..."
		//searchableService.reindex()
		//println "finished indexing."
		def searchString = "egfr"
		def searchResult = []
		searchResult = searchableService.search([defaultOperator:"and"],{
			must({
				queryString(searchString+"*")
				polyAlias(searchString)
				})
			must({                                  
					alias("findings") 
			        alias("targets")
					alias("studies")
			   })
		})
		println searchResult.total
		if(searchResult.results){
			searchResult.results.each{
				def resultClass = ClassUtils.getShortName(it.getClass())
				println resultClass
				if(resultClass == "MoleculeTarget"){ 
					println "$it.molecule.name is a target of: $it.protein.name"
					
				}
				if(resultClass == "StudyDataSource"){
					println "$it.longName was found with id $it.id"
				}
			}
		}else
			println "no results"
	}
	
	/**
  void testSearchIndex(){
		try { 
			def terms = []
			//terms << searchableService.termFreqs("longName")
			//terms << searchableService.termFreqs("shortName")
			//terms << searchableService.termFreqs("cancerSite")
			terms << Finding.termFreqs("title")
			terms << StudyDataSource.termFreqs("abstractText")
			//terms << searchableService.termFreqs("name")
			//terms << searchableService.termFreqs("symbol",size:50)
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
			
			}
			
		 } catch (SearchEngineQueryParseException ex) { 
		 	println ex
		 }
	}
	
		
		/**void testSearchTargetIndex(){
			try { 
				def gaterms = []
				gaterms << GeneAlias.termFreqs("symbol",size:50)
				def pterms = []
				pterms << Protein.termFreqs("name")
				def mterms = []
				mterms << Molecule.termFreqs("name")

				gaterms.flatten().each{
					//println "$it.term of $it.propertyName was referenced $it.freq times"
				}
				pterms.flatten().each{
					//println "$it.term of $it.propertyName was referenced $it.freq times"
				}
				mterms.flatten().each{
					//println "$it.term of $it.propertyName was referenced $it.freq times"
				}
			}catch (SearchEngineQueryParseException ex) { 
				 	println ex
			}
		}
		
		void testLigandSearch(){
			try { 
			def searchTerm = "EGFR"
			def targets = []
				//is it a gene symbol?
				def alias = annotationService.findGeneByAlias(searchTerm)
				if(alias){
					println "its a gene symbol"
					def proteinNames = []
					def queryStringToCall = ""
					def proteins = alias.gene.proteins
					proteinNames = proteins.collect{it.name}
					if(proteinNames){
						proteinNames.each{ pname ->
							queryStringToCall += "$pname OR "
						}
						searchTerm = queryStringToCall.substring(0,queryStringToCall.lastIndexOf('OR'))
						println searchTerm
						targets = Molecule.search{
							queryString("EGFR")
							must(between("weight",430.00,435.00,true))
						}
					}
					else{
						println "no proteins found"
					}
				}
				else{
				//it must be a protein name or molecule name
				println "it must be a protein name or molecule name -- search with string"
				targets = Molecule.search{
					queryString(searchTerm)
				}
					
				}
			println targets
			}catch (SearchEngineQueryParseException ex) { 
				 	println ex
			}
		}**/

}
