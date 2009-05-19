import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor

class StudyContextInterceptor extends ClosureEventTriggeringInterceptor {
	String onPrepareStatement(String sql) {
	 	String prepedStatement = super.onPrepareStatement(sql);
	//println "SQL: ${prepedStatement} STUDY: ${StudyContext.getStudy()}"
		prepedStatement = prepedStatement.replaceAll("__STUDY_SCHEMA__", StudyContext.getStudy());
		return prepedStatement;
	}
}