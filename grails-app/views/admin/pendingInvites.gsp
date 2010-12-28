<html>
    <head>
        <title>GDOC - Administration</title>
		<meta name="layout" content="report" />
		<jq:plugin name="ui"/>
		<jq:plugin name="jqgrid"/>
		<g:javascript>
			var selectedIds = [];
			var selectAll = false;
			var currPage = 1;
			$(document).ready(function(){
				
				$("#searchResults").jqGrid({ 
					url:'viewInvites', 
					datatype: "json", 
					colNames:${session.ucolumnNames}, 
					colModel:${session.ucolumnJson}, 
					height: 350, 
					rowNum:25, 
					rowList:[25,50], 
					//imgpath: gridimgpath, 
					pager: $('#pager'), 
					sortname: 'id', 
					viewrecords: true, 
					sortorder: "desc", 
					//multiselect: true, 
					caption: "User Search Results",
					onSelectAll: function(all, checked) {
						selectAll = checked;
						selectedIds = [];
					},
					onPaging: function(direction) {
						if($("#searchResults").getGridParam('selarrrow')) {
								selectedIds[currPage] = $("#searchResults").getGridParam('selarrrow');
						}


					},
					onSelectRow: function(rowid) {
							$("#userField").val(rowid);
					},
					gridComplete: function() {
						currPage = $("#searchResults").getGridParam("page");
						var ids = selectedIds[currPage];
						if(selectAll) {
							selectAllItems();
						} else if(ids) {
							for(var i = 0; i < ids.length; i++) {
								$("#searchResults").setSelection(ids[i]);
							}

							if(ids.length == $("#searchResults").getGridParam("rowNum")) {
								$("#cb_jqg").attr('checked', true);
							}
						}

					},
					onSortCol: function() {
						selectAll = false;
						selectedIds = [];
					}
				});
			});
			</g:javascript>
    </head>
    <body>
		
		<div id="centerContent">
			<p style="font-size:14pt">GDOC Admin Panel - Pending Invite Search</p><br />
			<g:if test="${!session.uresults}">
				No results found.
			</g:if>
			<g:else>
			
				<table id="searchResults" class="scroll" cellpadding="0" cellspacing="0"></table>
				<div id="pager" class="scroll" style="text-align:center;height: 45px"></div>
			</g:else>
			
			
		</div>
		
	</body>
	
</html>