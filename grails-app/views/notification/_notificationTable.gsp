<g:each in="${session.notifications}" var="notification">
<g:def var="queryDate" value="${new java.sql.Date(notification.item.taskId.substring(notification.item.taskId.indexOf('_') + 1).toLong())}" />
	<g:if test="${notification.status == 'Complete'}">
		<div style="float: left;"><g:link controller="analysis" action="view" id="${notification.item.taskId}">${notification.item.taskId.substring(0, notification.item.taskId.indexOf('_'))}</g:link> (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) </div>
		<div class="status" style="float: right;">${notification.status}</div>
	</g:if>
	<g:elseif test="${notification.status == 'Error'}">
		<div style="float: left;">${notification.item.taskId.substring(0, notification.item.taskId.indexOf('_'))} (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) </div>
		<div class="status" style="float: right;">${notification.status}</div>
	</g:elseif>		
	<g:else>
		<div style="float: left;">${notification.item.taskId.substring(0, notification.item.taskId.indexOf('_'))} (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) </div>
		<div class="status" style="float: right;">${notification.status} <img style="height: 12px" src="${createLinkTo(dir:'images',file:'indicator.gif')}" border="0" />
		</div>
	</g:else>
	<br/>
</g:each>