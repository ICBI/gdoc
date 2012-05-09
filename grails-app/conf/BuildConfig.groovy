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
		
	}
	dependencies {
		compile("org.tmatesoft.svnkit:svnkit:1.3.5") {
            excludes "jna", "trilead-ssh2", "sqljet"
			export = false
        }
	}
	plugins {
		build 'edu.georgetown.gcore:gcore:latest.integration'
		build 'edu.georgetown.gcore:analysis-core:latest.integration'
		build 'edu.georgetown.gcore:cin:latest.integration'
		build 'edu.georgetown.gcore:cytoscape:latest.integration'
		build 'edu.georgetown.gcore:gene-expression-search:latest.integration'
		build 'edu.georgetown.gcore:genome-browser:latest.integration'
		build 'edu.georgetown.gcore:group-comparison:latest.integration'
		build 'edu.georgetown.gcore:heatmap:latest.integration'
		build 'edu.georgetown.gcore:km:latest.integration'
		build 'edu.georgetown.gcore:molecule-target:latest.integration'
		build 'edu.georgetown.gcore:pca:latest.integration'
		build 'edu.georgetown.gcore:quick-start:latest.integration'
		compile	':taggable:0.6.1'
		compile	':spring-security-core:1.2.7.3'
		compile	':spring-security-ldap:1.0.5'
		compile	':recaptcha:0.5.2'
		compile	':hibernate:2.0.3'
		compile ':jms:1.2'
		build ':release:2.0.0'
		build(':svn:1.0.2') {
			export = false
		}
		runtime ':tomcat:2.0.3'
	}
}
grails.plugin.location.'nextgen' = "../grails-nextgen"
//grails.plugin.location.'gcore' = "../grails-gcore"
//grails.plugin.location.'analysis-core' = "../grails-analysis-core"
//grails.plugin.location.'group-comparison' = "../grails-group-comparison"
//grails.plugin.location.'heatmap' = "../grails-heatmap"
//grails.plugin.location.'cin' = "../cin"
//grails.plugin.location.'genome-browser' = "../grails-genome-browser"
//grails.plugin.location.'pca' = "../pca"
//grails.plugin.location.'km' = "../grails-km"
//grails.plugin.location.'cytoscape' = "../cytoscape"
//grails.plugin.location.'molecule-target' = "../molecule-target"
//grails.plugin.location.'quick-start' = "../grails-quick-start"