import grails.converters.*

@Mixin(ControllerMixin)
@Extension(type=ExtensionType.SEARCH, menu="Browse Genome")
class GenomeBrowserController {

	def index = {
		def chromosomes = 1..22
		def chrs = []
		chromosomes.each {
			chrs << it
		}
		chrs << "X"
		chrs << "Y"
		session.chromosomes = chrs
		
		[diseases:getDiseases(),myStudies:session.myStudies]
	}
	
    def view = { GenomeBrowserCommand cmd ->
	
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		}
		
		if(params.searchType == 'feature')
			session.browseLocation = cmd.hiddenLocation
		else {
			log.debug params.location
			if(!params.location || cmd.location == '')
				params.location = '1..2'
			session.browseLocation = "CHR" + cmd.chromosome + ":" + cmd.location + ".." + cmd.location
		}
		session.showTracks = cmd.trackMatch
		// Create refrence sequences
		def refSeqs = []
		
		def chromosomes = 1..22
		def chrs = []
		chromosomes.each {
			chrs << it
		}
		chrs << "X"
		chrs << "Y"
		chrs.each {
			def chromosome = Feature.findByChromosomeAndType(it, "CHROMOSOME")
			def sequence = [:]
			sequence.length = chromosome.endPosition
			sequence.name = "chr${it}"
			sequence.seqDir = "/content/jbrowse/seq/${it}"
			sequence.seqChunkSize = 20000
			sequence.end = chromosome.endPosition
			sequence.start = 0
			refSeqs << sequence
		}
		session.sequences = refSeqs as JSON
		
		// Create tracks
		def tracks = []
		
		def args = [:]
		args.args = ["chunkSize" : 20000]
		args.url = "/content/jbrowse/seq/{refseq}/"
		args.type = "SequenceTrack"
		args.label = "DNA"
		args.key = "DNA"
		tracks << args
		
		def chr = [:]
		chr.url = "/content/jbrowse/tracks/{refseq}/ChromosomeBand/trackData.json"
		chr.type = "FeatureTrack"
		chr.label = "ChromosomeBand"
		chr.key = "Cytobands"
		
		tracks << chr
		
		def geneSymbols = [:]
		geneSymbols.url = "/content/jbrowse/tracks/{refseq}/genes/trackData.json"
		geneSymbols.type = "FeatureTrack"
		geneSymbols.label = "genes"
		geneSymbols.key = "Genes"
		
		tracks << geneSymbols
		
		
/*		def gwas = [:]
		gwas.url = "/content/data/tracks/{refseq}/gwas/trackData.json"
		gwas.type = "FeatureTrack"
		gwas.label = "gwas"
		gwas.key = "GWAS Catalog"
		
		tracks << gwas*/
		
		def omim = [:]
		omim.url = "/content/jbrowse/tracks/{refseq}/omim/trackData.json"
		omim.type = "FeatureTrack"
		omim.label = "omim"
		omim.key = "OMIM Genes"
		
		tracks << omim
		
/*		def mrna = [:]
		mrna.url = "/content/data/tracks/{refseq}/mRNA/trackData.json"
		mrna.type = "FeatureTrack"
		mrna.label = "mRNA"
		mrna.key = "mRNA"
		
		tracks << mrna*/
		
		def snp = [:]
		snp.url = "/content/jbrowse/tracks/{refseq}/snp/trackData.json"
		snp.type = "FeatureTrack"
		snp.label = "snp"
		snp.key = "SNPs (130)"
		
		tracks << snp
		
		def mirna = [:]
		mirna.url = "/content/jbrowse/tracks/{refseq}/mirna/trackData.json"
		mirna.type = "FeatureTrack"
		mirna.label = "mirna"
		mirna.key = "sno/miRNA"
		
		tracks << mirna
		
		def genes = [:]
		genes.url = "/content/jbrowse/tracks/{refseq}/refseq/trackData.json"
		genes.type = "FeatureTrack"
		genes.label = "refseq"
		genes.key = "RefSeq Genes"
		
		tracks << genes
		
		if(!params.omicsData || !session.study) {
			session.tracks = tracks as JSON
			return
		}
		
		StudyContext.setStudy(session.study.schemaName)
		def analysis = ReductionAnalysis.findAll()
		
		if(!analysis) {
			session.tracks = tracks as JSON
			return
		}
			
		def patients = analysis.collect {
			if(it.biospecimen)
				[it.biospecimen.patient, it.name]
			else
				[it.name, it.name]
		}
		log.debug "PATIENTSS: " + patients
		patients.each { reduction ->
			def patientTrack = [:]
			patientTrack.url = "/content/data/tracks/{refseq}/${reduction[1]}.wig.json"
			patientTrack.label = "${reduction[1]}.wig"
			patientTrack.type = "ImageTrack"
			def clinicalDataString = ""
			if(reduction[0].metaClass.hasProperty(reduction[0], "clinicalData")) {
				log.debug "IN ${reduction[0].clinicalData}, ${reduction[1]}"
				reduction[0].clinicalData.keySet().sort().each{ key ->
					clinicalDataString += "${key}: ${reduction[0].clinicalData[key]}<br/>"
				}
			} else {
				clinicalDataString += reduction[0]
			}
			patientTrack.key = "<div class=\"patientTooltip\" title=\"${clinicalDataString}\">${reduction[1]}</div>"

			tracks << patientTrack
		}
		
		session.tracks = tracks as JSON

	}

	def data = {
		log.debug "got request for: ${request.forwardURI}"
		def link = request.forwardURI.replace("/gdoc/genomeBrowser", "/content/jbrowse")
		redirect(url: link)

	}
	
}
