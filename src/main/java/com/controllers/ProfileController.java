package com.controllers;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.model.Difficulty;
import com.model.InterviewQuestion;
import com.model.Profile;
import com.model.SystemFacade;
import com.model.User;
import com.nocturnal.App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProfileController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label solvedValue;
    @FXML
    private Label rankValue;
    @FXML
    private Label upvotesValue;
    @FXML
    private Label memberSinceValue;
    @FXML
    private ProgressBar easyProgress;
    @FXML
    private ProgressBar mediumProgress;
    @FXML
    private ProgressBar hardProgress;
    @FXML
    private Label easyProgressValue;
    @FXML
    private Label mediumProgressValue;
    @FXML
    private Label hardProgressValue;
    @FXML
    private Label schoolValue;
    @FXML
    private Label majorValue;
    @FXML
    private Label gradYearValue;

    @FXML
    private void initialize() {
        SystemFacade facade = SystemFacade.getInstance();
        User user = facade.getCurrentUser();

        if (user == null) {
            nameLabel.setText("Not logged in");
            emailLabel.setText("");
            roleLabel.setText("-");
            return;
        }

        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        emailLabel.setText(user.getEmail());
        roleLabel.setText(buildRoleText(user));

        Profile profile = user.getProfile();
        upvotesValue.setText(String.valueOf(profile.getTotalUpvotes()));
        memberSinceValue.setText(user.getCreatedAt()
            .format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
        rankValue.setText("#" + Math.max(1, 5000 - profile.getTotalUpvotes() * 10));

        schoolValue.setText(profile.getSchool().isEmpty() ? "-" : profile.getSchool());
        majorValue.setText(profile.getMajor().isEmpty() ? "-" : profile.getMajor());
        gradYearValue.setText(profile.getGradYear() == 0 ? "-"
            : String.valueOf(profile.getGradYear()));

        ArrayList<InterviewQuestion> questions = facade.getAllQuestions();
        populateSolved(questions);
        populateProgress(questions);
    }

    private void populateSolved(ArrayList<InterviewQuestion> questions) {
        int total = questions.size();
        int attempted = 0;
        for (InterviewQuestion q : questions) {
            if (q.getTotalAttempts() > 0) attempted++;
        }
        solvedValue.setText(attempted + "/" + total);
    }

    private void populateProgress(ArrayList<InterviewQuestion> questions) {
        setProgress(questions, Difficulty.EASY, easyProgress, easyProgressValue);
        setProgress(questions, Difficulty.MEDIUM, mediumProgress, mediumProgressValue);
        setProgress(questions, Difficulty.HARD, hardProgress, hardProgressValue);
    }

    private void setProgress(ArrayList<InterviewQuestion> questions, Difficulty diff,
                             ProgressBar bar, Label label) {
        int total = 0;
        int completed = 0;
        for (InterviewQuestion q : questions) {
            if (q.getDifficulty() == diff) {
                total++;
                if (q.getTotalAttempts() > 0) completed++;
            }
        }
        double ratio = total == 0 ? 0.0 : (double) completed / total;
        bar.setProgress(ratio);
        label.setText(completed + "/" + total);
    }

    private String buildRoleText(User user) {
        if (user.isAdmin() && user.isContributor()) return "Admin, Contributor";
        if (user.isAdmin()) return "Admin";
        if (user.isContributor()) return "Contributor";
        return "Member";
    }

    @FXML
    private void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    private void goToSettings() throws IOException {
        App.setRoot("settings");
    }
}
