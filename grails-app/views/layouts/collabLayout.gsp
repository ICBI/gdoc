
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
    <title><g:layoutTitle /></title>
		<!--link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'reset.css')}"/-->
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'grids.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'thickbox.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'jquery.tooltip.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'superfish.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'superfish-vertical.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css/cupertino',  file: 'jquery-ui-1.7.1.custom.css')}" />	
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styledButton.css')}" />
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'tags.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'jquery.autocomplete.css')}"/>
		<g:javascript library="jquery"/>
		<g:javascript src="gdoc.js"/>
		<g:layoutHead/>
		
</head>
<body style="background-color:#334477">
	
	
	
<g:set var="activePage" value="${params.controller}" /> 
<div id="doc2" class="yui-t1">
	<div id="hd">
		<!-- Header start -->
    <g:render template="/common/headerWorkflow"/>
    <!-- Header end -->
	</div>
	<div class="c" style="background:#fff;border:1px solid #000;padding:3px;">
	<div id="bd">
		<div id="yui-main">
			<div class="yui-b">
				<div class="yui-gc">
					<div class="yui-u first">
						<g:layoutBody/><br />
					</div>
					<div class="yui-u">
						<br /><br /><br />
						<div style="height:600px;overflow:scroll;">
						<g:render template="/notification/invitationTable"/>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="c" style="padding:5px">
		<div id="navigation" class="yui-b first">
			<g:render template="/common/leftbar"/>
		</div>
		</div>
	</div>
	<div id="ft">
		<!-- Footer start -->
    <g:render template="/common/footer"/>
    <!-- Footer end -->
	</div>
	</div>
</div>
<g:javascript>
// code to set height of left bar
jQuery(document).ready(function() {
	$('#navigation').height($('#yui-main').height());
});
</g:javascript>
</body>
</html>