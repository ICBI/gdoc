import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * @author Sergey Nebolsin (nebolsin@prophotos.ru)
 * @author Finn Herpich (finn.herpich <at> marfinn-software <dot> de)
 */
class JQueryTagLib implements ApplicationContextAware {
    def jQueryConfig

    def jQueryVersion = ConfigurationHolder.config.jquery.version
    def jQuerySources = ConfigurationHolder.config.jquery.sources


    static namespace = "jq"

    /**
     * Includes a plugin javascript file
     *
     * @param attrs A plugin to use
     */
    def plugin = {attrs, body ->
        if(attrs.name) {
            

            // TODO kick this damn need for the config-file
            jQueryConfig.plugins."${attrs.name}".each {
                out << '<script type="text/javascript" src="'
                out << createLinkTo(dir:"js/" + jQuerySources, file:it)
                out << '"></script>'
            }
        }
    }

    /**
     * Adds the jQuery().ready function to the code
     *
     * @param attrs No use
     * @param body  The javascript code to execute
     */
    def jquery = {attrs, body ->
        out << '<script type="text/javascript"> jQuery().ready(function(){'
        out << body()
        out << '}); </script>'
    }

    /**
     * Simple tag to make an element toggleable
     *
     * @param attrs List with the arguments
     *              sourceId -> link-element which fires the toggle action
     *              targetId -> id of the element to toggle
     *              event    -> event to fire the toggle action on (OPTIONAL)
     *              speed    -> effect-speed (OPTIONAL)
     */
    def toggle = {attrs ->
        // Default values
        if(!attrs.event) attrs.event = 'click'
        if(!attrs.speed) attrs.speed = 'normal'                         

        // out
        out << /jQuery("#${attrs['sourceId']}").${attrs['event']}(function(){jQuery("#${attrs['targetId']}").toggle("${attrs['speed']}"); return false; });/
    }

    // TODO add more fancy stuff here ;)
    // Tabs
    // Accordion -> Bassistance-plugin
    // tooltips -> Bassistance-plugin
    // caroussel
    // autocomplete
    // dialogs
    // moar

    /**
     * Creates a jQuery-function which returns the value of the specified element
     *
     * @param attrs Must contain either an attribute selector or elementId that specifies the target element
     */
    def fieldValue = {attrs ->
        def selector

        if(attrs.selector) {
            selector = attrs['selector']
        } else if(attrs.elementId) {
            selector = /#${attrs['elementId']}/
        }

        out << /jQuery('${selector}').fieldValue()[0]/
    }

    /**
     * Well, set the application context...
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        jQueryConfig = applicationContext.jQueryConfig
    }

    
    def toggleelement = {attrs ->
        log.info('toggleelement is deprecated, please use toggle instead')
        out << /jQuery("#${attrs['linkId']}").${attrs['event']}(function(){ jQuery("#${attrs['elementId']}").toggle("${attrs['speed']}"); return false; });/
    }
}