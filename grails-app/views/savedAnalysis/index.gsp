
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<title>Saved Analysis</title>
	<g:javascript library="jquery"/>
	<script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'thickbox-compressed.js')}"></script>
	<script type="text/javascript">
		function toggle(element){
			$('#'+element+'_content').slideToggle();
			$('.'+element+'_toggle').toggle();
		}
		</script>
	
</head>
<body>
	<div class="body">
		<p class="pageHeading">Saved Analysis</p><br />

		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
	
<g:panel title="My Saved Analysis" styleClass="welcome" contentClass="myPanelContent" id="savedAnalysis">
	<div id="analysisContainer">
	<g:render template="/savedAnalysis/savedAnalysisTable" />
	</div>
</g:panel>

	</div>

</body>