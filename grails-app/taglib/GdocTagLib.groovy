class GdocTagLib {
	def navigationLink = { attrs, body ->
		if (attrs.controller == params.controller){
			out << "<h3>${attrs.name}</h3>"
		} else {
			out << link([controller: attrs.controller]){attrs.name}
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
}
