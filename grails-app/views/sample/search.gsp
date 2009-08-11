<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		</script>
        <title>Sample Search Results</title>         
    </head>
    <body>
	<p style="font-size:14pt">${params.id} Samples</p>
	<br/>
	${flash.error}
	<br/>
	<div id="centerContent" class="welcome">
		<g:if test="${session.summary}">
		<g:render template="/common/query_details" bean="${session.sampleQuery}" />
		<br/>
		<br/>
		<g:panel id="studyPanel" title="Sample Search Results" styleClass="welcome" collapse="true">
				<table class="studyTable" style="width: 100%">
						<tr>
							<th>Datasource</th>
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