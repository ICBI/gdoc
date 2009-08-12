<g:javascript library="jquery"/>

<g:if test="${session.notifications}">
<g:each in="${session.notifications}" var="item">
	<g:if test="${item.analysis}">
		<g:render template="savedAnalysisItem" bean="${item}" var="notification" />
	</g:if>

	<g:else>
		<g:render template="genePatternItem" bean="${item}" var="job" />
	</g:else>
</g:each>
</g:if>
<g:else>
No notifications at this time.
</g:else>