package listener;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpSessionListener; 
import javax.servlet.http.HttpSessionEvent;
import org.codehaus.groovy.grails.commons.ApplicationHolder;
import serviceinterfaces.SessionCleanerServiceInt;
import org.springframework.context.ApplicationContext;
import java.util.HashSet;

public class GDOCSessionListener implements HttpSessionListener { 
	private static final Log log = LogFactory.getLog(GDOCSessionListener.class);

	public void sessionCreated(HttpSessionEvent event) { 
		log.debug("Session created"); 
		
		
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		log.debug("Session destroyed."); 
		String userId = (String) event.getSession().getAttribute("userId");
		HashSet tempLists = (HashSet) event.getSession().getAttribute("tempLists");
		HashSet tempAnalyses = (HashSet) event.getSession().getAttribute("tempAnalyses");
		ApplicationContext ctx = (ApplicationContext)ApplicationHolder.getApplication().getMainContext();
		SessionCleanerServiceInt service = (SessionCleanerServiceInt) ctx.getBean("cleanupService");
		if(service != null && userId != null && (tempLists !=null) && (tempAnalyses!=null)){
			log.debug(userId + " has destroyed session, cleanup temp data");
			if(!(tempLists.isEmpty()) || !(tempAnalyses.isEmpty())){
				log.debug(userId + " found temporary data");
				service.cleanup(userId,tempLists,tempAnalyses);
			}	
		}
	}
}