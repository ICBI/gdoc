<g:javascript library="jquery"/>

<g:each in="${session.notifications}" var="notification">
<div class="notificationContainer" style="height: 10px">
	<g:def var="queryDate" value="${new java.sql.Date(notification.item.taskId.substring(notification.item.taskId.indexOf('_') + 1).toLong())}" />
	<g:if test="${notification.status == 'Complete'}">
		<div style="float: left;">
			<g:if test="${notification.type == AnalysisType.CLASS_COMPARISON}">
				<g:link controller="analysis" action="view" id="${notification.item.taskId}">${notification.item.taskId.substring(0, notification.item.taskId.indexOf('_'))}</g:link> (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) 
			</g:if>
			<g:else>
				<g:link controller="geneExpression" action="view" id="${notification.item.taskId}">${notification.item.taskId.substring(0, notification.item.taskId.indexOf('_'))}</g:link> (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) 
			</g:else>
			<a href="javascript:void(0)" >
			<img alt="Share with Collaboration Group" title="Share with Collaboration Group" style="height: 18px;  vertical-align: bottom;" src="${createLinkTo(dir: 'images', file: 'share.png')}" /></a>
			<a href="javascript:void(0)" onclick="if(confirm('Are you sure?')){${remoteFunction(action:'delete', id:notification.item.taskId, update:'notifications_content')}return false;}">
			<img alt="Delete Analysis" title="Delete Analysis" style="vertical-align: bottom;" src="${createLinkTo(dir: 'images', file: 'cross.png')}"/></a>		
		</div>
		<div class="status" style="float: right; padding-top: 5px;">
			${notification.status}
		</div>
	</g:if>
	<g:elseif test="${notification.status == 'Error'}">
		<div style="float: left;">
			${notification.item.taskId.substring(0, notification.item.taskId.indexOf('_'))} (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) 
			<a href="javascript:void(0)" onclick="if(confirm('Are you sure?')){${remoteFunction(action:'delete', id:notification.item.taskId, update:'notifications_content')}return false;}">
			<img alt="Delete Analysis" title="Delete Analysis" style="vertical-align: bottom;" src="${createLinkTo(dir: 'images', file: 'cross.png')}"/></a>		
		</div>
		<div class="status" style="float: right;">${notification.status}</div>
	</g:elseif>		
	<g:else>
		<div style="float: left;">
			${notification.item.taskId.substring(0, notification.item.taskId.indexOf('_'))} (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) 
			<a href="javascript:void(0)" onclick="if(confirm('Are you sure?')){${remoteFunction(action:'delete', id:notification.item.taskId, update:'notifications_content')}return false;}">
			<img alt="Delete Analysis" title="Delete Analysis" style="vertical-align: bottom;" src="${createLinkTo(dir: 'images', file: 'cross.png')}"/></a>		
		</div>
		<div class="status" style="float: right;">${notification.status} <img style="height: 12px" src="${createLinkTo(dir:'images',file:'indicator.gif')}" border="0" />
		</div>
	</g:else>
	</div>
	<br/>
</g:each>