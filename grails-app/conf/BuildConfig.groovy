// war file setup
grails.project.war.file = "target/${appName}.war"
grails.war.resources = {stagingDir ->
	delete(file: "$stagingDir/WEB-INF/lib/jbossall-client.jar")
	delete(file: "$stagingDir/WEB-INF/lib/com.springsource.org.aopalliance-1.0.0.jar")
	delete(file: "$stagingDir/WEB-INF/lib/com.springsource.org.apache.commons.lang-2.4.0.jar")
	delete(file: "$stagingDir/WEB-INF/lib/com.springsource.org.apache.commons.logging-1.0.4.jar")
	delete(file: "$stagingDir/WEB-INF/lib/org.springframework.aop-3.0.3.RELEASE.jar")
	delete(file: "$stagingDir/WEB-INF/lib/org.springframework.asm-3.0.3.RELEASE.jar")
	delete(file: "$stagingDir/WEB-INF/lib/org.springframework.beans-3.0.3.RELEASE.jar")
	delete(file: "$stagingDir/WEB-INF/lib/org.springframework.context-3.0.3.RELEASE.jar")
	delete(file: "$stagingDir/WEB-INF/lib/org.springframework.core-3.0.3.RELEASE.jar")
	delete(file: "$stagingDir/WEB-INF/lib/org.springframework.expression-3.0.3.RELEASE.jar")
	delete(file: "$stagingDir/WEB-INF/lib/org.springframework.security.core-3.0.4.RELEASE.jar")
	delete(file: "$stagingDir/WEB-INF/lib/org.springframework.transaction-2.5.6.SEC01.jar")
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
		compile 'edu.georgetown.gcore:gcore:2.2'
		compile 'edu.georgetown.gcore:analysis-core:2.0.0'
		compile 'edu.georgetown.gcore:cin:2.0.1'
		compile 'edu.georgetown.gcore:cytoscape:2.0.0'
		compile 'edu.georgetown.gcore:gene-expression-search:2.0.1'
		compile 'edu.georgetown.gcore:genome-browser:2.0.1'
		compile 'edu.georgetown.gcore:group-comparison:2.1.0'
		compile 'edu.georgetown.gcore:heatmap:2.0.1'
		compile 'edu.georgetown.gcore:km:2.0.1'
		compile 'edu.georgetown.gcore:molecule-target:2.0.2'
		compile 'edu.georgetown.gcore:pca:2.1.0'
		//compile 'edu.georgetown.gcore:quick-start:2.0.0'
		compile 'edu.georgetown.gcore:next-gen:latest.integration'
		compile 'edu.georgetown.gcore:dicom-viewer:latest.integration'
		compile	':taggable:0.6.1'
		compile	':spring-security-core:1.2.7.3'
		compile	':spring-security-ldap:1.0.5'
		compile	':recaptcha:0.5.2'
		compile	':hibernate:2.1.1'
		compile ':jms:1.2'
		compile	':jquery:1.7.2'
		//compile ':searchable:0.6.6'
		build ':release:2.0.4'
		build(':svn:1.0.2') {
			export = false
		}
		build ':tomcat:2.1.1'
	}
}
//grails.plugin.location.'gcore' = "../plugins/grails-gcore"
//grails.plugin.location.'nextgen' = "../plugins/grails-nextgen"
//grails.plugin.location.'dicom-viewer' = "../plugins/grails-dicom-viewer"
//grails.plugin.location.'gene-expression-search' = "../plugins/grails-gene-expression-search"
//grails.plugin.location.'analysis-core' = "../plugins/grails-analysis-core"
//grails.plugin.location.'group-comparison' = "../plugins/grails-group-comparison"
//grails.plugin.location.'heatmap' = "../plugins/grails-heatmap"
//grails.plugin.location.'cin' = "../plugins/grails-cin"
//grails.plugin.location.'genome-browser' = "../plugins/grails-genome-browser"
//grails.plugin.location.'pca' = "../plugins/grails-pca"
//grails.plugin.location.'km' = "../plugins/grails-km"
//grails.plugin.location.'cytoscape' = "../plugins/grails-cytoscape"
//grails.plugin.location.'molecule-target' = "../plugins/grails-molecule-target"
//grails.plugin.location.'quick-start' = "../plugins/grails-quick-start"
