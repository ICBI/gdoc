import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource
import org.springframework.beans.factory.InitializingBean

class FileBasedAnnotationService implements InitializingBean {
	
	def annotations
	
	void afterPropertiesSet() {
		//Resource resource = new ClassPathResource("/WEB-INF/data/Affy_U133Plus2_reporter_symbol_entrezid.tab")
		
	}
	
}