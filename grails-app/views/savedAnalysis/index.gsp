
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
			<span style="font-size:12px">&nbsp;&nbsp; |time period: ${session.analysisFilter} day(s)|</span>
		</g:if>
		</p>
		<span style="display:none" class="ajaxController">savedAnalysis</span>	
		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
		<div style="margin:5px 5px 5px 5px;position:relative;left:400px">
			<span>
			<g:form name="filterForm" action="index">
			<g:select name="analysisFilter" 
				noSelection="${['':'Filter By Days...']}"
				value="${session.analysisFilter?:'value'}"
				from="${timePeriods}"
				optionKey="key" optionValue="value">
			</g:select>
			</g:form>
			</span>
		</div
<g:panel title="My Saved Analysis" styleClass="welcome" contentClass="myPanelContent" id="savedAnalysis">
	<div id="analysisContainer">
	<g:render template="/savedAnalysis/savedAnalysisTable" />
	</div>
</g:panel>

	</div>

</body>