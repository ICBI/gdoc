<html>
    <head>
        <meta name="layout" content="report" />
        <title>Analysis Results</title>      
<g:javascript library="jquery"/>   

    </head>
    <body>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'jquery.contextmenu.css')}"/>
	<jq:plugin name="ui"/>
	<jq:plugin name="styledButton"/>
	<jq:plugin name="contextmenu"/>
	<g:javascript src="geneLink.js"/>
	
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
			'dropdown' : { 'element' : 'ul' },
			// action can be specified as a single function to be fired on any click event
			'role' : 'select', // one of {button, checkbox, select}, default is button. Checkbox/select change some other defaults
			'defaultValue' : "foobar", // default value for select, doubles as default for checkboxValue.on if checkbox, default is empty
			'name' : 'testSelect', // name to use for hidden input field for checkbox and select so form can submit values
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
	<jq:plugin name="jqgrid"/>
	<g:javascript>
		var selectedIds = [];
		var selectAll = false;
		var currPage = 1;
		var kmData = ${session.endpoints};
		$(document).ready(function(){
			getCleanedTags();
			jQuery("#searchResults").jqGrid({ 
				url:'<%= createLink(action:"results",controller:"analysis") %>', 
				datatype: "json", 
				colNames:${session.columnNames}, 
				colModel:${session.columnJson}, 
				height: 350, 
				rowNum:50, 
				rowList:[25,50], 
				pager: jQuery('#pager'), 
				sortname: 'pvalue', 
				viewrecords: true, 
				sortorder: "desc", 
				multiselect: true, 
				caption: "Analysis Results",
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
									if(${session.analysis?.tags?.contains(DataType.MICRORNA.tag())}){
										$('a[href*="geneLink"]').geneLink({'menuType': 'microrna','advancedMenu': false, 'kmData': kmData, 'spinner': $('#GEspinner')});
									} else if(${session.analysis?.tags?.contains(DataType.COPY_NUMBER.tag())}){
										$('a[href*="geneLink"]').geneLink({'menuType': 'copynumber','advancedMenu': false, 'kmData': kmData, 'spinner': $('#GEspinner')});
									} else {
										$('a[href*="geneLink"]').geneLink({'advancedMenu': true, 'kmData': kmData, 'spinner': $('#GEspinner')});
									}
				},
				onSortCol: function() {
									selectAll = false;
									selectedIds = [];
				}
			});
			jQuery("#searchResults").jqGrid('navGrid','#pager',{add:false,edit:false,del:false,search:false, refresh: false});
			jQuery("#searchResults").jqGrid('navButtonAdd','#pager',{
			       caption:"Export results", 
			       onClickButton : function () { 
				       $('#download').submit();
			       } 
			});
			jQuery("#listAdd li").click( function() { 			
				var s; 
				var author = '${session.userId}'
				s = jQuery("#searchResults").getGridParam('selarrrow'); 
				if(s.length == 0) {
					jQuery('#message').html("No IDs selected.")
					jQuery('#message').css("display","block");
					window.setTimeout(function() {
					  jQuery('#message').empty().hide();
					}, 10000);
				} else {
					var tags = new Array();
					tags.push("analysis");	
					var selectedItem = this.title;
					tags.push(selectedItem);
					var typetags = '${session.analysis?.tags?.minus('_temporary')}';
					typetags = typetags.replace("[", "");
					typetags = typetags.replace("]", "");
					var dataType = typetags;
					var dataTypes = dataType.split(",");
					for(var i = 0; i < dataTypes.length; i++) {
						tags.push(dataTypes[i]);
					}
					var listName = jQuery('#list_name').val();
					listName = encodeURIComponent(listName);
					${remoteFunction(action:'saveFromQuery',controller:'userList', update:'message', onLoading:'showSaveSpinner(true)', onComplete: 'showSaveSpinner(false)', params:'\'ids=\'+ s+\'&author.username=\'+author+\'&tags=\'+tags+\'&selectAll=\'+ selectAll+\'&name=\'+listName')}
				}
				
			
			}); 
			jQuery('#reporterForm').submit(function(){
				var selected = jQuery("#searchResults").getGridParam('selarrrow'); 
				if(selected.length == 0) {
					jQuery('#message').html("No IDs selected.")
					jQuery('#message').css("display","block");
					window.setTimeout(function() {
					  jQuery('#message').empty().hide();
					}, 10000);
					return false;
				} else {
					$('#search').attr("disabled", "true");
					jQuery('#selectAll').val(selectAll);
					jQuery('#reporterIds').val(selected);
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
			}, 10000);
		}
		
		function getCleanedTags(){
			var tags = '${session.analysis?.tags}';
			tags = tags.replace("[", "");
			tags = tags.replace("]", "");
			jQuery('#repdataSetType').val(tags);
			return tags;
		}
		
	</g:javascript>
	<br/>
	
	<p style="font-size:14pt">Analysis Results</p>
	<p style="font-size:12pt">Current Study: 
	<span id="label" style="display:inline-table">
		<g:if test="${!session.study}">no study currently selected</g:if>
		${session.study?.shortName}
	</span>
	</p>
	<div id="centerContent">
		<br/>
			<g:render template="analysis_details" bean="${session.analysis}" />
			<br/>
			<g:if test="${!session.results || (session.results.resultEntries.size() == 0)}">
				<br /><br />
				No results found for this analysis. <br /><br />
			</g:if>
			<g:else>
				
				<g:if test="${session.userId}">

					<div style="margin:5px 5px 5px 50px;">
						<span style="vertical-align:5px"> <label for="list_name">List Name:</label>
							<g:textField name="list_name" size="15" maxlength="15"/>
						</span>
						<span class="bla" id="listAdd">Save Selected ⇣
								<ul>
									<li title="reporter">Reporters</li>
									<g:if test="${session.analysis?.tags?.contains(DataType.GENE_EXPRESSION.tag())}">
										<li title="gene">Gene Symbols</li>
									</g:if>
								</ul>
							</span>
							</div>	
							<br/>
							
							<g:form name="reporterForm" action="drawHeatMap" controller="heatMap">
								<g:hiddenField name="groups" value="${session.analysis.query.baselineGroup + ',' + session.analysis.query.groups}"/>
								<g:hiddenField name="dataFile" value="${session.analysis.query.dataFile}" />
								<g:hiddenField name="reporterIds" />
								<g:hiddenField name="fromComparison" value="true"/>
								<g:hiddenField name="selectAll" value="false"/>
								<g:hiddenField name="study" value="${session.analysis.studySchemas()[0]}"/>
 								<g:hiddenField id="repdataSetType" name="dataSetType" />
								<g:submitButton name="search" value="View HeatMap for selected reporters" />
							</g:form>
							<g:form name="geneExpressionKm" action="submitGEPlot" controller="km">
								<g:hiddenField name="groups" value="ALL"/>
								<g:hiddenField name="geneName" class="geneName"/>
								<g:hiddenField name="endpoint"/>
								<g:hiddenField name="dataFile" value="${session.analysis.query.dataFile}" />
								<g:hiddenField name="study" value="${session.analysis.studySchemas()[0]}"/>
							</g:form>	
							<g:form name="geneExpression" action="search" controller="geneExpression">
							
								<g:hiddenField name="groups" value="${session.analysis.query.baselineGroup + ',' + session.analysis.query.groups}"/>
								<g:hiddenField name="geneName" class="geneName"/>
								<g:hiddenField name="dataFile" value="${session.analysis.query.dataFile}" />
								<g:hiddenField name="study" value="${session.analysis.studySchemas()[0]}"/>
							</g:form>
							<g:form name="download" action="download">
							</g:form>
							<br/>
							
						<span id="message" style="display:none">
							
						</span>
						<g:if test="${flash.reporterError}">
							<span id="message" class="message">	${flash.reporterError}</span><br/>
						</g:if>
						<span id="saveSpinner" style="visibility:hidden"><img src='/gdoc/images/spinner.gif' alt='Wait'/></span>
						<span id="GEspinner" style="visibility:hidden"><img src='/gdoc/images/spinner.gif' alt='Wait'/>  Submitting analysis.  Please wait....<br/><br/></span>
				</g:if>
				<table id="searchResults" class="scroll" cellpadding="0" cellspacing="0" style="position:absolute; z-index: 1000;"></table>
				<div id="pager" class="scroll" style="text-align:center;height: 45px"></div>
			</g:else>
			<br/>
			<br/>
	</div>
	</body>
	
</hmtl>