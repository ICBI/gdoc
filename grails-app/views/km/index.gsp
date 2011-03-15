<html>
    <head>
        <meta name="layout" content="main" />

	<g:javascript library="jquery"/>
        <title>Create KM Plot</title>         
    </head>
    <body>
				
	<p style="font-size:14pt">Create KM Plot </p>
		
		<div id="studyPicker">
			<g:render template="/studyDataSource/studyPicker" plugin="gcore"/>
		</div>

		<div id="searchDiv">
			<g:render template="/km/studyForm"/>
		</div>
		
	</body>
	
</hmtl>