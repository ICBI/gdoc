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
		
		def copyNumberTrack = [:]
		copyNumberTrack.url = "genomeBrowser/data/{refseq}/CopyNumber"
		copyNumberTrack.label = "CopyNumber"
		copyNumberTrack.type = "FeatureTrack"
		copyNumberTrack.key = "Copy Number"
		
		tracks << copyNumberTrack
		
		session.tracks = tracks as JSON
		
		def refSeqs = []
		def sequence = [:]
		sequence.length = 50000
		sequence.name = "1"
		sequence.seqDir = "data/seq/1"
		sequence.seqChunkSize = 20000
		sequence.end = 50000
		sequence.start = 0
		refSeqs << sequence
		
		def sequence2 = [:]
		sequence2.length = 100000
		sequence2.name = "2"
		sequence2.seqDir = "data/seq/2"
		sequence2.seqChunkSize = 20000
		sequence2.end = 100000
		sequence2.start = 0
		refSeqs << sequence2
		
		session.sequences = refSeqs as JSON
	}

	def data = {
		println params.chromosome + " : " + params.dataType
		def jsonResponse = [:]
		jsonResponse.headers = ["start","end","strand","id","name","phase"]
		jsonResponse.featureCount = 5
		jsonResponse.featureNCList = [ [9999,11500,1,103,"","1"], [10000,11600,1,104,"","0"], [12999,17200,1,105,"","0"], [14000,18200,1,108,"","1"], [19000,23000,1,107,"", "2"]]
		jsonResponse.key = params.dataType
		jsonResponse.className = "patient"
		jsonResponse.clientConfig = null
		jsonResponse.arrowheadClass = null
		jsonResponse.rangeMap = []
		jsonResponse.label = "Copy Number"
		jsonResponse.type = "FeatureTrack"
		jsonResponse.subfeatureHeaders = []
		jsonResponse.sublistIndex = 1
		render jsonResponse as JSON
	}
	
}
