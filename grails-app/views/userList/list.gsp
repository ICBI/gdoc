<strong>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="listsMain" />
	<title>User Lists</title>
	<g:javascript library="jquery"/>
	<script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'thickbox-compressed.js')}"></script>
	<script type="text/javascript">
		function toggle(element){
			$('#'+element+'_content').slideToggle();
			$('.'+element+'_toggle').toggle();
		}
		</script>
		
</head>
<body>
	<div class="body">
		<p class="pageHeading">
			Manage Lists
			<br/>
			<g:link class="thickbox" name="Upload custom list" action="upload" style="font-size: 12px;"
	params="[keepThis:'true',TB_iframe:'true',height:'350',width:'400',title:'someTitle']">Upload List</g:link>
		</p><br />
	
	<div class="list" id="allLists">
		<g:render template="/userList/userListTable" model="${['userListInstanceList':userListInstanceList]}"/>
	</div>
	


</div>
</body>
</html>
</strong>