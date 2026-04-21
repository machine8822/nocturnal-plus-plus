package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import com.model.InterviewQuestion;
import com.model.SystemFacade;
import com.model.User;
import com.nocturnal.App;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

public class QuestionLibrary {
    @FXML
    private VBox questionListContainer;

    @FXML
    private void initialize() {
        SystemFacade driver = SystemFacade.getInstance();
        User currentUser = driver.getCurrentUser();
        ArrayList<InterviewQuestion> questions = driver.getAllQuestions();

        questions.sort(Comparator.comparing(InterviewQuestion::getLastUpdated,
                Comparator.nullsLast(Comparator.naturalOrder())).reversed());

        // populateUser(currentUser);
        populateQuestions(questions);
    }

    private void populateQuestions(ArrayList<InterviewQuestion> questions) {
        for (InterviewQuestion question : questions) {
            HBox questionRow = createQuestionRow(question);
            questionListContainer.getChildren().add(questionRow);
        }
    }

    private HBox createQuestionRow(InterviewQuestion question) {
        HBox row = new HBox();
        Label title = new Label(question.getTitle());
        Label difficulty = new Label(question.getDifficulty().toString());
        row.getChildren().addAll(title, difficulty);
        return row;
    }

    @FXML
    private void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

}
