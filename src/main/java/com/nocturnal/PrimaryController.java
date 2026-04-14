package com.nocturnal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML
    private TextField loginEmailField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private TextField signupNameField;

    @FXML
    private TextField signupEmailField;

    @FXML
    private PasswordField signupPasswordField;

    @FXML
    private PasswordField signupConfirmPasswordField;

    @FXML
    private Label statusLabel;

    @FXML
    private void handleLogin() {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter email and password.");
            return;
        }

        statusLabel.setText("Login stub: authenticated for " + email);
    }

    @FXML
    private void handleSignup() {
        String name = signupNameField.getText().trim();
        String email = signupEmailField.getText().trim();
        String password = signupPasswordField.getText();
        String confirmPassword = signupConfirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please complete all sign-up fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match.");
            return;
        }

        statusLabel.setText("Sign-up stub: account created for " + name);
    }
}
