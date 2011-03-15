<html>
    <head>
        <meta name="layout" content="main" />
        <title>G-DOC HeatMap Viewer</title>  
       <g:javascript library="jquery" />
		<jq:plugin name="livequery"/>

    </head>
    <body>
	<p style="font-size:14pt">Heatmap Viewer: Search Gene Expression, microRNA, Copy Number, Metabolomics Data</p>
	
	<div id="studyPicker">
		<g:render template="/studyDataSource/studyPicker"  plugin="gcore"/>
	</div>

	<div id="searchDiv">
		<g:render template="/heatMap/studyForm"/>
	</div>
	
	</body>
	
</html>