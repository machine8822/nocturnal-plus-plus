package com.controllers;

import java.io.IOException;

import com.nocturnal.App;

import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void back() throws IOException {
        App.setRoot("home");
    }

    @FXML
    private void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

}