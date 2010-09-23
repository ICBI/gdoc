<html>
    <head>
        <title>Georgetown Database of Cancer</title>
		<g:render template="/common/flex_header"/>
		<g:javascript src="swfobject.js"/>
		<g:javascript src="deployJava.js" base="http://java.com/js/"/>
		<g:javascript src="BrowserDetect.js"/>
		
		<meta name="layout" content="splash" />
		<g:javascript library="jquery"/>
		
		<!-- styling -->
		<g:javascript>
			$(document).ready(function() {

				checkBrowser();
				checkFlash();
				checkJava();
				$('#javaLink').click(function() {
					deployJava.installLatestJRE();
					return false;
				});
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
				} else if (BrowserDetect.browser == 'Chrome') {
					browserPass = true
					browser = BrowserDetect.browser + " " + BrowserDetect.version;
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
					<div align="center" style="font-size: 1.8em; font-weight:bold;">
						G-DOC System Requirements Check
					</div>
					<br/>
					<br/>
					<br/>
					<br/>					
					<table class="checkTable" border="1" style="margin:auto; font-size: 1.0em">
						<tr style="padding:4px 4px 4px 4px;background-color:#f2f2f2">
							<td>Required</td>
							<td>Your Computer</td>
							<td>Pass?</td>
							<td>Download</td>
						</tr>
						<tr>
							<td>Mozilla Firefox 3.5+, Google Chrome or Internet Explorer 8.0+</td>
							<td><div id="browserVersion"></div></td>
							<td>
								<div id="browserPass" align="center">
									<img src="${createLinkTo(dir: 'images', file: 'accept.png')}" alt="Pass" style="display:none" class="pass"/>
									<img src="${createLinkTo(dir: 'images', file: 'cancel.png')}" alt="Fail" class="fail"/>
								</div>
							</td>
							<td>
								<a href="http://www.mozilla.com/firefox" target="_blank">Firefox</a><br/>
								<a href="http://www.google.com/chrome/" target="_blank">Chrome</a><br/>
								<a href="http://www.microsoft.com/windows/internet-explorer/default.aspx" target="_blank">Internet Explorer</a>
							</td>
						</tr>
						<tr>
							<td>Flash 10.0+</td>
							<td><div id="flashVersion"></div></td>
							<td><div id="flashPass" align="center">
								<img src="${createLinkTo(dir: 'images', file: 'accept.png')}" alt="Pass" style="display:none" class="pass"/>
								<img src="${createLinkTo(dir: 'images', file: 'cancel.png')}" alt="Fail" class="fail"/>
							</div></td>
							<td><a href="http://get.adobe.com/flashplayer/" target="_blank">Flash</a></td>
						</tr>	
						<tr>
							<td>Java 1.6+</td>
							<td><div id="javaVersion"></div></td>
							<td><div id="javaPass" align="center">
								<img src="${createLinkTo(dir: 'images', file: 'accept.png')}" alt="Pass" style="display:none" class="pass"/>
								<img src="${createLinkTo(dir: 'images', file: 'cancel.png')}" alt="Fail" class="fail"/>
							</div></td>
							<td><a href="#" id="javaLink" target="_blank">Java</a></td>
						</tr>
						</table>	
							
				</div>
    </body>
</html>