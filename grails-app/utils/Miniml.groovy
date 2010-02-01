class MinimlParser {
	
	def debug = false
	
	def parse(filename) {
		def study = new XmlParser().parse(filename)
		println study.Series.Title.text()
		println study.Series.Summary.text()
		study.Series['Data-Table'].each {
			println it.Title.text()
		}
	}
	
	def createClinicalFile(inputFile, outputFile, strategy) {
		def clinicalValues = buildClinicalTable(inputFile, strategy)
		writeMatrixFile(clinicalValues, outputFile)
	}
	
	def createStudyFile(inputFile, outputFile, studyName) {
		def study = new XmlParser().parse(inputFile)
		def headers = ['SHORT_NAME', 'LONG_NAME', 'ABSTRACT', 'CANCER_SITE', 'PATIENT_ID_NAME', 'INTEGRATED', 
					   'OVERALL_ACCESS', 'SCHEMA_NAME', 'USE_IN_GUI', 'CONTENT_TYPES', 'SHOW_CONTENT_IN_GUI']
		def data = [:]
		data['SHORT_NAME'] = studyName
		data['LONG_NAME'] = study.Series.Title.text()
		def abstractText = study.Series.Summary.text().replace('\n', '<br/>')
		data['ABSTRACT'] = abstractText
		data['PATIENT_ID_NAME'] = 'Sample iid'
		data['INTEGRATED'] = 1
		data['OVERALL_ACCESS'] = 'PUBLIC'
		data['SCHEMA_NAME'] = studyName
		data['USE_IN_GUI'] = 1
		data['CONTENT_TYPES'] = 'CLINIC, MICROARRAY'
		data['SHOW_CONTENT_IN_GUI'] = '1, 1'
		writeStudyFile(outputFile, headers, data)
	}
	
	def writeStudyFile(outputFile, headers, data) {
		new File(outputFile).withWriter { out ->
			out.writeLine(headers.join('\t'))
			def temp = []
			headers.each {
				if(data[it])
					temp << data[it]
				else 
					temp << ""
			}
			out.writeLine(temp.join('\t'))
		}
	}
	
	def buildClinicalTable(filename, strategy) {
		def study = new XmlParser().parse(filename)
		def clinicalTable = [:]
		study.Sample.each { sample ->
			clinicalTable[sample.'@iid'] = strategy(sample)
		}
		return clinicalTable
	}
	
	def printSample(dataMap) {
		def sample = dataMap.iterator().next()
		println "Sample ID: " + sample.getKey()
		println "Clinical Attributes and Values: "
		sample.getValue().each {
			println "ATTRIBUTE: " + it.getKey() + " | VALUE: " + it.getValue()
		}
	}
	
	def writeMatrixFile(dataMap, filename) {
		new File(filename).withWriter { out ->
			def headers = dataMap.collect { key, value ->
				return value.keySet().collect {
					return it.toUpperCase()
				}
			}.flatten() as Set
			if(debug)
				println "\t" + headers.join("\t")
			out.writeLine("\t" + headers.join("\t"))
			dataMap.each { sampleId, values ->
				def data = [sampleId]
				headers.each { column ->
					if(values.get(column)) {
						data << values.get(column)
					} else {
						data << ""
					}
				}
				if(debug)
					println data.join("\t")
				out.writeLine(data.join("\t"))
			}
		}
	}
	
}

class MinimlHelpers {

	public static strategies = [ 
		{ sample ->
			return this.characteristicRegexParser(/(.*)?:(.*)?/, sample)
		},
		{ sample ->
			def clinicalVals = [:]
			sample.Channel.Characteristics.each { item ->
				if(item.'@tag') {
					clinicalVals[item.'@tag'.toUpperCase()] = item.text()
				} else {
					clinicalVals[sample.Channel.Characteristics.indexOf(item).toString().toUpperCase()] = item.text()
				}
			}
			return clinicalVals
		}
	]
	
	private static Closure characteristicRegexParser = { pattern, sample ->
		def clinicalVals
		sample.Channel.Characteristics.each { item ->
			item.value().each {
				clinicalVals = parseClinicalValues(pattern, it)
			}
		}
		return clinicalVals
	}
	
	private static def parseClinicalValues(pattern, text) {
		def matcher = ( text =~ pattern )
		def values = [:]
		matcher.each { match, attribute, value ->
			values[attribute.trim().toUpperCase()] = value.trim()
		}
		return values
	}
}

def parser = new MinimlParser(debug: true)
//parser.createClinicalFile("../data/desmedt/GSE16391_family.xml", "output.txt", MinimlHelpers.characteristicRegexParser.curry(/(.*)?:(.*)?/))

//parser.createClinicalFile("../data/zhang/GSE12093_family.xml", "output.txt", MinimlHelpers.characteristicRegexParser.curry(/(.*)?:(.*)?/))

//parser.createClinicalFile("../data/zhou/GSE7378_family.xml", "output.txt", MinimlHelpers.multiCharacteristicParser)

parser.createClinicalFile("/Users/acs224/dev/georgetown/gdoc-data-import/data/sotiriou/GSE2990_family.xml", "output.txt", MinimlHelpers.characteristicRegexParser.curry(/(.*?)?:(.*?)?(?:\/\/|\n)/))