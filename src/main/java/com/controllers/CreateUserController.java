package com.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import com.model.Profile;
import com.model.SystemFacade;
import com.model.User;
import com.nocturnal.App;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
    private TextField passwordVisible;

    @FXML
    private CheckBox showPasswordToggle;
    @FXML
    private TextField school;
    @FXML
    private TextField major;
    @FXML
    private TextField gradYear;

    @FXML
    private void initialize() {
        passwordVisible.textProperty().bindBidirectional(password.textProperty());
        setPasswordVisibility(false);
    }

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
        

        User newUser = new User(
                UUID.randomUUID(),
                emailAddress,
                User.hashPassword(pass),
                firstNameString,
                lastNameString,
                LocalDateTime.now(),
                null,
                false,
                false,
                new Profile(schoolString, majorString, gradYe, 5, null),
                new ArrayList<>()
        );

        // If any is blank/wrong needs to notify user
        if (firstNameString.isEmpty() || lastNameString.isEmpty() || emailAddress.isEmpty() || pass.isEmpty()
                || schoolString.isEmpty() || majorString.isEmpty() || gradYear.getText().isEmpty()) {
            System.out.println("All fields must be filled out.");
            return;
        }
        
        if (driver.addUser(newUser)) {
            System.out.println("User: " + firstNameString + " " + lastNameString + " has been successfully created");
            driver.saveAllData();
            App.setRoot("login");
            return;
        } else {
            System.out.println("Sorry, we couldn't create the user. Email already exists.");
            return;
        }


    }

    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void togglePasswordVisibility() {
        setPasswordVisibility(showPasswordToggle.isSelected());
    }

    private void setPasswordVisibility(boolean visible) {
        password.setVisible(!visible);
        password.setManaged(!visible);
        passwordVisible.setVisible(visible);
        passwordVisible.setManaged(visible);
    }

}
