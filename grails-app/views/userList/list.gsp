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
	<script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'jquery.editableText.js')}"></script>
	
	
	<script type="text/javascript">
		function toggle(element){
			$('#'+element+'_content').slideToggle();
			$('.'+element+'_toggle').toggle();
		}
		function makeEditable(list){
		var listTitle = $('#'+list+"_title");
		var listEdit = 	listTitle.find('.edit');
		listEdit.click();
		var listEditToolbar = listTitle.find('.editableToolbar');
		listEditToolbar.children().css("width","16px");
		$('.'+list+"_name").focus();
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
				
				$("[class*='_name']").each(function(index){
					$(this).editableText({
					          // default value
					          newlinesEnabled: false

					});
				});
				
				$("[class*='_name']").change(function(){
				         var newValue = $(this).html();
						 var id = $(this).attr("id").split("_name")[0];
				         // do something
				         // For example, you could place an AJAX call here:
				        $.ajax({
				          type: "POST",
				          url: "/gdoc/userList/renameList",
				          data: "newNameValue=" + newValue + "&id=" + id,
				          success: function(msg){
				            $('.message').html(msg);
							$('.message').css("display","block");
							if(msg.indexOf("updated")!=-1){
								$("#userListIds option[value='"+ id +  "']").text(newValue);
								$('.editableToolbar').children().css("width","0px");
							}else{
								makeEditable(id);
							}
							window.setTimeout(function() {
							  $('.message').remove();
							}, 1500);
				          }
				       });
				   });
				
			
	} );
	
		
	
	
	</g:javascript>
	

	<div class="body">
		<p class="pageHeading">
			Manage Lists  
			<span style="display:none" class="ajaxController">userList</span>	
			
			<g:if test="${session.listFilter}">
				<span style="font-size:12px">size: ${userListInstanceList.size()}&nbsp;&nbsp; |time period: ${session.listFilter} day(s)|</span>
			</g:if>
			<br/>
			<span id="message" class="message" style="display:none"></span>
			<table style="margin:5px 5px 5px 5px;border:1px solid gray;background-color:#f2f2f2"><tr>
				

			<td style="padding:5px 5px 5px 15px;">
			<span>
			<g:form name="filterForm" action="list">
			Filter:&nbsp;<g:select name="listFilter" 
				noSelection="${['':'Filter By Days...']}"
				value="${session.listFilter?:'value'}"
				from="${timePeriods}"
				optionKey="key" optionValue="value">
			</g:select>
			</g:form>
			</span></td>
			
			<td>
			<span class="controlBarUpload" id="controlBarUpload">
			<g:link class="thickbox" name="Upload custom list" action="upload" 
			style="font-size: 12px;color:black;text-decoration:none;background-color:#E6E6E6;padding: 4px 13px;width:100px;border: 1px solid #a0a0a0;margin: 10px 3px 1px 3px;"
		params="[keepThis:'true',TB_iframe:'true',height:'350',width:'400',title:'someTitle']">Upload List</g:link>
			</span>
			</td>

			<td><g:form name="delListForm" action="deleteMultipleLists">
			<span class="controlBarUpload" id="controlBarDelete">
			<g:submitButton name="del" value="Delete List (s)" style="font-size: 12px;color:black;text-decoration:none;padding: 3px 8px;background-color:#E6E6E6;border: 1px solid #a0a0a0;margin: 5px 3px 3px 5px;" onclick="return confirm('Are you sure?');" /></span></td>
			</tr>
			</table>
	
	<div class="list" id="allLists">
		<g:render template="/userList/userListTable" model="${['userListInstanceList':userListInstanceList]}"/>
	</div>
	
</g:form>

</div>
</body>
</html>
</strong>