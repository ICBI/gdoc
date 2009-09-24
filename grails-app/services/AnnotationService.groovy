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
		println "REPORTERS ${reporterNames}"
		return reporterNames
	}
	
	def findGeneForReporter(reporterId) {
		def reporter = Reporter.findByName(reporterId)
		return reporter.geneSymbol
	}
	
	def findReportersForGeneList(listName) {
		def lists = UserList.findAll()
		def list = lists.find { item ->
			item.name == listName
		}
		
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
	
	def findGeneByAlias(alias) {
		def geneAlias = GeneAlias.findBySymbol(alias.toUpperCase())
		if(geneAlias) {
			geneAlias = GeneAlias.findByGeneIdAndOfficial(geneAlias.geneId, true)
		}
		return geneAlias
	}
	
	def searchForReportersByGene(gene) {
		def criteria = Reporter.createCriteria()
		def reporters = criteria.list{
			eq("geneSymbol", gene.toUpperCase())
		}
		return reporters
	}
}