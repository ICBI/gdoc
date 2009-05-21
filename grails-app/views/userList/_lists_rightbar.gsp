<br/>
<script type="text/javascript">
	function showOption(element){
		if(element.value == 'Intersect Lists'){
			$('venn').display='none';
		}
	}
</script>

<g:panel id="myPanel" title="List Tools" styleClass="prefs" panelColor="userLogPanelTitle" contentClass="myPanelContent">
	<g:if test="${userListInstanceList.size()>0}">
		<div>
		<g:form name="listToolForm" update="test"
		    action="join" url="${[action:'join']}">
		<g:select name="listAction" from="${['Intersect Lists','Join Lists', 'Difference']}" 
				keys="${['intersect','join','diff']}" onchange="showOption(this)" /><br /><br />
		<g:select name="userListId"
				  noSelection="['':'-Choose up to 3 lists-']"
				  from="${userListInstanceList}"
		          optionKey="name" 
				  optionValue="name"
				  multiple="true"
				  size="3"
				  />
		<br /><br />
		<input type="submit" value="submit" />
		<input type="button" value="Venn Diagram" id="venn" style="display:inline" onclick="javascript:void(0)"/>
		</g:form>
	 </div>
	</g:if>
</g:panel>
