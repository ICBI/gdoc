import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager

grailsHome = Ant.antProject.properties."env.GRAILS_HOME"
includeTargets << grailsScript("Bootstrap")
includeTargets << grailsScript("Init")

target(main: "Load biospecimen data into the DB") {
	depends(clean, compile, classpath)
	
	// Load up grails contexts to be able to use GORM
	loadApp()
	configureApp()
	
	println "Please specify a project name:"
	def projectName = new InputStreamReader(System.in).readLine().toUpperCase()
	def mappingFile = new File("dataImport/${projectName}/${projectName}_biospecimen_table.txt")
	
	if(!mappingFile.exists()) {
		println "Cannot find biospecimen attribute data file at dataImport/${projectName}/${projectName}_biospecimen_table.txt.  Please check the study name and try again."
		return
	}
	def dataSourceClass = classLoader.loadClass('StudyDataSource')
	def study = dataSourceClass.findBySchemaName(projectName)
	while(!study) {
		println "Project with name: $projectName does not exist.  Unable to load biospecimen data."
		return
	}
	
	println "start loading biospecimen data"
	loadBiospecimenData(projectName)
	
	println "Biospecimen data loading for $projectName was successful"
}


def loadBiospecimenData(projectName) {
	def biospecimenData = new File("dataImport/${projectName}/${projectName}_biospecimen_table.txt")
	def sessionFactory = appCtx.getBean("sessionFactory")
	def biospecimenService = appCtx.getBean("biospecimenService")
	def entityInterceptor = appCtx.getBean("entityInterceptor")
	def session = sessionFactory.openSession(entityInterceptor)
	def trans = session.beginTransaction()
	TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
	try {
		def biospecimens = []
		def attributeHash = [:]
		def biospecimenAndData = [:]
		biospecimenData.eachLine { line, number ->
			def data = line.split("\t", -1)
			data = data.collect { it.trim() }
			if(number != 1) {
				def biospecimen = [:]
				biospecimen.name = data[0]
				biospecimen.patientId = data[1]
				biospecimen.insertUser = "acs224"
				biospecimen.insertDate = new Date()
				biospecimen.insertMethod = "load-data"
				biospecimens << biospecimen
				def biospecimenDataHash = [:]
				attributeHash.each { key, value ->
					biospecimenDataHash[value] = data[key]
				}
				biospecimenAndData[data[0]] = biospecimenDataHash
				
			} else {
				data.eachWithIndex { value, index ->
					if(index != 0) {
						attributeHash[index] = value
					}
				}
			}
		}
		def auditInfo = [insertUser: 'acs224', insertMethod: 'load-data', insertDate: new Date()]
		biospecimenService.createBiospecimensForStudy(projectName, biospecimens,biospecimenAndData,auditInfo)
		/**if(biospecimenAndData){
			def auditInfo = [insertUser: 'acs224', insertMethod: 'load-data', insertDate: new Date()]
			biospecimenAndData.each { biospecimenName, values ->
				biospecimenService.addDataValuesToBiospecimen(projectName, biospecimenName, values, auditInfo)
			}
			loadBiospecimenDataValues(biospecimenAndData,projectName)
		}**/
		trans.commit()
	} catch (Exception e) {
		e.printStackTrace()
		trans.rollback()
	}
	TransactionSynchronizationManager.unbindResource(sessionFactory)
}

def loadBiospecimenDataValues(biospecimenAndData,projectName) {
	def sessionFactory = appCtx.getBean("sessionFactory")
	def biospecimenService = appCtx.getBean("biospecimenService")
	def entityInterceptor = appCtx.getBean("entityInterceptor")
	def session = sessionFactory.openSession(entityInterceptor)
	def trans = session.beginTransaction()
	TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
	try {
		def auditInfo = [insertUser: 'acs224', insertMethod: 'load-data', insertDate: new Date()]
		biospecimenAndData.each { biospecimenName, values ->
			biospecimenService.addDataValuesToBiospecimen(projectName, biospecimenName, values, auditInfo)
		}
		trans.commit()
	} catch (Exception e) {
		e.printStackTrace()
		trans.rollback()
	}
	TransactionSynchronizationManager.unbindResource(sessionFactory)
}

setDefaultTarget(main)
