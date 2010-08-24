<html>
    <head>
        <meta name="layout" content="main" />
        <title>G-DOC HeatMap Viewer</title>  
       <g:javascript library="jquery" />
		<jq:plugin name="livequery"/>

    </head>
    <body>
	<p style="font-size:14pt">Heatmap Viewer: Search Gene Expression Data</p>
	
	<div id="studyPicker">
		<g:render template="/studyDataSource/studyPicker"/>
	</div>

	<div id="searchDiv">
		<g:render template="/heatMap/studyForm"/>
	</div>
	
	</body>
	
</html>