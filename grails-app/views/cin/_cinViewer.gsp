<g:javascript library="jquery"/>

<g:set var="uiId" value="${img}" />

<div>
<span> 

	<div style="float:middle">
	<a href="#" onclick="toggle('${uiId}');return false;" style="cursor: pointer;">${uiId}
	<img class="${uiId}_toggle" src="${createLinkTo(dir: 'images', file: 'expand.gif')}" />
	<img class="${uiId}_toggle" src="${createLinkTo(dir: 'images', file: 'collapse.gif')}"
	width="13"
	height="14" border="0" alt="Show/Hide" title="Show/Hide" style="display:none" />
	</a>
	</div>
	<div id="${uiId}_content" style="border:0px solid black;display:none;padding-bottom:5px">
	<span><img class="" src="${createLink(controller:'cin', action:'file', params: [name: uiId])}" /></span><br />
	</div>

	</span>

</div>
