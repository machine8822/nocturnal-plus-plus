package com.controllers;

import com.nocturnal.App;

import javafx.fxml.FXML;


public class DashboardController {
    @FXML
    private void logout() throws Exception {
        App.setRoot("login");
    }
    
}
