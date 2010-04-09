import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager

grailsHome = Ant.antProject.properties."env.GRAILS_HOME"
includeTargets << grailsScript("Bootstrap")
includeTargets << grailsScript("Init")

target(main: "Load High Throughput Data") {
	depends(clean, compile, classpath)
	
	// Load up grails contexts to be able to use GORM
	loadApp()
	configureApp()
	
	println "Please specify a project name:"
	def projectName = new InputStreamReader(System.in).readLine().toUpperCase()
	def htFiles = new File("dataImport/${projectName}/${projectName}_ht_files.txt")
	def mappingFile = new File("dataImport/${projectName}/${projectName}_biospecimen_mapping.txt")
	
/*	if(!studyFile.exists()) {
		println "Cannot find high throughput metadata file at dataImport/${projectName}/${projectName}_ht_files.txt.  Please check the study name and try again."
		return
	}
	def dataSourceClass = classLoader.loadClass('StudyDataSource')
	def study = dataSourceClass.findBySchemaName(projectName)
	while(study) {
		println "Project with name: $projectName does not exist.  Unable to load high throughput data."
		return
	}*/
	
	def htDataService =  appCtx.getBean('htDataService')
	
	def params = [:]
	params.schemaName = "WANG"
	params.name = "test2.rda"
	params.description = "test r data file"
	params.relativePath = "NORMALIZED"
	params.fileSize = 10000
	params.fileType = "PLIER_NORMALIZED"
	params.fileFormat = "RBINARY"
	params.dataLevel = "NORMALIZED"
	params.insertUser = "acs224"
	params.insertMethod = "test"
	params.insertDate = new Date()
	def sessionFactory = appCtx.getBean("sessionFactory")

	def session = sessionFactory.getCurrentSession()
	def trans = session.beginTransaction()
	loadRawFiles(projectName, mappingFile)
	trans.commit()
	
	println "High throughput data loading for $projectName was successful"
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
				description = data[5]
			}
			def params = [:]
			params.schemaName = schemaName
			params.fileName = data[2]
			params.patientId = data[0]
			params.name = data[1]
			params.design = data[3]
			params.relativePath = "RAW"
			params.fileSize = 0
			params.fileType = "CEL"
			params.fileFormat = "BINARY"
			params.dataLevel = "RAW"
			params.description = ""
			params.insertUser = "acs224"
			params.insertMethod = "load-ht-data"
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
	norm.relativePath = "NORMALIZED"
	norm.fileSize = 10000
	norm.fileType = "PLIER_NORMALIZED"
	norm.fileFormat = "RBINARY"
	norm.dataLevel = "NORMALIZED"
	norm.insertUser = "acs224"
	norm.insertMethod = "load-ht-data"
	norm.insertDate = new Date()
	return htDataService.loadNormalizedFileWithPriors(priorFiles, norm)
}

setDefaultTarget(main)
