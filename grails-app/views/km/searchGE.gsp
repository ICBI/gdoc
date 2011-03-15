<html>
    <head>
        <meta name="layout" content="report" />
        <title>KM Plot Results</title>     
				<g:render template="/common/flex_header" plugin="gcore"/>
					<g:javascript library="jquery" />
					<jq:plugin name="tooltip"/>
					<jq:plugin name="ui"/>
    </head>
    <body>
	<br/>
	<p style="font-size:14pt">KM Plot (Gene Expression-based) Results 
		<img class="info" title="The Kaplan Meier values are determined by performing a gene expression analysis on the gene entered and finding the reporter with the highest mean expression across all samples. When the expression for each sample (based on that reporter) is determined,  we subtract the mean expression from that actual expression value to determine the sample's fold change. Based on the fold change parameter (either default to '2' or passed as param), we group each sample into one of the 3 following classifications: 1) sample's fold change > fold change param 2) sample's fold change < negative fold change param 3) sample's fold change in between fold change param and negative fold change param (e.g. 2 > 0.56 > -2)" src="${createLinkTo(dir:'images',file:'information.png')}" border="0" /></p>
	<div id="centerContent">
		<p style="font-size:12pt">Current Study: 
		<span id="label" style="display:inline-table">
			<g:if test="${!session.study}">no study currently selected</g:if>
			${session.study?.shortName}
		</span>
		</p>
		<br/>
		<g:flex component="KMPlot" width="700px" height="1050px" />
	</div>
	</body>
	
</hmtl>