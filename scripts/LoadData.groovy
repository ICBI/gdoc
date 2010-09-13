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
	def isPublic = false
	try {
		println "Cleaning up schema...."
		executeScript("sql/study_cleanup_template.sql", [projectName: projectName], true)
		println "Creating user ${projectName}...."
		executeScript("sql/01_study_setup_template.sql", [projectName: projectName])
		println "Creating schema for project ${projectName}...."
		executeScript("sql/02_study_schema_template.sql", [projectName: projectName])

		def sql = groovy.sql.Sql.newInstance(CH.config.dataSource.url, projectName,
		                     "cur34c4nc3r", CH.config.dataSource.driverClassName)
	
		def engine = new groovy.text.SimpleTemplateEngine() 
		def template = engine.createTemplate(new File("sql/03_study_grants_template.sql").text) 
	
		Writable writable = template.make([projectName: projectName])
		println "Adding grants for project ${projectName}...."
		writable.toString().eachLine {
			if(it){
				sql.execute(it.replace(';', ''))
			}
		}
		println "Loading study information for $projectName...."
		isPublic = loadStudyData(projectName)
		println "Loading clinical attributes for $projectName...."
		loadClinicalData(projectName)
		println "Loading patient data for $projectName...."
		loadPatientData(projectName)
		loadClinicalDataValues(projectName)
	} catch (Throwable e) {
		e.printStackTrace()
		"Data loading for $projectName was not successful"
		return
	}
	def jmxService =  appCtx.getBean('jmxService')
	jmxService.flushConnectionPool()
	println "is public $isPublic"
	if(isPublic) {
		println "Adding $projectName as a public study."
		addPublicStudy(projectName)
	}
	println "Data loading for $projectName was successful"
}

def executeScript(script, optionsHash, continueError = false) {
	def dataSource =  appCtx.getBean('dataSource')
	
	def engine = new groovy.text.SimpleTemplateEngine() 
	def template = engine.createTemplate(new File(script).text) 
	
	Writable writable = template.make(optionsHash)
	
	def resource = new org.springframework.core.io.ByteArrayResource(writable.toString().getBytes())
	org.springframework.test.jdbc.SimpleJdbcTestUtils.executeSqlScript(new org.springframework.jdbc.core.simple.SimpleJdbcTemplate(dataSource), resource, continueError)
	
}

def loadStudyData(projectName) {
	
	def studyFile = new File("dataImport/${projectName}/${projectName}_study_table.txt")
	def studyDataSourceService = appCtx.getBean("studyDataSourceService")
	def sessionFactory = appCtx.getBean("sessionFactory")

	def session = sessionFactory.getCurrentSession()
	def trans = session.beginTransaction()
	def isPublic = false
	try {
		studyFile.eachLine { line, number ->
			if(number != 1) {
				def data = line.split('\t')
				def params = [:]
				params.shortName = data[0]
				params.schemaName = data[7]
				params.longName = data[1]
				params.abstractText = data[2]
				params.cancerSite = data[3]
				params.patientIdName = data[4]
				params.integrated = data[5]
				params.overallAccess = data[6]
				params.useInGui = data[8]
				params.insertUser = "acs224"
				params.insertDate = new Date()
				params.insertMethod = "load-data"
				// Setup data source content
				def contents = []
				def contentTypes = data[9].split(',')
				def showContent = data[10].split(',')
				contentTypes.eachWithIndex { item, index ->
					def content = [:]
					content.type = item.trim()
					println "convert " + showContent[index] + " to integer "
					println showContent[index].class
					content.showInGui = showContent[index].trim().toInteger()
					contents << content
				}
				def dataSource = studyDataSourceService.createWithContent(params, contents)
				def contactFile = new File("dataImport/${projectName}/${projectName}_contact_table.txt")
				def contactParams = [:]
				contactFile.eachLine { lineData, contactLineNumber ->
					if(contactLineNumber != 1) {
						def contactData = lineData.split('\t', -1)
						contactParams.netid = contactData[0]
						contactParams.lastName = contactData[1]
						contactParams.firstName = contactData[2]
						contactParams.suffix = contactData[3]
						contactParams.email = contactData[4]
						contactParams.notes = contactData[5]
						contactParams.insertUser = "acs224"
						contactParams.insertDate = new Date()
						contactParams.insertMethod = "load-data"
						if(contactData[6] == 'PI') {
							studyDataSourceService.addPi(dataSource.id, contactParams)
						} else if(contactData[6] == 'POINT_OF_CONTACT') {
							studyDataSourceService.addPoc(dataSource.id, contactParams)
						}
						isPublic = contactParams.firstName == 'Public'
					}
				}
				
			}
		}
		trans.commit()
	} catch (Exception e) {
		trans.rollback()
		throw e
	}
	return isPublic
}

def loadClinicalData(projectName) {
	def clinicalTypes = new File("dataImport/${projectName}/${projectName}_clinical_type.txt")
	def clinicalVocabs = new File("dataImport/${projectName}/${projectName}_clinical_vocab.txt")
	
	def sessionFactory = appCtx.getBean("sessionFactory")
	def attributeService = appCtx.getBean("attributeService")

	def session = sessionFactory.getCurrentSession()
	def trans = session.beginTransaction()
	try {
		def attributes = []
		clinicalTypes.eachLine { line, number ->
			if(number != 1) {
				def data = line.split("\t", -1)
				def params = [:]
				params.shortName = data[2]
				params.longName = data[3]
				params.definition = data[4]
				params.value = data[5]
				params.semanticGroup = data[6]
				params.gdocPreferred = data[7]
				params.cadsrId = data[8]
				params.evsId = data[9]
				params.qualitative = data[10]
				params.continuous = data[11]
				params.vocabulary = data[12]
				params.oracleDatatype = data[13]
				params.unit = data[14]
				params.lowerRange = data[15]
				params.upperRange = data[16]
				params.insertUser = "acs224"
				params.insertDate = new Date()
				params.insertMethod = "load-data"
				attributes << params
			}
		}
		attributeService.createAll(attributes)
		
		
		clinicalVocabs.eachLine { line, number ->
			if(number != 1) {
				def types = []
				def data = line.split("\t", -1)
				def shortName = data[0]
				def params = [:]
				params.term = data[1]
				params.termMeaning = data[2]
				params.evsId = data[3]
				params.definition = data[4]
				params.insertUser = "acs224"
				params.insertDate = new Date()
				params.insertMethod = "load-data"
				types << params
				attributeService.addVocabsToAttribute(shortName, types)
			}
		}
		
		trans.commit()
	} catch (Exception e) {
		trans.rollback()
		throw e
	}
}

def loadPatientData(projectName) {
	def patientData = new File("dataImport/${projectName}/${projectName}_clinical_table.txt")
	
	def sessionFactory = appCtx.getBean("sessionFactory")
	def patientService = appCtx.getBean("patientService")
	def entityInterceptor = appCtx.getBean("entityInterceptor")
	def session = sessionFactory.openSession(entityInterceptor)
	def trans = session.beginTransaction()
	TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
	try {
		def patients = []
		def attributeHash = [:]
		def patientAndData = [:]
		patientData.eachLine { line, number ->
			def data = line.split("\t", -1)
			data = data.collect { it.trim() }
			if(number != 1) {
				def patient = [:]
				patient.dataSourceInternalId = data[0]
				patient.insertUser = "acs224"
				patient.insertDate = new Date()
				patient.insertMethod = "load-data"
				patients << patient
				def patientDataHash = [:]
				attributeHash.each { key, value ->
					patientDataHash[value] = data[key]
				}
				patientAndData[data[0]] = patientDataHash
			} else {
				data.eachWithIndex { value, index ->
					if(index != 0) {
						attributeHash[index] = value
					}
				}
			}
		}
		patientService.createPatientsForStudy(projectName, patients, patientAndData)
		trans.commit()
	} catch (Exception e) {
		trans.rollback()
		throw e
	} 
	TransactionSynchronizationManager.unbindResource(sessionFactory)
}

def loadClinicalDataValues(projectName) {
	def patientData = new File("dataImport/${projectName}/${projectName}_clinical_table.txt")
	
	def sessionFactory = appCtx.getBean("sessionFactory")
	def patientService = appCtx.getBean("patientService")
	def entityInterceptor = appCtx.getBean("entityInterceptor")
	def session = sessionFactory.openSession(entityInterceptor)
	def trans = session.beginTransaction()
	TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
	try {
		def patients = []
		def attributeHash = [:]
		def patientAndData = [:]
		patientData.eachLine { line, number ->
			def data = line.split("\t", -1)
			if(number != 1) {
				def patient = [:]
				patient.dataSourceInternalId = data[0]
				patient.insertUser = "acs224"
				patient.insertDate = new Date()
				patient.insertMethod = "load-data"
				patients << patient
				def patientDataHash = [:]
				attributeHash.each { key, value ->
					patientDataHash[value] = data[key]
				}
				patientAndData[data[0]] = patientDataHash
				
			} else {
				data.eachWithIndex { value, index ->
					if(index != 0) {
						attributeHash[index] = value
					}
				}
			}
		}
		def auditInfo = [insertUser: 'acs224', insertMethod: 'load-data', insertDate: new Date()]
		patientAndData.each { originalDataSourceId, values ->
			patientService.addClinicalValuesToPatient(projectName, originalDataSourceId, values, auditInfo)
		}
		trans.commit()
	} catch (Exception e) {
		trans.rollback()
		throw e
	}
	TransactionSynchronizationManager.unbindResource(sessionFactory)
}

def addPublicStudy(projectName) {
	def dataSource = appCtx.getBean("dataSource")
	SimpleNamingContextBuilder builder =
		SimpleNamingContextBuilder.emptyActivatedContextBuilder()

	builder.bind("java:/gdoc", dataSource);
	def authManager = SecurityServiceProvider.getAuthorizationManager("gdoc")
	
	// Create protection element
	ProtectionElement pe = authManager.getProtectionElement(projectName, 'StudyDataSource')
	if(!pe) {
		pe = new ProtectionElement()
		pe.protectionElementName = projectName + '_DATA'
		
		pe.objectId = projectName
		pe.attribute = 'StudyDataSource'
		authManager.createProtectionElement(pe)
		//attach to protection group
		authManager.assignProtectionElement('PUBLIC', pe.objectId, pe.attribute)
	} 
}

setDefaultTarget(main)
