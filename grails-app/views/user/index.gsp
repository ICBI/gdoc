<html>
    <head>
        <title>Welcome to GDOC</title>
		<meta name="layout" content="main" />
    </head>
    <body>
				
				<br/><g:if test="${session.userId}">
				 <p style="font-size:14pt">${session.userId}'s home page</p>
				</g:if>
				
				<div id="centerContent" class="welcome">
					<g:panel id="studyPanel" title="EDIN" styleClass="welcome" collapse="true">
						EDIN Study Data
					</g:panel>
					<br/>
					<g:panel id="studyPanel2" title="FCR" styleClass="welcome" collapse="true">
						FCR Study Data
					</g:panel>
				</div>
				
			
				
						<br/>
					
					<br/>
				
				
    </body>
</html>