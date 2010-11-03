import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager
import gov.nih.nci.security.AuthenticationManager
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement
import gov.nih.nci.security.SecurityServiceProvider
import org.springframework.mock.jndi.SimpleNamingContextBuilder

grailsHome = Ant.antProject.properties."env.GRAILS_HOME"
includeTargets << grailsScript("Bootstrap")
includeTargets << grailsScript("Init")

target(main: "Load outcome relpase data into the DB") {
	depends(clean, compile, classpath)
	
	// Load up grails contexts to be able to use GORM
	loadApp()
	configureApp()

	println "Please specify a project name:"
	def projectName = new InputStreamReader(System.in).readLine().toUpperCase()
	def successful = false;
	def dataSourceClass = classLoader.loadClass('StudyDataSource')
	def study = dataSourceClass.findBySchemaName(projectName)
	while(!study) {
		println "Project with name: $projectName doesn't exist. This script should be run when all data is loaded."
		return
	}
	def outcomeFile = new File("dataImport/${projectName}/${projectName}_outcomes.txt")
	if(!outcomeFile.exists()) {
		println "Cannot find study outcome file at dataImport/${projectName}/${projectName}_outcomes.txt.  Please locate or build file and try again."
		return
	}
	try {
		println "load all outcome data...."
	    loadOutcomeData(study, outcomeFile)
	} catch (Throwable e) {
		e.printStackTrace()
		"Outcome-data loading for $projectName was not successful"
		return
	}
	println "Outcome-data loading for $projectName was successful"
}


def loadOutcomeData(study, outcomeFile) {
	println "begin data load"
	def createdSuccessfully = false
	def outcomeService = appCtx.getBean("outcomeDataService")
	def sessionFactory = appCtx.getBean("sessionFactory")
	def session = sessionFactory.getCurrentSession()
	def trans = session.beginTransaction()
	
	try{
	outcomeFile.eachLine { lineData, outcomeLineNumber ->
		if(outcomeLineNumber != 1) {
			def outcomeData = lineData.split('\t', -1)
			println outcomeData
			def name = outcomeData[0] 
			def query = outcomeData[1].replaceAll('"'," ")
			def description = outcomeData[2] 
			if(name && query && description){
				def outcomeClass = classLoader.loadClass('Outcome')
				def results = outcomeClass.findAll("from Outcome as outcome where outcome.outcomeType = :type and outcome.studyDataSource = :study", [type:name, study:study])
				if(results){
					println "found existing $name data for $study.schemaName study...this run will skip loading this data."
				}else{
					outcomeService.addQueryOutcome(study,name, query, description)
					println "finished loading $name data"
				}
			}
		}
	}
	trans.commit()	
	println "committed data"
	} catch (Exception e) {
				trans.rollback()
				throw e
	}
	
}


def buildCriteriaMap(outcomeData){
	println "build outcome criteria"
	def criteriaMap = [:]
	if(outcomeData[1] == 'Time'){
		criteriaMap[outcomeData[2]] = [min:outcomeData[4],max:outcomeData[5]]
	}else if(outcomeData[1] == 'Attribute'){
		criteriaMap[outcomeData[2]] = outcomeData[3]
	}
	println "built -> $criteriaMap"
	return criteriaMap
}


setDefaultTarget(main)
