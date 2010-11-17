import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager

grailsHome = Ant.antProject.properties."env.GRAILS_HOME"
includeTargets << grailsScript("Bootstrap")
includeTargets << grailsScript("Init")

target(main: "Load CIN Data") {
	depends(clean, compile, classpath)
	
	// Load up grails contexts to be able to use GORM
	loadApp()
	configureApp()
	
	println "Please specify a project name:"
	def projectName = new InputStreamReader(System.in).readLine().toUpperCase()
	def mappingFile = new File("dataImport/${projectName}/${projectName}_cin_biospecimen_mapping.txt")
	
	if(!mappingFile.exists()) {
		println "Cannot find high throughput metadata file at dataImport/${projectName}/${projectName}_cin_biospecimen_mapping.txt.  Please check the study name and try again."
		return
	}
	def dataSourceClass = classLoader.loadClass('StudyDataSource')
	def study = dataSourceClass.findBySchemaName(projectName)
	while(!study) {
		println "Project with name: $projectName does not exist.  Unable to load cin data."
		return
	}
	
	def sessionFactory = appCtx.getBean("sessionFactory")

	def session = sessionFactory.getCurrentSession()
	def trans = session.beginTransaction()
	loadRawFiles(projectName, mappingFile)
	trans.commit()
	
	println "CIN data loading for $projectName was successful"
}

def loadRawFiles(schemaName, mappingFile) {
	def allFiles = []
	def loadedFiles = []
	def rdaFile = null
	def description = null
	mappingFile.eachLine { line, number ->
		if(number != 1) {
			def data = line.split('\t')
			if(!rdaFile || rdaFile != data[4]) {
				if(allFiles.size > 0) {
					loadedFiles << loadNormFile(schemaName, rdaFile, description, allFiles)
					allFiles = []
				}
				rdaFile = data[4]
				description = data[5].replace("\"", "")
			}
			def params = [:]
			params.schemaName = schemaName
			params.fileName = data[2]
			params.patientId = data[0]
			params.name = data[1]
			params.design = data[3]
			params.relativePath = "RAW"
			params.fileSize = 0
			/*if (schemaName == "BRC_FINETTI_2009_01") {
				params.fileType = "TXT"
				params.fileFormat = "TEXT"
			} else {*/
			params.fileType = "CEL"
			params.fileFormat = "BINARY"	
			//}
			params.dataLevel = "RAW"
			params.description = ""
			params.insertUser = "lac223"
			params.insertMethod = "load-cin-data"
			params.insertDate = new Date()
			params.designTable = "HTARRAY_DESIGN"
			allFiles << params
		}
	}
	if(allFiles.size > 0) {
		loadedFiles << loadNormFile(schemaName, rdaFile, description, allFiles)
		allFiles = []
	}
	return loadedFiles
	
}

def loadNormFile(schemaName, fileName, description, priorFiles) {
	def htDataService =  appCtx.getBean('htDataService')
	
	def norm = [:]
	norm.schemaName = schemaName
	norm.name = fileName
	norm.description = description
	norm.relativePath = "INTERPRETED"
	norm.fileSize = 10000
	norm.fileType = "CIN"
	norm.fileFormat = "RBINARY"
	norm.dataLevel = "INTERPRETED"
	norm.insertUser = "lac223"
	norm.insertMethod = "load-cin-data"
	norm.insertDate = new Date()
	return htDataService.loadNormalizedFileWithPriors(priorFiles, norm)
}

setDefaultTarget(main)
