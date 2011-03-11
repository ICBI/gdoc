<g:javascript library="jquery"/>

<g:if test="${principal}">
	<g:set var="uiId" value="${evidence.id+'_principal'}" />
</g:if>
<g:else>
	<g:set var="uiId" value="${evidence.id}" />
</g:else>

<g:if test="${evidence.url}">
 	<div style="float:middle"><b>Literature/Article:</b>
	<span><a href="${evidence.url}" target="_blank">${evidence.url}</a></span>
	</div>
</g:if>

<g:if test="${evidence.userList}">
	<div>
	<span> 
	<%--g:if test="${(session.sharedListIds.contains(evidence.userList.id)) || evidence.userList.author.username == session.userId}"--%>
		<g:set var="listItems" value="${evidence.userList.listItems.collect{it.value}}" />
		<div style="float:middle"><b>User List:</b>
		<a href="#" onclick="toggle('${uiId}');return false;" style="cursor: pointer;">${evidence.userList.name}
		<img class="${uiId}_toggle"src="${createLinkTo(dir: 'images', file: 'expand.gif')}" />
		<img class="${uiId}_toggle" src="${createLinkTo(dir: 'images', file: 'collapse.gif')}"
		width="13"
		height="14" border="0" alt="Show/Hide" title="Show/Hide" style="display:none" />
		</a>
		</div>
		<div id="${uiId}_content" style="border:0px solid black;display:none;padding-bottom:5px">
		<span>${listItems}</span><br />
		<g:if test="${evidence.userList.tags.contains('patient')}">
			<a href="#" onclick="return false">view clinical report</a> of these patients
		</g:if>
		</div>
		
		</span>
	<%--/g:if--%>
	
	
	<span><i>*${evidence.description}</i></span>
	</div>
</g:if>
<g:if test="${evidence.savedAnalysis}">
	<div>
		<span><b>Saved Analysis</b>: 
		<%--g:if test="${(session.sharedAnalysisIds.contains(evidence.savedAnalysis.id)) || evidence.savedAnalysis.author.username == session.userId}"--%>
		<g:if test="${evidence.savedAnalysis.type == AnalysisType.CLASS_COMPARISON}">
			<g:link controller="analysis" action="view"  id="${evidence.savedAnalysis.id}">${evidence.savedAnalysis.type}</g:link>
		</g:if>
		<g:elseif test="${evidence.savedAnalysis.type == AnalysisType.GENE_EXPRESSION}">
			<g:link controller="geneExpression" action="view" id="${evidence.savedAnalysis.id}">${evidence.savedAnalysis.type}</g:link> 
		</g:elseif>
		<g:elseif test="${evidence.savedAnalysis.type == AnalysisType.KM_PLOT}">
			<g:link controller="km" action="repopulateKM" id="${evidence.savedAnalysis.id}">${evidence.savedAnalysis.type}</g:link> 
		</g:elseif>
		<g:elseif test="${evidence.savedAnalysis.type == AnalysisType.KM_GENE_EXPRESSION}">
				<g:link controller="km" action="repopulateKM" id="${evidence.savedAnalysis.id}">${evidence.savedAnalysis.type}</g:link> 
		</g:elseif>
		<g:elseif test="${evidence.savedAnalysis.type == AnalysisType.PCA}">
				<g:link controller="pca" action="view" id="${evidence.savedAnalysis.id}">${evidence.savedAnalysis.type}</g:link> 
		</g:elseif>
		<%--/g:if--%>
		
	<br /><span><i>*${evidence.description}</i></span>
	</div>
</g:if>
<g:if test="${evidence.relatedFinding}">
	<div>
	<span><b>Related Finding</b>(<g:link action="show" id="${evidence.relatedFinding.id}">view finding</g:link>): <span style="font-size:.8em">${evidence.relatedFinding.title}</span>
	<span>*${evidence.description}</span>
	</div>
</g:if>