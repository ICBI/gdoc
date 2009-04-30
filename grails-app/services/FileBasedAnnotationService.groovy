import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource
import org.springframework.beans.factory.InitializingBean

class FileBasedAnnotationService {
	
	def annotations
	
	def findReportersForGene(gene) {
		def reporters = []
		annotations.each {
			if(it.value.toLowerCase() == gene.toLowerCase())
				reporters << it.key
		}
		return reporters
	}
	
}