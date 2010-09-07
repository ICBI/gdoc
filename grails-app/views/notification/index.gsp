<html>
<head>
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
					url: "${createLink(controller: 'notification', action: 'check')}",
					success: function(data) {
						//console.log(data);
						//jQuery("#notifications_content").html(data);
						document.getElementById('notifications_content').innerHTML = data;
						
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
		  }, 5000);
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
	<%--g:panel title="Invitations and Messages" styleClass="invitations" contentClass="myPanelContent" id="invitations">
		(<span style='font-style:italic'>last 30 days</span>)
		<g:render template="/notification/invitationTable" />
	</g:panel--%>
	<br/>
		<form id="gpForm" action="${grailsApplication.config.genePatternUrl}/gp/pages/index.jsf" method="POST" target="genepattern">
			<input type="hidden" name="workspaceId" value="${genePatternId()}" />
		</form>
	<br /><br />
</body>

</hmtl>