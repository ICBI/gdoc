<g:javascript library="jquery"/>
	<g:javascript>
	function showToolsSpinner(show) {
		if(show == true){
			$("#toolSpinner").css("visibility","visible");
			cleanup();
		}else{
			$("#toolSpinner").css("visibility","hidden");
			cleanup(); 
		}
	}
	function showPageSpinner(show,className) {
		var pageSpinner = className+"_pageSpinner";
		if(show == true){
			$("#"+pageSpinner).css("visibility","visible");
		}else{
			$("#"+pageSpinner).css("visibility","hidden");

		}
	}
	
	function cleanup() {
				window.setTimeout(function() {
				  $('#listName').val("");
				  $("#userListIds").val(null);
				  $('.message').remove();
				}, 1500);
			}	
	function finishDelete(value){
		$("#userListIds option[value='"+ value +  "']").remove();
		cleanup();
	}
	
	</g:javascript>
	<g:javascript>
	$(document).ready( function () {
			  $("[class*='_tags']").each(function(index){
				$(this).tagbox({
								grouping: '"',
								separator:/[,]/ ,
								autocomplete:true
							});
				
			  });
			
			$("[class*='_name']").each(function(index){
				$(this).contentEditable="true";
			});
			  
			$("[class='tag']").each(function(index){
				var span = $(this).find('label').find('span');
				if(span.html() == 'clinicalM' || 
					span.html() == 'patientM' ||
						span.html() == 'reporterM' ||
						span.html() == 'geneM'){
					var newStr = $(this).find('label').find('span').html();
			        var tag = newStr.substring(0, newStr.length-1);
					$(this).find('label').find('abbr').remove();
					$(this).find('label').find('input').remove();
					$(this).find('label').find('span').remove();
					$(this).find('label').append("<div style='margin: 3px;margin-left:15px;display:inline-block';padding-top:5px>" + tag + "</div>");
				}
				//console.log($(this).find('label'));
				//console.log(span.html());
				//console.log(abbr);
			});
			
			
	} );
	
	</g:javascript>
	
	<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
	</g:if>
	
	
	<g:if test="${userListInstanceList.size()>0}">
	
	
	
 	<g:panel title="My Lists" styleClass="welcome" >
	
	<table class="listTable" width="100%" cellpadding="5">
		<tr>
			<td style="background-color:white;">
				<g:each in="${userListInstanceList}" status="i" var="userListInstance">
				
				
				
				<div id="${userListInstance.id}_div" style="collapse:true;border:0px solid red;margin-bottom:5px;padding:3px 3px 3px 3px">
					
					<g:if test="${session.userId.equals(userListInstance.author.loginName)}">
					<div style="border:0px solid black;width:2%;float:left;padding-right:10px"><g:checkBox class="the_checkbox" name="deleteList"
						 value="${userListInstance.id}" checked="false"/></div>
					</g:if>
					
					<div id="${userListInstance.id}_title" style="border:0px solid black;height: 20px;">
					<div style="border:0px solid black;width:40%;float:left">	
	<span class="${userListInstance.id}_name" id="${userListInstance.id}_name" style="font-weight:bold;padding-left:5px;padding-right:5px">${fieldValue(bean:userListInstance, field:'name')} </span>
						
							
					</div>
					<div id="${userListInstance.id}_toggleContainer" style="border:0px solid black;float:left; vertical-align: top">
						<div id="${userListInstance.id}_tog" style="border:0px solid black;height: 20px; cursor: pointer;"
						onclick="toggle('${userListInstance.id}');">
						<img class="${userListInstance.id}_toggle"src="${createLinkTo(dir: 'images', file: 'expand.gif')}"
						width="13"
						height="14" border="0" alt="Show/Hide" title="Show/Hide" 
		onclick="if($('#${userListInstance.id}_listItems').length == 0){var classn = this.className;
{${remoteFunction(action:'getListItems', id:userListInstance.id,onLoading:'showPageSpinner(true,classn)',onComplete:'showPageSpinner(false,classn)', update:userListInstance.id+'_content')}return false;}}"/>
						
						<img class="${userListInstance.id}_toggle" src="${createLinkTo(dir: 'images', file: 'collapse.gif')}"
						width="13"
						height="14" border="0" alt="Show/Hide" title="Show/Hide" style="display:none" />
						<span id="${userListInstance.id}_toggle_pageSpinner" style="visibility:hidden;display:inline"><img src='/gdoc/images/spinner.gif' alt='Wait'/></span>
						</div>
					</div>
					<span style="float:right">
						
						<g:formatDate date="${userListInstance.dateCreated}" format="h:mm M/dd/yyyy"/>
					</span>
					</div>
					<g:if test="${session.userId.equals(userListInstance.author.loginName)}">
					<div style="border:0px solid black;width:20%;float:right">	
						<a href="javascript:void(0)" style="padding-right:5px">
						<img alt="edit list" title="Rename list" src="${createLinkTo(dir: 'images', file: 'pencil.png')}" onclick="makeEditable(${userListInstance.id});" /></a>
						
						
						<g:link class="thickbox" name="Share &nbsp; ${userListInstance.name} &nbsp; with collaboration groups?" action="share" controller="share" 
params="[id:userListInstance.id,name:userListInstance.name,type:'USER_LIST',keepThis:'true',TB_iframe:'true',height:'250',width:'400',title:'someTitle']"><img alt="share list" title="Share list" style="height: 18px;padding-right:5px" src="${createLinkTo(dir: 'images', file: 'share.png')}"/></a></g:link>
						<g:link action="export" style="padding-right:5px;" id="${userListInstance.id}">
							<img alt="export list" title="Export list" src="${createLinkTo(dir: 'images', file: 'export.png')}" />
						</g:link>
						
						<g:if test="${userListInstance.tags.contains('gene')}">
						<g:link action="exportToCytoscape" style="padding-right:5px;" id="${userListInstance.id}">
							<img alt="View Cancer-Gene Index network" title="View Cancer-Gene Index network" src="${createLinkTo(dir: 'images', file: 'chart_line.png')}" />
						</g:link>
						</g:if>
		<%--a href="javascript:void(0)" onclick="if(confirm('Are you sure?')){var classn ='${userListInstance.id}_toggle';${remoteFunction(action:'deleteList',onLoading:'showPageSpinner(true,classn)',onComplete:'showPageSpinner(false,classn)', id:userListInstance.id,update:'allLists',onSuccess:'finishDelete(\''+userListInstance.id+'\')')}return false;}">
						<img alt="delete list" title="Delete list" src="${createLinkTo(dir: 'images', file: 'cross.png')}"/></a--%>
					</div>
					</g:if>
					<g:else>
					<div style="border:0px solid black;width:50%;float:right">	
						Shared by: ${userListInstance.author.firstName}&nbsp;${userListInstance.author.lastName}&nbsp;(author)
						<g:link action="export" style="padding-right:5px;" id="${userListInstance.id}">
						<img alt="export list" title="Export list" src="${createLinkTo(dir: 'images', file: 'export.png')}" />
						</g:link>
						<g:if test="${userListInstance.tags.contains('gene')}">
						<g:link action="exportToCytoscape" style="padding-right:5px;" id="${userListInstance.id}">
							<img alt="View Cancer-Gene Index network" title="View Cancer-Gene Index network" src="${createLinkTo(dir: 'images', file: 'chart_line.png')}" />
						</g:link>
						</g:if>
					</div>
					</g:else>

				
				<div id="${userListInstance.id}_content" style="border:0px solid black;display:none;padding-bottom:5px">
					<%--g:render template="/userList/userListDiv" model="${['userListInstance':userListInstance,'listItems':userListInstance.listItems]}"/--%>
				</div>
				<div style="border-bottom:1px solid grey;background-color:#f3f3f3;padding-bottom:5px;">
					Studies: 
					<g:if test="${userListInstance.studies.size()>0}">
						${userListInstance.studyNames().join(", ")}
					</g:if><br/>
					Tags:
					<g:if test="${userListInstance.tags.size()>0}">
					<input type="text" name="${userListInstance.id}_tags_name" class="${userListInstance.id}_tags tags clearfix" value="${userListInstance.tags.replace(' ',',')}">
					</input>
					</g:if>
					<g:if test="${userListInstance.tags.size()==0}">
					<input type="text" name="${userListInstance.id}_tags_name"  class="${userListInstance.id}_tags tag_box tags clearfix" />
					</g:if>
					
				</div>
		</div>
	</g:each>
</td>
</tr>
</table>
</g:panel>

<g:javascript library="jquery"/>
<jq:plugin name="tagbox"/>
<g:javascript>

 $("[class*='_tags']").each(function(index){
	$(this).tagbox({
					grouping: '"',
					separator:/[,]/ ,
					autocomplete:true
				});
	
  });
  
$("[class='tag']").each(function(index){
	var span = $(this).find('label').find('span');
	if(span.html() == 'clinicalM' || 
		span.html() == 'patientM' ||
			span.html() == 'reporterM'){
		var newStr = $(this).find('label').find('span').html();
		var tag = newStr.substring(0, newStr.length-1);
		$(this).find('label').find('abbr').remove();
		$(this).find('label').find('input').remove();
		$(this).find('label').find('span').remove();
		$(this).find('label').append("<div style='margin: 3px;margin-left:15px;display:inline-block';padding-top:5px>" + tag + "</div>");
	}
	//console.log($(this).find('label'));
	//console.log(span.html());
	//console.log(abbr);
});

</g:javascript>
</g:if>
<g:else>
<p>Currently, you have no saved lists during the time period of 
	<g:if test="${session.listFilter}">
		<span>${session.listFilter} day(s).</span><br />
		Try expanding the time period by selected the "Filter By Date" button above.
	</g:if>
	
	</p>
</g:else>
