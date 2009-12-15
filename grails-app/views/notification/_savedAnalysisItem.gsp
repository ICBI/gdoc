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
	<div class="status" style="float: right;">${notification.status}</div>
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