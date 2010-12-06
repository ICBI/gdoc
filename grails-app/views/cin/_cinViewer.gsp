<g:javascript library="jquery"/>
<g:javascript library="jquery-1.3.2.min.js"/>

<div>
<span> 
	<div id="cin-heatmap-view-container" >
		<div id="cin-heatmap-container">
			<div id="cin-chr-selector"></div>
			<img src="${createLinkTo(dir: 'images', file: 'transparentMap.gif')}" width='436' height='436' border='0' alt='' usemap='#chrMapView' class='map' />
			<map name='chrMapView' id='chrMapView'>
				<area id='chr1' shape='rect' alt='Chromosome 1' coords='380,130, 436,140' href='#chr1' onmouseover="hovers(true, 390,130, 30,4)" onmouseout="hovers(false)" />
				<area id='chr2' shape='rect' alt='Chromosome 2' coords='380,140, 436,150' href='#chr2' onmouseover="hovers(true, 390,142, 30,4)" onmouseout="hovers(false)" />
				<area id='chr3' shape='rect' alt='Chromosome 3' coords='380,150, 436,160 ' href='#chr3' onmouseover="hovers(true, 390,154, 30,4)" onmouseout="hovers(false)" />
				<area id='chr4' shape='rect' alt='Chromosome 4' coords='380,160, 436,170' href='#chr4' onmouseover="hovers(true, 390,165, 30,4)" onmouseout="hovers(false)" />
				<area id='chr5' shape='rect' alt='Chromosome 5' coords='380,170, 436,180 ' href='#chr5' onmouseover="hovers(true, 390,176, 30,4)" onmouseout="hovers(false)" />
				<area id='chr6' shape='rect' alt='Chromosome 6' coords='380,180, 436,195' href='#chr6' onmouseover="hovers(true, 390,189, 30,4)" onmouseout="hovers(false)" />
				<area id='chr7' shape='rect' alt='Chromosome 7' coords='380,195, 436,205' href='#chr7' onmouseover="hovers(true, 390,202, 30,4)" onmouseout="hovers(false)" />
				<area id='chr8' shape='rect' alt='Chromosome 8' coords='380,205, 436,215' href='#chr8' onmouseover="hovers(true, 390,213, 30,4)" onmouseout="hovers(false)" />
				<area id='chr9' shape='rect' alt='Chromosome 9' coords='380,215, 436,225' href='#chr9' onmouseover="hovers(true, 390,225, 30,4)" onmouseout="hovers(false)" />
				<area id='chr10' shape='rect' alt='Chromosome 10' coords='380,225, 436,240' href='#chr10' onmouseover="hovers(true, 390,237, 30,4)" onmouseout="hovers(false)" />
				<area id='chr11' shape='rect' alt='Chromosome 11' coords='380,240, 436,250' href='#chr11'  onmouseover="hovers(true, 390,248, 30,4)" onmouseout="hovers(false)" />
				<area id='chr12' shape='rect' alt='Chromosome 12' coords='380,250, 436,265' href='#chr12' onmouseover="hovers(true, 390,260, 30,4)" onmouseout="hovers(false)" />
				<area id='chr13' shape='rect' alt='Chromosome 13' coords='380,265, 436,275' href='#chr13' onmouseover="hovers(true, 390,273, 30,4)" onmouseout="hovers(false)" />
				<area id='chr14' shape='rect' alt='Chromosome 14' coords='380,275, 436,285' href='#chr14'  onmouseover="hovers(true, 390,285, 30,4)" onmouseout="hovers(false)" />
				<area id='chr15' shape='rect' alt='Chromosome 15' coords='380,285, 436,305' href='#chr15' onmouseover="hovers(true, 390,296, 30,4)" onmouseout="hovers(false)" />
				<area id='chr16' shape='rect' alt='Chromosome 16' coords='380,305, 436,320' href='#chr16' onmouseover="hovers(true, 390,308, 30,4)" onmouseout="hovers(false)"  />
				<area id='chr17' shape='rect' alt='Chromosome 17' coords='380,320, 436,330' href='#chr17' onmouseover="hovers(true, 390,320, 30,4)" onmouseout="hovers(false)" />
				<area id='chr18' shape='rect' alt='Chromosome 18' coords='380,330, 436,340' href='#chr18' onmouseover="hovers(true, 390,332, 30,4)" onmouseout="hovers(false)" />
				<area id='chr19' shape='rect' alt='Chromosome 19' coords='380,340, 436,350' href='#chr19' onmouseover="hovers(true, 390,344, 30,4)" onmouseout="hovers(false)" />
				<area id='chr20' shape='rect' alt='Chromosome 20' coords='380,350, 436,360' href='#chr20'  onmouseover="hovers(true, 390,356, 30,4)" onmouseout="hovers(false)" />
				<area id='chr21' shape='rect' alt='Chromosome 21' coords='380,360, 436,370' href='#chr21' onmouseover="hovers(true, 390,368, 30,4)" onmouseout="hovers(false)" />
				<area id='chr22' shape='rect' alt='Chromosome 22' coords='380,370, 436,380' href='#chr22' onmouseover="hovers(true, 390,380, 30,4)" onmouseout="hovers(false)" />
				
			</map>
			
			<img src="${createLink(controller:'cin', action:'file', params: [name: 'heatmap'])}" width='436' height='436' class="chrBg" />
		</div>
			<table >
				<tbody>			
					<tr>
						<td>
						
							<div id="cin-cytobands-container">
								<g:each var="i" in="${ (1..22) }">
									<g:set var="chr" value='chromosome_${i}' />
									<ul class="chr${i}-list">
										<li><h4 align='center'>chromosome ${i} cytobands CIN overview</h4></li>
										<span><img class="" src="${createLink(controller:'cin', action:'file', params: [name: chr])}" /></span>
									</ul>
								</g:each>		
							</div>
						</td>
					</tr>
		
				</tbody>
			</table>
	</div>

	</span>

</div>
