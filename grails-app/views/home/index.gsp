<html>
    <head>
        <title>Welcome to GDOC</title>
		<meta name="layout" content="main" />
		
    </head>
    <body>
				<jq:plugin name="ui"/>
				<script type="text/javascript">
				$(document).ready(function(){
				  $("#centerTabs").tabs();
				});
				</script>
				<br/>
				<div style="font-size:16px;margin-bottom:5px">Welcome to GDOC</div>
				<div id="centerContent" class="welcomeTitle">
						<p>The Georgetown Database of Cancer is integrates multiple datatypes to present
						a unified data view, allowing for rapid data exploration.</p>
						
						<p>Lombardi's world-renowned Research Faculty are discovering cancer
							 risk factors, designing effective prevention strategies, and learning
							 how to detect cancers earlier. They are developing and testing the cancer
							 treatments of tomorrow - targeted therapies that will improve both survival
							 and quality of life.</p>
						<br/>
					<br/>
					<div class="tabDiv">
						<div id="centerTabs" class="tabDiv">
						    <ul>
						        <li><a href="#fragment-4"><span>Patient Metrics</span></a></li>
						        <li><a href="#fragment-5"><span>Study Metrics</span></a></li>
						        <li><a href="#fragment-6"><span>Other Metrics</span></a></li>
						    </ul>
						    <div id="fragment-4">
						        Total number of patients across studies: 134<br />
								Average length of study: 200 days<br /><br /><br /><br /><br /><br />
						    </div>
						    <div id="fragment-5">
										GDOC Data
						    </div>
						    <div id="fragment-6">
										GDOC Test
						    </div>
						</div>
					</div>
				</div>
    </body>
</html>