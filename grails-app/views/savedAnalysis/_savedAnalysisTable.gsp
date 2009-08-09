<g:javascript library="jquery"/>

<table class="listTable" width="100%" cellpadding="5">
	

<g:each in="${savedAnalysis}" var="analysis">
<tr>
	<td style="background-color:white;">
		<g:if test="${savedAnalysis.size()>0}">
<div class="notificationContainer" style="height: 10px">
		<div style="float: left;">
			<g:if test="${analysis.type == AnalysisType.CLASS_COMPARISON}">
				<g:link controller="analysis" action="view"  id="${analysis.id}">${analysis.type}</g:link>
			</g:if>
			<g:elseif test="${analysis.type == AnalysisType.GENE_EXPRESSION}">
				<g:link controller="geneExpression" action="view" id="${analysis.id}">${analysis.type}</g:link> 
			</g:elseif>
			<g:elseif test="${analysis.type == AnalysisType.KM_PLOT}">
				<g:link controller="km" action="repopulateKM" id="${analysis.id}">${analysis.type}</g:link> 
			</g:elseif>
				&nbsp;&nbsp;<span><g:formatDate date="${analysis.dateCreated}" format="h:mm M/dd/yyyy"/></span>
			</div>
				<g:if test="${session.userId.equals(analysis.author.loginName)}">
				<div style="border:0px solid black;width:20%;float:right">	
					
					<g:link class="thickbox" name="Share &nbsp; analysis &nbsp; with collaboration groups?" action="share" controller="share" 
params="[id:analysis.id,name:'analysis',type:'SAVED_ANALYSIS',keepThis:'true',TB_iframe:'true',height:'250',width:'400',title:'someTitle']"><img alt="share list" style="height: 18px;padding-right:20px" src="${createLinkTo(dir: 'images', file: 'share.png')}"/></a></g:link>

				<a href="javascript:void(0)" onclick="if(confirm('Are you sure?')){${remoteFunction(action:'delete', id:analysis.id, update:'analysisContainer')}return false;}">
				<img alt="Delete Analysis" title="Delete Analysis" style="vertical-align: bottom;" src="${createLinkTo(dir: 'images', file: 'cross.png')}"/></a>
				

				</div>
				</g:if>
				<g:else>
				<div style="border:0px solid black;width:50%;float:right">	
					Shared by: ${analysis.author.firstName}&nbsp;${analysis.author.lastName}&nbsp;(author)
				</div>
				</g:else>
		
		</div>	
		</div>
	
	</div>
	<br/>
	</g:if>
	</td>
	</tr>
</g:each>
</table>
