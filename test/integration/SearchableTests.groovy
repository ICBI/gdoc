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
	
	
	/**void testSearchStemming(){
		def searchString = "EGFR"
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
					//alias("targetRelationships")
			   })
		})
		println searchResult
		if(searchResult.results){
			searchResult.results.each{
				def resultClass = ClassUtils.getShortName(it.getClass())
				println resultClass
				if(resultClass == "MoleculeTarget"){ 
					println "$it.molecule.name is a target of: $it.protein.accession"
					
				}
				if(resultClass == "StudyDataSource"){
					println "$it.longName was found with id $it.id"
				}
				
			}
		}else
			println "no results"
	}**/
	
/**
  void testSearchIndex(){
		try { 
			def terms = []
			
			//terms << searchableService.termFreqs("longName")
			//terms << searchableService.termFreqs("shortName")
			//terms << searchableService.termFreqs("disease")
			//terms << Finding.termFreqs("title")
			//terms << StudyDataSource.termFreqs("abstractText")
			//terms << searchableService.termFreqs("name")
			terms << searchableService.termFreqs("symbol",size:30000)
			terms.flatten().each{
				if(it.term.contains('egfr')){
					println "$it.term of $it.propertyName was referenced $it.freq times"
				}
			}
			
			/**def comp = searchableService.compass
			def lastResult =""
			QueryParser parser = new QueryParser("disease", new StandardAnalyzer()); 
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
	}**/
	
		
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
		}**/
		
		void testLigandSearch(){
			try { 
			def searchTerm = "EGFR"
			def targets = []
				//is it a gene symbol?
				def alias// = annotationService.findGeneByAlias(searchTerm)
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
							//queryString("EGFR")
							must(between("weight",600.00,900.00,true))
						}
					}
					else{
						println "no proteins found"
					}
				}
				else{
				//it must be a protein name or molecule name
				println "filter"
					def myStudies = ["DDG_COLLAB","PUBLIC"]
					def orString = myStudies.join(" OR ")
					println  "my search string is $orString"
					targets = Molecule.search{
						must(between("weight",0.0,90.00,true))
						must(queryString(orString))
					}
					
				}
				println "filtered results -> $targets.total"
				targets.results.each{
					    if(it.protectionGroup){
							println it.protectionGroup.name
							println it.weight
						}else{
							println "none"
						}
				}
		
			}catch (SearchEngineQueryParseException ex) { 
				 	println ex
			}
		}

}
