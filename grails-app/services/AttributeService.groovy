class AttributeService {

    boolean transactional = true

    def createAll(attributes) {
		attributes.each {
			if(!CommonAttributeType.findByShortName(it.shortName)) {
				def att = new CommonAttributeType(it)
				if(!att.save(flush:true))
					println att.errors
			}
		}
    }

	def addVocabsToAttribute(shortName, vocabs) {
		def attribute = CommonAttributeType.findByShortName(shortName)
		if(!attribute)
			return
		vocabs.each {
			def vocab = new AttributeVocabulary(it)
			attribute.addToVocabs(vocab)
			if(!vocab.save(flush: true))
				println vocab.errors
		}
	}
}
