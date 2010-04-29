<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />

	<g:javascript library="jquery"/>
        <title>Create KM Plot</title>         
    </head>
    <body>
				
	<p style="font-size:14pt">Create KM Plot / <g:link controller="analysis">Other Group Analyses</g:link></p>
		
		<div id="studyPicker">
			<g:render template="/studyDataSource/studyPicker"/>
		</div>

		<div id="searchDiv">
			<g:render template="/km/studyForm"/>
		</div>
		
	</body>
	
</hmtl>