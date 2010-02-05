import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

grailsHome = Ant.antProject.properties."env.GRAILS_HOME"
includeTargets << grailsScript("Bootstrap")
includeTargets << grailsScript("Init")

target(main: "Load data into the DB") {
	depends(clean, compile, classpath)
	
	// Load up grails contexts to be able to use GORM
	loadApp()
	configureApp()
	
	println "Please specify a project name:"
	def projectName = new InputStreamReader(System.in).readLine().toUpperCase()
	def studyFile = new File("dataImport/${projectName}/${projectName}_study_table.txt")
	if(!studyFile.exists()) {
		println "Cannot find study metadata file at dataImport/${projectName}/${projectName}_study_table.txt.  Please check the study name and try again."
		return
	}
	def dataSourceClass = classLoader.loadClass('StudyDataSource')
	def study = dataSourceClass.findBySchemaName(projectName)
	while(study) {
		println "Project with name: $projectName already exists.  Unable to overwrite data in this schema."
		return
	}
	println "Cleaning up schema...."
	executeScript("sql/study_cleanup_template.sql", projectName, true)
	println "Creating tablespace ${projectName}...."
	executeScript("sql/01_create_tablespace_template.sql", projectName)
	println "Creating user ${projectName}...."
	executeScript("sql/02_study_setup_template.sql", projectName)
	println "Creating schema for project ${projectName}...."
	executeScript("sql/03_study_schema_template.sql", projectName)

	def sql = groovy.sql.Sql.newInstance(CH.config.dataSource.url, projectName,
	                     "change_me", CH.config.dataSource.driverClassName)
	
	def engine = new groovy.text.SimpleTemplateEngine() 
	def template = engine.createTemplate(new File("sql/04_study_grants_template.sql").text) 
	
	Writable writable = template.make([projectName: projectName])
	writable.toString().eachLine {
		if(it)
			sql.execute(it.replace(';', ''))
	}
	loadData(projectName)
}

def executeScript(script, projectName, continueError = false) {
	def dataSource =  appCtx.getBean('dataSource')
	
	def engine = new groovy.text.SimpleTemplateEngine() 
	def template = engine.createTemplate(new File(script).text) 
	
	Writable writable = template.make([projectName: projectName])
	
	def resource = new org.springframework.core.io.ByteArrayResource(writable.toString().getBytes())
	org.springframework.test.jdbc.SimpleJdbcTestUtils.executeSqlScript(new org.springframework.jdbc.core.simple.SimpleJdbcTemplate(dataSource), resource, continueError)
	
}

def loadData(projectName) {
	
}

setDefaultTarget(main)
