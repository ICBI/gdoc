<br/>
<g:panel id="myPanel" title="My gdoc" styleClass="prefs" panelColor="userLogPanelTitle" contentClass="myPanelContent">
	<ul>
		<li><g:link controller="notification">Notifications</g:link></li>
		<li><g:navigationLink name="Saved Lists" controller="userList">Saved Lists</g:navigationLink></li>
		<li><g:navigationLink name="Saved Analysis" controller="savedAnalysis">Saved Analysis</g:navigationLink></li>
		<li><g:navigationLink name="Collaboration Groups" controller="collaborationGroups">Collaboration Groups</g:navigationLink>
			<ul style="margin-left:15px">
				<li>-<g:link controller="collaborationGroups">Manage my groups</g:link></li>
				<li>-<g:link controller="collaborationGroups" params="[requestGroupAccess:true]">Request Access</g:link></li>
			</ul>
		</li>
		
	</ul>
</g:panel>
