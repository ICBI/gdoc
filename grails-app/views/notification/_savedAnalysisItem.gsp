<g:javascript library="jquery"/>
<g:if test="${notification.type != AnalysisType.KM_GENE_EXPRESSION && notification.type != AnalysisType.KM_PLOT}">
<div class="notificationContainer" style="height: 10px">
<g:if test="${notification.status == 'Complete'}">
	<div style="float: left;">
		<g:if test="${notification.type == AnalysisType.CLASS_COMPARISON}">
			<g:link controller="analysis" action="view" id="${notification.id}">${notification.type}</g:link> (<g:formatDate date="${notification.dateCreated}" format="h:mm M/dd/yyyy"/> ) 
		</g:if>
		<g:elseif test="${notification.type == AnalysisType.PCA}">
			<g:link controller="pca" action="view" id="${notification.id}">${notification.type}</g:link> (<g:formatDate date="${notification.dateCreated}" format="h:mm M/dd/yyyy"/> ) 
		</g:elseif>		
		<g:elseif test="${notification.type == AnalysisType.HEATMAP}">
			<g:link controller="heatMap" action="view" id="${notification.id}">${notification.type}</g:link>
			<%--APPLET CODE="edu/stanford/genetics/treeview/applet/ButtonApplet.class"
			  archive="/gdoc/applets/treeview/TreeViewApplet.jar,/gdoc/applets/treeview/nanoxml-2.2.2.jar,/gdoc/applets/treeview/Dendrogram.jar" width=75 height=20
			    alt="Your browser understands the &lt;APPLET&gt; tag but isn't running the applet, for some reason.">
			    Your browser is completely ignoring the &lt;APPLET&gt; tag!
			    <param name="cdtFile" value="${grailsApplication.config.grails.serverURL}/gdoc/heatMap/file?id=${notification.id}&name=cluster.cdt">
			    <param name="cdtName" value="${grailsApplication.config.grails.serverURL}/gdoc/heatMap/file?id=${notification.id}name=cluster">
				<param name="plugins" value="edu.stanford.genetics.treeview.plugin.dendroview.DendrogramFactory">
			  </applet--%>
			
			 (<g:formatDate date="${notification.dateCreated}" format="h:mm M/dd/yyyy"/> ) 
		</g:elseif>		
		<g:else>
			<g:link controller="geneExpression" action="view" id="${notification.id}">${notification.type}</g:link> (<g:formatDate date="${notification.dateCreated}" format="h:mm M/dd/yyyy"/> ) 
		</g:else>
	</div>
	<div class="status" style="float: right; ">
		${notification.status}
	</div>
</g:if>
<g:elseif test="${notification.status == 'Error'}">
	<div style="float: left;">${notification.type} (<g:formatDate date="${notification.dateCreated}" format="h:mm M/dd/yyyy"/> ) 
	</div>
	<div class="status errorStatus" style="float: right;text-decoration:underline;cursor:pointer" id="${notification.id}">${notification.status.toUpperCase()}</div>
</g:elseif>		
<g:else>
	<div style="float: left;">${notification.type} (<g:formatDate date="${notification.dateCreated}" format="h:mm M/dd/yyyy"/> ) 
	</div>
	<div class="status" style="float: right;">${notification.status} <img style="height: 12px" src="${createLinkTo(dir:'images',file:'indicator.gif')}" border="0" />
	
	<a href="javascript:void(0)" onclick="if(confirm('Are you sure?')){${remoteFunction(action:'delete', id:notification.id, update:'notifications')}return false;}">
	<img alt="Delete Analysis" title="Delete Analysis" style="vertical-align: bottom;" src="${createLinkTo(dir: 'images', file: 'cross.png')}"/></a>
	</div>
</g:else>
</div>
<br/>
</g:if>