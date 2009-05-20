<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<title>Submit GenePattern Analysis</title>         
</head>
<body>
	<g:javascript library="jquery"/>
	<p style="font-size:14pt">Submit GenePattern Analysis</p>
	<br/>
	<g:form name="genePatternForm" action="submit">
	<div class="clinicalSearch">
		<br/>
		Select Groups:
		<br/>
		<table width="400px;">
			<tr>
				<td>
					<g:multiselect id="left" from="${session.lists}" optionKey="name" optionValue="name" 
							multiple="true" size="10" style="width: 150px"/>
				</td>
				<td>
					<table>
						<tr>
							<td>
								<a href="#" id="Add"> Add   </a> 
							</td>
						</tr>
						<tr>
							<td>
								<a href="#" id="Remove"> Remove   </a> 
							</td>
						</tr>
					</table>
				</td>
				<td>
					<g:multiselect id="right" name="groups" multiple="true" size="10" style="width: 150px"
						from="${flash.cmd?.groups}" class="${hasErrors(bean:flash.cmd,field:'groups','errors')}"/> 
				</td>
			</tr>
			<tr>
				<td colspan="3">
					&nbsp;
				</td>
			</tr>		
			<tr>
				<td colspan="3">
					<div class="errorDetail">
						<g:renderErrors bean="${flash.cmd?.errors}" field="groups" />
					</div>
				</td>
			</tr>
		</table>
		<br/>
		Dataset:
		<br/>
		<g:select name="dataFile" 
				from="${[[display:'PLIER Normalized', value:'EdinPlier_22APR2009.Rda']]}"
				optionKey="value" optionValue="display"/>
		<br/>
	</div>
	<br/>
	<g:submitButton name="submit" value="Submit GenePattern Analysis"/>
	</g:form>
	

</body>

</hmtl>