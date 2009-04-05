<g:javascript>
$(document).ready(function()
{
	slide('#sliding-navigation', 25, 15, 150, .8);
});
</g:javascript>
<div id="navigation-block">
    <ul id="sliding-navigation">
        <li class="sliding-element"><g:navigationLink name="Home" controller="home"/></li>
        <li class="sliding-element"><a href="#">Search for Data</a>
				<ul class="Menu">
					<li><a href="#">Clinical</a></li>
					<li><a href="#">Genomic</a></li>
				</ul>
	    </li>
        <!--li class="sliding-element"><g:navigationLink name="Data Elements" controller="dataElements"/></li-->
        <li class="sliding-element"><g:navigationLink name="Perform Analysis" controller="analysis"/></li>
        <li class="sliding-element"><a href="#">Help</a></li>
    </ul>
</div>







