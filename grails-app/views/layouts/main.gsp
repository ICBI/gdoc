
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title><g:layoutTitle /></title>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'layout.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css/cupertino',  file: 'jquery-ui-1.7.1.custom.css')}" />	
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
		<g:javascript library="jquery"/>
		<g:javascript src="sliding_effect.js" />
		<g:layoutHead/>
		<g:javascript src="menu.js" />
		
</head>
<body>
<g:set var="activePage" value="${params.controller}" /> 
<div id="header">
    <!-- Header start -->
    <g:render template="/common/header"/>
    <!-- Header end -->
</div>
<div class="colmask holygrail">
    <div class="colmid">
        <div class="colleft">
            <div class="col1wrap">
                <div class="col1">
                    <!-- Column 1 start -->
                 		<g:layoutBody/>
                    <!-- Column 1 end -->
                </div>
            </div>
            <div class="col2">
                <!-- Column 2 start -->
                <g:render template="/common/leftbar"/>
				<!-- Column 2 end -->
			</div>
			
			<div class="col3">
                <!-- Column 3 start -->
                <g:render template="/common/rightbar"/>
                <!-- Column 3 end -->
            </div>
			
		
			
        </div>
    </div>
</div>
<div id="footer">
    <!-- Footer start -->
    <g:render template="/common/footer"/>
    <!-- Footer end -->
</div>

<!-- submenu
<div id="mymenu1" class="outlinemenu">
<ul>
<li><a href="${createLink(controller:'clinical')}">Clinical</a></li>
<li><a href="#" >Genomic</a></li>
</ul>
</div>

<script type="text/javascript">
//jkoutlinemenu.definemenu("anchorid", "menuid", "mouseover|click", optional_width, optional_height)
jkoutlinemenu.definemenu("designanchor", "mymenu1", "mouseover", 180)
</script>-->

</body>
</html>