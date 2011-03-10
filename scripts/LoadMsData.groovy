import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager

grailsHome = Ant.antProject.properties."env.GRAILS_HOME"
includeTargets << grailsScript("Bootstrap")
includeTargets << grailsScript("Init")

target(main: "Load Mass Spec Data") {
	depends(clean, compile, classpath)
	
	// Load up grails contexts to be able to use GORM
	loadApp()
	configureApp()
	
	println "Please specify a project name:"
	def projectName = new InputStreamReader(System.in).readLine().toUpperCase()
	
	def peakFile = new File("dataImport/${projectName}/${projectName}_ms_peak_list.txt")
	
	if(!peakFile.exists()) {
		println "Cannot find peak file at dataImport/${projectName}/${projectName}_ms_peak_list.txt.  Please check the study name and try again."
		return
	}
	def dataSourceClass = classLoader.loadClass('StudyDataSource')
	def study = dataSourceClass.findBySchemaName(projectName)
	while(!study) {
		println "Project with name: $projectName does not exist.  Unable to load mass spec data."
		return
	}
	
	def sessionFactory = appCtx.getBean("sessionFactory")

	def session = sessionFactory.getCurrentSession()
	def trans = session.beginTransaction()
	
	loadPeaks(projectName, peakFile)
	trans.commit()
	
	println "Mass spec data loading for $projectName was successful"
}

def loadPeaks(projectName, mappingFile) {
	def htDataService =  appCtx.getBean('htDataService')
	
	def rdaFile = null
	def allPeaks = []
	mappingFile.eachLine { line, number ->
		if(number != 1) {
			def data = line.split('\t')
			if(!rdaFile || rdaFile != data[0]) {
				if(allPeaks.size > 0) {
					htDataService.loadPeaks(rdaFile, allPeaks)
					allPeaks = []
				}
				rdaFile = data[0]
			}
			def params = [:]
			params.schemaName = projectName
			params.name = data[1]
			params.retention = data[2]
			params.mz = data[3]
			params.insertUser = "acs224"
			params.insertMethod = "load-ms-data"
			params.insertDate = new Date()
			allPeaks << params
		}
	}
	if(allPeaks.size > 0) {
		htDataService.loadPeaks(rdaFile, allPeaks)
		allPeaks = []
	}
}
 
setDefaultTarget(main)
