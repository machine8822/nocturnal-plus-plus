package com.controllers;

import com.nocturnal.App;

import javafx.fxml.FXML;


public class DashboardController {
    @FXML
    private void goToCreateQuestion() throws Exception {
        App.setRoot("createQuestion");
    }

    @FXML
    private void logout() throws Exception {
        App.setRoot("login");
    }
    
}
