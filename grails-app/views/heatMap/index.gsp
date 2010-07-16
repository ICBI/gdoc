<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>G-DOC HeatMap Viewer</title>  
       <g:javascript library="jquery" />
		<jq:plugin name="livequery"/>

    </head>
    <body>
		<g:javascript>
		$(document).ready(function() {

			loadDataSets($('#dataSetType').val());
			jQuery('#dataSetType').livequery(function() {
				$(this).change(function() {
					loadDataSets(this.value)
				});
			});
		});

		function loadDataSets(dataType) {
			if(dataType) {
				$.ajax({
					url: "${createLink(action:'selectDataType')}",
					data: "dataType=" + dataType,
					cache: false,
					success: function(html) {
			 			$("#dataDiv").html(html);
					}
				});
			}
		}
		</g:javascript>
	<p style="font-size:14pt">Heatmap Viewer: Search Gene Expression Data</p>
	
	<div id="studyPicker">
		<g:render template="/studyDataSource/studyPicker"/>
	</div>

	<div id="searchDiv">
		<g:render template="/heatMap/studyForm"/>
	</div>
	
	</body>
	
</html>