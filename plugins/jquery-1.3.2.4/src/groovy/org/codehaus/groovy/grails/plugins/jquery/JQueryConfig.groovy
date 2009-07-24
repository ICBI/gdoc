package org.codehaus.groovy.grails.plugins.jquery;

import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 * TODO: write javadoc
 *
 * @author Sergey Nebolsin (nebolsin@prophotos.ru)
 */
public class JQueryConfig {
    def defaultPlugins
    def plugins = [:]

    def init() {
        ApplicationHolder.application.metadata.findAll{key, value ->
            key.startsWith('jquery.plugins')
        }.each {key, value ->
            // wtf?
            def pluginName = (key.length() >= 16)? key[15..-1] : "(ungrouped)"
            plugins."$pluginName" = value.split(",") as List
        }
        
        defaultPlugins = ApplicationHolder.application.config.jquery?.defaultPlugins
    }
}
