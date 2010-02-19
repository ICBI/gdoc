<%@ page import="org.springframework.util.ClassUtils" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		</script>
        <title>G-DOC Finding ${params?.id}</title>  
 		<g:javascript library="jquery"/>
      	<jq:plugin name="tooltip"/>
		<jq:plugin name="ui"/>
		<g:javascript>
			function toggle(element){
				$('#'+element+'_content').slideToggle();
				$('.'+element+'_toggle').toggle();
			}
		</g:javascript>
    </head>
    <body>
	<p style="font-size:14pt">Finding #${params?.id}</p>
	<br/>
	
	<div id="centerContent" class="welcome">
		<g:if test="${finding}">
			<table class="viewerTable" style="width: 100%;">
				<tbody><tr>
					<td style="background-color: rgb(233, 255, 168);"><b>Title</b>:<i>${finding.title}</i></td>
				</tr>
				<g:if test="${finding.principalEvidence}">
				<tr>
					<td><b>Principal Evidence</b>:
					<g:render template="/finding/evidenceViewer" model="${['evidence':finding.principalEvidence,'principal':true]}"/></td>
				</tr>
				</g:if>
				<tr>
					<td><b>Author</b>: ${finding.author?.firstName}&nbsp;${finding.author?.lastName}</td>
				</tr>

				<tr>
					<td><b>Date posted</b>: <g:formatDate date="${finding.dateCreated}" format="M/dd/yyyy"/></td>
				</tr>


				<tr>
					<td><b>Description</b>:<br />
						${finding.description}</td>
				</tr>
				<g:if test="${finding.supportingEvidence}">
				<tr>
					<td><b>Supporting Evidence: (click to view)</b>:<br />
						<g:each in="${finding.supportingEvidence}" var="evidence">
						<br />
			<g:render template="/finding/evidenceViewer" model="${['evidence':evidence]}"/>
						</g:each>
					</td>
				</tr>
				</g:if>

				</tbody>
			</table><br>
		</g:if>
		<g:else>
			<p style="font-size:12pt">No finding found for id: ${params.id}</p>
		</g:else>
	</div>
	
	</body>
</html>