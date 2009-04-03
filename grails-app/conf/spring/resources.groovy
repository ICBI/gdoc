// Place your Spring DSL code here
beans = {
    eventTriggeringInterceptor(StudyContextInterceptor)

 	jdbcTemplate(org.springframework.jdbc.core.JdbcTemplate) {
        dataSource = ref('dataSource')
    }
}