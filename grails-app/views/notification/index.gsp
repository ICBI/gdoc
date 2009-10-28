<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<g:javascript library="jquery"/>
	<title>Notifications</title>         
</head>
<body>
	<g:javascript>
		jQuery(document).ready(function() {
			var refreshId = setInterval(function() {
		  	jQuery.ajax({
					type: "POST",
					url: "${createLink(contoller: 'notification', action: 'check')}",
					success: function(data) {
						jQuery("#notifications_content").html(data);
						var status = jQuery(".status").map(function() {
							return jQuery(this).html();
						});
						var allStats = jQuery.grep(status, function(item) {
							return (item.indexOf("Running") >= 0);
						});
						if(allStats && allStats.length == 0) {
							clearInterval(refreshId);
						}
						addClickHandler();
					},
					error: function(data) {
						clearInterval(refreshId);
					}
				});
		  }, 2000);
			addClickHandler();
		});
		
		function addClickHandler() {
			jQuery('.genePatternLink').click(function() {
				jQuery('#gpForm').submit();
				return false;
			})
		}
		
	</g:javascript>
	<p style="font-size:14pt">Notifications</p>
	<br/>
	<g:panel title="My Running Analysis" styleClass="notifications" contentClass="myPanelContent" id="notifications">
		<g:render template="/notification/notificationTable" />
	</g:panel>
	<g:panel title="Invitations" styleClass="invitations" contentClass="myPanelContent" id="invitations">
		<g:render template="/notification/invitationTable" />
	</g:panel>
	<br/>
		<form id="gpForm" action="http://141.161.54.201:8080/gp/pages/index.jsf" method="POST" target="genepattern">
			<input type="hidden" name="workspaceId" value="${genePatternId()}" />
		</form>
	<br /><br />
</body>

</hmtl>