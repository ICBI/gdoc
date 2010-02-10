import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager

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
	def dataSourceClass = classLoader.loadClass('StudyDataSource')
	def contentClass = classLoader.loadClass('DataSourceContent')
	
	def studyFile = new File("dataImport/${projectName}/${projectName}_study_table.txt")
	def sessionFactory = appCtx.getBean("sessionFactory")
	def session = sessionFactory.getCurrentSession()
	def trans = session.beginTransaction()
	try {
		studyFile.eachLine { line, number ->
			if(number != 1) {
				def data = line.split('\t')
				def dataSource = dataSourceClass.newInstance()
				dataSource.shortName = data[0]
				dataSource.schemaName = data[7]
				dataSource.longName = data[0]
				dataSource.abstractText = data[2]
				dataSource.cancerSite = "MULTIPLE"
				dataSource.patientIdName = data[4]
				dataSource.integrated = data[5]
				dataSource.overallAccess = data[6]
				dataSource.useInGui = data[8]
				dataSource.insertUser = "acs224"
				dataSource.insertDate = new Date()
				dataSource.insertMethod = "load-data"
				println "Saving $dataSource"

				// Setup data source content
				def contentTypes = data[9].split(',')
				def showContent = data[10].split(',')
				contentTypes.eachWithIndex { item, index ->
					def content = contentClass.newInstance()
					content.type = item.trim()
					content.showInGui = showContent[index].trim().toInteger()
					dataSource.addToContent(content)
				}
				if (!dataSource.save(flush: true)) 
					println dataSource.errors
					
				def pi = createPi(projectName)
				dataSource.addToPis(pi)
				def poc = createPoc(projectName)
				dataSource.addToPocs(poc)
				dataSource.merge()
			}
		}
	} catch (Exception e) {
		e.printStackTrace()
		trans.rollback()
	}
	trans.commit()
}

def createPi(projectName) {
	return createContact(projectName, 'PI')
}

def createPoc(projectName) {
	return createContact(projectName, 'POINT_OF_CONTACT')
}

def createContact(projectName, contactType) {
	def contactFile = new File("dataImport/${projectName}/${projectName}_contact_table.txt")
	def contactClass = classLoader.loadClass('Contact')
	def contact
	contactFile.eachLine {
		def data = it.split('\t')
		if(data[6] == contactType) {
			contact = contactClass.findByLastNameAndFirstName(data[1], data[2])
			if(!contact) {
				contact = contactClass.newInstance()
				contact.netid = data[0]
				contact.lastName = data[1]
				contact.firstName = data[2]
				contact.suffix = data[3]
				contact.email = data[4]
				contact.notes = data[5]
				contact.insertUser = "acs224"
				contact.insertDate = new Date()
				contact.insertMethod = "load-data"
				contact.save(flush:true)
			}
		}
	}
	return contact
}

setDefaultTarget(main)
