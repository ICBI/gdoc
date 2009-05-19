

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>Share</title>
	<g:javascript library="jquery"/>
	<script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'thickbox-compressed.js')}"></script>
	
	
</head>
<body>
	<g:if test="${params.failure}">
	<div class="errorMessage" style="color:red">${flash.message}</div><br />
	</g:if>
	<g:if test="${params.success}">
		<div class="successMessage" style="color:#007000">${flash.message}</div><br />
	</g:if>
	<g:if test="${params.id}">
	<div>Select the collaboration groups you would like to share '<span style="color:blue">${params.name}</span>' with:</div><br />
	<g:form name="shareForm" on404="alert('not found!')" update="[success:'smessage',failure:'error']" 
	    action="shareItem" url="${[controller:'share',action:'shareItem']}"
		onComplete="alert(${flash.message})">
	  	<g:each in="${session.myCollaborationGroups}" var="group">
		<input type="checkbox" name="groups" value="${group}" />${group}<br />
		</g:each>
	  <g:hiddenField name="type" value="${params.type}" />
	  <g:hiddenField name="name" value="${params.name}" />
	  <g:hiddenField name="itemId" value="${params.id}" /><br />
	  <input type="submit" value="share" />
	</g:form>

	
	</g:if>
	
</body>
</html>
