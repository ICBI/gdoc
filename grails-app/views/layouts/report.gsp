<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
    <title><g:layoutTitle/></title>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'reset.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'grids.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css/cupertino',  file: 'jquery-ui-1.7.1.custom.css')}" />	
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'ui.jqgrid.css')}" />	
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'superfish.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'jquery.tooltip.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styledButton.css')}"/>
		<g:javascript library="jquery"/>
		<g:layoutHead/>
		
</head>
<body style="background-color:#334477">
	
<div id="doc3" class="yui-t1">
<div id="hd" style="overflow:hidden;">
    <!-- Header start -->
    <g:render template="/common/header"/>

    <!-- Header end -->
</div>
<div class="c" style="background:#fff;border:.5px solid #000;padding:3px 0;">
	<div style="padding:5px">
	<g:if test="${controllerName != 'admin'}">
		<g:render template="/common/nav_top" />
	</g:if>
	
<br/>
<div id="bd" class="reportBody" style="clear: both;">		
	<g:layoutBody/>
</div>

<div id="ft">
    <!-- Footer start -->
    <g:render template="/common/footer"/>
    <!-- Footer end -->
</div>
</div>
</div>
</div>
</body>
</html>