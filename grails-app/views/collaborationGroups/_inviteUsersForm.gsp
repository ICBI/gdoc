<g:javascript library="jquery"/>
<g:javascript src="jquery/scrollTable/scrolltable.js"/>
<g:javascript src="jquery/scrollTable/jscrolltable.js"/>
<jq:plugin name="ui"/>
<jq:plugin name="autocomplete"/>
<g:javascript>
$(document).ready(function(){
   	$("#q").autocomplete("/gdoc/search/userAutocomplete",{
			max: 130,
			scroll: true,
			multiple:false,
			matchContains: true,
			dataType:'json',
			parse: function(data){
				var array = jQuery.makeArray(data);
				for(var i=0;i < data.length;i++) {
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


  });
</g:javascript>
<g:if test="${managedMemberships}">
<div style="padding:5px;background-color:#f2f2f2;">
 <g:form action="showUsers" autocomplete="off">
	Enter user last name or username: <g:textField name="userId" id="q" /><br /><br />
	<g:submitButton name="search" value="Search Users" />
</g:form>
</div>
</g:if>
<g:else>
You currently do not manage any collaboration groups of your own.
</g:else>

