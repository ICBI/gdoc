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
	
	def optionMap = [scripts: []] 

	args?.tokenize().each {  token -> 
		def nameValue = token =~ "--?(.*)=(.*)" 
		if (nameValue.matches()) { // this token is a name/value pair 
			optionMap[nameValue[0][1]] = nameValue[0][2] 
		} else { // single item token, only expecting scriptName 
			optionMap["scripts"] << token 
		} 
	}
	
	def mappingFile
	if(!optionMap.file)
		mappingFile = new File("dataImport/${projectName}/${projectName}_biospecimen_mapping.txt")
	else
		mappingFile = new File("dataImport/${projectName}/${optionMap.file}")
	
	if(!mappingFile.exists()) {
		println "Cannot find high throughput metadata file at ${mappingFile.absoluteFile}.  Please check the study name and try again."
		return
	}
	def dataSourceClass = classLoader.loadClass('StudyDataSource')
	def study = dataSourceClass.findBySchemaName(projectName)
	while(!study) {
		println "Project with name: $projectName does not exist.  Unable to load high throughput data."
		return
	}
	
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
