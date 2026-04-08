package com.controllers;

import java.io.IOException;

import com.nocturnal.App;

import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void goToCreateUser() throws IOException {
        App.setRoot("createUser");
    }

}
