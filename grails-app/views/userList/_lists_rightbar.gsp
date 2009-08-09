<br/>
<script type="text/javascript">
	function showOption(element){
		if(element.value == 'Intersect Lists'){
			$('venn').display='none';
		}
	}
</script>
<g:javascript>
function showToolsSpinner(show) {
	if(show == true){
		$("#toolSpinner").css("visibility","visible");
	}else{
		$("#toolSpinner").css("visibility","hidden"); 
	}
}
</g:javascript>

<g:panel id="myPanel" title="List Tools" styleClass="prefs" panelColor="userLogPanelTitle" contentClass="myPanelContent">
	<g:if test="${userListInstanceList.size()>0}">
		<div>
 		<g:formRemote name="listToolForm" update="allLists" onLoading="showToolsSpinner(true)"
		    onComplete="showToolsSpinner(false)" action="tools" url="${[action:'tools']}">
		<span>List Action: 
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
		<input type="submit" value="Submit"/>
		<input type="button" value="Venn Diagram" id="venn" style="display:inline" onclick="javascript:void(0)"/><br />
		</g:formRemote>
		<span id="toolSpinner" style="visibility:hidden"><img src='/gdoc/images/spinner.gif' alt='Wait'/></span>
	 </div>
	</g:if>
</g:panel>
