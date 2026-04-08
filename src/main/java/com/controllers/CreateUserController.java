package com.controllers;

import java.io.IOException;

import com.nocturnal.App;

import javafx.fxml.FXML;

public class CreateUserController {
    @FXML
    private void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }
}
