<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="report" />
        <title>Analysis Results</title>      
<g:javascript library="jquery"/>   
    </head>
    <body>
	<jq:plugin name="ui"/>
	<jq:plugin name="jqgrid"/>
	<g:javascript>
		$(document).ready(function(){
			jQuery("#searchResults").jqGrid({ 
				url:'', 
				datatype: "json", 
				colNames:${session.columnNames}, 
				colModel:${session.columnJson}, 
				height: 350, 
				rowNum:50, 
				rowList:[25,50], 
				//imgpath: gridimgpath, 
				pager: jQuery('#pager'), 
				sortname: 'id', 
				viewrecords: true, 
				sortorder: "desc", 
				multiselect: true, 
				caption: "Analysis Results" }
			);
			jQuery("#listAdd").click( function() { 
								/*
				var s; 
				var author = '${session.userId}'
				s = jQuery("#searchResults").getGridParam('selarrrow'); 

				var tags = new Array();
				tags.push("clinical");
				tags.push("patient");
				${remoteFunction(action:'saveFromQuery',controller:'userList', update:'message', params:'\'ids=\'+ s+\'&name=\'+     document.getElementById(\'list_name\').value+\'&author.username=\'+author+\'&tags=\'+tags')}
				*/
				
			
			}); 
		
		});
	</g:javascript>
	<br/>
	<p style="font-size:14pt">Analysis Results</p>
	<div id="message"></div>
	<div id="centerContent">
		<br/>
			
			<g:if test="${!session.results}">
				No results found.
			</g:if>
			<g:else>
				<table id="searchResults" class="scroll" cellpadding="0" cellspacing="0"></table>
				<div id="pager" class="scroll" style="text-align:center;height: 45px"></div>
			</g:else>
			<br/>
			<br/>
	</div>
	</body>
	
</hmtl>