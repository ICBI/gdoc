<html>
    <head>
        <meta name="layout" content="report" />
        <title>PCA Results</title>  
				<g:render template="/common/flex_header" plugin="gcore"/>
    </head>
    <body>
	<br/>
	<p style="font-size:14pt">PCA Results</p>
	<div id="centerContent" height="800px">
		<p style="font-size:12pt">Current Study: 
		<span id="label" style="display:inline-table">
			<g:if test="${!session.study}">no study currently selected</g:if>
			${session.study?.shortName}
		</span>
		</p>
		<br/>
		<%--
		<g:render template="/analysis/analysis_details" bean="${session.results}" />
		--%>
		<br/>
		<br/>
		<g:flex component="PCAPlot" width="620px" height="540px" flashvars="analysisId=${params.id}"/>
	</div>
	<br/>
	<br/>
	</body>
	
</hmtl>