package com.controllers;

import java.io.IOException;

import com.nocturnal.App;

import javafx.fxml.FXML;

public class CreateUserController {
    @FXML
    private void createUser() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("login");
    }

}
