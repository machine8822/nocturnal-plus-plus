package com.model;

import java.util.ArrayList;

public class DataWriter {

    private String basePath;

    public DataWriter(String basePath) {
        this.basePath = basePath;
    }

    public boolean saveUsers(ArrayList<User> users) {
        // TODO: Implement JSON writing later
        System.out.println("Stub: saveUsers called");
        return true;
    }

    public boolean saveQuestions(ArrayList<InterviewQuestion> questions) {
        // TODO: Implement JSON writing later
        System.out.println("Stub: saveQuestions called");
        return true;
    }

}