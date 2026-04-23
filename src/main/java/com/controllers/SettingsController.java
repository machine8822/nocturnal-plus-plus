package com.controllers;

import java.io.IOException;

import com.nocturnal.App;

import javafx.fxml.FXML;

public class SettingsController {

    @FXML
    private void goBack() throws IOException {
        App.setRoot("profile");
    }
}
