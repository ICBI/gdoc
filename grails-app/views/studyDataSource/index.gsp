<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		</script>
        <title>Study DataSource</title>         
    </head>
    <body>
	<p style="font-size:14pt">Study Data Sources</p>
	<div id="centerContent" class="welcome">
		<g:panel id="studyPanel" title="My Studies" styleClass="welcome" collapse="true">
			<g:each in="${myStudies}" var="study">
				${study.shortName}<br/>
			</g:each>
		</g:panel>
		<br/>
		<g:panel id="studyPanel2" title="Other Studies" styleClass="welcome" collapse="true">
			<g:each in="${otherStudies}" var="study">
				${study.shortName}<br/>
			</g:each>
		</g:panel>
	</div>
	</body>
	
</hmtl>