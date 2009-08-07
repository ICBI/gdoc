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
							<g:each in="${session.summary}" var="s">
								<th>${s.key}</th>
							</g:each>
						</tr>
						<tr>
							<g:each in="${session.summary}" var="i">
								<td>${i.value}</td>
							</g:each> 
						</tr>	
				</table>
		</g:panel>
		</g:if>
		<br/>
	</div>
	</body>
	
</hmtl>