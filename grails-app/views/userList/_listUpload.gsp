<g:panel id="uploadPanel" title="Upload List" styleClass="prefs" panelColor="userLogPanelTitle" contentClass="myPanelContent">
		<div>
		<g:form name="uploadListForm" action="upload" method="post" >
		<g:select name="listAction" from="${['Intersect Lists','Join Lists', 'Difference']}" 
				keys="${['intersect','join','diff']}" onchange="showOption(this)" /><br /><br />
		    <input type="file" name="file" size="12"/>
		<br /><br />
		<input type="submit" value="Submit" />
		</g:form>
	 </div>
</g:panel>
