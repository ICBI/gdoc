<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>G-DOC Gene Expression Search</title>  
       <g:javascript library="jquery" />
    </head>
    <body>
	
	<p style="font-size:14pt">Search Gene Expression Data</p>
	
	<div id="studyPicker">
		<g:render template="/studyDataSource/studyPicker"/>
	</div>

	<div id="searchDiv">
		<g:render template="/geneExpression/studyForm"/>
	</div>
	
	</body>
	
</hmtl>