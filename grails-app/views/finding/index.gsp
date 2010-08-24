<html>
    <head>
        <meta name="layout" content="main" />
		</script>
        <title>G-DOC Findings</title> 
        
    </head>
    <body>
	<p style="font-size:14pt">Most Recent Findings</p>
	<br/>
	
	<div id="centerContent" class="welcome">
		<g:each in="${findings}" var="finding">
			<table class="viewerTable" style="width: 75%;">
				<tbody><tr>
					<td style="background-color: rgb(233, 255, 168);"><b>Title</b>:<i>${finding.title}</i></td>
				</tr>


				<tr>
					<td><b>Curator</b>: ${finding.author?.firstName}&nbsp;${finding.author?.lastName}</td>
				</tr>

				<tr>
					<td><b>Date posted</b>: <g:formatDate date="${finding.dateCreated}" format="M/dd/yyyy"/></td>
				</tr>


				<tr>
					<td><g:link action="show" id="${finding.id}">view details...</g:link></td>
				</tr>

				</tbody>
			</table><br>
		</g:each>
	</div>
	
	</body>
</html>