import org.hibernate.EmptyInterceptor

class StudyContextInterceptor extends EmptyInterceptor {
	String onPrepareStatement(String sql) {
	 	String prepedStatement = super.onPrepareStatement(sql);
		prepedStatement = prepedStatement.replaceAll("__STUDY_SCHEMA__", StudyContext.getStudy());
		return prepedStatement;
	}
}