<html>
    <head>
        <title>Georgetown Database of Cancer</title>
		<g:render template="/common/flex_header"/>
		<g:javascript src="swfobject.js"/>
		<g:javascript src="deployJava.js" base="http://java.com/js/"/>
		<g:javascript src="BrowserDetect.js"/>
		
		<meta name="layout" content="splash" />
		<g:javascript library="jquery"/>
		<jq:plugin name="curvycorners"/>
		
		<!-- styling -->
		<g:javascript>
			$(document).ready(function() {

				checkBrowser();
				checkFlash();
				checkJava();
			});
			function checkBrowser() {
				var browser = ""
				var browserPass = false;
				if(BrowserDetect.browser == 'Explorer') {
					if(jQuery.browser.version >= 8)
						browserPass = true
					else 
						browserPass = false
					browser = "Internet Explorer " + jQuery.browser.version;
				} else if(BrowserDetect.browser == 'Firefox') {
					if(BrowserDetect.version >= 3.5) {
						browserPass = true;
					} else { 
						browserPass = false;
					}
					browser = "Mozilla Firefox " + BrowserDetect.version;
				} else { 
					browser = BrowserDetect.browser + " " + BrowserDetect.version;
					browserPass = false;
				}
				$('#browserVersion').text(browser);
				passCheck($('#browserPass'), (browserPass));
			}
			
			function checkFlash() {
				var flashPass = false;
				$('#flashVersion').text("Flash " + swfobject.getFlashPlayerVersion().major + "." + swfobject.getFlashPlayerVersion().minor);
				if(swfobject.getFlashPlayerVersion().major >= 10)
					flashPass = true;
				passCheck($('#flashPass'), flashPass);
			}
			
			function checkJava() {
				$('#javaVersion').text("Java version(s): " + deployJava.getJREs().join(", "));
				var javaPass = deployJava.versionCheck("1.6.0+");
				passCheck($('#javaPass'), javaPass);
			}
			
			function passCheck(item, pass) {
				if(pass) {
					$(item).find('.fail').hide();
					$(item).find('.pass').show();
				} else {
					$(item).find('.pass').hide();
					$(item).find('.fail').show();
				}
			}
		</g:javascript>
    </head>
    <body>
				<jq:plugin name="ui"/>
				
				<div id="centerContent" style="margin:20px">
					
						
					<br/>
					<h1>System Requirements Check</h1>
					<br/>
					<br/>
					
					<table class="checkTable" border="1" style="margin:auto; font-size: 1.0em">
						<tr style="padding:4px 4px 4px 4px;background-color:#f2f2f2">
							<td>Required</td>
							<td>Your Computer</td>
							<td>Pass?</td>
						</tr>
						<tr>
							<td>Internet Explorer 8.0+ or Mozilla Firefox 3.5+</td>
							<td><div id="browserVersion"></div></td>
							<td>
								<div id="browserPass" align="center">
									<img src="${createLinkTo(dir: 'images', file: 'accept.png')}" alt="Pass" style="display:none" class="pass"/>
									<img src="${createLinkTo(dir: 'images', file: 'cancel.png')}" alt="Fail" class="fail"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>Flash 10.0+</td>
							<td><div id="flashVersion"></div></td>
							<td><div id="flashPass" align="center">
								<img src="${createLinkTo(dir: 'images', file: 'accept.png')}" alt="Pass" style="display:none" class="pass"/>
								<img src="${createLinkTo(dir: 'images', file: 'cancel.png')}" alt="Fail" class="fail"/>
							</div></td>
						</tr>	
						<tr>
							<td>Java 1.6+</td>
							<td><div id="javaVersion"></div></td>
							<td><div id="javaPass" align="center">
								<img src="${createLinkTo(dir: 'images', file: 'accept.png')}" alt="Pass" style="display:none" class="pass"/>
								<img src="${createLinkTo(dir: 'images', file: 'cancel.png')}" alt="Fail" class="fail"/>
							</div></td>
						</tr>
						</table>	
							
				</div>
    </body>
</html>