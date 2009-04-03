import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )

availableJqueryPlugins = [
        "form": [
                title: "",
                description: "",
                documentation: "",
                files : [
                        "jquery.form.js": "http://jqueryjs.googlecode.com/svn/trunk/plugins/form/jquery.form.js"
                ]
        ],
        "facebox" : [
                title: "",
                description: "",
                documentation: "",
                files : [
                        "jquery.form.js": "http://jqueryjs.googlecode.com/svn/trunk/plugins/form/jquery.form.js"
                ]
        ],
        "treeview" : [
                title: "",
                description: "",
                documentation: "",
                files : [
                        "jquery.treeview.js": "http://jqueryjs.googlecode.com/svn/trunk/plugins/treeview/jquery.treeview.js",
                        "jquery.treeview.async.js": "http://jqueryjs.googlecode.com/svn/trunk/plugins/treeview/jquery.treeview.async.js",
                        "jquery.treeview.sortable.js": "http://jqueryjs.googlecode.com/svn/trunk/plugins/treeview/jquery.treeview.sortable.js"

                ]
        ]
]
