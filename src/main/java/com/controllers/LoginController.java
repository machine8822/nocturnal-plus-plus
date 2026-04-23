package com.controllers;

import java.io.IOException;

import com.model.SystemFacade;
import com.model.User;
import com.nocturnal.App;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField emailInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private void login() throws IOException {
        SystemFacade driver = SystemFacade.getInstance();

        String email = emailInput.getText();
        String password = passwordInput.getText();

        // Grant Smith Password: password

        User loggedInUser = driver.login(email, password);
        if (loggedInUser != null) {
            System.out.println(
                    "Login successful for user: " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
            App.setRoot("dashboard");
        } else {
            System.out.println("Invalid credentials for email: " + email);
        }

    }

    @FXML
    private void goToCreateUser() throws IOException {
        App.setRoot("createUser");
    }

    @FXML
    private void goBack() {
        // Clear inputs when back button is clicked on login page
        emailInput.clear();
        passwordInput.clear();
    }

}