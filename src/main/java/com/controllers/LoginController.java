package com.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import com.nocturnal.App;

public class LoginController {

    @FXML
    private void back() throws IOException {
        App.setRoot("home");
    }
}