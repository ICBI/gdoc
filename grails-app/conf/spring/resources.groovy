// Place your Spring DSL code here
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

beans = {
    entityInterceptor(StudyContextInterceptor)

	jndiTemplate(org.springframework.jndi.JndiTemplate) {
		environment = ["java.naming.factory.initial":"org.jnp.interfaces.NamingContextFactory",
							"java.naming.provider.url": CH.config.jmsserver,
							"java.naming.factory.url.pkgs":"org.jboss.naming:org.jnp.interfaces"]
	}
	
 	jdbcTemplate(org.springframework.jdbc.core.JdbcTemplate) {
        dataSource = ref('dataSource')
    }
	jmsTemplate(org.springframework.jms.core.JmsTemplate) {
		connectionFactory = ref('connectionFactory')
		defaultDestination = ref('sendQueue')
		receiveTimeout = 30000
	}
	connectionFactory(org.springframework.jndi.JndiObjectFactoryBean) {
		jndiName = "ConnectionFactory"
		jndiTemplate = ref('jndiTemplate')
	}
	receiveQueue(org.springframework.jndi.JndiObjectFactoryBean) {
		jndiName = "queue/${CH.config.responseQueue}"
		jndiTemplate = ref('jndiTemplate')
	}
	sendQueue(org.springframework.jndi.JndiObjectFactoryBean) {
		jndiName = "queue/SharedAnalysisRequest"
		jndiTemplate = ref('jndiTemplate')
	}	
	securityServiceProxy(SecurityService) {
		
	}
	securityService(org.springframework.aop.scope.ScopedProxyFactoryBean){
		targetBeanName="securityServiceProxy"
		proxyTargetClass=true
	} 
	remoteService(org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean) {
		serviceUrl="${CH.config.caTissueUrl}/catissuecore/http/remoteService"
		serviceInterface="gov.nih.nci.system.comm.common.ApplicationServiceProxy"
		httpInvokerRequestExecutor(org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor)
	}
		
}