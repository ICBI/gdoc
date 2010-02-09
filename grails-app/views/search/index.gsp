<%@ page import="org.springframework.util.ClassUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.SearchableUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.lucene.LuceneUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.util.StringQueryUtils" %>
<html>
  <head>

    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta name="layout" content="main" />
	<script type="text/javascript" src="/gdoc/js/jquery/jquery-1.3.2.js"></script>

		<script type="text/javascript" src="/gdoc/js/jquery/jquery.ui.js"></script>
	
    <title><g:if test="${params.q && params.q?.trim() != ''}">${params.q} - </g:if>G-DOC Search</title>
    <style type="text/css">
      


      #header form input {
        padding: .1em;
      }

      #header .hint {
        color: gray;
      }

      #header h1 a {
        text-decoration: none;
        
          color: dimgray;
      }

      #header h1 {
          
          float: left;
      }

      #header h1 span {
          
          color: #424242;
      }

      #header form {
          margin-left: 22em;
          padding-top: .1em;
      }

      .title {
        margin: 1em 0;
        padding: .3em .5em;
        text-align: right;
        background-color: seashell;
        border-top: 1px solid lightblue;
      }

      .result {
        margin-bottom: 1em;
      }

      .result .displayLink {
        color: green;
      }

      .result .name {
        font-size: larger;
      }

      .paging a.step {
        padding: 0 .3em;
      }

      .paging span.currentStep {
          font-weight: bold;
      }

    </style>
    <script type="text/javascript">
        var focusQueryInput = function() {
            document.getElementById("q").focus();
        }
    </script>
  </head>
  <body onload="focusQueryInput();">
	
  <div id="header">
    <%--h1><a href="http://grails.org/Searchable+Plugin" target="_blank">Grails <span>Searchable</span> Plugin</a></h1>
    <g:form url='[controller: "search", action: "index"]' id="searchableForm" name="searchableForm" method="get">
        <g:textField name="q" value="${params.q}" size="50"/> <input type="submit" value="Search" />
    </g:form--%>
    <div style="clear: both; display: none;" class="hint">See <a href="http://lucene.apache.org/java/docs/queryparsersyntax.html">Lucene query syntax</a> for advanced queries</div>
  </div>
  <div id="main">
    <g:set var="haveQuery" value="${params.q?.trim()}" />
    <g:set var="haveResults" value="${searchResult?.results}" />
    <div class="title">
      <span>
        <g:if test="${haveQuery && haveResults}">
          Showing <strong>${searchResult.offset + 1}</strong> - <strong>${searchResult.results.size() + searchResult.offset}</strong> of <strong>${searchResult.total}</strong>
          results for <strong>${params.q}</strong>
        </g:if>
        <g:else>
        &nbsp;
        </g:else>
      </span>
    </div>

    <g:if test="${haveQuery && !haveResults && !parseException}">
      <p>Nothing matched your query - <strong>${params.q}</strong></p>
      <g:if test="${!searchResult?.suggestedQuery}">
        <g:if test="${suggs}">
		  <p>Suggestions:</p>
	      <p>Did you mean any of the following?<br />
			<g:each in="${suggs}" var="suggestion" status="i">
				<g:link controller="search" action="index" params="[q: suggs[i]]">${suggs[i]}</g:link><br />
			</g:each>
		  </p>
	    </g:if>
      </g:if>
    </g:if>

    

    <g:if test="${parseException}">
      <p>Your query - <strong>${params.q}</strong> - is not valid.</p>
      <p>Suggestions:</p>
      <ul>
        <li>Fix the query: see <a href="http://lucene.apache.org/java/docs/queryparsersyntax.html">Lucene query syntax</a> for examples</li>
        <g:if test="${LuceneUtils.queryHasSpecialCharacters(params.q)}">
          <li>Remove special characters like <strong>" - [ ]</strong>, before searching, eg, <em><strong>${LuceneUtils.cleanQuery(params.q)}</strong></em><br />
              <em>Use the Searchable Plugin's <strong>LuceneUtils#cleanQuery</strong> helper method for this: <g:link controller="search" action="index" params="[q: LuceneUtils.cleanQuery(params.q)]">Search again with special characters removed</g:link></em>
          </li>
          <li>Escape special characters like <strong>" - [ ]</strong> with <strong>\</strong>, eg, <em><strong>${LuceneUtils.escapeQuery(params.q)}</strong></em><br />
              <em>Use the Searchable Plugin's <strong>LuceneUtils#escapeQuery</strong> helper method for this: <g:link controller="search" action="index" params="[q: LuceneUtils.escapeQuery(params.q)]">Search again with special characters escaped</g:link></em><br />
              <em>Or use the Searchable Plugin's <strong>escape</strong> option: <g:link controller="searchable" action="index" params="[q: params.q, escape: true]">Search again with the <strong>escape</strong> option enabled</g:link></em>
          </li>
        </g:if>
      </ul>
    </g:if>

    <g:if test="${haveResults}">
      <div class="results">
        <g:each var="result" in="${searchResult.results}" status="index">
          <div class="result">
            <g:set var="className" value="${ClassUtils.getShortName(result.getClass())}" />
				<g:if test="${className == 'StudyDataSource'}">
					<div>
						<g:link style="color:blue;font-size:1.2em" action="show" controller="studyDataSource" id="${result.id}">
							${result.shortName}
						</g:link> (StudyDataSource)
						<g:if test="${result.abstractText}">
							<g:set var="desc" value="${result.abstractText}" />
				            	<g:if test="${desc.size() > 120}">
									<g:set var="desc" value="${desc[0..120] + '...'}" />
								</g:if>
				            <div class="desc">${result.longName}:${desc.encodeAsHTML()}</div>
						</g:if>
						<span style="color:green">cancer site: ${result.cancerSite},PI:${result.pocs.lastName}</span>
					</div>
				</g:if>
            	<g:if test="${className == 'MoleculeTarget'}">
					<div>
						<a style="color:blue;font-size:1.2em" href="/gdoc/moleculeTarget?target=${result.protein.name}">
							${result.molecule.name}
						</a> (Target Molecule)
						<g:if test="${result.molecule.formula}">
							<g:set var="desc" value="${result.molecule.formula}" />
				            	<g:if test="${desc.size() > 120}">
									<g:set var="desc" value="${desc[0..120] + '...'}" />
								</g:if>
				            <div class="desc">${desc.encodeAsHTML()}</div>
						</g:if>
						<g:else>
						No binding data available
						</g:else>
						<span style="color:green">target: ${result.protein.name}</span>
					</div>
				</g:if>
            
          </div>
        </g:each>
      </div>

      <div>
        <div class="paging">
          <g:if test="${haveResults}">
              Page:
              <g:set var="totalPages" value="${Math.ceil(searchResult.total / searchResult.max)}" />
              <g:if test="${totalPages == 1}"><span class="currentStep">1</span></g:if>
              <g:else><g:paginate controller="searchable" action="index" params="[q: params.q]" total="${searchResult.total}" prev="&lt; previous" next="next &gt;"/></g:else>
          </g:if>
        </div>
      </div>
    </g:if>
  </div>
  </body>
</html>
