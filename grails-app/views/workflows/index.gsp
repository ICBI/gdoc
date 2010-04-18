<html>
    <head>
        <title>Georgetown Database of Cancer</title>
		<meta name="layout" content="workflowsLayout" />
		<g:javascript library="jquery"/>
		<g:javascript src="jquery/scrollTable/scrolltable.js"/>
		<g:javascript src="jquery/scrollTable/jscrolltable.js"/>
		<!-- styling -->
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'scrollable-navig.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'dialog.css')}"/>
		
    </head>
    <body>
				<jq:plugin name="ui"/>
				<jq:plugin name="autocomplete"/>
				<g:javascript>



				  $(document).ready(function(){
				   	$("#q").autocomplete("/gdoc/search/relevantTerms",{
							max: 130,
							scroll: true,
							multiple:false,
							matchContains: true,
							dataType:'json',
							parse: function(data){
								var array = jQuery.makeArray(data);
								for(var i=0;i<data.length;i++) {
				 					var tempValue = data[i];
									var tempResult = data[i];
									array[i] = { data:data[i], value: tempValue, result: tempResult};
							    }
								return array;
							},
				            formatItem: function(data, i, max) {
										return data;
									},

							formatResult: function(data) {
										return data;
									}
					});


				  });


				</g:javascript>
				<div style="width:75%;">
			
				
				<g:if test="${flash.message}">
					<div class="message" style="width:75%">${flash.message}</div>
				</g:if>
				
				
				<div id="centerContent">
						<div style="text-align:center;margin-top:35px;margin-bottom:25px">
							<g:form autocomplete="off" controller="search" action="index">

							 <input name="q" id="q" type="text" value="" size="35"></input>

							<input type="submit" value="search gdoc" />
							</g:form>
							<span style="font-size:.8em;margin-top:8px;">(enter genes, proteins, cancer sites, studies, investigators...)</span>
						</div>
						<br/>
						
						<!-- root element for scrollable -->
				</div>
				
				<table style="width:100%;">
					<tr>
						<td valign="top" style="padding:8px;width:33%">
				<div class="dialog">
				 <div class="content">
				  <div class="t"></div>
				  <!-- Your content goes here -->
				  <h1 style="font-size:1.2em;border-bottom:2px solid white">Search</h1><br />
					<a style="color:#fff;padding:5px" href='#'>Findings</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Biospecimens</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Clinical Data</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Browse Genome</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Molecular Profiling Data</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Genes</a> <br /><br />
					<a style="color:#fff;padding:5px" href='#'>Compounds/Drug Targets</a> <br />
				 </div>
				 <div class="b"><div></div></div>
				
				
				</div>		
					</td>
					<td valign="top" style="padding:8px;width:33%">
			<div class="dialog3">
			 <div class="content">
			  <div class="t"></div>
			  <!-- Your content goes here -->
			  	<h1 style="font-size:1.2em;border-bottom:2px solid white">Analyze</h1><br />
					<a style="color:#fff;padding:5px" href='#'>Basic Stats</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Group Comparison</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Classification</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Correlations/Multi Omics</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Pathways/Networks</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Molecular Docking</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Gene Pattern</a><br />
			 </div>
			 <div class="b"><div></div></div>
			</div>		
				</td>
				<td valign="top" style="padding:8px;width:34%">
		<div class="dialog2">
		 <div class="content">
		  <div class="t"></div>
		  <!-- Your content goes here -->
		  	<h1 style="font-size:1.2em;border-bottom:2px solid white">My G-DOC</h1><br />
				<g:link controller="notification" style="color:#fff;padding:5px">Notifications</g:link><br /><br />
				<g:link name="View My Saved Lists" controller="userList" style="color:#fff;padding:5px">Saved Lists</g:link><br /><br />
				<g:link name="View My Saved Analysis" controller="savedAnalysis" style="color:#fff;padding:5px">Saved Analysis</g:link><br /><br />
				<g:link name="Collaboration Groups" controller="collaborationGroups" style="color:#fff;padding:5px">Collaboration Groups</g:link>
					<ul style="margin-left:15px"><br />
						<li>-<g:link controller="collaborationGroups" style="color:#fff;padding:5px">Manage my groups</g:link></li><br />
						<li>-<g:link controller="collaborationGroups" params="[requestGroupAccess:true]" style="color:#fff;padding:5px">Request Access</g:link></li>
					</ul>
				<br />
				<br />
				
		 </div>
		 <div class="b"><div></div></div>
		</div>		
			</td>
					</tr>
					
					</table>	
					<br/>
					</div>
    </body>
</html>