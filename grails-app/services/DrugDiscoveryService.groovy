@DataExtension(type=DataExtensionType.GENE, label="Target Data")
class DrugDiscoveryService {
	def annotationService
	def grailsApplication
	
	/**
	Currently finds all molecule targets for a gene alias
	**/
	def findTargetsFromAlias(alias) {
		def bindings = []
		def geneAlias = annotationService.findGeneByAlias(alias)
		if(geneAlias){
			def gene = geneAlias.gene
			//println "Gene alias $geneAlias.symbol has gene"
			if(gene){
				def proteins = []
				proteins = gene.proteins
				if(proteins && proteins.bindings){
					//println "Gene $gene.title transcribes proteins"
					//println "bindings were found from $geneAlias"
					proteins.each{
						bindings.addAll(it.bindings)
					}
					return bindings
				}else return false
			}else return false
		}else return false
		
    }

	def createLink(item) {
		def targets = findTargetsFromAlias(item)
		def targetLinks = []
		if(targets){
			targets.each{ target ->
				def link = "<a href='/${grailsApplication.metadata['app.name']}/moleculeTarget/show/"+target.id+"'>"+target+"</a>"
				targetLinks << link
			}
		}
		if(!targetLinks)
			return ""
		return targetLinks.toString()
	}
	
}