<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Venn Diagram</title>
	<meta name="layout" content="report" />
    <g:javascript library="jquery"/>
	<jq:plugin name="DOMWindow"/>
	
    <style type="text/css">
      div.example {margin 20px; padding: 20px;}
      div canvas {float: left;}
      div br {clear: both;}
      pre {padding-top: 10px; border: 2px dashed #999999; background: #dddddd;} 
    </style>
    <script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'excanvas.js')}"></script>
	
	<script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'canvasXpressNEW.min.js')}"></script>
	
	<g:javascript>
	function getData(){
		return ${vennNumbers};
	}
	
	function getVCompartments(){
		return ${compartments};
	}
	
	function getListNames(){
		return ${allListsNames};
	}
	
	function getGraphData(){
		return ${graphData};
	}
	
	function saveList(vennCircleName){
		var dictionary = ${dictionary};
		$('#ids').val(dictionary[vennCircleName]);
		if($('#ids').val() == ""){
			$('#example8Null').css("display","block");
			$('#saveForm').css("display","none");
		}
		else{
			$('#example8Null').css("display","none");
			$('#saveForm').css("display","block");
			var test =$('#ids').val().replace(",","--");
			$('#itemsDiv').html('<span>'+dictionary[vennCircleName]+'</span>');
		}
		$('.example8DOMWindow').trigger('click');
	}
	

      var showVenn = function () {
 		new CanvasXpress('canvas3',getData(),getGraphData());
		}
    </g:javascript>

	<g:javascript>
	$(document).ready( function () {
			$('.example8DOMWindow').click(function(){ 
			    $.openDOMWindow({ 
				    height:400,
			        loader:1, 
			        loaderImagePath:'animationProcessing.gif', 
			        loaderHeight:16,
			        loaderWidth:17, 
			        windowSourceID:'#example8Content' 
			    }); 
			    return false; 
			});
			$('.example5closeDOMWindow').closeDOMWindow({eventType:'click'}); 
			showVenn();

	} );
	
	</g:javascript>
  </head>
  <body onload="showVenn()">
	<br />
    <h1 style="font-size:1.2em">Venn Diagram</h1><br />
	<span id="label" style="display:block">
		
		${session.study?.shortName}</span>
	</span>
	<a href="#" class="example8DOMWindow" style="display:none">-</a>
	
	<div id="example8Content" align="left" style="display:none;text-align:left;"> 
		<div id="saveForm">
			<g:formRemote action="saveFromQuery"  name="saveFromQuery" url="${[action:'saveFromQuery']}" update="updateMe">
			<p style="font-size:1.1em;display:inline-table">Save your list </p>
			<table class="studyTable" style="width:55%;background-color:#f2f2f2">
				<g:if test="${tags}">
	   			<tr>
					<td>List Type: 
					</td>
					<td style="text-align:left">${tags}
						<g:hiddenField name="tags" value="${tags}" />
					</td>
				</tr>
			</g:if>
			<tr>
				<td>List Name:
				</td>
				<td>
					<g:textField name="name" size="15" maxlength="15" />
					<g:textField name="ids" value="" style="display:none" />
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:right">
					<input type="button" class="example5closeDOMWindow" value="Cancel" style="padding-right:5px"/>
					<g:submitButton name="submit" id="submitButton" value="Save"/>
				</td>
			</tr>
		</table>
		</g:formRemote><br />
		<div id="updateMe">
			
		</div>
		
      
      	<br />
		<p>The following items will be saved into a list viewable in the 'Saved Lists page' upon clicking 'Save' button</p>
		<div id="itemsDiv" style="border:1px solid gray;width:500px;height:130px;overflow: scroll;margin-top:10px"></div>
		
    	</div>
		<br />
		<div id="example8Null" align="left" style="display:none;text-align:left;">
		 	<p class="errorDetail" style="font-size:1.2em">The Venn area you've selected does not contain any items.</p><br />
		  </div>
		  <span class="example5closeDOMWindow" style="float:right;display:inline-table;cursor:pointer;text-decoration:underline">close</span>
		
	  </div>
	
	  
	 
	</div>
	<div class="canvas" style="margin:0 auto;width:100%">
      <canvas class="a" id="canvas3" width="600" height="600"></canvas>
      <br><br>
    </div>


  </body>
</html>
