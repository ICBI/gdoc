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
	
	println "Please specify the name of a design file to load:"
	def fileName = new InputStreamReader(System.in).readLine()
	
	def metadataFile = new File("dataImport/designs/${fileName}")
	def reporterFileName = fileName.substring(0, fileName.indexOf(".txt"))
	def reporterFile = new File("dataImport/designs/${reporterFileName}_REPORTERS.txt")
	
	if(!metadataFile.exists()) {
		println "Cannot find array design file at ${metadataFile.absoluteFile}.  Please check the path and try again."
		return
	}
	if(!reporterFile.exists()) {
		println "Cannot find reporter file at ${reporterFile.absoluteFile}.  Please check the path and try again."
		return
	}
	
	
	def sessionFactory = appCtx.getBean("sessionFactory")

	def session = sessionFactory.getCurrentSession()
	def trans = session.beginTransaction()
	def designName = loadArrayDesign(metadataFile)
	loadReporters(designName, reporterFile)
	trans.commit()
	
	println "Successfully loaded ${designName}."
}

def loadArrayDesign(metadataFile) {
	def htDataService =  appCtx.getBean('htDataService')
	def arrayDesignClass = classLoader.loadClass('ArrayDesign')
	def design
	metadataFile.eachLine { line, number ->
		if(number != 1) {
			def data = line.split('\t')
			def params = [:]
			params.platform = data[0]
			params.provider = data[1]
			params.arrayType = data[2]
			params.insertUser = "load user"
			params.insertMethod = "load-array-design"
			params.insertDate = new Date()
			def arrayDesign = arrayDesignClass.findByPlatform(params.platform)
			if(arrayDesign) {
				println "Array design ${params.platform} already exists.  Please remove from the DB if you wish to reload."
				System.exit(1)
			}
			design = htDataService.loadArrayDesign(params)
		}
	}
	return design.platform
}

def loadReporters(arrayName, reporterFile) {
	def htDataService =  appCtx.getBean('htDataService')
	def reporters = []
	reporterFile.eachLine { line, number ->
		if(number != 1) {
			def data = line.split('\t')
			def params = [:]
			params.name = data[0]
			if(data.length < 2)
				params.geneSymbol = ""
			else 
				params.geneSymbol = data[1]
			params.insertUser = "load user"
			params.insertMethod = "load-array-design"
			params.insertDate = new Date()
			reporters << params
		}
	}
	htDataService.loadReportersForDesign(arrayName, reporters)
	
}


setDefaultTarget(main)
