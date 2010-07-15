class AttributeService {

    boolean transactional = true

    def createAll(attributes) {
		attributes.each {
			if(!CommonAttributeType.findByShortName(it.shortName)) {
				def att = new CommonAttributeType(it)
				if(!att.save(flush:true))
					log.debug att.errors
			}
		}
    }

	def addVocabsToAttribute(shortName, vocabs) {
		def attribute = CommonAttributeType.findByShortName(shortName)
		if(!attribute)
			return
		vocabs.each { item ->
			if(!attribute.vocabs.find { it.term == item.term}) {
				def vocab = new AttributeVocabulary(item)
				attribute.addToVocabs(vocab)
				if(!vocab.save(flush: true))
					log.debug vocab.errors
			}
		}
	}
}
