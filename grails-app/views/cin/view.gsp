<html>
    <head>
        <meta name="layout" content="report" />
        <title>CIN Results</title>  
		<g:javascript library="jquery"/>
        <jq:plugin name="ui"/>
		<g:javascript>
			function toggle(element){
				$('#'+element+'_content').slideToggle();
				$('.'+element+'_toggle').toggle();
			}
		</g:javascript>
    </head>
    <body>
	<br/>
	<p style="font-size:14pt">CIN Results</p>
	<div id="centerContent" height="800px">
		<p style="font-size:12pt">Current Study: 
		<span id="label" style="display:inline-table">
			<g:if test="${!session.study}">no study currently selected</g:if>
			${session.study?.shortName}
		</span>
		</p>
		<br/>
		
		<g:render template="/cin/analysis_details" bean="${session.analysis}" />
		
		<br/>
		<br/>
		<table >
			<tbody>			
			<tr>
				<td>
					<g:each in="['heatmap', 'chromosome_1', 'chromosome_2', 'chromosome_3', 'chromosome_4', 'chromosome_5', 'chromosome_6', 'chromosome_7', 'chromosome_8', 'chromosome_9', 'chromosome_10', 'chromosome_11', 'chromosome_12', 'chromosome_13', 'chromosome_14', 'chromosome_15', 'chromosome_16', 'chromosome_17', 'chromosome_18', 'chromosome_19', 'chromosome_20', 'chromosome_21', 'chromosome_22']" var="img">
					<br />
					<g:render template="/cin/cinViewer" model="${['img':img]}"/>
					</g:each>
				</td>
			</tr>
			</tbody>
		</table><br>
		
	</div>
	<br/>
	<br/>
	</body>
	
</hmtl>