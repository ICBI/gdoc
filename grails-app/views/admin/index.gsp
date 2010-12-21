<html>
    <head>
        <title>GDOC - Administration</title>
		<meta name="layout" content="workflowsLayout" />
		<g:javascript library="jquery"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'scrollable-navig.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'dialog.css')}"/>
		
		<g:javascript>
			function success(){
					$("#spinner").css("visibility","hidden");
			}
			function loading(){
					$("#spinner").css("visibility","visible");
					$("#status").html("Status: loading")
			}
			 
		</g:javascript>
    </head>
    <body>
		<g:javascript src="jquery/scrollTable/scrolltable.js"/>
		<g:javascript src="jquery/scrollTable/jscrolltable.js"/>
		<jq:plugin name="ui"/>
		<jq:plugin name="autocomplete"/>
		<g:javascript>
		$(document).ready(function(){
		   	$("#q").autocomplete("/gdoc/search/userAutocomplete",{
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
		<div id="centerContent">
			<p style="font-size:14pt">GDOC Admin Panel</p><br />
			
			<g:if test="${flash.message}">
				<span class="message" style="margin-botton:10px">${flash.message}</span><br />
			</g:if>
			<br />
			<table style="border:1px solid black;width:85%">
				<tr>
					<th style="background-color:silver">Data-Loading Tasks</th>
				</tr>
				<tr>
					<td>
						<div>
						<div style="padding:7px;background-color:seashell;">
				<g:link action="reload" onclick="loading()">reload data availability</g:link><br />
						<span id="status">Status: 
							<g:if test="${loadedStudies}">${loadedStudies.size()} studies loaded</g:if>
							<g:else> there are no studies loaded at this time</g:else>
						</span>
						<span id="spinner" style="visibility:hidden;display:inline-table"><img src='/gdoc/images/spinner.gif' alt='Wait'/></span>
						</div>
						
								<div style="padding:5px;background-color:#f2f2f2;">
								<g:if test="${loadedStudies}">
								${loadedStudies}
								</g:if>
								<g:else>
								no studies loaded
								</g:else>
								</div>
						</div>
					</td>
				</tr>
				</table>
				<br />
				<table style="border:1px solid black;width:85%">
				<tr>
					<th style="background-color:silver">User Admin</th>
				</tr>
				<tr>
					<td>
						<div>
						<div style="padding:7px;background-color:seashell;">Find a user<br />
						
						</div>
						
								<div style="padding:5px;background-color:#f2f2f2;">
							     <g:form action="searchUsers" autocomplete="off">
									User Id (email OR netId): <g:textField name="userId" id="q" /><br /><br />
									<g:submitButton name="search" value="Search Users" />
								</g:form>
								</div>
						</div>
					</td>
				</tr>
			</table>
			
			<br />
			<table style="border:1px solid black;width:85%">
			<tr>
				<th style="background-color:silver">Collaboration Admin</th>
			</tr>
			<tr>
				<td>
					<div>
					<div style="padding:7px;background-color:seashell;">Groups/Invitations<br />
					
					</div>
					
							<div style="padding:5px;background-color:#f2f2f2;">
							<g:link action="searchGroups">View All Groups</g:link><br /><br />
							<g:link action="pendingInvites">Pending Invites</g:link>
							
							</div>
					</div>
				</td>
			</tr>
		</table>
			
			
		</div>
		
	</body>
	
</html>