<g:javascript library="jquery"/>
<script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'jquery.editableText.js')}"></script>
	<g:javascript>
	$(document).ready(function() {
		
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
					if(msg=="saved"){
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
		
	}
	</g:javascript>
	<table id="${userListInstance.id}_listItems">
		
		<g:each in="${listItems}" status="j" var="list_item">
			<tr>
				<td>
					<g:if test="${userListInstance.tags.contains('gene')}">
						<a href="http://www.genecards.org/cgi-bin/carddisp.pl?gene=${list_item.value}" target="_blank">${list_item.value}</a>
						&nbsp;&nbsp;&nbsp;
						<g:if test="${metadata}">
							<g:if test="${metadata[list_item.id]}">
								<span style="color:red;font-size:1.1em">*</span>
								<g:each in="${metadata[list_item.id]}" var="itemMetaData">
									${itemMetaData.key} found: ${itemMetaData.value}
								</g:each>
							</g:if>
						</g:if>
					</g:if>
					<g:else>
						${list_item.value}
					</g:else>
				</td>
				<g:if test="${session.userId.equals(userListInstance.author.loginName)}">
				<td><a href="javascript:void(0)" 	onclick="if(confirm('Are you sure?')){var classn ='${userListInstance.id}_toggle';${remoteFunction(action:'deleteListItem',id:list_item.id,update:userListInstance.id+'_content',onLoading:'showPageSpinner(true,classn)',onComplete:'showPageSpinner(false,classn)')}return false;}">delete</a></td>
				</g:if>
			</tr>

		</g:each>
	</table>

	<g:javascript>
	
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
							if(msg=="saved"){
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

</g:javascript>