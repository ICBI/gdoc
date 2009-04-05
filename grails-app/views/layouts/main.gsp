
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title><g:layoutTitle /></title>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'reset.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'grids.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'superfish.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'superfish-vertical.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css/cupertino',  file: 'jquery-ui-1.7.1.custom.css')}" />	
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
		<g:javascript library="jquery"/>
		<g:javascript src="sliding_effect.js" />
		<g:layoutHead/>
		<g:javascript src="menu.js" />
		
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
						<g:layoutBody/>
					</div>
					<div class="yui-u">
						<g:render template="/common/rightbar"/>
					</div>
				</div>
			</div>
		</div>
		<div id="navigation" class="yui-b first" style="height: 400px">
			<g:render template="/common/leftbar"/>
		</div>
	</div>
	<div id="ft">
		<!-- Footer start -->
    <g:render template="/common/footer"/>
    <!-- Footer end -->
	</div>
</div>
<%--
<div id="header">

</div>
<div class="colmask holygrail">
    <div class="colmid">
        <div class="colleft">
            <div class="col1wrap">
                <div class="col1">
                 		
                </div>
            </div>
            <div class="col2">
                <g:render template="/common/leftbar"/>
			</div>
			
			<div class="col3">
                
            </div>
			
		
			
        </div>
    </div>
</div>
<div id="footer">

</div>
--%>
</body>
</html>