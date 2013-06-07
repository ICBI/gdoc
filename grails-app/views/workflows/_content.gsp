<jq:plugin name="tooltip"/>
<jq:plugin name="ui"/>
<g:javascript>
	$(document).ready(function (){
		$('.info').tooltip({showURL: false});
		
	}
</g:javascript>
				<div style="width:100%;padding-right:5px">
					<p style="font-size:1.2em;color:#336699;padding-top:10px">Select an initial study to work with, OR use the <g:link controller="quickStart" style="color:#336699;" class="info" title="Let G-DOC organize subjects (patients, cell line, etc) by cancer type, enabling you to stratify 2 groups by outcome (Treated vs. Non-treated) and quickly take you to the next step of performing an analysis.">Quick Start Feature</g:link> to instantly find and analyze data:</p>
						<table border="0" cellpadding="0" style="width:100%">
						
								<tr>
									<td style="color:#336699;text-align:center;width:50%" valign="top">
										<p style="font-size:1.1em;text-decoration:underline;padding-top:0px" valign="top">
												<g:render template="/studyDataSource/studyPicker" plugin="gcore"/>
										</p>
									</td>
								</tr>	
								<%--tr>
									<td valign="top" style="color:#336699;text-align:center">
										<img src="${createLinkTo(dir:'images',file:'quickStart.png')}" border="0" />
										<p style="font-size:1.1em;text-decoration:underline;padding-top:7px;">
											<g:link controller="quickStart" style="color:#336699;">Quick Start</g:link></p>
										<p style="font-size:.8em;text-align:left">Let G-DOC organize subjects (patients, cell line, animal models) by cancer type, enabling you to stratify 2 groups by outcome or experimental design (e.g Relapse, Treated vs. Non-treated) and quickly take you to the next step of performing an analysis.</p>
									</td>
								</tr--%>
								
						</table>
				</div>
				<div style="width:100%"><br />
						<table border="0" cellspacing="10" style="width:100%">
							<tr>
								<td colspan="6" class="titleBar">
									<p style="margin-top:4px;color:#336699">Advanced Tools and Features</p>
								</td>
							</tr>
								<tr style="color:#336699;border-bottom:1px solid #336699;" valign="top">
									<td valign="top" style="width:33%">
										<img src="${createLinkTo(dir:'images',file:'searchIcon.png')}" border="0" />
										<p class="info" title="Query G-DOC study data (Patient, Cell Line and Animal Model) and save lists for use in later analyses (click 'Studies' to the left). Explore data in the genome browser. View details of recent findings." style="font-size:1.1em;text-decoration:underline;padding-top:0px;padding-left:25px;cursor:pointer;">Search</p>
										
									</td>
									
								
									<td valign="top">
										<img src="${createLinkTo(dir:'images',file:'analysisIcon.png')}" border="0" />
										<p class="info" title="Perform high-throughput analyses on cohorts of patients to discover differences among patients in regard to 'omics' data" style="font-size:1.1em;text-decoration:underline;padding-top:0px;padding-left:22px;cursor:pointer">Analyze</p>
										
									</td>
									
									<td valign="top">
										
										<img src="${createLinkTo(dir:'images',file:'myGdoc.png')}" border="0" />
										<p class="info" title="Manage your lists, analyses and memberships. Share data you've collected and discovered via this collaboration feature." style="font-size:1.1em;text-decoration:underline;padding-top:0px;padding-left:15px;cursor:pointer">My G-DOC</p>
										
									</td>
									
								</tr>
									<tr>
										<td valign="top">
											<p style="font-size:.8em;margin-top:10px">
												<g:searchLinks/>
											</p>
										</td>
										<td valign="top">
											<p style="font-size:.8em;margin-top:10px;">
												<g:analysisLinks/>
											</p>
										</td>
										<td valign="top">
											<p style="font-size:.8em;margin-top:10px">
	<g:link controller="notification">Notifications</g:link><br /><br />
	<g:link name="View My Saved Lists" controller="userList">Saved Lists</g:link><br /><br />
	<g:link name="View My Saved Analysis" controller="savedAnalysis">Saved Analysis</g:link><br /><br />
	<g:link name="Collaboration Groups" controller="collaborationGroups">Manage my groups / Request access</g:link>
			</p>
										</td>
									</tr>
						</table>
				</div>		
