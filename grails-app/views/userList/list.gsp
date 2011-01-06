<strong>

<html>
<head>
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
					if($('#listFilter').val() && $('#listFilter').val() == 'search') {
						$('#listFilter').val("search");
						showSearchForm(true);
					}
					if($('#listFilter').val() && $('#listFilter').val() != 'search'){
						$('#filterForm').submit();
					}
		 		});
		
				$('#filterSearchForm').submit(function() {
					if($('#searchTerm').val() == ""){
						alert("please enter a search term");
						return false;
					}else{
						return true;
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
	function showSearchForm(show){
		if(show){
			$("#searchBox").css("display","block");
		}
		else{
			$("#searchBox").css("display","none");
		}
	}
		
	
	
	</g:javascript>
	

	<div class="body">
		<p class="pageHeading">
			Manage Lists  
			
			<span style="display:none" class="ajaxController">userList</span>	
			
			<g:if test="${session.listFilter}">
				<span style="font-size:12px">total: ${allLists}&nbsp;&nbsp; </span>
			</g:if>
			<br/>
			<span id="message" class="message" style="display:none"></span>
			
			<table style="margin:5px 5px 5px 5px;border:1px solid gray;background-color:#f2f2f2"><tr>
				

			<td style="padding:5px 5px 5px 15px;">
			<span>
			<g:form name="filterForm" action="list">
			Filter:&nbsp;<g:select name="listFilter" 
				noSelection="${['':'Filter Lists...']}"
				value="${session.listFilter?:'value'}"
				from="${timePeriods}"
				optionKey="key" optionValue="value">
			</g:select>
			
			</span>
			<g:if test="${session.listFilter!='search'}">
			<div id="searchBox" style="display:none;padding-top:8px">
			</g:if>
			<g:else>
			<div id="searchBox" style="padding-top:8px">
			</g:else>
			Search by list name or list item<br />
			<g:textField name="searchTerm" id="searchTerm" size="15" />
			<g:submitButton value="search" name="searchButton"/>
			</div>
			</g:form>
			</td>
			
			<td>
			<span class="controlBarUpload" id="controlBarUpload">
			<g:link class="thickbox" name="Upload custom list" action="upload" 
			style="font-size: 12px;color:black;text-decoration:none;background-color:#E6E6E6;padding: 4px 13px;width:100px;border: 1px solid #a0a0a0;margin: 10px 3px 1px 3px;"
		params="[keepThis:'true',TB_iframe:'true',height:'350',width:'400',title:'someTitle']">Upload List</g:link>
			</span>
			</td>

			<td><g:form name="delListForm" action="deleteMultipleLists">
			<span class="controlBarUpload" id="controlBarDelete">
			<g:submitButton name="del" value="Delete List (s)" style="font-size: 12px;color:black;text-decoration:none;padding: 4px 10px;background-color:#E6E6E6;border: 1px solid #a0a0a0;margin: 5px 3px 3px 5px;" onclick="return confirm('Are you sure?');" /></span></td>
			</tr>
		</table>
			
			<g:if test="${allLists > 0 && userListInstanceList.size() >0}">
			<div id="pager1" style="text-align:right;padding:2px 10px 3px 0px">
			<g:set var="totalPages" value="${Math.ceil(allLists / userListInstanceList.size())}" />

		    <g:if test="${totalPages == 1}">
		        <span class="currentStep">1</span>
		    </g:if>
		   	<g:else>
    			<g:paginate controller="userList" action="list"
                total="${allLists}" prev="&lt; previous" next="next &gt;"/>
			</g:else>
			</div>
	
			<div class="list" id="allLists">
				<g:render template="/userList/userListTable" model="${['userListInstanceList':userListInstanceList]}"/>
			</div>
	
			<div id="pager2" style="text-align:right;padding:2px 10px 3px 0px">
				<g:set var="totalPages" value="${Math.ceil(allLists/ userListInstanceList.size())}" />

    			<g:if test="${totalPages == 1}">
        			<span class="currentStep">1</span>
    			</g:if>
    			<g:else>
        			<g:paginate controller="userList" action="list" 
                    total="${allLists}" prev="&lt; previous" next="next &gt;"/>
    			</g:else>
			</div>
			</g:if>
			<g:else>
				<p>Currrently, you have no saved lists</p>
			</g:else>
		</g:form>

</div>
</body>
</html>
</strong>