<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="report" />
        <title>Clinical Search Results</title>         
    </head>
    <body>
	<jq:plugin name="ui"/>
	<jq:plugin name="jqgrid"/>

	<g:javascript>
		$(document).ready(function(){
			jQuery("#searchResults").jqGrid({ 
				url:'view', 
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
				caption: "Patient Search Results" }
			);
			jQuery("#listAdd").click( function() { 
				var s; 
				s = jQuery("#searchResults").getGridParam('selarrrow'); 
				alert('Added items to list: ' + s); 
			}); 
		});
	</g:javascript>
	<br/>
	<p style="font-size:14pt">Clinical Search Results</p>
	<div id="centerContent">
		<br/>
			<g:if test="${!session.results}">
				No results found.
			</g:if>
			<g:else>
				<a href="javascript:void(0)" id="listAdd">Save to List</a>
				<table id="searchResults" class="scroll" cellpadding="0" cellspacing="0"></table>
				<div id="pager" class="scroll" style="text-align:center;height: 45px"></div>
			</g:else>
			<br/>
			<br/>
	</div>
	</body>
	
</hmtl>