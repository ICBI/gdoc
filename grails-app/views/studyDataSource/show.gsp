<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		</script>
        <title>${currStudy.shortName} Details</title>         
    </head>
    <body>
	<p style="font-size:14pt">${currStudy.shortName} Details</p>
	<br/>
	<div id="centerContent" class="welcome">
		<g:panel id="studyPanel" title="Study Details" styleClass="welcome" >
		<table class="studyTable" width="100%">
			<tr>
				<td class="label" width="20%">Study Name</td>
				<td>${currStudy.shortName}</td>
			</tr>
			<tr>
				<td class="label" >Study Abstract</td>
				<td>${currStudy.abstractText}</td>
			</tr>			
			<tr>
				<td class="label" >Principal Investigator(s)</td>
				<td>
					<g:each in="${currStudy.pis}" var="pi">
						${pi.firstName} ${pi.lastName}, ${pi.suffix}<br/>
					</g:each>
				</td>
			</tr>
			<tr>
				<td class="label" >Cancer Type</td>
				<td>${currStudy.cancerSite}</td>
			</tr>			
			<tr>
				<td class="label" >Point(s) of Contact</td>
				<td>
					<g:each in="${currStudy.pocs}" var="poc">
						${poc.firstName} ${poc.lastName}<br/>
					</g:each>
				</td>
			</tr>
		</table>
		</g:panel>
		<br/>
		<g:panel id="studyPanel" title="Data Type Details" styleClass="welcome" >
		<table class="studyTable" width="100%">
			<g:if test="${currStudy.content}">
				<tr>
					<td>
						<g:each in="${currStudy.content}" var="content">
							&nbsp;|&nbsp;${content.type}
						</g:each>
					</td>
				</tr>
			</g:if>
			<tr>
				<th>Data Type</th>
				<th>Number of Elements</th>
				<th>Search</th>
			</tr>
			<tr>
				<g:if test="${clinicalElements}">
					<td>Clinical Data</td>
					<td>${clinicalElements.size} Clinical Elements</td>
					<td><g:link controller="clinical">Search</g:link></td>
				</g:if>
				<g:else>
					<td colspan="2">This Study DataSource currently has no Clinical Elements</td>
					<td>No Clinical Search available at this time</td>
				</g:else>
			</tr>
			<tr>
				<g:if test="${currStudy.hasGenomicData()}">
					<td colspan="2">Genomic Data</td>
					<td>Select 'perform analysis' from menu</td>
				</g:if>
				<g:else>
					<td colspan="2">This Study DataSource currently has no Genomic Data</td>
					<td>No Analysis available at this time</td>
				</g:else>
			</tr>		
		</table>
		</g:panel>
	</div>
	</body>
	
</hmtl>