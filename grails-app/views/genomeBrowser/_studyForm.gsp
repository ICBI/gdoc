<g:if test="${session.study}">
	Please Select Data Types to Display: <br/><br/>
	<g:managedCheckBox name="dataTypes" value="COPY_NUMBER" disabled="${!session.study.hasCopyNumberData()}" value="false"/> Copy Number Segment Data <br/>
<%--	<g:managedCheckBox name="dataTypes" value="MICROARRAY" disabled="${!session.study.hasGenomicData()}" value="false"/> Gene Expression Data <br/> ---%>
	
</g:if>
