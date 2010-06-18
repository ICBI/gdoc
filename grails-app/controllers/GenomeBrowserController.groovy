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
	
    def view = { 
	
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
		
		def genes = [:]
		genes.url = "/content/data/tracks/{refseq}/refseq/trackData.json"
		genes.type = "FeatureTrack"
		genes.label = "refseq"
		genes.key = "RefSeq Genes"
		
		tracks << genes
		
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
		
		if(!params.omicsData) {
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
		if(params.dataType != "Patient") {
			println "got request for: ${request.forwardURI}"
			def link = request.forwardURI.replace("/gdoc/genomeBrowser", "/content")
			redirect(url: link)
		}
		println params.chromosome + " : " + params.dataType + " : " + params.id
		def jsonResponse = [:]
		jsonResponse.headers = ["start","end","strand","id","name","phase"]
		def chromosome = params.chromosome.replace("chr", "")
		jsonResponse.featureNCList = buildFeatures(params.id, chromosome)
		jsonResponse.featureCount = jsonResponse.featureNCList.size()
		
		jsonResponse.key = params.dataType
		jsonResponse.className = "copyNumber"
		jsonResponse.clientConfig = null
		jsonResponse.arrowheadClass = null
		jsonResponse.rangeMap = []
		jsonResponse.label = "Copy Number"
		jsonResponse.type = "FeatureTrack"
		jsonResponse.subfeatureHeaders = []
		jsonResponse.sublistIndex = 1
		render jsonResponse as JSON
	}
	
	private buildFeatures(patientId, chromosome) {
		StudyContext.setStudy("INDIVDEMO")
		def analysis = ReductionAnalysis.findAll()
		println analysis
		def reduction = analysis.find {
			it.biospecimen.patient.id.toString() == patientId
		}
		if(!reduction)
			return []
		def values = reduction.locationValues.findAll {
			it.chromosome == chromosome
		}
		def returnVals = []
		values.each {
			def color = 0
			if(it.value > 1.75)
				color = 1
			if(it.value > 1.9)
				color = 2
			if(it.value > 2)
				color = 3
			if(it.value > 2.1)
				color = 4	
			if(it.value > 2.2)
				color = 5
			if(it.value > 2.3)
				color = 6
			if(it.value > 2.5)
				color = 7				
			returnVals << [it.startPosition, it.endPosition, 1, it.id, "", color, it.value]
		}
		return returnVals.sort { it[0] }
	}
	
}
