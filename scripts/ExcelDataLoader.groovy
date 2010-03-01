import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager


grailsHome = Ant.antProject.properties."env.GRAILS_HOME"
includeTargets << grailsScript("Bootstrap")
includeTargets << grailsScript("Init")

target(main: "Load data into the DB from Excel file") {
	depends(clean, compile, classpath)
	
	// Load up grails contexts to be able to use GORM
	loadApp()
	configureApp()
	load()
	//manipulateSDF("test")
	
}

def load(){
	def sessionFactory = appCtx.getBean("sessionFactory")
	def session = sessionFactory.getCurrentSession()
	def trans = session.beginTransaction()
	def moleculeService = appCtx.getBean("moleculeService")
	try {
		println "load data: $trans"
		moleculeService.createMolecules("/Users/kmr75/Documents/gu/gdocRelated/DDG-Schema/DDGData_Clean.xls")
		trans.commit()
	} catch (Exception e) {
		trans.rollback()
		e.printStackTrace()
	}
	
	
}

def manipulateSDF(projectName) {
	println "read from sdf file:"
	def sdfFile= new File('/Users/kmr75/Documents/gu/gdocRelated/DDG-Schema/Sample-Compounds-Clean.sdf')
	def nameSdfFile= new FileWriter('/Users/kmr75/Documents/gu/gdocRelated/DDG-Schema/Sample-Compounds-WithNames.sdf')
	
	def newCompound = false
	def numberOfCopmounds = 0
	def compoundName = ""
	sdfFile.eachLine { line ->
		if(newCompound){
			compoundName = line
			//println "found file name: $line"
			numberOfCopmounds++
			newCompound = false
		}
		if(line == '$$$$'){
			newCompound = true
		}
		if(line == 'M  END' && compoundName){
			line+= "\n>  <NAME>\n $compoundName \n"
		}
		nameSdfFile.append(line + "\n")
	}
	println "read $numberOfCopmounds compounds"
}

setDefaultTarget(main)
