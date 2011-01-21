<br/>
<g:javascript library="jquery"/>
<g:javascript>
	function showOption(){
		if($("#listAction option:selected").val()=='venn'){
			$("#name").css("display","none");
		}
		else{
			$("#name").css("display","inline");
		}
	}
	function validate(){
		if(!($("#userListIds option:selected").size() > 1)){
			alert("Please select at least 2 lists");
			return false;
		}else if($("#listAction option:selected").val()=='diff'){
			if($("#userListIds option:selected").size() != 2){
				alert("Please select 2 lists to view difference");
				return false;
			}
		}else if($("#listAction option:selected").val()=='venn'){
			if($("#userListIds option:selected").size() > 4){
				alert("Please select no more than 4 lists to view Venn diagram");
				return false;
			}else{
				$("#listToolForm").submit();
			}
		}
	}

</g:javascript>

<g:panel id="myPanel" title="List Tools" styleClass="prefs" panelColor="userLogPanelTitle" contentClass="myPanelContent">
	<g:if test="${toolsLists.size()>0}">
		<div>
 		<g:form name="listToolForm" update="allLists" onLoading="showToolsSpinner(true)"
		    onComplete="showToolsSpinner(false)" action="tools" url="${[action:'tools']}" method="post">
		<span style="margin-bottom:5px;">List Action: <br />
		<g:select name="listAction" from="${['Venn Diagram','Intersect Lists','Join Lists', 'Difference']}" 
				keys="${['venn','intersect','join','diff']}" onchange="showOption(this)" /></span><br />
		<span id="name" style="display:none;margin-top:5px;">List Name: <br /><g:textField name="listName" size="17" maxlength="15"/></span><br />
		<g:select style="margin-top:5px;width:180px" name="userListIds"
				  from="${toolsLists}"
		          optionKey="id" 
				  optionValue="name"
				  multiple="true"
				  size="12"
				  />
		<br /><br />
		<g:actionSubmit value="Submit"  action="tools" onclick="return validate()"/>

		</g:form>
		<span id="toolSpinner" style="visibility:hidden"><img src='/gdoc/images/spinner.gif' alt='Wait'/></span>
	 </div>
	</g:if>
	<g:else>
	Tools options will appear when lists are available.
	</g:else>
</g:panel>
