<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		</script>
        <title>Sample Summary</title>         
    </head>
    <body>
	<p style="font-size:14pt">${params.id} Samples</p>
	<br/>
	${flash.error}
	<br/>
	<div id="centerContent" class="welcome">
		<g:if test="${session.summary}">
		<g:panel id="studyPanel" title="Sample Summary" styleClass="welcome" collapse="true">
				<table class="studyTable" style="width: 100%">
						<tr>
							<th>Shared Resource</th>
							<th>Sample Count</th>
						</tr>
						<g:each in="${session.summary}" var="i">
							<tr>
								<td>${i.key}</td>
								<td>${i.value}</td>
							</tr>
						</g:each> 
				</table>
		</g:panel>
		</g:if>
		<br/>
	</div>
	</body>
	
</hmtl>