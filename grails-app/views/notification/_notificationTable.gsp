<g:javascript library="jquery"/>

<g:each in="${session.notifications}" var="notification">
<div class="notificationContainer" style="height: 10px">
	<g:def var="queryDate" value="${new java.sql.Date(notification.analysis.item.taskId.substring(notification.analysis.item.taskId.indexOf('_') + 1).toLong())}" />
	<g:if test="${notification.analysis.status == 'Complete'}">
		<div style="float: left;">
			<g:if test="${notification.type == AnalysisType.CLASS_COMPARISON}">
				<g:link controller="analysis" action="view" id="${notification.analysis.item.taskId}">${notification.analysis.item.taskId.substring(0, notification.analysis.item.taskId.indexOf('_'))}</g:link> (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) 
			</g:if>
			<g:else>
				<g:link controller="geneExpression" action="view" id="${notification.analysis.item.taskId}">${notification.analysis.item.taskId.substring(0, notification.analysis.item.taskId.indexOf('_'))}</g:link> (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) 
			</g:else>
			<a href="javascript:void(0)" >
			<img alt="Share with Collaboration Group" title="Share with Collaboration Group" style="height: 18px;  vertical-align: bottom;" onclick="${remoteFunction(action:'share', id:notification.analysis.item.taskId, update:'notifications_content')}" src="${createLinkTo(dir: 'images', file: 'share.png')}" /></a>
			<a href="javascript:void(0)" onclick="if(confirm('Are you sure?')){${remoteFunction(action:'delete', id:notification.analysis.item.taskId, update:'notifications_content')}return false;}">
			<img alt="Delete Analysis" title="Delete Analysis" style="vertical-align: bottom;" src="${createLinkTo(dir: 'images', file: 'cross.png')}"/></a>		
		</div>
		<div class="status" style="float: right; padding-top: 5px;">
			${notification.analysis.status}
		</div>
	</g:if>
	<g:elseif test="${notification.analysis.status == 'Error'}">
		<div style="float: left;">
			${notification.analysis.item.taskId.substring(0, notification.analysis.item.taskId.indexOf('_'))} (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) 
			<a href="javascript:void(0)" onclick="if(confirm('Are you sure?')){${remoteFunction(action:'delete', id:notification.analysis.item.taskId, update:'notifications_content')}return false;}">
			<img alt="Delete Analysis" title="Delete Analysis" style="vertical-align: bottom;" src="${createLinkTo(dir: 'images', file: 'cross.png')}"/></a>		
		</div>
		<div class="status" style="float: right;">${notification.analysis.status}</div>
	</g:elseif>		
	<g:else>
		<div style="float: left;">
			${notification.analysis.item.taskId.substring(0, notification.analysis.item.taskId.indexOf('_'))} (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) 
			<a href="javascript:void(0)" onclick="if(confirm('Are you sure?')){${remoteFunction(action:'delete', id:notification.analysis.item.taskId, update:'notifications_content')}return false;}">
			<img alt="Delete Analysis" title="Delete Analysis" style="vertical-align: bottom;" src="${createLinkTo(dir: 'images', file: 'cross.png')}"/></a>		
		</div>
		<div class="status" style="float: right;">${notification.analysis.status} <img style="height: 12px" src="${createLinkTo(dir:'images',file:'indicator.gif')}" border="0" />
		</div>
	</g:else>
	</div>
	<br/>
</g:each>