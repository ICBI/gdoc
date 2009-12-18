<html>
    <head>
        <meta name="layout" content="report" />
        <title>PCA Results</title>  
				<g:render template="/common/flex_header"/>
    </head>
    <body>
	<br/>
	<p style="font-size:14pt">PCA Results</p>
	<div id="centerContent" height="800px">
		<br/>
		<%--
		<g:render template="/analysis/analysis_details" bean="${session.results}" />
		--%>
		<br/>
		<br/>
		<g:flex component="PCAPlot" width="560px" height="540px" />
	</div>
	<br/>
	<br/>
	</body>
	
</hmtl>