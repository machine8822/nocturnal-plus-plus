package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.model.InterviewQuestion;
import com.model.SystemFacade;
import com.model.User;
import com.nocturnal.App;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


public class BookmarksController {
    @FXML
    private ListView<InterviewQuestion> bookmarksListView = new ListView<>();

    @FXML
    private void initialize() {
        SystemFacade driver = SystemFacade.getInstance();
        User currentUser = driver.getCurrentUser();

        populateBookmarks(currentUser);
    }

    // populate the listView
    private void populateBookmarks(User currentUser) {

        if (currentUser == null) {
            return;
        }

        SystemFacade driver = SystemFacade.getInstance();

        ArrayList<InterviewQuestion> bookmarkedQuestions = new ArrayList<>();

        if (currentUser.getBookmarkedQuestionIds() == null || currentUser.getBookmarkedQuestionIds().isEmpty()) {
            return;
        }

        for (UUID bookmarkedId : currentUser.getBookmarkedQuestionIds()) {

            for (InterviewQuestion question : driver.getAllQuestions()) {
                if (question.getQuestionId().equals(bookmarkedId)) {
                    bookmarkedQuestions.add(question);
                    break;
                }
            }
        }

        bookmarksListView.setItems(FXCollections.observableArrayList(bookmarkedQuestions));
        
    }

    @FXML
    private void goToProfile() throws IOException {
        App.setRoot("profile");
    }



}
