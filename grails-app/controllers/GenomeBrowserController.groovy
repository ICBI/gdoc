import grails.converters.*

class GenomeBrowserController {

    def index = { 
		def tracks = []
		
		def args = [:]
		args.args = ["chunkSize" : 20000]
		args.url = "data/seq/{refseq}/"
		args.type = "SequenceTrack"
		args.label = "DNA"
		args.key = "DNA"
		tracks << args
		
		def chr = [:]
		chr.url = "data/tracks/{refseq}/Cytobands/trackData.json"
		chr.type = "FeatureTrack"
		chr.label = "Cytobands"
		chr.key = "Cytobands"
		
		tracks << chr
		
		def genes = [:]
		genes.url = "data/tracks/{refseq}/UCSCGenes/trackData.json"
		genes.type = "FeatureTrack"
		genes.label = "UCSCGenes"
		genes.key = "UCSC Genes"
		
		tracks << genes
		
		def gwas = [:]
		gwas.url = "data/tracks/{refseq}/GWAS/trackData.json"
		gwas.type = "FeatureTrack"
		gwas.label = "GWAS"
		gwas.key = "GWAS Catalog"
		
		tracks << gwas
		
		def omim = [:]
		omim.url = "data/tracks/{refseq}/OMIM/trackData.json"
		omim.type = "FeatureTrack"
		omim.label = "OMIM"
		omim.key = "OMIM Genes"
		
		tracks << omim
		
		def mrna = [:]
		mrna.url = "data/tracks/{refseq}/mRNA/trackData.json"
		mrna.type = "FeatureTrack"
		mrna.label = "mRNA"
		mrna.key = "mRNA"
		
		tracks << mrna
		
		def snp = [:]
		snp.url = "data/tracks/{refseq}/SNP/trackData.json"
		snp.type = "FeatureTrack"
		snp.label = "SNPs-130"
		snp.key = "SNPs (130)"
		
		tracks << snp
		
		StudyContext.setStudy("INDIVDEMO")
		def analysis = ReductionAnalysis.findAll()
		def patients = analysis.collect {
			it.biospecimen.patient
		}
		
		patients.each {
			def patientTrack = [:]
			patientTrack.url = "genomeBrowser/data/{refseq}/Patient/${it.id}"
			patientTrack.label = "Patient${it.id}"
			patientTrack.type = "FeatureTrack"
			patientTrack.key = "Patient ${it.id}"

			tracks << patientTrack
		}
		
		session.tracks = tracks as JSON
		
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
			sequence.seqDir = "data/seq/${it}"
			sequence.seqChunkSize = 20000
			sequence.end = chromosome.endPosition
			sequence.start = 0
			refSeqs << sequence
		}
		session.sequences = refSeqs as JSON
	}

	def data = {
		println params.chromosome + " : " + params.dataType + " : " + params.id
		def jsonResponse = [:]
		jsonResponse.headers = ["start","end","strand","id","name","phase"]
		//jsonResponse.featureNCList = [ [9999,11500,1,103,"","1"], [10000,11600,1,104,"","0"], [12999,17200,1,105,"","0"], [14000,18200,1,108,"","1"], [19000,23000,1,107,"", "2"]]
		def chromosome = params.chromosome.replace("chr", "")
		println "CHROMOSOME : ${chromosome}"
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
