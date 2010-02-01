grailsHome = Ant.antProject.properties."env.GRAILS_HOME"
includeTargets << grailsScript("Bootstrap")
includeTargets << grailsScript("Init")

target(main: "The description of the script goes here!") {
	depends(clean, compile, classpath)
	
	// Load up grails contexts to be able to use GORM
	loadApp()
	configureApp()
	
	println "Please specify a project name:"
	def projectName = new InputStreamReader(System.in).readLine().toUpperCase()
	def dataSource = classLoader.loadClass('StudyDataSource')
	def study = dataSource.findBySchemaName(projectName)
	while(study) {
		println "Project with name: $projectName already exists.  Please choose another name:"
		projectName = new InputStreamReader(System.in).readLine().toUpperCase()
		study = dataSource.findBySchemaName(projectName)
	}
	println "Using project name: $projectName"
	Ant.input(addProperty: "project.location", message: "Please specify a path to the GEO MiNIML File:")
	println "Attempting to parse file located at ${Ant.antProject.properties.'project.location'}"
	
	def parserClass = classLoader.loadClass('MinimlParser')
	def helperClass = classLoader.loadClass('MinimlHelpers')
	def parser = parserClass.newInstance()
	
	// Reading study information
	println "Reading study metadata from file."
	mkdir(dir:"dataImport/$projectName")
	
	parser.createStudyFile(Ant.antProject.properties.'project.location', "dataImport/${projectName}/${projectName}_study_table.txt", projectName)
	println "Created study metadata file at dataImport/${projectName}/${projectName}_study_table.txt"
	
	def table
	for(def i = 0; i < helperClass.strategies.size(); i++) {
		println ""
		println "Attempting to read clinical attributes/values......."
		try {
			table = parser.buildClinicalTable(Ant.antProject.properties."project.location", helperClass.strategies[i])
			parser.printSample(table)
		} catch (Exception e) {
			println "Error. Trying again......"
			continue
		} 
		println ""
		println "Does this look like valid clinical data? [y/N]"
		def result = new InputStreamReader(System.in).readLine() 
		if(result == "y") {
			break
		} 
		table = null
	}
	if(!table) {
		println "No clinical data found or this program does not support the format of the clinical data.  Please provide the clinical data in the required format or contact the developer of this program."
		return
	}
	println "Creating clinical data file: dataImport/${projectName}/${projectName}_clinical_table.txt"
	parser.writeMatrixFile(table, "dataImport/${projectName}/${projectName}_clinical_table.txt")
	println "Success!  Please review the generated files for accuracy, then run the loading process."

}

setDefaultTarget(main)
