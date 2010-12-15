<g:javascript library="jquery"/>
<g:javascript library="jquery-1.3.2.min.js"/>

<div>
<span>
						
	<div id="cin-heatmap-view-container" >
		<div id="cin-heatmap-container">
			<div id="cin-chr-selector"></div>
			<img src="${createLinkTo(dir: 'images', file: 'transparentMap.gif')}" width='480' height='480' border='0' alt='' usemap='#chrMapView' class='map' />
			<map name='chrMapView' id='chrMapView'>
				<area id='chr1' shape='rect' alt='Chromosome 1' coords='153,80, 167,130' href='#chr1' onmouseover="hovers(true, 153,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr2' shape='rect' alt='Chromosome 2' coords='167,80, 181,130' href='#chr2' onmouseover="hovers(true, 167,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr3' shape='rect' alt='Chromosome 3' coords='181,80, 195,130' href='#chr3' onmouseover="hovers(true, 181,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr4' shape='rect' alt='Chromosome 4' coords='195,80, 207,130' href='#chr4' onmouseover="hovers(true, 195,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr5' shape='rect' alt='Chromosome 5' coords='207,80, 221,130' href='#chr5' onmouseover="hovers(true, 208,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr6' shape='rect' alt='Chromosome 6' coords='221,80, 235,130' href='#chr6' onmouseover="hovers(true, 222,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr7' shape='rect' alt='Chromosome 7' coords='235,80, 249,130' href='#chr7' onmouseover="hovers(true, 236,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr8' shape='rect' alt='Chromosome 8' coords='249,80, 263,130' href='#chr8' onmouseover="hovers(true, 250,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr9' shape='rect' alt='Chromosome 9' coords='263,80, 277,130' href='#chr9' onmouseover="hovers(true, 264,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr10' shape='rect' alt='Chromosome 10' coords='277,80, 291,130' href='#chr10' onmouseover="hovers(true, 277,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr11' shape='rect' alt='Chromosome 11' coords='291,80, 305,130' href='#chr11'  onmouseover="hovers(true, 291,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr12' shape='rect' alt='Chromosome 12' coords='305,80, 319,130' href='#chr12' onmouseover="hovers(true, 305,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr13' shape='rect' alt='Chromosome 13' coords='319,80, 333,130' href='#chr13' onmouseover="hovers(true, 319,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr14' shape='rect' alt='Chromosome 14' coords='333,80, 347,130' href='#chr14'  onmouseover="hovers(true, 333,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr15' shape='rect' alt='Chromosome 15' coords='347,80, 361,130' href='#chr15' onmouseover="hovers(true, 347,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr16' shape='rect' alt='Chromosome 16' coords='361,80, 375,130' href='#chr16' onmouseover="hovers(true, 361,80, 7,45)" onmouseout="hovers(false)"  />
				<area id='chr17' shape='rect' alt='Chromosome 17' coords='375,80, 389,130' href='#chr17' onmouseover="hovers(true, 375,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr18' shape='rect' alt='Chromosome 18' coords='389,80, 403,130' href='#chr18' onmouseover="hovers(true, 389,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr19' shape='rect' alt='Chromosome 19' coords='403,80, 417,130' href='#chr19' onmouseover="hovers(true, 403,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr20' shape='rect' alt='Chromosome 20' coords='417,80, 431,130' href='#chr20'  onmouseover="hovers(true, 417,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr21' shape='rect' alt='Chromosome 21' coords='431,80, 445,130' href='#chr21' onmouseover="hovers(true, 431,80, 7,45)" onmouseout="hovers(false)" />
				<area id='chr22' shape='rect' alt='Chromosome 22' coords='445,80, 459,130' href='#chr22' onmouseover="hovers(true, 445,80, 7,45)" onmouseout="hovers(false)" />
				
			</map>
			<img src="${createLink(controller:'cin', action:'file', params: [name: 'heatmap'])}" width='480' height='480' class="chrBg" />
		</div>
						
		<div id="cin-cytobands-container">
			<g:each var="i" in="${ (1..22) }">
				<g:set var="chr" value='chromosome_${i}' />
				<ul class="chr${i}-list">
					<li><b><p align='center'>chromosome ${i} cytobands CIN overview</p></b></li>
					<span><img class="" src="${createLink(controller:'cin', action:'file', params: [name: chr])}" /></span>
				</ul>
			</g:each>		
		</div>

	</div>

	</span>

</div>
