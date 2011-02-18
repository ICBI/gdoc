<html>
<head>
	<meta name="layout" content="main" />
	<g:javascript library="jquery"/>
	<jq:plugin name="tooltip"/>
	<title>Notifications</title>         
</head>
<body>
	<g:javascript>
		jQuery(document).ready(function() {
			addToolTips();
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
						addToolTips();
					},
					error: function(data) {
						clearInterval(refreshId);
					}
				});
		  }, 5000);
		});
		
		function addClickHandler() {
			jQuery('.genePatternLink').click(function() {
				jQuery('#gpForm').submit();
				return false;
			})
		}
		
		function addToolTips() {
			jQuery('.errorStatus').tooltip({
				bodyHandler: function() { 
					return jQuery.ajax({
						type: "POST",
						async: false,
						data: {id: this.id},
						url: "${createLink(controller: 'notification', action: 'error')}"
					}).responseText;
				}, 
				showURL: false
			});
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
	<br /><br />
</body>

</hmtl>