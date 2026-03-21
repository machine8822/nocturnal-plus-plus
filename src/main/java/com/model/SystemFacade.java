package com.model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author King
 */

public class SystemFacade {
    private static SystemFacade instance;
    private UserList users;
    private QuestionList questions;
    private User currentUser;
    private InterviewQuestion currentQuestion;

    private SystemFacade() {
        users = UserList.getInstance();
        questions = QuestionList.getInstance();
        currentUser = null;
        currentQuestion = null;
    }

    public static SystemFacade getInstance() {
        if (instance == null) {
            instance = new SystemFacade();
        }
        return instance;
    }

    public User login(String identifier, String pass) {
        User user = users.authenticate(identifier, pass);
        if (user != null) {
            currentUser = user;
            ArrayList<InterviewQuestion> allQuestions = new ArrayList<>(questions.getAll());
            currentQuestion = allQuestions.isEmpty() ? null : allQuestions.get(0);
        } else {
            currentUser = null;
            currentQuestion = null;
        }
        return user;
    }

    public boolean logout() {
        saveAllData();
        currentUser = null;
        currentQuestion = null;
        return true;
    }

    public boolean saveAllData() {
        boolean usersSaved = DataWriter.saveUsers(users.getUsers());
        boolean questionsSaved = DataWriter.saveQuestions(new ArrayList<>(questions.getAll()));
        return usersSaved && questionsSaved;
    }

    public void selectQuestion(UUID id) {
        currentQuestion = questions.getQuestion(id);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public InterviewQuestion getCurrentQuestion() {
        return currentQuestion;
    }

    public ArrayList<InterviewQuestion> getQuestionsByCategory(Category cat) {
        return new ArrayList<>(questions.getByCategory(cat));
    }

    public ArrayList<InterviewQuestion> getQuestionsByDifficulty(Difficulty diff) {
        return new ArrayList<>(questions.getByDifficulty(diff));
    }

    public boolean addUser(User user) {
        return users.addUser(user);
    }

    public boolean deleteUser(UUID userId) {
        return users.removeUser(userId);
    }

    public ArrayList<User> getUsers() {
        return users.getUsers();
    }

    public boolean addQuestion(InterviewQuestion question) {
        return questions.addQuestion(question);
    }

}
