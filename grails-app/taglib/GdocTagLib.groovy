import edu.georgetown.lombardi.gdoc.genepattern.EncryptionUtil
import org.apache.commons.lang.StringUtils

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
		if(session.userId) {
			def encryptedId = EncryptionUtil.encrypt(session.userId)
			out << encryptedId
		}
	}
	
	def managedCheckBox = { attrs ->
	          def value = attrs.remove('value')
	          def name = attrs.remove('name')
	          def disabled = attrs.remove('disabled')
	          if(!value) value = false
	          out << '<input type="hidden" '
	          out << "name=\"_${name}\" />"
	          out << '<input type="checkbox" '
	          out << "name=\"${name}\" "
	          if(value != 'false') {
	                out << "checked=\"$value\" "
	          }
	          if(disabled)
	          {
	          	out << 'disabled="disabled" '
	          }
	        // process remaining attributes
	        //outputAttributes(attrs)

	// close the tag, with no body 
			out << ' />' 
	}
	
}
