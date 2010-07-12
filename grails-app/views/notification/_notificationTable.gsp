<g:javascript library="jquery"/>
<jq:plugin name="tooltip"/>
<jq:plugin name="livequery"/>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery(".status").livequery(function() {
			jQuery(this).tooltip({showURL: false});
		});
	})
</script>
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
No running analysis at this time.
</g:else>