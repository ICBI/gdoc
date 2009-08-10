<br/>
<script type="text/javascript">
	function showOption(){
		if($("#listAction option:selected").val()=='intersect'){
			$("#venn").css("display","inline");
		}else{
			$("#venn").css("display","none");
		}
	}
	function validate(){
		if($("#userListIds option:selected").size() == 0){
			alert("Please select lists");
			return false;
		}else if($("#listAction option:selected").val()=='diff'){
			if($("#userListIds option:selected").size() != 2){
				alert("Please select 2 lists to view difference");
				return false;
			}
		}
	}
</script>


<g:panel id="myPanel" title="List Tools" styleClass="prefs" panelColor="userLogPanelTitle" contentClass="myPanelContent">
	<g:if test="${userListInstanceList.size()>0}">
		<div>
			
	
 		<g:formRemote name="listToolForm" update="allLists" onLoading="showToolsSpinner(true)"
		    onComplete="showToolsSpinner(false)" action="tools" url="${[action:'tools']}">
		<span>List Action: <br />
		<g:select name="listAction" from="${['Intersect Lists','Join Lists', 'Difference']}" 
				keys="${['intersect','join','diff']}" onchange="showOption(this)" /></span><br /><br />
		<span>List Name: <g:textField name="listName" /></span><br /><br />
		<g:select name="userListIds"
				  from="${userListInstanceList}"
		          optionKey="id" 
				  optionValue="name"
				  multiple="true"
				  size="3"
				  />
		<br /><br />
		<g:actionSubmit value="Submit" onclick="return validate()"/>
		<input type="button" value="Venn Diagram" id="venn" style="display:inline" onclick="javascript:void(0)"/><br />
		</g:formRemote>
		<span id="toolSpinner" style="visibility:hidden"><img src='/gdoc/images/spinner.gif' alt='Wait'/></span>
	 </div>
	</g:if>
</g:panel>
