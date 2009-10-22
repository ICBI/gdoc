<strong>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="listsMain" />
	<title>User Lists</title>
	<g:javascript library="jquery"/>
	<jq:plugin name="ui"/>
	<jq:plugin name="styledButton"/>
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
		
		
		 // this is unfortunately needed due to a race condition in safari
		 // limit the selector to only what you know will be buttons :)
		$("span.controlBarUpload").css({
			 'padding' : '3px 20px',
			 'font-size' : '13px'
		});

		$("span.controlBarUpload").styledButton({
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
	
	
	</g:javascript>
	

	<div class="body">
		<p class="pageHeading">
			Manage Lists 
			<g:if test="${session.listFilter}">
				<span style="font-size:12px">&nbsp;&nbsp; |time period: ${session.listFilter} day(s)|</span>
			</g:if>
			<br/>
			<span id="message" class="message" style="display:none"></span>
			<table style="margin:5px 5px 5px 5px;position:relative;left:275px;"><tr><td>
			<span class="controlBarUpload" id="controlBarUpload">
			<g:link class="thickbox" name="Upload custom list" action="upload" style="font-size: 12px;color:black;text-decoration:none"
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