package com.controllers;

import java.io.IOException;

import com.nocturnal.App;
import com.model.*;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateUserController {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField emailAdd;
    @FXML
    private PasswordField password;
    @FXML
    private TextField school;
    @FXML
    private TextField major;
    @FXML
    private TextField gradYear;

    @FXML
    private void createUser() throws IOException {
        SystemFacade driver = SystemFacade.getInstance();

        String firstNameString = firstName.getText();
        String lastNameString = lastName.getText();
        String emailAddress = emailAdd.getText();
        String pass = password.getText();
        String schoolString = school.getText();
        String majorString = major.getText();
        int gradYe = Integer.parseInt(gradYear.getText());

        // If any is blank/wrong needs to notify user
        if (firstNameString.isEmpty() || lastNameString.isEmpty() || emailAddress.isEmpty() || pass.isEmpty()
                || schoolString.isEmpty() || majorString.isEmpty() || gradYear.getText().isEmpty()) {
            System.out.println("All fields must be filled out.");
            return;
        }
        // Need to implement using "getUserByEmail" to check if email is already taken
        if (driver.getUsers().isEmailTaken(emailAddress)) {
            System.out.println("Email is already taken. Please choose a different email.");
            return;
        }

        // If successful, needs to update user.json and send user back to login

    }

    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("login");
    }

}
