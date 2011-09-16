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
	return [browserPass, browser];
}

function checkFlash() {
	var flashPass = false;
	if(swfobject.getFlashPlayerVersion().major >= 10)
		flashPass = true;
	return [flashPass, "Flash " + swfobject.getFlashPlayerVersion().major + "." + swfobject.getFlashPlayerVersion().minor];
}

function checkJava() {
	var javaPass = deployJava.versionCheck("1.6.0+");
	return [javaPass, "Java version(s): " + deployJava.getJREs().join(", ")];
}