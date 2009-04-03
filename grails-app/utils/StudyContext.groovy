class StudyContext {

    private static final ThreadLocal studyContext = new ThreadLocal()

    static void setStudy(String study) {
        studyContext.set(study);
    }

    static getStudy() {
        return studyContext.get();
    }

    static void clear() {
        studyContext.remove();
    }
}