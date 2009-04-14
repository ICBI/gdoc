<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<title>Notifications</title>         
</head>
<body>
	<p style="font-size:14pt">Notifications</p>
	<br/>
	<g:panel title="My Notifications" styleClass="notifications" contentClass="myPanelContent">
	
	<g:each in="${session.notifications}" var="notification">
	<g:def var="queryDate" value="${new java.sql.Date(notification.value.item.taskId.substring(notification.value.item.taskId.indexOf('_') + 1).toLong())}" />
		<g:if test="${notification.value.status == 'Complete'}">
			<div style="float: left;"><g:link controller="analysis" action="view" id="${notification.value.item.taskId}">${notification.value.item.taskId.substring(0, notification.value.item.taskId.indexOf('_'))}</g:link> (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) </div>
			<div style="float: right;">${notification.value.status}</div>
		</g:if>
		<g:elseif test="${notification.value.status == 'Error'}">
			<div style="float: left;">${notification.value.item.taskId.substring(0, notification.value.item.taskId.indexOf('_'))} (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) </div>
			<div style="float: right;">${notification.value.status}</div>
		</g:elseif>		
		<g:else>
			<div style="float: left;">${notification.value.item.taskId.substring(0, notification.value.item.taskId.indexOf('_'))} (<g:formatDate date="${queryDate}" format="h:mm M/dd/yyyy"/> ) </div>
			<div style="float: right;">${notification.value.status} <img style="height: 12px" src="${createLinkTo(dir:'images',file:'indicator.gif')}" border="0" />
			</div>
		</g:else>
		<br/>
	</g:each>
	</g:panel>

</body>

</hmtl>