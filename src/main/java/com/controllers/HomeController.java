package com.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import com.nocturnal.App;

public class HomeController {

    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("login");
    }
}
