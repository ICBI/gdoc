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
		var selectedIds = [];
		var selectAll = false;
		var currPage = 1;
		$(document).ready(function(){
			jQuery("#searchResults").jqGrid({ 
				url:'view', 
				datatype: "json", 
				colNames:${session.columnNames}, 
				colModel:${session.columnJson}, 
				height: 350, 
				rowNum:25, 
				rowList:[25,50], 
				//imgpath: gridimgpath, 
				pager: jQuery('#pager'), 
				sortname: 'id', 
				viewrecords: true, 
				sortorder: "desc", 
				multiselect: true, 
				caption: "Patient Search Results",
				onSelectAll: function(all, checked) {
					selectAll = checked;
					selectedIds = [];
				},
				onPaging: function(direction) {
					if(jQuery("#searchResults").getGridParam('selarrrow')) {
							selectedIds[currPage] = jQuery("#searchResults").getGridParam('selarrrow');
					}
				
					
				},
				gridComplete: function() {
					currPage = jQuery("#searchResults").getGridParam("page");
					var ids = selectedIds[currPage];
					if(selectAll) {
						selectAllItems();
					} else if(ids) {
						for(var i = 0; i < ids.length; i++) {
							jQuery("#searchResults").setSelection(ids[i]);
						}
						
						if(ids.length == jQuery("#searchResults").getGridParam("rowNum")) {
							jQuery("#cb_jqg").attr('checked', true);
						}
					}
					
				},
				onSortCol: function() {
					selectAll = false;
					selectedIds = [];
				}
			});
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
					${remoteFunction(action:'saveFromQuery',controller:'userList', update:'message', onSuccess: 'success()', params:'\'ids=\'+ s+\'&name=\'+    listName+\'&author.username=\'+author+\'&tags=\'+tags+\'&selectAll=\'+ selectAll')}
				}
			}); 
		});
		
		function selectAllItems() {
			jQuery('#searchResults tbody tr').each(function() {
				jQuery("#searchResults").setSelection(this.id);
			});
			jQuery("#cb_jqg").attr('checked', true);
		}
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