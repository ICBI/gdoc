<g:if test="${session.notifications}">
<g:each in="${session.notifications}" var="item">
	<g:render template="savedAnalysisItem" bean="${item}" var="notification" />
</g:each>

</g:if>
<g:else>
No running analysis at this time.
</g:else>