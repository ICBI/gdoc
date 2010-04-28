
<br/>
<g:if test="${session.userId}">
    <g:render template="/common/portal_rightbar"/>
</g:if>

<g:if test="${!feedMap}">
</g:if>
<g:else>
<g:panel title="News" styleClass="news"  contentClass="myPanelContent">
		<g:each in="${feedMap}" var="key,value">
			<div><span style="font-size:9px">>> </span><a href="${value}" target="_blank">${key}</a></div>
			</g:each>
</g:panel>
	</g:else>
<br/>
<!--div id="rightColumn" class="tabDiv">
	<div id="tabs" >
	    <ul>
	        <li><a href="#fragment-1"><span>One</span></a></li>
	        <li><a href="#fragment-2"><span>Two</span></a></li>
	        <li><a href="#fragment-3"><span>Three</span></a></li>
	    </ul>
	    <div id="fragment-1">
	        GDOC News
	    </div>
	    <div id="fragment-2">
					GDOC Data
	    </div>
	    <div id="fragment-3">
					GDOC Test
	    </div>
	</div>
</div-->