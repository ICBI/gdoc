<html>
    <head>
        <meta name="layout" content="report" />
        <title>Clinical Search Results</title>      
<g:javascript library="jquery"/>   
    </head>
    <body>
	<jq:plugin name="ui"/>
	<jq:plugin name="jqgrid"/>
	<jq:plugin name="styledButton"/>
	<g:javascript>
	$(document).ready( function () {
		 // this is unfortunately needed due to a race condition in safari
		 // limit the selector to only what you know will be buttons :)
		$("span.bla").css({
			 'padding' : '3px 20px',
			 'font-size' : '12px'
		});
		$("span.bla").styledButton({
			'orientation' : 'alone', // one of {alone, left, center, right} default is alone
			'role' : 'button', // one of {button, checkbox, select}, default is button. Checkbox/select change some other defaults
			'defaultValue' : "foobar", // default value for select, doubles as default for checkboxValue.on if checkbox, default is empty
			'name' : 'testButton', // name to use for hidden input field for checkbox and select so form can submit values
			// enable a dropdown menu, default is none
			'clear' : true // in firefox 2 the buttons have to be floated to work properly, set this to true to have them display in a new line
		
		});
	} );
	
	function showSaveSpinner(show) {
			if(show == true){
				$("#saveSpinner").css("visibility","visible");
				success();
			}else{
				$("#saveSpinner").css("visibility","hidden");
				jQuery('#message').css("display","block");
				success(); 
			}
	}
	</g:javascript>
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
				subGrid: ${session.subgridModel != [:]},
				subGridUrl: 'biospecimen',
				subGridModel: ${session.subgridModel},
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
				var author = '${session.userId}';
				if(jQuery("#searchResults").getGridParam('selarrrow')) {
						selectedIds[currPage] = jQuery("#searchResults").getGridParam('selarrrow');
				}
				s = selectedIds;//jQuery("#searchResults").getGridParam('selarrrow'); 
				if(s.length == 0) {
					jQuery('#message').html("No IDs selected.")
					jQuery('#message').css("display","block");
					window.setTimeout(function() {
					  jQuery('#message').empty().hide();
					}, 8000);
				} else {
					var tags = new Array();
					tags.push("clinical");
					tags.push("patient");
					var listName = jQuery('#list_name').val();
					${remoteFunction(action:'saveFromQuery',controller:'userList', update:'message', onLoading:'showSaveSpinner(true)', onComplete: 'showSaveSpinner(false)', params:'\'ids=\'+ s+\'&name=\'+    listName+\'&author.username=\'+author+\'&tags=\'+tags+\'&selectAll=\'+ selectAll')}
				}
			}); 
			jQuery("#searchResults").jqGrid('navGrid','#pager',{add:false,edit:false,del:false,search:false, refresh: false});
			jQuery("#searchResults").jqGrid('navButtonAdd','#pager',{
			       caption:"Export results", 
			       onClickButton : function () { 
				       $('#download').submit();
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
			window.setTimeout(function() {
			  jQuery('#message').empty().hide();
			}, 2500);
			
		
		}
	</g:javascript>
	<br/>
	<p style="font-size:14pt">Clinical Search Results</p>
	<div id="centerContent">
		<br/>
			<p style="font-size:12pt">Current Study: 
			<span id="label" style="display:inline-table">
				<g:if test="${!session.study}">no study currently selected</g:if>
				${session.study?.shortName}
			</span>
			</p>
			<g:if test="${!session.results}">
				No results found.
			</g:if>
			<g:else>
				<g:if test="${session.userId}">
				<g:if test="${session.study}">
					<span id="Study" style="display:none">${session.study.schemaName}</span>
				</g:if>
				<g:form name="download" action="download">
				</g:form>
				<div style="margin:5px 5px 5px 50px">
					<span style="vertical-align:5px"> <label for="list_name">List Name:</label>
						<g:textField name="list_name" size="15" maxlength="15"/>
					</span>
				<span class="bla" id="listAdd">Save Selected
					</span><br />
				<span id="message" style="display:none"></span>
				<span id="saveSpinner" style="visibility:hidden"><img src='/gdoc/images/spinner.gif' alt='Wait'/></span>
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
	
</html>