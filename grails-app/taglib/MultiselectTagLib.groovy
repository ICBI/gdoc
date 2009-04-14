/**
 * multiselect taglib
 * adapted from select functionality in Grails' FormTagLib.groovy file
 *
 * Intended as a stopgap until multiselect functionality is added
 * to Grails permanently
 *
 * Additional functionality for multiple select behaviour
 * provided by Michael Kimsal
 *
 * Original FormTagLib code
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 */

import org.springframework.web.servlet.support.RequestContextUtils as RCU
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler

class MultiselectTagLib {

    /**
     * A helper tag for creating HTML selects
     *
     * Examples:
     * <g:select name="user.age" from="${18..65}" value="${age}" />
     * <g:select name="user.company.id" from="${Company.list()}" value="${user?.company.id}" optionKey="id" />
     */
    def multiselect = { attrs ->
    def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
    def locale = RCU.getLocale(request)
        def writer = out
        attrs.id = attrs.id ? attrs.id : attrs.name
        def from = attrs.remove('from')
        def keys = attrs.remove('keys')
        def optionKey = attrs.remove('optionKey')
        def optionValue = attrs.remove('optionValue')
        def value = attrs.remove('value')
        def valueMessagePrefix = attrs.remove('valueMessagePrefix')
                def noSelection = attrs.remove('noSelection')
        if (noSelection != null) {
            noSelection = noSelection.entrySet().iterator().next()
        }

        writer << "<select name=\"${attrs.remove('name')}\" "
        // process remaining attributes
        outputAttributes(attrs)

        writer << '>'
        writer.println()

        if (noSelection) {
                    renderNoSelectionOption(noSelection.key, noSelection.value, value)
            writer.println()
        }

        // create options from list
        if(from) {
            from.eachWithIndex { el,i ->
                def keyValue = null
                writer << '<option '
                if(keys) {
                    keyValue = keys[i]
                    writeValueAndCheckIfSelected(keyValue, value, writer)
                }
                else if(optionKey) {
                    if(optionKey instanceof Closure) {
                        keyValue = optionKey(el)
                    }
                    else if(el !=null && optionKey == 'id' && grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE, el.getClass().name)) {
                        keyValue = el.ident()
                    }
                    else {
                        keyValue = el[optionKey]
                    }
                    writeValueAndCheckIfSelected(keyValue, value, writer)
                }
                else {
                        keyValue = el
                    writeValueAndCheckIfSelected(keyValue, value, writer)
                }
                writer << '>'
                if(optionValue) {
                    if(optionValue instanceof Closure) {
                         writer << optionValue(el).toString().encodeAsHTML()
                    }
                    else {
                        writer << el[optionValue].toString().encodeAsHTML()
                    }
                }
                else if(valueMessagePrefix) {
                        def message = messageSource.getMessage("${valueMessagePrefix}.${keyValue}", null, null, locale)
                        if(message != null) {
                                writer << message.encodeAsHTML()
                        }
                        else if (keyValue) {
                                writer << keyValue.encodeAsHTML()
                        }
                                        else {
                            def s = el.toString()
                        if(s) writer << s.encodeAsHTML()
                        }
                }
                else {
                    def s = el.toString()
                    if(s) writer << s.encodeAsHTML()
                }
                writer << '</option>'
                writer.println()
            }
        }
        // close tag
        writer << '</select>'
    }

    private writeValueAndCheckIfSelected(keyValue, value, writer){
        writer << "value=\"${keyValue}\" "
        if(keyValue?.toString() == value?.toString()) {
            writer << 'selected="selected" '
        }
        if(value?.contains(keyValue?.toString())) {
            writer << 'selected="selected" '
        }
    }

    /**
     * Dump out attributes in HTML compliant fashion
     */
    void outputAttributes(attrs)
    {
        attrs.remove( 'tagName') // Just in case one is left
        attrs.each { k,v ->
            out << k << "=\"" << v.encodeAsHTML() << "\" "
        }
    }

}