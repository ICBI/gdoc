import edu.georgetown.lombardi.gdoc.genepattern.EncryptionUtil

class GdocTagLib {
	def navigationLink = { attrs, body ->
		if (attrs.controller == params.controller && !attrs.id){
			out << "<h3>${attrs.name}</h3>"
		} else {
			if(attrs.id) {
				out << link([controller: attrs.controller, id: attrs.id]){attrs.name}
			} else {
				out << link([controller: attrs.controller]){attrs.name}
			}
		}
	}
	
	def panel = { attrs, body ->
		out << render(template: '/common/panel', bean: [body: body, attrs: attrs])
	}
	
	def validationInput = { attrs, body ->
		out << render(template: '/common/validation_input', bean: [body: body, attrs: attrs])
	}
	
	def flex = { attrs, body ->
		out << render(template: '/common/flex_content', bean: [body: body, attrs: attrs])
	}
	
	def genePatternId = { attrs ->
		def encryptedId = EncryptionUtil.encrypt(session.userId)
		out << encryptedId
	}
}
