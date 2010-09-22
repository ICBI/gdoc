<g:javascript library="jquery"/>
<jq:plugin name="autocomplete"/>
<g:javascript>



  jQuery(document).ready(function(){
   	jQuery("#q").autocomplete("/gdoc/search/relevantTerms",{
			max: 130,
			scroll: true,
			multiple:false,
			matchContains: true,
			dataType:'json',
			parse: function(data){
				var array = jQuery.makeArray(data);
				for(var i=0;i<data.length;i++) {
 					var tempValue = data[i];
					var tempResult = data[i];
					array[i] = { data:data[i], value: tempValue, result: tempResult};
			    }
				return array;
			},
            formatItem: function(data, i, max) {
						return data;
					},
			
			formatResult: function(data) {
						return data;
					}
	});
	
	if($('#loginName')) {
		$("#loginName").click(function(){
			$(this).val("");
			$(this).css("color","black");
		})
	}
	
	
  });


</g:javascript>


<table border="0" cellpadding="0" cellspacing="0" width="100%"><tr><td>
<a href="/gdoc"><img src="${createLinkTo(dir:'images',file:'gdocLogo.png')}" border="0" alt="G-DOC logo" /></a>
</td><td valign="bottom" style="text-align:right;padding:7px">


<g:if test="${!session.userId}">
<g:if test="${flash.cmd instanceof LoginCommand && flash.message}">
<div align="right" id="success" style="color:white">${flash.message}</div>
</g:if>
<g:form name="loginForm" url="[controller:'login',action:'login']" update="[success:'message',failure:'error']">
       <input name="loginName" id="loginName" type="text" style="color:gray" size="10" onclick="clear()" value="net-Id"></input>
       <input name="password" id="password" type="password" size="10"></input>
       <input type="submit" value="login" />
</g:form >
<span style="color:white;padding-top:8px;font-size:.9em">Not registered? <g:link controller="registration" style="color:white">register now</g:link> (NetId required)</span>
</g:if>
<g:else>
<div style="float:left;margin-top: 20px;">
	<g:form autocomplete="off" controller="search" action="index">
	
	 <input name="q" id="q" type="text" value="" size="15"></input>
	
	<input type="submit" value="search gdoc" />
	</g:form>
	
</div>
<div style="float:right;color:#f2f2f2">
	<div>Logged in as: ${session.userId}</div>
	<div>
		<g:if test="${session.isGdocAdmin}">
		<g:link style="color:#f2f2f2" controller="admin">Admin</g:link>
		<span style="font-weight:bold;color:#fff;padding-left:5px;padding-right:5px">|</span>
		</g:if>
		<g:link style="color:#f2f2f2" action="logout" controller="login" update="success">Logout</g:link>
	</div>
</div>
</g:else>



</td>
</tr></table>