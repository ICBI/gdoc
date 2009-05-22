<g:javascript library="jquery"/>

<div class="notificationContainer" style="height: 10px">
	<g:if test="${job.complete}">
		<div style="float: left;">
			<a href="#" class="genePatternLink" >GenePattern Job ${job.jobId}</a> (<g:formatDate date="${job.dateCreated}" format="h:mm M/dd/yyyy"/>)
		</div>
		<div class="status" style="float: right;">
			Complete
		</div>
	</g:if>
	<g:else>
		<div style="float: left;">
			GenePattern Job ${job.jobId} (<g:formatDate date="${job.dateCreated}" format="h:mm M/dd/yyyy"/>)
		</div>
		<div class="status" style="float: right;">Running <img style="height: 12px" src="${createLinkTo(dir:'images',file:'indicator.gif')}" border="0" />
		</div>
	</g:else>
</div>
<br/>
