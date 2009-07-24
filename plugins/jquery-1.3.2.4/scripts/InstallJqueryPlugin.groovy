import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU
import org.codehaus.groovy.grails.commons.ConfigurationHolder

def jQueryVersion = ConfigurationHolder.config.jquery.version
def jQuerySources = ConfigurationHolder.config.jquery.sources

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File("${grailsHome}/scripts/Init.groovy")

includeTargets << new File("${jqueryPluginDir}/scripts/_InitJqueryPlugin.groovy")

target('default': "The description of the script goes here!") {
    installJQueryPlugin()
}

target(installJQueryPlugin: "Installs the jQuery plugin") {
    if(args) {
        pluginName = args.trim()
    } else {
        Ant.input(addProperty:"jquery.install.plugin.name", message:"Enter the plugin name")
        pluginName = Ant.antProject.properties."jquery.install.plugin.name"
    }
    installPlugin()
}

target(installPlugin: "Installs the plugin") {
    if(pluginName && availableJqueryPlugins."$pluginName") {
        pluginFiles = availableJqueryPlugins."$pluginName".files
        downloadPluginFiles()
        Ant.property(file: "${basedir}/application.properties")
        def pluginsList = Ant.antProject.properties.get('jquery.plugins')?:"" + pluginFiles.keySet().join(",")
        Ant.propertyfile(file: "${basedir}/application.properties") {
            entry(key: "jquery.plugins", value: pluginsList)
        }
    } else {
        event("StatusError", ["Cannot find specified plugin, use 'grails list-jquery-plugins' to get the list of available plugins"])
    }
}

/* FIXME: No signature of method: InstallJqueryPlugin_groovy.get() is applicable for argument types: (java.util.LinkedHashMap) values: {["dest":.../web-app/js/jquery/jquery.treeview.js, "src":"http://jqueryjs.googlecode.com/svn/trunk/plugins/treeview/jquery.treeview.js", "verbose":true]} */
target(downloadPluginFiles: "Downloads plugin's files to the application's js folder") {
    if(pluginFiles) {
        pluginFiles.each {fileName, url ->
            get(dest: "${basedir}/web-app/js/${jQuerySources}/${fileName}",
                    src: url,
                    verbose: true)
        }
    }
}