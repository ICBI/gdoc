// Place your Spring DSL code here
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.apache.commons.dbcp.BasicDataSource
import javax.jms.Queue
import javax.jms.QueueConnectionFactory

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
		connectionFactory = ref('jmsConnectionFactory')
		defaultDestination = ref('sendQueue')
		receiveTimeout = 30000
	}
	jmsConnectionFactory(org.springframework.jndi.JndiObjectFactoryBean) {
		jndiName = "ConnectionFactory"
		jndiTemplate = ref('jndiTemplate')
		if(GrailsUtil.environment == GrailsApplication.ENV_TEST) {
			lookupOnStartup = false
			proxyInterface = javax.jms.QueueConnectionFactory
		}
	}
	receiveQueue(org.springframework.jndi.JndiObjectFactoryBean) {
		jndiName = "queue/${CH.config.responseQueue}"
		jndiTemplate = ref('jndiTemplate')
		if(GrailsUtil.environment == GrailsApplication.ENV_TEST) {
			lookupOnStartup = false
			proxyInterface = javax.jms.Queue
		}
	}
	sendQueue(org.springframework.jndi.JndiObjectFactoryBean) {
		jndiName = "queue/SharedAnalysisRequest"
		jndiTemplate = ref('jndiTemplate')
		if(GrailsUtil.environment == GrailsApplication.ENV_TEST) {
			lookupOnStartup = false
			proxyInterface = javax.jms.Queue
		}
	}	
	securityServiceProxy(SecurityService) {bean ->
	  	bean.scope = 'session'
		jdbcTemplate = ref('jdbcTemplate')
	}
	securityService(org.springframework.aop.scope.ScopedProxyFactoryBean){
		targetBeanName="securityServiceProxy"
		proxyTargetClass=true
	} 
	switch(GrailsUtil.environment) { 
		case GrailsApplication.ENV_TEST: 
			testDataSource(BasicDataSource) { 
				driverClassName = "oracle.jdbc.pool.OracleConnectionPoolDataSource" 
				url = "jdbc:oracle:thin:@localhost:1521:gdoc" 
				username = "guidoc" 
				password = "cure4cancer" 
			} 
			break

	}	
}