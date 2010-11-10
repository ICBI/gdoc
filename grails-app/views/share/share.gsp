

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>Share</title>
	<g:javascript library="jquery"/>
	<script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'thickbox.js')}"></script>
	<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>
	
</head>
<div class="body">
			<p class="pageHeading">
    Share ${params.name}
			</p>
			<div class="clinicalSearch">
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
			<g:if test="${group != 'PUBLIC'}">
				<input type="checkbox" name="groups" value="${group.toUpperCase()}" />${group.toUpperCase()}<br />
			</g:if>
			<g:else>
				<g:if test="${session.isGdocAdmin}">
					<input type="checkbox" name="groups" value="${group.toUpperCase}" />${group.toUpperCase()}<br />
				</g:if>
			</g:else>
		</g:each>
	  <g:hiddenField name="type" value="${params.type}" />
	  <g:hiddenField name="name" value="${params.name}" />
	  <g:hiddenField name="itemId" value="${params.id}" /><br />
	  <input type="submit" value="share" />
	</g:form>

	
	</g:if>
	</div>
 </div>
</body>
</html>
