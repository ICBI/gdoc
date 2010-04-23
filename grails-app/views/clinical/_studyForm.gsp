<g:if test="${session.study}">
<g:form name="searchForm" action="search">
	<g:each in="${session.dataTypes}">
		<g:if test="${it.target == 'PATIENT'}">
			<g:set var="type" value="clinical"/>
		</g:if>
		<g:else>
			<g:set var="type" value="biospecimen" />
		</g:else>
		<div class="clinicalSearch">
			<div style="float: left">
				${it.longName} 	
				<img class="info" title="${it.definition}" src="${createLinkTo(dir:'images',file:'information.png')}" border="0" />
			</div>
		<div style="float: right; vertical-align: middle">
			<img class="close"src="${createLinkTo(dir:'images',file:'cross.png')}" border="0" />
		</div>
		<br/>
		<br/>
			<g:if test="${it.vocabulary}">
				<div align="left">
					<g:if test="${flash.params}">
						<g:select name="${type + '_' + it.shortName}" 
								noSelection="${['':'Select One...']}" value="${flash.params[type + '_' + it.shortName]}"
								from="${it.vocabs}" optionKey="term" optionValue="termMeaning">
						</g:select>
					</g:if>
					<g:else>
						<g:select name="${type + '_' + it.shortName}" 
								noSelection="${['':'Select One...']}"
								from="${it.vocabs}" optionKey="term" optionValue="termMeaning">
						</g:select>
					</g:else>
				</div>
				<br/>
			</g:if>
			<g:elseif test="${it.qualitative}">
				<g:if test="${flash.params}">
					<g:textField name="${type + '_' + it.shortName}"  value="${flash.params[type + '_' + it.shortName]}"/>
				</g:if>
				<g:else>
					<g:textField name="${type + '_' + it.shortName}"  />
				</g:else>
				<br/>
			</g:elseif>
			<g:elseif test="${it.lowerRange != null && it.upperRange != null}">
				<div align="center">
					<label for="rangeValue" style="padding-left: 130px">Range:</label>
					<g:if test="${flash.params}">
						<g:textField name="${type + '_range_' + it.shortName}" class="rangeValue" value="${flash.params[type + '_range_' + it.shortName]}" style="border:0; font-weight:bold; background: #E6E6E6;"  />
					</g:if>
					<g:else>
						<g:textField name="${type + '_range_' + it.shortName}" class="rangeValue" style="border:0; font-weight:bold; background: #E6E6E6;"  />
					</g:else>
					<br/>
					<br/>
					<table>
						<tr>
							<td style="padding-right: 10px">${it.lowerRange}</td>
							<td>
								<div class="slider-range" style="width: 250px"></div>
							</td>
							<td style="padding-left: 10px">${it.upperRange}</td>
						</tr>
					</table>
				</div>
				<script type="text/javascript">
					var item = jQuery('.slider-range').get(jQuery('.slider-range').size() - 1); 
					jQuery(item).data("minval", "${it.lowerRange}");
					jQuery(item).data("maxval", "${it.upperRange}");
				</script>
				<br/>
			</g:elseif>
			<g:else>
				Between 
					<g:if test="${flash.errors}">
						<g:textField name="${type + '_' + it.shortName}" class="${flash.errors[type + '_' + it.shortName] ? 'errors' : ''}" value="${flash.params[type + '_' + it.shortName][0]}" /> 
					</g:if>
					<g:else>
						<g:textField name="${type + '_' + it.shortName}" /> 
					</g:else>
				and 
					<g:if test="${flash.errors}">
						<g:textField name="${type + '_' + it.shortName}" class="${flash.errors[type + '_' + it.shortName] ? 'errors' : ''}" value="${flash.params[type + '_' + it.shortName][1]}"/> 
					</g:if>
					<g:else>
						<g:textField name="${type + '_' + it.shortName}" /> 
					</g:else>
				</g:else>
		</div>
	</g:each>
	<br/>
	<br/>
	<g:submitButton name="submit" value="Search"/>
</g:form>
</g:if>

<g:else>
No study currently selected.
</g:else>