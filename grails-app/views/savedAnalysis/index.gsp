
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<title>Saved Analysis</title>
	<g:javascript library="jquery"/>
	<jq:plugin name="ui"/>
	<jq:plugin name="styledButton"/>
	<jq:plugin name="tagbox"/>
	<script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'thickbox-compressed.js')}"></script>
	<script type="text/javascript">
		function toggle(element){
			$('#'+element+'_content').slideToggle();
			$('.'+element+'_toggle').toggle();
		}
		</script>
	<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styledButton.css')}" />
</head>
<body>
	<g:javascript>
	$(document).ready( function () {
		 	$('#analysisFilter').change(function() {
				if($('#analysisFilter').val()) {
					$('#filterForm').submit();
				}
	 		});
		});

	</g:javascript>
	
	
	<div class="body">
		<p class="pageHeading">Saved Analysis
		<g:if test="${session.analysisFilter}">
			<span style="font-size:12px">total: ${allAnalysesSize}&nbsp;&nbsp; |filter: ${session.analysisFilter}|</span>
		</g:if>
		</p>
		<span style="display:none" class="ajaxController">savedAnalysis</span>	
		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
		<table style="margin:5px 5px 5px 5px;border:1px solid gray;background-color:#f2f2f2;"><tr>
			

		<td style="padding:5px 5px 5px 15px;">
			<span>
			<g:form name="filterForm" action="index">
			Filter:&nbsp;<g:select name="analysisFilter" 
				noSelection="${['':'Filter...']}"
				value="${session.analysisFilter?:'value'}"
				from="${timePeriods}"
				optionKey="key" optionValue="value">
			</g:select>
			</g:form>
			</span></td>

		<td><g:form name="delAnalysisForm" action="deleteMultipleAnalyses">
		<span class="controlBarUpload" id="controlBarDelete">
		<g:submitButton name="del" value="Delete Analyses" style="font-size: 12px;color:black;text-decoration:none;padding: 3px 8px;background-color:#E6E6E6;border: 1px solid #a0a0a0;margin: 5px 3px 3px 5px;" onclick="return confirm('Are you sure?');" />
		</span>
		</g:form></td>
		</tr>
		</table>
		<g:if test="${savedAnalysis.size() > 0}">
		<div id="pager1" style="text-align:right;padding:2px 10px 3px 0px">
		<g:set var="totalPages" value="${Math.ceil(allAnalysesSize / savedAnalysis.size())}" />

	    <g:if test="${totalPages == 1}">
	        <span class="currentStep">1</span>
	    </g:if>
	    <g:else>
	        <g:paginate controller="savedAnalysis" action="index" 
	                    total="${savedAnalysis.totalCount}" prev="&lt; previous" next="next &gt;"/>
	    </g:else>
		</div>
		
<g:panel title="My Saved Analysis" styleClass="welcome" contentClass="myPanelContent" id="savedAnalysis">
	<div id="analysisContainer">
	<g:render template="/savedAnalysis/savedAnalysisTable" />
	</div>
</g:panel>
	
	
		<div id="pager2" style="text-align:right;padding:2px 10px 3px 0px">
		<g:set var="totalPages" value="${Math.ceil(allAnalysesSize / savedAnalysis.size())}" />

	    <g:if test="${totalPages == 1}">
	        <span class="currentStep">1</span>
	    </g:if>
	    <g:else>
	        <g:paginate controller="savedAnalysis" action="index" 
	                    total="${savedAnalysis.totalCount}" prev="&lt; previous" next="next &gt;"/>
	    </g:else>
		</div>
		</g:if>
		<g:else>
			<p>Currrently, you have no saved analyses</p>
		</g:else>
	</div>

</body>