import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )  
includeTargets << new File("${jqueryPluginDir}/scripts/_InitJqueryPlugin.groovy")

target('default': "Displays the list of available jQuery plugins") {
    listPlugins()                                              
}

target(listPlugins: "The implementation task") {
    println "\nAvailable jQuery plugins:"
    availableJqueryPlugins.each { name, pluginInfo ->
        println "    $name : ${pluginInfo.description}"
    }
    println "\nUse 'grails install-jquery-plugin [pluginName]' to install the plugin."
}