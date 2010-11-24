<html>
    <head>
        <meta name="layout" content="main" />
        <title>G-DOC Chromosomal Instability Index</title>  
       <g:javascript library="jquery" />
		<jq:plugin name="livequery"/>

    </head>
    <body>
	<p style="font-size:14pt">Chromosomal Instability Index</p>
	
	<div id="studyPicker">
		<g:render template="/studyDataSource/studyPicker"/>
	</div>

	<div id="searchDiv">
		<g:render template="/cin/studyForm"/>
	</div>
	
	</body>
	
</html>