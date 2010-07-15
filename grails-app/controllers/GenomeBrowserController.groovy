import grails.converters.*

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
		
		def diseases = session.myStudies.collect{it.cancerSite}
		diseases.remove("N/A")
		def myStudies = session.myStudies
		[diseases:diseases as Set,myStudies:myStudies]
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
			sequence.seqDir = "/content/data/seq/${it}"
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
		args.url = "/content/data/seq/{refseq}/"
		args.type = "SequenceTrack"
		args.label = "DNA"
		args.key = "DNA"
		tracks << args
		
		def chr = [:]
		chr.url = "/content/data/tracks/{refseq}/ChromosomeBand/trackData.json"
		chr.type = "FeatureTrack"
		chr.label = "ChromosomeBand"
		chr.key = "Cytobands"
		
		tracks << chr
		
		def geneSymbols = [:]
		geneSymbols.url = "/content/data/tracks/{refseq}/genes/trackData.json"
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
		omim.url = "/content/data/tracks/{refseq}/omim/trackData.json"
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
		snp.url = "/content/data/tracks/{refseq}/snp/trackData.json"
		snp.type = "FeatureTrack"
		snp.label = "snp"
		snp.key = "SNPs (130)"
		
		tracks << snp
		
		def mirna = [:]
		mirna.url = "/content/data/tracks/{refseq}/mirna/trackData.json"
		mirna.type = "FeatureTrack"
		mirna.label = "mirna"
		mirna.key = "sno/miRNA"
		
		tracks << mirna
		
		def genes = [:]
		genes.url = "/content/data/tracks/{refseq}/refseq/trackData.json"
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
			it.biospecimen.patient
		}
		
		patients.each {
			def patientTrack = [:]
			patientTrack.url = "/content/data/tracks/{refseq}/patient_${it.id}.wig.json"
			patientTrack.label = "patient_${it.id}.wig"
			patientTrack.type = "ImageTrack"
			def clinicalDataString = ""
			it.clinicalData.keySet().sort().each{ key ->
				clinicalDataString += "${key}: ${it.clinicalData[key]}<br/>"
			}
			patientTrack.key = "<div class=\"patientTooltip\" title=\"${clinicalDataString}\">Patient ${it.id}</div>"

			tracks << patientTrack
		}
		
		session.tracks = tracks as JSON

	}

	def data = {
		log.debug "got request for: ${request.forwardURI}"
		def link = request.forwardURI.replace("/gdoc/genomeBrowser", "/content")
		redirect(url: link)

	}
	
}
