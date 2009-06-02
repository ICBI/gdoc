<html>
    <head>
        <meta name="layout" content="report" />
        <title>Gene Expression Search Results</title>  
				<g:render template="/common/flex_header"/>
    </head>
    <body>
	<br/>
	<p style="font-size:14pt">Gene Expression Search Results</p>
	<div id="centerContent" height="800px">
		<br/>
		<g:render template="/analysis/analysis_details" bean="${session.results}" />
		<br/>
		<br/>
		<g:flex component="GeneExpressionPlot" width="750px" height="550px" />
	</div>
	</body>
	
</hmtl>