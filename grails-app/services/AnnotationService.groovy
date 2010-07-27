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
		def criteria = Reporter.createCriteria()
		def reporters = criteria {
			projections {
				property('name')
			}
			arrayDesigns {
				eq("platform", platformName)
			}
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
}