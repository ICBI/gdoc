<html>
    <head>
        <meta name="layout" content="report" />
        <title>KM Plot Results</title>     
				<g:render template="/common/flex_header" plugin="gcore"/>
    </head>
    <body>
	<br/>
	<p style="font-size:14pt">KM Plot Results</p>
	<p style="font-size:12pt">Current Study: 
	<span id="label" style="display:inline-table">
		<g:if test="${!session.study}">no study currently selected</g:if>
		${session.study?.shortName}
	</span>
	</p>
	<div id="centerContent" height="800px">
		<br/>	
				<g:flex component="KMPlot" width="700px" height="660px" />
	</div>
	</body>
	
</hmtl>