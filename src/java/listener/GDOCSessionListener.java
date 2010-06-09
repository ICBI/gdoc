package listener;

/**import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;**/
import javax.servlet.http.HttpSessionListener; 
import javax.servlet.http.HttpSessionEvent;
import org.codehaus.groovy.grails.commons.ApplicationHolder;
import serviceinterfaces.SessionCleanerServiceInt;
import org.springframework.context.ApplicationContext;


public class GDOCSessionListener implements HttpSessionListener { 
	//private static final Log log = LogFactory.getLog(GDOCSessionListener.class);

	public void sessionCreated(HttpSessionEvent event) { 
		System.out.println("Session created"); 
		
		
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		System.out.println("Session destroyed."); 
		String userId = (String) event.getSession().getAttribute("userId");
		ApplicationContext ctx = (ApplicationContext)ApplicationHolder.getApplication().getMainContext();
		SessionCleanerServiceInt service = (SessionCleanerServiceInt) ctx.getBean("cleanupService");
		if(service != null && userId != null){
			System.out.println(userId + " has destroyed session, cleanup temp data");
			service.cleanup(userId);
		}
	}
}