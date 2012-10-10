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
		mavenRepo "https://gdoc-stage.georgetown.edu/artifactory/plugins-release-local/"
		grailsCentral()
		mavenCentral()
		ebr()
		
	}
	dependencies {
		compile("org.tmatesoft.svnkit:svnkit:1.3.5") {
            excludes "jna", "trilead-ssh2", "sqljet"
			export = false
        }
	}
	plugins {
		compile 'edu.georgetown.gcore:gcore:2.0.0'
		compile 'edu.georgetown.gcore:analysis-core:2.0.0'
		compile 'edu.georgetown.gcore:cin:2.0.0'
		compile 'edu.georgetown.gcore:cytoscape:2.0.0'
		compile 'edu.georgetown.gcore:gene-expression-search:2.0.0'
		compile 'edu.georgetown.gcore:genome-browser:2.0.0'
		compile 'edu.georgetown.gcore:group-comparison:2.0.0'
		compile 'edu.georgetown.gcore:heatmap:2.0.0'
		compile 'edu.georgetown.gcore:km:2.0.0'
		compile 'edu.georgetown.gcore:molecule-target:2.0.0'
		compile 'edu.georgetown.gcore:pca:2.0.0'
		compile 'edu.georgetown.gcore:quick-start:2.0.0' 
		compile 'edu.georgetown.gcore:next-gen:2.0.0'
		compile	':taggable:0.6.1'
		compile	':spring-security-core:1.2.7.3'
		compile	':spring-security-ldap:1.0.5'
		compile	':recaptcha:0.5.2'
		compile	':hibernate:2.1.1'
		compile ':jms:1.2'
		compile	':jquery:1.7.2'
		compile ':searchable:0.5.5'
		build ':release:2.0.4'
		build(':svn:1.0.2') {
			export = false
		}
		build ':tomcat:2.1.1'
	}
}
//grails.plugin.location.'nextgen' = "../grails-nextgen"
//grails.plugin.location.'gcore' = "../grails-gcore"
//grails.plugin.location.'gene-expression-search' = "../grails-gene-expression-search"
//grails.plugin.location.'analysis-core' = "../grails-analysis-core"
//grails.plugin.location.'group-comparison' = "../grails-group-comparison"
//grails.plugin.location.'heatmap' = "../grails-heatmap"
//grails.plugin.location.'cin' = "../grails-cin"
//grails.plugin.location.'genome-browser' = "../grails-genome-browser"
//grails.plugin.location.'pca' = "../grails-pca"
//grails.plugin.location.'km' = "../grails-km"
//grails.plugin.location.'cytoscape' = "../grails-cytoscape"
//grails.plugin.location.'molecule-target' = "../grails-molecule-target"
//grails.plugin.location.'quick-start' = "../grails-quick-start"