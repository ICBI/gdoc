<div class="notificationContainer" style="height: 10px">
<g:if test="${notification.analysis.status == 'Complete'}">
	<div style="float: left;">
		<g:if test="${notification.type == AnalysisType.CLASS_COMPARISON}">
			<g:link controller="analysis" action="view" id="${notification.analysis.item.taskId}">${notification.analysis.item.taskId.substring(0, notification.analysis.item.taskId.indexOf('_'))}</g:link> (<g:formatDate date="${notification.dateCreated}" format="h:mm M/dd/yyyy"/> ) 
		</g:if>
		<g:else>
			<g:link controller="geneExpression" action="view" id="${notification.analysis.item.taskId}">${notification.analysis.item.taskId.substring(0, notification.analysis.item.taskId.indexOf('_'))}</g:link> (<g:formatDate date="${notification.dateCreated}" format="h:mm M/dd/yyyy"/> ) 
		</g:else>
	</div>
	<div class="status" style="float: right; ">
		${notification.analysis.status}
	</div>
</g:if>
<g:elseif test="${notification.analysis.status == 'Error'}">
	<div style="float: left;">
		${notification.analysis.item.taskId.substring(0, notification.analysis.item.taskId.indexOf('_'))} (<g:formatDate date="${notification.dateCreated}" format="h:mm M/dd/yyyy"/> ) 
	</div>
	<div class="status" style="float: right;">${notification.analysis.status}</div>
</g:elseif>		
<g:else>
	<div style="float: left;">
		${notification.analysis.item.taskId.substring(0, notification.analysis.item.taskId.indexOf('_'))} (<g:formatDate date="${notification.dateCreated}" format="h:mm M/dd/yyyy"/> ) 
	</div>
	<div class="status" style="float: right;">${notification.analysis.status} <img style="height: 12px" src="${createLinkTo(dir:'images',file:'indicator.gif')}" border="0" />
	</div>
</g:else>
</div>
<br/>