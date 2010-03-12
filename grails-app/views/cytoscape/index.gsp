<% response.setContentType("application/x-java-jnlp-file"); 
  response.setHeader( "Pragma", "no-cache");
  response.setDateHeader( "Expires", 0 );%>
<%@ page import="java.io.*" %>

<g:set var="basepath" value="${request.getRequestURL().toString().split(request.getContextPath())[0]}" />
<g:set var="filepath" value="${request.getRequestURL().toString().split(request.getContextPath())[0]}" />

<?xml version="1.0" encoding="UTF-8"?>
<jnlp codebase="${basepath}/gdoc/cytoscapeJnlp">
  <security>
    <all-permissions />
  </security>
  <information>
    <title>Cytoscape Webstart</title>
    <vendor>Cytoscape Collaboration</vendor>
    <homepage href="http://cytoscape.org" />
    <offline-allowed />
  </information>
  <resources>
    <j2se version="1.5+" max-heap-size="1024M" />
    <!--All lib jars that cytoscape requires to run should be in this list-->
    <jar href="${basepath}/gdoc/cytoscapeJnlp/cytoscape.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/activation.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/biojava-1.4.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/colt.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/coltginy.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/com-nerius-math-xform.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/commons-cli-1.x-cytoscape-custom.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/concurrent.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/cytoscape-cruft-obo.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/cytoscape-geom-rtree.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/cytoscape-geom-spacial.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/cytoscape-graph-dynamic.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/cytoscape-graph-fixed.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/cytoscape-render-export.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/cytoscape-render-immed.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/cytoscape-render-stateful.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/cytoscape-task.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/cytoscape-util-intr.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/ding.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/FastInfoset.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/fing.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-export-2.1.1.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-graphics2d-2.1.1.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-graphicsio-2.1.1.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-graphicsio-java-2.1.1.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-graphicsio-ps-2.1.1.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-graphicsio-svg-2.1.1.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-io-2.0.2.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-jas-plotter-2.2.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-swing-2.0.3.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-util-2.0.2.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/freehep-xml-2.1.1.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/giny.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/glf.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/http.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/i4jruntime.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/itext-2.0.4.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jaxb-api.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jaxb-impl.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jaxws-api.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jaxws-rt.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jaxws-tools.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jdom-1.0.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jhall.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jnlp.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jsr173_1.0_api.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jsr181-api.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/jsr250-api.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/junit.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/l2fprod-common-all.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/looks-2.1.4.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/phoebe.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/piccolo.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/resolver.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/saaj-api.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/saaj-impl.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/sjsxp.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/stax-ex.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/streambuffer.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/swing-layout-1.0.3.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/swingx-2006_10_27.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/tclib.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/undo.support.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/violinstrings-1.0.2.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/lib/wizard.jar" />
    <!--These are the plugins you wish to load, edit as necessary.-->
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/AutomaticLayout.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/biopax.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/browser.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/cPath.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/cpath2.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/CytoscapeEditor.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/filter.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/filters.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/GraphMerge.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/linkout.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/ManualLayout.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/psi_mi.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/quick_find.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/SBMLReader.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/TableImport.jar" />
    <jar href="${basepath}/gdoc/cytoscapeJnlp/plugins/core/yLayouts.jar" />
  </resources>
  <!--This starts-up Cytoscape, specify your plugins to load, and other command line arguments.  Plugins not specified here will not be loaded.-->
  <application-desc main-class="cytoscape.CyMain">
    <argument>-p</argument>
    <argument>csplugins.layout.LayoutPlugin</argument>
    <argument>-p</argument>
    <argument>org.mskcc.biopax_plugin.plugin.BioPaxPlugIn</argument>
    <argument>-p</argument>
    <argument>browser.AttributeBrowserPlugin</argument>
    <argument>-p</argument>
    <argument>org.cytoscape.coreplugin.cpath.plugin.CPathPlugIn</argument>
    <argument>-p</argument>
    <argument>org.cytoscape.coreplugin.cpath2.plugin.CPathPlugIn2</argument>
    <argument>-p</argument>
    <argument>cytoscape.editor.CytoscapeEditorPlugin</argument>
    <argument>-p</argument>
    <argument>filter.cytoscape.CsFilter</argument>
    <argument>-p</argument>
    <argument>cytoscape.filters.FilterPlugin</argument>
    <argument>-p</argument>
    <argument>GraphMerge.GraphMerge</argument>
    <argument>-p</argument>
    <argument>linkout.LinkOutPlugin</argument>
    <argument>-p</argument>
    <argument>ManualLayout.ManualLayoutPlugin</argument>
    <argument>-p</argument>
    <argument>org.cytoscape.coreplugin.psi_mi.plugin.PsiMiPlugIn</argument>
    <argument>-p</argument>
    <argument>csplugins.quickfind.plugin.QuickFindPlugIn</argument>
    <argument>-p</argument>
    <argument>sbmlreader.SBMLReaderPlugin</argument>
    <argument>-p</argument>
    <argument>edu.ucsd.bioeng.coreplugin.tableImport.TableImportPlugin</argument>
    <argument>-p</argument>
    <argument>yfiles.YFilesLayoutPlugin</argument>
	<argument>-N</argument>
	<argument>${sifUrl}</argument>
	<argument>-n</argument>
	<argument>${nodeUrl}</argument>
	<argument>-e</argument>
	<argument>${edgeUrl}</argument>
  </application-desc>
</jnlp>


