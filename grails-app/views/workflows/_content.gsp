				<div style="float:left;width:45%;padding-right:5px">
						<table border="0">
							<tr>
								<td colspan="2" class="titleBar">
									<p style="margin-top:4px;color:#336699">Getting Started with G-DOC</p>
								</td>
							</tr>
								<tr>
									
									<td valign="top" colspan="2" style="color:#336699;">
										<p style="font-size:.8em">
										Most users prefer to start using G-DOC to compare and analyze how groups of subjects within a cancer study differ, either by attributes or via various 'omics' characteristics. The typical process of searching for unique  lists before analysis is done for you in the Quick Start feature below. 
									</td>
								</tr>
								<tr>
									<td valign="top">
										<img src="${createLinkTo(dir:'images',file:'quickStart.png')}" border="0" />
									</td>
									<td valign="top" style="color:#336699;">
										<p style="font-size:1.1em;text-decoration:underline;padding-top:7px">
											<g:link controller="quickStart" style="color:#336699;">Quick Start</g:link></p>
										<p style="font-size:.8em">Let G-DOC organize subjects (patients, cell line, animal models) by cancer type, enabling you to stratify 2 groups by outcome or experimental design (e.g Relapse, Treated vs. Non-treated) and quickly take you to the next step of performing an analysis.</p>
									</td>
								</tr>
								<tr>
									<td>
										<img src="${createLinkTo(dir:'images',file:'tutorialsIcon.png')}" border="0" />
									</td>
									<td style="color:#336699">
										<p style="font-size:1.1em;text-decoration:underline;padding-top:0px">
											<g:link controller="tutorials" style="color:#336699;">Tutorials</g:link></p>
										<p style="font-size:.8em">Watch step-by-step movies of workflows that are available within the G-DOC application. More instruction is also available in the <g:link controller="help" style="color:#336699;">help section.</g:link></p>
									</td>
								</tr>
						</table>
				</div>
				<div style="float:right;width:45%">
						<table border="0">
							<tr>
								<td colspan="2" class="titleBar">
									<p style="margin-top:4px;color:#336699">Features</p>
								</td>
							</tr>
								<tr style="color:#336699;border-bottom:1px solid #336699;" valign="top">
									<td>
										<p class="info" title="Query G-DOC study data (Patient, Cell Line and Animal Model) and save lists for use in later analyses (click 'Studies' to the left). Explore data in the genome browser. View details of recent findings." style="font-size:1.1em;text-decoration:underline;padding-top:7px;padding-left:25px;cursor:pointer;">Search</p>
										<img src="${createLinkTo(dir:'images',file:'searchIcon.png')}" border="0" />
									</td>
									<td>
										
											<p style="font-size:.8em;margin-top:20px">
												<g:searchLinks/>
											</p><br />
									</td>
								</tr>
								<tr style="color:#336699;border-bottom:1px solid #336699;" valign="top">
									<td>
										<p class="info" title="Perform high-throughput analyses on cohorts of patients to discover differences among patients in regard to 'omics' data" style="font-size:1.1em;text-decoration:underline;padding-top:0px;padding-left:22px;cursor:pointer">Analyze</p>
										<img src="${createLinkTo(dir:'images',file:'analysisIcon.png')}" border="0" />
									</td>
									<td style="padding-bottom:5px">
											<p style="font-size:.8em;margin-top:10px">
												<g:analysisLinks/>
											</p>
									</td>
								</tr>
								<tr style="color:#336699" valign="top">
									<td>
										<p class="info" title="Manage your lists, analyses and memberships. Share data you've collected and discovered via this collaboration feature." style="font-size:1.1em;text-decoration:underline;padding-top:7px;padding-left:15px;cursor:pointer">My G-DOC</p>
										
										<img src="${createLinkTo(dir:'images',file:'myGdoc.png')}" border="0" />
									</td>
									<td>
										<p style="font-size:.8em;margin-top:20px">
<g:link controller="notification" style="color:#336699;">Notifications</g:link><br /><br />
<g:link name="View My Saved Lists" controller="userList" style="color:#336699;">Saved Lists</g:link><br /><br />
<g:link name="View My Saved Analysis" controller="savedAnalysis" style="color:#336699;">Saved Analysis</g:link><br /><br />
<g:link name="Collaboration Groups" controller="collaborationGroups" style="color:#336699;x">Manage my groups / Request access</g:link>
		</p>
									</td>
								</tr>
						</table>
				</div>		
