package com.controllers;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import com.model.Difficulty;
import com.model.InterviewQuestion;
import com.model.SystemFacade;
import com.model.User;
import com.nocturnal.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class DashboardController {
    @FXML
    private Label sidebarUserName;

    @FXML
    private Label sidebarUserMeta;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label memberSinceValue;

    @FXML
    private Label solvedValue;

    @FXML
    private Label rankValue;

    @FXML
    private Label continueQuestionLabel;

    @FXML
    private Label continueDifficultyChip;

    @FXML
    private Label continueCategoryLabel;

    @FXML
    private VBox recentQuestionsRows;

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
    private Button goToCreateQuestion;

    @FXML
    private void initialize() {
        SystemFacade facade = SystemFacade.getInstance();
        User currentUser = facade.getCurrentUser();
        ArrayList<InterviewQuestion> questions = facade.getAllQuestions();

        questions.sort(Comparator.comparing(InterviewQuestion::getLastUpdated,
            Comparator.nullsLast(Comparator.naturalOrder())).reversed());

        populateUser(currentUser);
        populateSummary(currentUser, questions);
        populateContinueStrip(facade, questions);
        populateRecentQuestions(questions);
        populateProgress(questions);
        applyContributorVisibility(currentUser);
    }

    @FXML
    private void goToCreateQuestion() throws IOException {
        App.setRoot("createQuestion");
    }

    @FXML
    private void goToLibrary() throws IOException {
        App.setRoot("questionLibrary");
    }

    @FXML
    private void goToProfile() throws IOException {
        App.setRoot("profile");
    }

    @FXML
    private void goToBookmarks() throws IOException {
        App.setRoot("bookmarks");
    }

    @FXML
    private void logout() throws IOException {
        SystemFacade.getInstance().logout();
        App.setRoot("login");
    }

    @FXML
    private void goBack() throws IOException {
        // Back button on dashboard logs out and returns to login
        logout();
    }

    private void applyContributorVisibility(User currentUser) {
        boolean canCreate = currentUser != null && currentUser.isContributor();
        goToCreateQuestion.setVisible(canCreate);
        goToCreateQuestion.setManaged(canCreate);
    }

    private void populateUser(User currentUser) {
        if (currentUser == null) {
            sidebarUserName.setText("Guest");
            sidebarUserMeta.setText("Not signed in");
            welcomeLabel.setText("Welcome");
            memberSinceValue.setText("-");
            rankValue.setText("-");
            return;
        }

        sidebarUserName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        sidebarUserMeta.setText(buildRoleText(currentUser));
        welcomeLabel.setText("Welcome, " + currentUser.getFirstName());
        memberSinceValue.setText(currentUser.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
        rankValue.setText("#" + Math.max(1, 5000 - currentUser.getProfile().getTotalUpvotes() * 10));
    }

    private void populateSummary(User currentUser, ArrayList<InterviewQuestion> questions) {
        int totalQuestions = questions.size();
        int attempted = 0;

        for (InterviewQuestion question : questions) {
            if (question.getTotalAttempts() > 0) {
                attempted++;
            }
        }

        solvedValue.setText(attempted + "/" + totalQuestions);

        if (currentUser == null) {
            rankValue.setText("-");
        }
    }

    private void populateContinueStrip(SystemFacade facade, ArrayList<InterviewQuestion> questions) {
        InterviewQuestion currentQuestion = facade.getCurrentQuestion();
        InterviewQuestion displayQuestion = currentQuestion;

        if (displayQuestion == null && !questions.isEmpty()) {
            displayQuestion = questions.get(0);
        }

        if (displayQuestion == null) {
            continueQuestionLabel.setText("Continue: No question yet");
            continueDifficultyChip.setText("-");
            continueCategoryLabel.setText("-");
            return;
        }

        continueQuestionLabel.setText("Continue: " + displayQuestion.getTitle());
        continueDifficultyChip.setText(displayQuestion.getDifficulty().name());
        continueCategoryLabel.setText(toDisplay(displayQuestion.getCategory().name()));
    }

    private void populateRecentQuestions(ArrayList<InterviewQuestion> questions) {
        recentQuestionsRows.getChildren().clear();

        int maxRows = Math.min(6, questions.size());
        for (int i = 0; i < maxRows; i++) {
            recentQuestionsRows.getChildren().add(buildQuestionRow(questions.get(i)));
        }

        if (maxRows == 0) {
            HBox emptyRow = new HBox();
            emptyRow.getStyleClass().add("table-row");

            Label noData = new Label("No questions available yet");
            noData.getStyleClass().addAll("table-cell", "large");

            Label col2 = new Label("-");
            Label col3 = new Label("-");
            Label col4 = new Label("-");
            col2.getStyleClass().add("table-cell");
            col3.getStyleClass().add("table-cell");
            col4.getStyleClass().add("table-cell");

            emptyRow.getChildren().addAll(noData, col2, col3, col4);
            recentQuestionsRows.getChildren().add(emptyRow);
        }
    }

    private HBox buildQuestionRow(InterviewQuestion question) {
        HBox row = new HBox();
        row.getStyleClass().add("table-row");
        row.setStyle("-fx-cursor: hand;");

        Label title = new Label(question.getTitle());
        title.getStyleClass().addAll("table-cell", "large");

        Label difficulty = new Label(toDisplay(question.getDifficulty().name()));
        difficulty.getStyleClass().add("table-cell");

        Label category = new Label(toDisplay(question.getCategory().name()));
        category.getStyleClass().add("table-cell");

        Label success = new Label(question.getTotalAttempts() == 0
            ? "--"
            : String.format("%.0f%%", question.getSuccessRate() * 100.0));
        success.getStyleClass().add("table-cell");

        row.getChildren().addAll(title, difficulty, category, success);

        row.setOnMouseClicked(event -> {
            SystemFacade.getInstance().selectQuestion(question.getQuestionId());
            try {
                App.setRoot("questionDetails");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return row;
    }

    private void populateProgress(ArrayList<InterviewQuestion> questions) {
        ProgressStats easyStats = calculateProgress(questions, Difficulty.EASY);
        ProgressStats mediumStats = calculateProgress(questions, Difficulty.MEDIUM);
        ProgressStats hardStats = calculateProgress(questions, Difficulty.HARD);

        easyProgress.setProgress(easyStats.ratio);
        mediumProgress.setProgress(mediumStats.ratio);
        hardProgress.setProgress(hardStats.ratio);

        easyProgressValue.setText(easyStats.completed + "/" + easyStats.total);
        mediumProgressValue.setText(mediumStats.completed + "/" + mediumStats.total);
        hardProgressValue.setText(hardStats.completed + "/" + hardStats.total);
    }

    private ProgressStats calculateProgress(ArrayList<InterviewQuestion> questions, Difficulty difficulty) {
        int total = 0;
        int completed = 0;

        for (InterviewQuestion question : questions) {
            if (question.getDifficulty() == difficulty) {
                total++;
                if (question.getTotalAttempts() > 0) {
                    completed++;
                }
            }
        }

        double ratio = total == 0 ? 0.0 : (double) completed / total;
        return new ProgressStats(completed, total, ratio);
    }

    private String buildRoleText(User user) {
        if (user.isAdmin() && user.isContributor()) {
            return "Admin, Contributor";
        }

        if (user.isAdmin()) {
            return "Admin";
        }

        if (user.isContributor()) {
            return "Contributor";
        }

        return "Member";
    }

    private String toDisplay(String enumName) {
        if (enumName == null || enumName.isBlank()) {
            return "-";
        }

        String text = enumName.replace('_', ' ').toLowerCase();
        String[] words = text.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (words[i].isEmpty()) {
                continue;
            }

            if (builder.length() > 0) {
                builder.append(' ');
            }

            builder.append(Character.toUpperCase(words[i].charAt(0)));
            if (words[i].length() > 1) {
                builder.append(words[i].substring(1));
            }
        }

        return builder.toString();
    }

    private static class ProgressStats {
        private final int completed;
        private final int total;
        private final double ratio;

        private ProgressStats(int completed, int total, double ratio) {
            this.completed = completed;
            this.total = total;
            this.ratio = ratio;
        }
    }
}
