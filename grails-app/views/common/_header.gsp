<table border="0" cellpadding="0" cellspacing="0" width="100%"><tr><td>
<img src="${createLinkTo(dir:'images',file:'gdocLogo.png')}" border="0" />
</td><td valign="bottom" style="text-align:right;padding:7px">


<g:if test="${!session.userId}">
<g:if test="${flash.message}">
<div class="message">${flash.message}</div>
</g:if>
<g:form name="loginForm" url="[controller:'login',action:'login']" update="[success:'message',failure:'error']">
       <input name="username" type="text" size="10"></input>
       <input name="password" type="password" size="10"></input>
       <input type="submit" value="login" />
</g:form >
</g:if>
<g:else>
<div style="float:left;margin-top: 20px;">
	<g:form>
	 <input name="terms" type="text" value="" size="25"></input>
	<input type="submit" value="search gdoc" />
	</g:form>
</div>
<div style="float:right;">
	<div>welcome,${session.userId}</div>
	<div><g:remoteLink action="logout" controller="login" id="1" update="success"  onLoading="showSpinner();">logout</g:remoteLink></div>
</div>
</g:else>



</td>
</tr></table>