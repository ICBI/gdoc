<%--
  Created by IntelliJ IDEA.
  User: finn
  Date: 21.12.2008
  Time: 17:18:44
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>jQuery-plugin examples</title>

         <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'style.css')}" />

        <g:javascript library="jquery"/>

		<jq:jquery>
			<jq:toggle sourceId="toggler" targetId="toggled" />
		</jq:jquery>

    </head>
    <body>
        <h1>jQuery-plugin examples</h1>

		<div id="toggle">
			<h2>Toggle</h2>
			
			<div id="toggleDemo">
				<h3>Demo</h3>
				<a id="toggler" href="#" >Click me</a><br/><br/>
				<div id="toggled" style="width: 200px; height: 200px; background-color: #efefef;">Toggle me</div>
			</div>

			<div id="toggleSource">
				<h3>Source</h3>
<pre>
&lt;head&gt;
    <span style="color: #0000ff;">&lt;jq:jquery&gt;
        &lt;jq:toggle sourceId="toggler" targetId="toggled" /&gt;
    &lt;/jq:jquery&gt;</span>
&lt;/head&gt;
&lt;body&gt;
    &lt;a <span style="color: #0000ff;">id="toggler"</span> href="#" &gt;Toggle&lt;/a&gt;
    &lt;div <span style="color: #0000ff;">id="toggled"</span> style="width: 200px; height: 200px; background-color: #efefef;"&gt;Toggle me&lt;/div&gt;
&lt;/body&gt;
</pre>
			</div>
		</div>
    </body>
</html>