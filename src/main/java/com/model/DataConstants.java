package com.model;

public abstract class DataConstants {

    protected static final String USER_FILE = "json/users.json";
    protected static final String QUESTION_FILE = "json/questions.json";

    protected static final String USER_FILE_JUNIT = "/json/users.json";
    protected static final String QUESTION_FILE_JUNIT = "/json/questions.json";

    public static boolean isJUnitTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }
}
