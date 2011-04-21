// war file setup
grails.war.destFile = "gdoc.war"
grails.war.resources = {stagingDir ->
	delete(file: "$stagingDir/WEB-INF/lib/jbossall-client.jar")
	delete(file: "$stagingDir/WEB-INF/lib/javax.jms.jar")
	delete(file: "$stagingDir/WEB-INF/lib/commons-collections-2.1.1.jar")
	delete(file: "$stagingDir/WEB-INF/lib/jboss-jca.jar")
	delete(file: "$stagingDir/WEB-INF/lib/ojdbc14.jar")
}
grails.plugin.location.'gcore' = "../gcore"
grails.plugin.location.'group-comparison' = "../group-comparison"
grails.plugin.location.'heatmap' = "../heatmap"
grails.plugin.location.'cin' = "../cin"
grails.plugin.location.'gene-expression-search' = "../gene-expression-search"
grails.plugin.location.'genome-browser' = "../genome-browser"
grails.plugin.location.'pca' = "../pca"
grails.plugin.location.'km' = "../km"
grails.plugin.location.'cytoscape' = "../cytoscape"
grails.plugin.location.'molecule-target' = "../molecule-target"
