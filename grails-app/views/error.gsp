
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>Error</title>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'reset.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'grids.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'thickbox.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'jquery.tooltip.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'superfish.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'superfish-vertical.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css/cupertino',  file: 'jquery-ui-1.7.1.custom.css')}" />	
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
		<g:javascript library="jquery" plugin="jquery"/>
		  		        <style type="text/css">
                               html, body {height: 100%;}
                          </style>
</head>
<body>
	
<g:set var="activePage" value="${params.controller}" />
    <div class="wrapper1" >
                  <div class="header">
                             <div id="doc2" class="yui-t1">
                             <br/>
                                 <!-- Header start -->
                                <g:render template="/common/header" plugin="gcore" />
                                 <!-- Header end -->
                             </div>
                 </div>

                <div id="doc2" class="yui-t1">
                    <div id="bd" style="padding-bottom:20px" >
                        <div id="yui-main" >
                        <br/><br/><br/><br/><br/><br/>
					<div class="yui-u first">
						<div class="desc">We are sorry, a system error has occurred.  Please try again.</div><br /><br/>
						<div id="details" style="display: none;">
							<g:if test="${exception}">
							    <h2>Stack Trace</h2>
							    <div class="stack">
							      <pre><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></pre>
							    </div>
							</g:if>
						</div>
					</div>
                        </div>
                    </div>
                </div>
        <div class="push"></div>
    </div>
    <div class="footer">
       <g:render template="/common/footer" plugin="gcore" />
    </div>

<g:javascript>
// code to set height of left bar
jQuery(document).ready(function() {
	$('#navigation').height($('#yui-main').height());
});
</g:javascript>
</body>
</html>