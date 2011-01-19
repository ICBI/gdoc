<g:javascript library="jquery"/>
<g:javascript library="jquery-1.3.2.min.js"/>

<div>
<span>				
	<div id="cin-heatmap-view-container" >
		<div id="cin-heatmap-container" >
			<div id="cin-chr-selector"></div>
			<img src="${createLinkTo(dir: 'images', file: 'transparentMap.gif')}" width='480' height='80' border='0' alt='' usemap='#chrMapView' class='map' />
			<map name='chrMapView' id='chrMapView'>
				<area id='chr1' shape='rect' alt='Chromosome 1' coords='85,25, 101,105' href='#chr1' onmouseover="hovers(true, 85,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr2' shape='rect' alt='Chromosome 2' coords='101,25, 118,105' href='#chr2' onmouseover="hovers(true, 101,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr3' shape='rect' alt='Chromosome 3' coords='118,25, 134,105' href='#chr3' onmouseover="hovers(true, 118,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr4' shape='rect' alt='Chromosome 4' coords='134,25, 150,105' href='#chr4' onmouseover="hovers(true, 134,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr5' shape='rect' alt='Chromosome 5' coords='150,25, 167,105' href='#chr5' onmouseover="hovers(true, 150,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr6' shape='rect' alt='Chromosome 6' coords='167,25, 184,105' href='#chr6' onmouseover="hovers(true, 167,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr7' shape='rect' alt='Chromosome 7' coords='184,25, 201,105' href='#chr7' onmouseover="hovers(true, 184,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr8' shape='rect' alt='Chromosome 8' coords='201,25, 217,105' href='#chr8' onmouseover="hovers(true, 201,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr9' shape='rect' alt='Chromosome 9' coords='217,25, 234,105' href='#chr9' onmouseover="hovers(true, 217,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr10' shape='rect' alt='Chromosome 10' coords='234,25, 250,105' href='#chr10' onmouseover="hovers(true, 234,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr11' shape='rect' alt='Chromosome 11' coords='250,25, 267,105' href='#chr11'  onmouseover="hovers(true, 250,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr12' shape='rect' alt='Chromosome 12' coords='267,25, 284,105' href='#chr12' onmouseover="hovers(true, 267,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr13' shape='rect' alt='Chromosome 13' coords='284,25, 300,105' href='#chr13' onmouseover="hovers(true, 284,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr14' shape='rect' alt='Chromosome 14' coords='300,25, 317,105' href='#chr14'  onmouseover="hovers(true, 300,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr15' shape='rect' alt='Chromosome 15' coords='317,25, 334,105' href='#chr15' onmouseover="hovers(true, 317,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr16' shape='rect' alt='Chromosome 16' coords='334,25, 350,105' href='#chr16' onmouseover="hovers(true, 334,29, 7,44)" onmouseout="hovers(false)"  />
				<area id='chr17' shape='rect' alt='Chromosome 17' coords='350,25, 367,105' href='#chr17' onmouseover="hovers(true, 350,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr18' shape='rect' alt='Chromosome 18' coords='367,25, 384,105' href='#chr18' onmouseover="hovers(true, 367,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr19' shape='rect' alt='Chromosome 19' coords='384,25, 400,105' href='#chr19' onmouseover="hovers(true, 384,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr20' shape='rect' alt='Chromosome 20' coords='400,25, 417,105' href='#chr20'  onmouseover="hovers(true, 400,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr21' shape='rect' alt='Chromosome 21' coords='417,25, 434,105' href='#chr21' onmouseover="hovers(true, 417,29, 7,44)" onmouseout="hovers(false)" />
				<area id='chr22' shape='rect' alt='Chromosome 22' coords='434,25, 450,105' href='#chr22' onmouseover="hovers(true, 434,29, 7,44)" onmouseout="hovers(false)" />
				
			</map>
			<img src="${createLink(controller:'cin', action:'file', params: [name: 'heatmap'])}" width='480' height='480' class="chrBg" />
		</div>
						
		<div id="cin-cytobands-container">
			<g:each var="i" in="${ (1..22) }">
				<g:set var="chr" value='chromosome_${i}' />
				<ul class="chr${i}-list">
					<span><img class="" src="${createLink(controller:'cin', action:'file', params: [name: chr])}" /></span>
				</ul>
			</g:each>		
		</div>

	</div>

	</span>

</div>
