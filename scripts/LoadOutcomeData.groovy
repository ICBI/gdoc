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
		println "load all clinical outcome data...."
		println "load all relapse outcome data...."
		successful = loadOutcomeData(study, outcomeFile)
	} catch (Throwable e) {
		e.printStackTrace()
		"Outcome-data loading for $projectName was not successful"
		return
	}
	if(successful)
	println "Outcome-data loading for $projectName was successful"
	else
	println "Outcome-data loading for $projectName was not successful"
}


def loadOutcomeData(study, outcomeFile) {
	println "begin data load"
	def createdSuccessfully = false
	def outcomeService = appCtx.getBean("outcomeDataService")
	def sessionFactory = appCtx.getBean("sessionFactory")
	def session = sessionFactory.getCurrentSession()
	def trans = session.beginTransaction()
	def isPublic = false
	
	def bundledRelapseCriteria = []
	def bundledMortalityCriteria = []
	def relapseCriteria = [:]
	def relapseCriteriaOrList = []
	def relapseCriteria2 = [:]
	def relapseCriteria2OrList = []
	def mortalityCriteria = [:]
	def mortalityCriteriaOrList = []
	def mortalityCriteria2 = [:]
	def mortalityCriteria2OrList = []
	def specimenCriteria = [:]
	
	
	println "add all $study.schemaName criteria"
	def outcomeLess
	def outcomeMore
	def descriptionLess
	def descriptionMore
	def outcomeLessM
	def outcomeMoreM
	def descriptionLessM
	def descriptionMoreM
	
	/**build criteria query for structured .txt file...should this just be  regular, verified SQL string instead of reading a .txt?**/
	outcomeFile.eachLine { lineData, outcomeLineNumber ->
		if(outcomeLineNumber != 1) {
			def outcomeData = lineData.split('\t', -1)
			def criteriaList = []
			if(outcomeData[0] && outcomeData[1]){
				if(outcomeData[0] == 'Relapse'){
					println "add some relapse criteria"
					if(outcomeData[6] == 'OR'){
						relapseCriteriaOrList << buildCriteriaMap(outcomeData)
					}else{
						criteriaList << buildCriteriaMap(outcomeData)
						relapseCriteria[outcomeLineNumber.toString()] = criteriaList
					}
					if(!outcomeLess)
						outcomeLess = outcomeData[0]
					if(!descriptionLess){
						descriptionLess = outcomeData[7]
					}
				}else if(outcomeData[0] == 'No Relapse'){
					println "add some no relapse criteria"
					if(outcomeData[6] == 'OR'){
						relapseCriteria2OrList << buildCriteriaMap(outcomeData)
					}else{
						criteriaList << buildCriteriaMap(outcomeData)
						relapseCriteria2[outcomeLineNumber.toString()] = criteriaList
					}
					if(!outcomeMore)
						outcomeMore = outcomeData[0]
					if(!descriptionMore){
						descriptionMore = outcomeData[7]
					}
				}else if(outcomeData[0] == 'Mortality'){
					println "add some mortality criteria"
					if(outcomeData[6] == 'OR'){
						mortalityCriteriaOrList << buildCriteriaMap(outcomeData)
					}else{
						criteriaList << buildCriteriaMap(outcomeData)
						mortalityCriteria[outcomeLineNumber.toString()] = criteriaList
					}
					if(!outcomeLessM)
						outcomeLessM = outcomeData[0]
					if(!descriptionLessM){
						descriptionLessM = outcomeData[7]
					}
					
				}else if(outcomeData[0] == 'No Mortality'){
					println "add some no mortality criteria"
					if(outcomeData[6] == 'OR'){
						mortalityCriteria2OrList << buildCriteriaMap(outcomeData)
					}else{
						criteriaList << buildCriteriaMap(outcomeData)
						mortalityCriteria2[outcomeLineNumber.toString()] = criteriaList
					}
					if(!outcomeMoreM)
						outcomeMoreM = outcomeData[0]
					if(!descriptionMoreM){
						descriptionMoreM = outcomeData[7]
					}
				}
				
			}
		}
	}
	
	//check for 'or' queries
	if(relapseCriteriaOrList)
		relapseCriteria["relapseCriteriaOrList"] = relapseCriteriaOrList
	if(relapseCriteria2OrList)
		relapseCriteria2["relapseCriteria2OrList"] = relapseCriteria2OrList
	if(mortalityCriteriaOrList)
		mortalityCriteria["mortalityCriteriaOrList"] = mortalityCriteriaOrList
	if(mortalityCriteria2OrList)
		mortalityCriteria2["mortalityCriteria2OrList"] = mortalityCriteria2OrList
	
	
	if(relapseCriteria && relapseCriteria2){
		bundledRelapseCriteria << relapseCriteria
		bundledRelapseCriteria << relapseCriteria2
		bundledRelapseCriteria << specimenCriteria
	}
	if(mortalityCriteria && mortalityCriteria2){
		bundledMortalityCriteria << mortalityCriteria
		bundledMortalityCriteria << mortalityCriteria2
	}
	
	
	
	try {
		println "send to outcome service"
		def relapseCreated = false
		def mortalityCreated = false
		if(bundledRelapseCriteria){
			println "send relapse crit"
			outcomeService.addQueryOutcomes(bundledRelapseCriteria,study,outcomeLess, outcomeMore, descriptionLess,descriptionMore)
			createdSuccessfully = true
		}
		
		if(bundledMortalityCriteria){
			println "send mortality crit"
			outcomeService.addQueryOutcomes(bundledMortalityCriteria,study,outcomeLessM, outcomeMoreM, descriptionLessM,descriptionMoreM)
			createdSuccessfully = true
		}
		
		trans.commit()
		
	
	} catch (Exception e) {
		trans.rollback()
		throw e
	}
	return createdSuccessfully
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
