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
	
	loadFileAndSubjects(projectName, mappingFile)
	
	println "High throughput data loading for $projectName was successful"
}

def loadFileAndSubjects(schemaName, mappingFile) {
	def htDataService =  appCtx.getBean('htDataService')
	def subjects = []
	def htFile = [:]
	htFile.schemaName = schemaName
	mappingFile.eachLine { line, number ->
		if(number != 1) {
			def data = line.split('\t')
			if(!htFile.name || htFile.name != data[4]) {
				// if this is a new file, load the previous file
				if(htFile.name) {
					htDataService.loadFileAndSubjects(subjects, htFile)
					subjects = []
				}
				htFile.name = data[4]
				htFile.description = data[5].replace("\"", "")
				htFile.design = data[3]
			}
			def params = [:]
			params.patientId = data[0]
			params.name = data[1]
			subjects << params
		}
		
	}
	htDataService.loadFileAndSubjects(subjects, htFile)
	return 
	
}

setDefaultTarget(main)
