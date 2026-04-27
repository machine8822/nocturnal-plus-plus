package com.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.model.Answer;
import com.model.InterviewQuestion;
import com.model.Section;
import com.model.SystemFacade;
import com.nocturnal.App;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class QuestionDetailsController {
    @FXML
    private Label breadcrumbLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label difficulty;

    @FXML
    private Label type;

    @FXML
    private Label category;

    @FXML
    private Label successRate;

    @FXML
    private Label totalAttempts;

    @FXML
    private TextArea description;

    @FXML
    private TextArea constraints;

    @FXML
    private TextArea examples;

    private InterviewQuestion activeQuestion;

    @FXML
    private void initialize() {
        activeQuestion = SystemFacade.getInstance().getCurrentQuestion();

        if (activeQuestion == null) {
            breadcrumbLabel.setText("/ Question Details");
            titleLabel.setText("No question selected");
            difficulty.setText("-");
            type.setText("-");
            category.setText("-");
            successRate.setText("0% Success");
            totalAttempts.setText("0 attempts");
            description.setText("Select a question from the library to view details.");
            constraints.setText("No constraints available.");
            examples.setText("No examples available.");
            return;
        }

        breadcrumbLabel.setText("/ " + activeQuestion.getTitle());
        titleLabel.setText(activeQuestion.getTitle());
        difficulty.setText(toDisplay(activeQuestion.getDifficulty().name()));
        type.setText(toDisplay(activeQuestion.getType().name()));
        category.setText(toDisplay(activeQuestion.getCategory().name()));
        successRate.setText(String.format("%.0f%% Success", activeQuestion.getSuccessRate() * 100.0));
        totalAttempts.setText(activeQuestion.getTotalAttempts() + " attempts");
        description.setText(orFallback(activeQuestion.getDescription(), "No description available."));

        Section section = getPrimarySection(activeQuestion.getSections());
        if (section == null) {
            constraints.setText("No constraints available.");
            examples.setText("No examples available.");
            return;
        }

        constraints.setText(joinLines(section.getConstraints(), "No constraints available."));
        examples.setText(joinLines(section.getExamples(), "No examples available."));
    }

    @FXML
    private void showAnswerPopup() {
        if (activeQuestion == null) {
            showInfo("No Question Selected", "Select a question from the library first.");
            return;
        }

        List<Answer> answers = collectAnswers(activeQuestion);
        if (answers.isEmpty()) {
            showInfo("No Answers Yet", "This question does not have a saved answer yet.");
            return;
        }

        Answer selectedAnswer = firstAnswerWithCode(answers);
        String code = selectedAnswer == null ? "" : selectedAnswer.getCodeSnippet();
        if (code.isBlank()) {
            showInfo("Answer Available", "An answer exists, but no code snippet was saved for it.");
            return;
        }

        String explanation = selectedAnswer == null ? "" : selectedAnswer.getExplanation();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Answer Code");
        alert.setHeaderText(activeQuestion.getTitle() + " - Example Solution");

        StringBuilder message = new StringBuilder();
        if (explanation != null && !explanation.isBlank()) {
            message.append("Explanation:\n").append(explanation).append("\n\n");
        }
        message.append("Code:\n").append(code);

        TextArea contentArea = new TextArea(message.toString());
        contentArea.setEditable(false);
        contentArea.setWrapText(false);
        contentArea.setPrefWidth(700);
        contentArea.setPrefHeight(420);
        contentArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 12;");

        alert.getDialogPane().setContent(contentArea);
        alert.getDialogPane().setPrefWidth(760);
        alert.getDialogPane().setPrefHeight(520);
        alert.showAndWait();
    }

    @FXML
    private void goToLibrary() throws IOException {
        App.setRoot("questionLibrary");
    }

    @FXML
    private void goToProfile() throws IOException {
        App.setRoot("profile");
    }

    private List<Answer> collectAnswers(InterviewQuestion question) {
        List<Answer> answers = new ArrayList<>();
        if (question == null) {
            return answers;
        }

        for (Section section : question.getSections()) {
            answers.addAll(section.getAnswers());
        }

        return answers;
    }

    private Answer firstAnswerWithCode(List<Answer> answers) {
        for (Answer answerItem : answers) {
            String code = answerItem.getCodeSnippet();
            if (code != null && !code.isBlank()) {
                return answerItem;
            }
        }
        return null;
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Section getPrimarySection(List<Section> sections) {
        if (sections == null || sections.isEmpty()) {
            return null;
        }
        return sections.get(0);
    }

    private String joinLines(List<String> items, String fallback) {
        if (items == null || items.isEmpty()) {
            return fallback;
        }

        List<String> clean = new ArrayList<>();
        for (String item : items) {
            if (item != null && !item.isBlank()) {
                clean.add(item);
            }
        }

        if (clean.isEmpty()) {
            return fallback;
        }

        return String.join("\n", clean);
    }

    private String orFallback(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value;
    }

    private String toDisplay(String enumName) {
        if (enumName == null || enumName.isBlank()) {
            return "-";
        }

        String text = enumName.replace('_', ' ').toLowerCase();
        String[] words = text.split(" ");
        StringBuilder builder = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                builder.append(word.substring(1));
            }
        }

        return builder.toString();
    }
}
