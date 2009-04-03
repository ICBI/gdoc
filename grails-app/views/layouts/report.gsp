<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title><g:layoutTitle/></title>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'layout.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css/cupertino',  file: 'jquery-ui-1.7.1.custom.css')}" />	
		<link rel="stylesheet" href="${createLinkTo(dir: 'css/flexigrid',  file: 'flexigrid.css')}" />
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'ui.jqgrid.css')}" />	
			
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
		<g:javascript library="jquery"/>
		<jq:plugin name="flexigrid"/>
		<g:layoutHead/>
</head>
<body>
<div id="header">
    <!-- Header start -->
    <g:render template="/common/header"/>
    <!-- Header end -->
</div>
<div id="content">
	<g:layoutBody/>
</div>
<div id="footer">
    <!-- Footer start -->
    <g:render template="/common/footer"/>
    <!-- Footer end -->
</div>
</body>
</html>