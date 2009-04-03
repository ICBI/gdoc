
//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'Ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
// Ant.mkdir(dir:"/Users/nebolsin/Projects/grails-jquery/grails-app/jobs")
//

Ant.property(environment:"env")
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"

includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )
checkVersion()
configureProxy()

jQueryVersion = "1.3.2"

Ant.sequential {
    mkdir(dir:"${grailsHome}/downloads")

    event("StatusUpdate", ["Downloading JQuery ${jQueryVersion}"])
     
    def files = ["jquery-${jQueryVersion}.js", "jquery-${jQueryVersion}.min.js"]

    mkdir(dir:"${basedir}/web-app/js/jquery")
    files.each {
        get(dest:"${basedir}/web-app/js/jquery/${it}",
            src:"http://jqueryjs.googlecode.com/files/${it}",
            verbose:true)
    }
}
event("StatusFinal", ["JQuery ${jQueryVersion} installed successfully"])

