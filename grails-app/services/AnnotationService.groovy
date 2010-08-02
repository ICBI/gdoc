class AnnotationService {
	
	
	def findReportersForGene(gene) {
		def reporters = searchForReportersByGene(gene)
		if(!reporters) {
			def aliasGene = findGeneByAlias(gene)
			if(aliasGene)
				reporters = searchForReportersByGene(aliasGene.symbol)
		}
		def reporterNames = reporters.collect {
			it.name
		}
		log.debug "REPORTERS ${reporterNames}"
		return reporterNames
	}
	
	def findReportersForGeneAndFile(gene, file) {
		def platform = findPlatformForFile(file)
		def platformReporters = findReportersByPlatform(platform)
		def geneReporters = findReportersForGene(gene)
		platformReporters.retainAll(geneReporters)
		log.debug "GOT REPORTERS FOR PLATFORM $platformReporters"
		return platformReporters
	}
	
	def findGeneForReporter(reporterId) {
		def reporter = Reporter.findByName(reporterId)
		if(reporter)
			return reporter.geneSymbol
		else 
			return null
	}
	
	def findReportersForGeneList(listName) {
		def list = UserList.findByName(listName)
		
		def listValues = list.listItems.collect {item ->
			item.value
		}
		def reporters = []
		listValues.each {
			def items = findReportersForGene(it)
			reporters.addAll(items)
		}
		return reporters
	}
	
	def findReportersForReporterList(listName) {
		def list = UserList.findByName(listName)

		def listValues = list.listItems.collect {item ->
			item.value
		}
		return listValues
	}
	
	def findGeneByAlias(alias) {
		def geneAlias = GeneAlias.findBySymbol(alias.toUpperCase())
		//log.debug "found geneAlias $geneAlias.symbol"
		if(geneAlias) {
			geneAlias = GeneAlias.findByGeneAndOfficial(geneAlias.gene, true)
			//log.debug "found official geneAlias $geneAlias.symbol , $geneAlias.gene"
		}
		return geneAlias
	}
	
	def findReportersByPlatform(platformName) {
		def criteria = ArrayDesign.createCriteria()
		def reporters = criteria {
			projections {
				reporters {
					property('name')
				}
			}
			eq("platform", platformName)
		}
		return reporters
	}
	
	def searchForReportersByGene(gene) {
		def criteria = Reporter.createCriteria()
		def reporters = criteria.list{
			eq("geneSymbol", gene.toUpperCase())
		}
		return reporters
	}
	
	def findPlatformForFile(fileName) {
		def criteria = Sample.createCriteria()
		def platform = criteria {
			projections {
				design {
					distinct('platform')
				}
			}
			eq("file", fileName)
		}
		return platform[0]
	}
}