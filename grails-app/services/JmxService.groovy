import javax.management.MBeanServerConnection
import javax.management.ObjectName
import org.jboss.resource.connectionmanager.JBossManagedConnectionPoolMBean
import javax.management.MBeanServerInvocationHandler

class JmxService {
	def jndiTemplate
	/**
	 * This method will flush the connection pool for the current server environment.
	 * This is necessary after a data load since the connections are cached with the 
	 * current roles and new roles were added during the load process.
	**/
	def flushConnectionPool() {
		MBeanServerConnection server = (MBeanServerConnection) jndiTemplate.lookup("jmx/invoker/RMIAdaptor");
		ObjectName mbeanName = ObjectName.getInstance("jboss.jca:name=gdoc,service=ManagedConnectionPool");
		JBossManagedConnectionPoolMBean pool = (JBossManagedConnectionPoolMBean)MBeanServerInvocationHandler.newProxyInstance(server, mbeanName,JBossManagedConnectionPoolMBean.class, false);
		pool.flush()
	}
}