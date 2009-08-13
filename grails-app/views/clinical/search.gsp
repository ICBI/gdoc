<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="report" />
        <title>Clinical Search Results</title>      
<g:javascript library="jquery"/>   
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
				rowNum:200, 
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
				var author = '${session.userId}'
				s = jQuery("#searchResults").getGridParam('selarrrow'); 
				if(s.length == 0) {
					jQuery('#message').html("No IDs selected.")
					jQuery('#message').css("display","block");
					window.setTimeout(function() {
					  jQuery('#message').remove();
					}, 2500);
				} else {
					var tags = new Array();
					tags.push("clinical");
					tags.push("patient");
					var listName = jQuery('#list_name').val();
					${remoteFunction(action:'saveFromQuery',controller:'userList', update:'message', onSuccess: 'success()', params:'\'ids=\'+ s+\'&name=\'+    listName+\'&author.username=\'+author+\'&tags=\'+tags')}
				}
			}); 
		});
		
		function success() {
			jQuery('#list_name').val("");
			jQuery('#message').css("display","block");
			window.setTimeout(function() {
			  jQuery('#message').remove();
			}, 2500);
		}
	</g:javascript>
	<br/>
	<p style="font-size:14pt">Clinical Search Results</p>
	<div id="centerContent">
		<br/>
			
			<g:if test="${!session.results}">
				No results found.
			</g:if>
			<g:else>
				<g:if test="${session.userId}">
				<div style="margin:5px 5px 5px 50px">
				<label for="list_name">List Name:</label><g:textField name="list_name" size="15" />
				<a href="javascript:void(0)" id="listAdd">Save items to List</a> | <g:navigationLink name="Saved Lists" controller="userList">Go to saved-lists page</g:navigationLink><br />
				<span id="message" class="message" style="display:none"></span>
				</div>
				</div>
				</g:if>
				<table id="searchResults" class="scroll" cellpadding="0" cellspacing="0"></table>
				<div id="pager" class="scroll" style="text-align:center;height: 45px"></div>
			</g:else>
			<br/>
			<br/>
	</div>
	</body>
	
</hmtl>