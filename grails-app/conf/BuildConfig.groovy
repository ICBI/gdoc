// war file setup
grails.war.destFile = "gdoc.war"
grails.war.resources = {stagingDir ->
	delete(file: "$stagingDir/WEB-INF/lib/jbossall-client.jar")
	delete(file: "$stagingDir/WEB-INF/lib/javax.jms.jar")
	delete(file: "$stagingDir/WEB-INF/lib/commons-collections-2.1.1.jar")
}
grails.plugin.repos.discovery.myRepository="http://10.168.2.21/grails-plugins/"