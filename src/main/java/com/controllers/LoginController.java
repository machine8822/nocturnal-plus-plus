package com.controllers;

import java.io.IOException;

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
        String email = emailInput.getText();
        String password = passwordInput.getText();

        if (email.equals("testing") && password.equals("password")) {
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);

            System.out.println("Login successful");

            App.setRoot("dashboard");
        } else {
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);

            System.out.println("Invalid credentials");
        }



    }

    @FXML
    private void goToCreateUser() throws IOException {
        App.setRoot("createUser");
    }



}