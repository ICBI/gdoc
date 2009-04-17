// Place your Spring DSL code here
beans = {
    eventTriggeringInterceptor(StudyContextInterceptor)

	jndiTemplate(org.springframework.jndi.JndiTemplate) {
		environment = ["java.naming.factory.initial":"org.jnp.interfaces.NamingContextFactory",
							"java.naming.provider.url":"jnp://141.161.25.154:1099",
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
		jndiName = "queue/AnalysisResponse"
		jndiTemplate = ref('jndiTemplate')
	}
	sendQueue(org.springframework.jndi.JndiObjectFactoryBean) {
		jndiName = "queue/SharedAnalysisRequest"
		jndiTemplate = ref('jndiTemplate')
	}	
}