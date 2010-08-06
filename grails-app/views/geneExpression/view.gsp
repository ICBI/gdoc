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
		<p style="font-size:12pt">Current Study: 
		<span id="label" style="display:inline-table">
			<g:if test="${!session.study}">no study currently selected</g:if>
			${session.study?.shortName}
		</span>
		</p>
		<br/>
		<g:render template="/analysis/analysis_details" bean="${session.results}" />
		<br/>
		<br/>
		<g:flex component="GeneExpressionPlot" width="750px" height="550px" />
	</div>
	</body>
	
</hmtl>