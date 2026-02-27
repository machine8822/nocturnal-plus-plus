package com.model;

import java.util.ArrayList;

public class DataLoader {

    private String basePath;

    public DataLoader(String basePath) {
        this.basePath = basePath;
    }

    public ArrayList<User> loadUsers() {
        // TODO: Implement JSON reading later
        System.out.println("Stub: loadUsers called");
        return new ArrayList<>();
    }

    public ArrayList<InterviewQuestion> loadQuestions() {
        // TODO: Implement JSON reading later
        System.out.println("Stub: loadQuestions called");
        return new ArrayList<>();
    }
}