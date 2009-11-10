<strong>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="listsMain" />
	<title>User Lists</title>
	<g:javascript library="jquery"/>
	<jq:plugin name="ui"/>
	<jq:plugin name="styledButton"/>
	<jq:plugin name="tagbox"/>
	<script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'thickbox-compressed.js')}"></script>
	<script type="text/javascript">
		function toggle(element){
			$('#'+element+'_content').slideToggle();
			$('.'+element+'_toggle').toggle();
		}
	</script>
	
</head>
<body>

	<g:javascript>
	$(document).ready( function () {
				$('#listFilter').change(function() {
					if($('#listFilter').val()) {
						$('#filterForm').submit();
					}
		 		});
			
	} );
	
		
	
	
	</g:javascript>
	

	<div class="body">
		<p class="pageHeading">
			Manage Lists 
			<span style="display:none" class="ajaxController">userList</span>	
			
			<g:if test="${session.listFilter}">
				<span style="font-size:12px">&nbsp;&nbsp; |time period: ${session.listFilter} day(s)|</span>
			</g:if>
			<br/>
			<span id="message" class="message" style="display:none"></span>
			<table style="margin:5px 5px 5px 5px;position:relative;left:275px;"><tr><td>
			<span class="controlBarUpload" id="controlBarUpload">
			<g:link class="thickbox" name="Upload custom list" action="upload" 
			style="font-size: 12px;color:black;text-decoration:none;padding: 3px 10px;width:100px;border: 1px solid #a0a0a0;margin: 10px 3px 1px 3px;"
	params="[keepThis:'true',TB_iframe:'true',height:'350',width:'400',title:'someTitle']">Upload List</g:link>
			</span></td><td style="padding:5px 5px 5px 15px;">
			<span>
			<g:form name="filterForm" action="list">
			<g:select name="listFilter" 
				noSelection="${['':'Filter By Days...']}"
				value="${session.listFilter?:'value'}"
				from="${timePeriods}"
				optionKey="key" optionValue="value">
			</g:select>
			</g:form>
			</span></td></tr>
		</table>
			
			<%--g:link class="thickbox" name="Upload custom list" action="upload" style="font-size: 12px;"
	params="[keepThis:'true',TB_iframe:'true',height:'350',width:'400',title:'someTitle']">Upload List</g:link>
		</p><br /--%>
	
	<div class="list" id="allLists">
		<g:render template="/userList/userListTable" model="${['userListInstanceList':userListInstanceList]}"/>
	</div>
	


</div>
</body>
</html>
</strong>