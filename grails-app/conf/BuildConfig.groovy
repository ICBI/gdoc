// war file setup
grails.project.war.file = "target/${appName}.war"
grails.war.resources = {stagingDir ->
	delete(file: "$stagingDir/WEB-INF/lib/jbossall-client.jar")
	delete(file: "$stagingDir/WEB-INF/lib/javax.jms.jar")
	delete(file: "$stagingDir/WEB-INF/lib/commons-collections-2.1.1.jar")
	delete(file: "$stagingDir/WEB-INF/lib/jboss-jca.jar")
	delete(file: "$stagingDir/WEB-INF/lib/ojdbc14.jar")
	delete(file: "$stagingDir/WEB-INF/lib/geronimo-jms_1.1_spec-1.1.1.jar")
	delete(file: "$stagingDir/WEB-INF/lib/geronimo-spec-jta-1.0.1B-rc4.jar")
	copy(file: "grails-app/conf/GcoreConfig.groovy", tofile: "${stagingDir}/WEB-INF/classes/GcoreConfig.groovy")
}

grails.project.dependency.resolution = {
	inherits("global") {
	        // uncomment to disable ehcache
	        // excludes 'ehcache'
	}
	repositories {
		grailsPlugins()
		grailsHome()
		grailsRepo "https://svn-bccfr.uis.georgetown.edu/svn/gdoc/gcore-plugins"
		grailsCentral()
		mavenCentral()
		
	}
	plugins {
		runtime 'edu.georgetown.gcore:cin:latest.integration'
		runtime 'edu.georgetown.gcore:cytoscape:latest.integration'
		runtime 'edu.georgetown.gcore:gcore:latest.integration'
		runtime 'edu.georgetown.gcore:gene-expression-search:latest.integration'
		runtime 'edu.georgetown.gcore:genome-browser:latest.integration'
		runtime 'edu.georgetown.gcore:group-comparison:latest.integration'
		runtime 'edu.georgetown.gcore:heatmap:latest.integration'
		runtime 'edu.georgetown.gcore:km:latest.integration'
		runtime 'edu.georgetown.gcore:molecule-target:latest.integration'
		runtime 'edu.georgetown.gcore:pca:latest.integration'
		runtime 'edu.georgetown.gcore:quick-start:latest.integration'
	}
}
//grails.plugin.location.'gcore' = "../grails-gcore"
//grails.plugin.location.'group-comparison' = "../grails-group-comparison"
//grails.plugin.location.'heatmap' = "../grails-heatmap"
//grails.plugin.location.'cin' = "../cin"
//grails.plugin.location.'genome-browser' = "../grails-genome-browser"
//grails.plugin.location.'pca' = "../pca"
//grails.plugin.location.'km' = "../grails-km"
//grails.plugin.location.'cytoscape' = "../cytoscape"
//grails.plugin.location.'molecule-target' = "../molecule-target"
//grails.plugin.location.'quick-start' = "../grails-quick-start"