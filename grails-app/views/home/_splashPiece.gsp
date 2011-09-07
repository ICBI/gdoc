<div align="center">
	<div id="reqMessage" class="errorBox" style="display: none;">
		<g:message code="home.notMeetRequirements"  />
	</div>
	<br/>
</div>
<g:javascript>
$(document).ready(function(){
       <!-- For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. --> 
    var swfVersionStr = "10.0.45";
    <!-- To use express install, set to playerProductInstall.swf, otherwise the empty string. -->
    var xiSwfUrlStr = "${resource(dir: 'visualizations', file: 'playerProductInstall.swf')}";
    var flashvars = {};
    var params = {};
    params.quality = "high";
    params.bgcolor = "#ffffff";
    params.allowscriptaccess = "sameDomain";
    params.allowfullscreen = "true";
    var attributes = {};
    attributes.id = "Main";
    attributes.name = "Main";
    attributes.align = "middle";
    swfobject.embedSWF(
        "${resource(dir: 'visualizations', file: 'Main.swf')}", "flashContent", 
        "900", "350", 
        swfVersionStr, xiSwfUrlStr, 
        flashvars, params, attributes);
	<!-- JavaScript enabled so display the flashContent div in case it is not replaced with a swf object. -->
	swfobject.createCSS("#flashContent", "display:block;text-align:left;");

	var browserData = checkBrowser();
	var flashData = checkFlash();
	var javaData = checkJava();

	if(!(browserData[0] && flashData[0] && javaData[0])) {
		$("#reqMessage").show();
	}
});
</g:javascript>


<div style="width:900px;border:1px solid silver;margin:0pt auto; text-align: center;height:350" align="center">
<div id="flashContent"></div>
</div>

        
          <noscript>
	<div style="width:900px;border:1px solid silver;margin:0pt auto; text-align: center;height:350" align="center">
            <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="900" height="350" id="Main">
              <param name="movie" value="Main.swf" />
              <param name="quality" value="high" />
              <param name="bgcolor" value="#ffffff" />
              <param name="allowScriptAccess" value="sameDomain" />
              <param name="allowFullScreen" value="true" />
              <!--[if !IE]>-->
              <object type="application/x-shockwave-flash" data="Main.swf" width="900" height="350">
                <param name="quality" value="high" />
                <param name="bgcolor" value="#ffffff" />
                <param name="allowScriptAccess" value="sameDomain" />
                <param name="allowFullScreen" value="true" />
                <!--<![endif]-->
                <!--[if gte IE 6]>-->
                <p> 
                  <g:message code="home.flashVersionGreater" />
                </p>
                <!--<![endif]-->
                <a href="http://www.adobe.com/go/getflashplayer">
                  <img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash Player" />
                </a>
                <!--[if !IE]>-->
              </object>
              <!--<![endif]-->
            </object>
</div>
          </noscript>