package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import com.model.InterviewQuestion;
import com.model.SystemFacade;
import com.nocturnal.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class QuestionLibrary {
    @FXML
    private ListView<InterviewQuestion> questionList = new ListView<>();

    @FXML
    private void initialize() {
        SystemFacade driver = SystemFacade.getInstance();
        ArrayList<InterviewQuestion> questions = driver.getAllQuestions();

        questions.sort(Comparator.comparing(InterviewQuestion::getLastUpdated,
                Comparator.nullsLast(Comparator.naturalOrder())).reversed());

        // populateUser(currentUser);
        populateQuestions(questions);
    }

    // populate the listView
    private void populateQuestions(ArrayList<InterviewQuestion> questions) {
        ObservableList<InterviewQuestion> observableQuestions = FXCollections.observableArrayList(questions);
        questionList.setItems(observableQuestions);

        // Set cells to display the title
        questionList.setCellFactory(param -> new ListCell<InterviewQuestion>() {
            @Override
            protected void updateItem(InterviewQuestion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });

        // Handle question selection
        questionList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Set the selected question in the SystemFacade and navigate to the
                // questionDetails
                SystemFacade.getInstance().selectQuestion(newSelection.getQuestionId());
                try {
                    App.setRoot("questionDetails");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @FXML
    private void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    private void logout() throws IOException {
        SystemFacade.getInstance().logout();
        App.setRoot("login");
    }

}
