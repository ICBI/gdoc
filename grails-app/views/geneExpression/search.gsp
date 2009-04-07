<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="report" />
				<g:javascript src="AC_OETags.js" />
				<g:javascript>
				<!--
				// -----------------------------------------------------------------------------
				// Globals
				// Major version of Flash required
				var requiredMajorVersion = 9;
				// Minor version of Flash required
				var requiredMinorVersion = 0;
				// Minor version of Flash required
				var requiredRevision = 124;
				// -----------------------------------------------------------------------------
				// -->
				</g:javascript>
        <title>Gene Expression Search Results</title>         
    </head>
    <body>
	<br/>
	<p style="font-size:14pt">Gene Expression Search Results</p>
	<div id="centerContent" height="800px">
		<br/>
				<script language="JavaScript" type="text/javascript">
				<!--
				// Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
				var hasProductInstall = DetectFlashVer(6, 0, 65);

				// Version check based upon the values defined in globals
				var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

				if ( hasProductInstall && !hasRequestedVersion ) {
					// DO NOT MODIFY THE FOLLOWING FOUR LINES
					// Location visited after installation is complete if installation is required
					var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
					var MMredirectURL = window.location;
				    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
				    var MMdoctitle = document.title;

					AC_FL_RunContent(
						"src", "playerProductInstall",
						"FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
						"width", "100%",
						"height", "100%",
						"align", "middle",
						"id", "GeneExpressionPlot",
						"quality", "high",
						"bgcolor", "#869ca7",
						"name", "GeneExpressionPlot",
						"allowScriptAccess","sameDomain",
						"type", "application/x-shockwave-flash",
						"pluginspage", "http://www.adobe.com/go/getflashplayer"
					);
				} else if (hasRequestedVersion) {
					// if we've detected an acceptable version
					// embed the Flash Content SWF when all tests are passed
					AC_FL_RunContent(
							"src", "/gdoc/GdocVisualization/GeneExpressionPlot",
							"width", "720px",
							"height", "500px",
							"align", "middle",
							"id", "GeneExpressionPlot",
							"quality", "high",
							"bgcolor", "#869ca7",
							"name", "GeneExpressionPlot",
							"allowScriptAccess","sameDomain",
							"type", "application/x-shockwave-flash",
							"pluginspage", "http://www.adobe.com/go/getflashplayer"
					);
				  } else {  // flash is too old or we can't detect the plugin
				    var alternateContent = 'Alternate HTML content should be placed here. '
				  	+ 'This content requires the Adobe Flash Player. '
				   	+ '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
				    document.write(alternateContent);  // insert non-flash content
				  }
				// -->
				</script>
				<noscript>
				  	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
							id="GeneExpressionPlot" width="100%" height="100%"
							codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
							<param name="movie" value="GeneExpressionPlot.swf" />
							<param name="quality" value="high" />
							<param name="bgcolor" value="#869ca7" />
							<param name="allowScriptAccess" value="sameDomain" />
							<embed src="GeneExpressionPlot.swf" quality="high" bgcolor="#869ca7"
								width="100%" height="100%" name="GeneExpressionPlot" align="middle"
								play="true"
								loop="false"
								quality="high"
								allowScriptAccess="sameDomain"
								type="application/x-shockwave-flash"
								pluginspage="http://www.adobe.com/go/getflashplayer">
							</embed>
					</object>
				</noscript>
			<br/>
			<br/>
	</div>
	</body>
	
</hmtl>