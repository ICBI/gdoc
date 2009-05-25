<g:javascript library="jquery"/>

<g:each in="${session.notifications}" var="item">
	<g:if test="${item.analysis}">
		<g:render template="savedAnalysisItem" bean="${item}" var="notification" />
	</g:if>

	<g:else>
		<g:render template="genePatternItem" bean="${item}" var="job" />
	</g:else>
</g:each>
