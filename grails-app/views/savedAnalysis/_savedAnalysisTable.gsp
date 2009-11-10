<g:javascript library="jquery"/>
<g:javascript>
$(document).ready( function () {
		  $("[class*='_tags']").each(function(index){
			$(this).tagbox({
							grouping: '"',
							separator:/[,]/ ,
							autocomplete:true
						});
			
		  });
		  
		$("[class='tag']").each(function(index){
			var span = $(this).find('label').find('span');
			if(span.html() == 'clinicalM' || 
				span.html() == 'patientM' ||
					span.html() == 'ReportersM'){
				var newStr = $(this).find('label').find('span').html();
				var tag = newStr.substring(0, newStr.length-1);
				$(this).find('label').find('abbr').remove();
				$(this).find('label').find('input').remove();
				$(this).find('label').find('span').remove();
				$(this).find('label').append("<div style='margin: 3px;margin-left:15px;display:inline-block';padding-top:5px>" + tag + "</div>");
			}
			//console.log($(this).find('label'));
			//console.log(span.html());
			//console.log(abbr);
		});
		
		
} );
</g:javascript>
<g:javascript>
function taggify(element,className){
	$(element).attr('class',className);
	$(element).tagbox();
}
</g:javascript>

<table class="listTable" width="100%" cellpadding="2">
	

<g:each in="${savedAnalysis}" var="analysis">
<g:if test="${analysis.type == AnalysisType.KM_GENE_EXPRESSION && analysis.query.geAnalysisId.toString() == 'null'}">
</g:if>
<g:else>
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
			<g:elseif test="${analysis.type == AnalysisType.KM_GENE_EXPRESSION}">
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
	</div><br />
	<div style="display:block;text-align:left;border-bottom:1px solid grey;background-color:#f3f3f3;padding-bottom:5px">
		Studies: 
		<g:if test="${analysis.studies.size()>0}">
		${analysis.studyNames().join(", ")}<br/>
		</g:if>
		Tags:
		<g:if test="${analysis.tags.size()>0}">
		<input type="text" name="${analysis.id}_tags_name" class="${analysis.id}_tags tags clearfix" value="${analysis.tags.replace(' ',',')}" />
		</g:if>
		<g:if test="${analysis.tags.size()==0}">
		<input type="text" name="${analysis.id}_tags_name"  class="${analysis.id}_tags tag_box tags clearfix" />
		</g:if>
		
	</div>
	<br/>
	</g:if>
	</td>
	</tr>
	</g:else>
</g:each>
</table>
<g:javascript library="jquery"/>
<jq:plugin name="tagbox"/>
<g:javascript>

 $("[class*='_tags']").each(function(index){
	$(this).tagbox({
					grouping: '"',
					separator:/[,]/ ,
					autocomplete:true
				});
	
  });
  
$("[class='tag']").each(function(index){
	var span = $(this).find('label').find('span');
	if(span.html() == 'clinicalM' || 
		span.html() == 'patientM' ||
			span.html() == 'ReportersM'){
		var newStr = $(this).find('label').find('span').html();
		var tag = newStr.substring(0, newStr.length-1);
		$(this).find('label').find('abbr').remove();
		$(this).find('label').find('input').remove();
		$(this).find('label').find('span').remove();
		$(this).find('label').append("<div style='margin: 3px;margin-left:15px;display:inline-block';padding-top:5px>" + tag + "</div>");
	}
	//console.log($(this).find('label'));
	//console.log(span.html());
	//console.log(abbr);
});

</g:javascript>