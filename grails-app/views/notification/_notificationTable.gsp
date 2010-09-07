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
	<g:render template="savedAnalysisItem" bean="${item}" var="notification" />
</g:each>

</g:if>
<g:else>
No running analysis at this time.
</g:else>