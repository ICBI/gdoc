
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title><g:layoutTitle /></title>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'reset.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'grids.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'thickbox.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'jquery.tooltip.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'superfish.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'superfish-vertical.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css/cupertino',  file: 'jquery-ui-1.7.1.custom.css')}" />	
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
		<g:javascript library="jquery"/>
		<g:javascript src="gdoc.js"/>
		<g:layoutHead/>
		
</head>
<body>
	
	
<g:set var="activePage" value="${params.controller}" /> 
<div id="doc3" class="yui-t1">
	<div id="hd">
		<!-- Header start -->
    <g:render template="/common/header"/>
    <!-- Header end -->
	</div>
	<div id="bd">
		<div id="yui-main">
			<div class="yui-b">
				<div class="yui-gc">
					<div class="yui-u first">
						<g:layoutBody/><br />
					</div>
					<div class="yui-u">
						<g:render template="/common/rightbar"/>
					</div>
				</div>
			</div>
		</div>
		<div id="navigation" class="yui-b first">
			<g:render template="/common/leftbar"/>
		</div>
	</div>
	<div id="ft">
		<!-- Footer start -->
    <g:render template="/common/footer"/>
    <!-- Footer end -->
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