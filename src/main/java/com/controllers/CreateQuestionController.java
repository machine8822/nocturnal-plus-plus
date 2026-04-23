package com.controllers;

import java.io.IOException;
import java.util.ArrayList;

import com.model.Category;
import com.model.DataType;
import com.model.Difficulty;
import com.model.InterviewQuestion;
import com.model.QuestionType;
import com.model.Section;
import com.model.SectionType;
import com.model.SystemFacade;
import com.model.User;
import com.nocturnal.App;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CreateQuestionController {
    @FXML
    private TextField titleInput;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private ChoiceBox<Difficulty> difficultyInput;

    @FXML
    private ChoiceBox<Category> categoryInput;

    @FXML
    private ChoiceBox<QuestionType> typeInput;

    @FXML
    private TextArea constraintsInput;

    @FXML
    private TextArea examplesInput;

    @FXML
    private TextField expectedComplexityInput;

    @FXML
    private TextField maxLinesInput;

    @FXML
    private TextField timeLimitInput;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        difficultyInput.setItems(FXCollections.observableArrayList(Difficulty.values()));
        categoryInput.setItems(FXCollections.observableArrayList(Category.values()));
        typeInput.setItems(FXCollections.observableArrayList(QuestionType.values()));

        difficultyInput.setValue(Difficulty.EASY);
        categoryInput.setValue(Category.ARRAY);
        typeInput.setValue(QuestionType.SHORT_ANSWER);

        statusLabel.setText("");
    }

    @FXML
    private void createQuestion() {
        String title = titleInput.getText() == null ? "" : titleInput.getText().trim();
        String description = descriptionInput.getText() == null ? "" : descriptionInput.getText().trim();

        if (title.isBlank() || description.isBlank()) {
            statusLabel.setText("Title and description are required.");
            return;
        }

        SystemFacade driver = SystemFacade.getInstance();
        User currentUser = driver.getCurrentUser();

        if (currentUser == null) {
            statusLabel.setText("You must be logged in to create a question.");
            return;
        }

        InterviewQuestion question = new InterviewQuestion(
            title,
            description,
            difficultyInput.getValue(),
            categoryInput.getValue(),
            typeInput.getValue(),
            currentUser.getUserId()
        );

        Section section = new Section("Description", description, DataType.STRING, SectionType.DESCRIPTION);
        section.setConstraints(parseLines(constraintsInput.getText()));
        section.setExamples(parseLines(examplesInput.getText()));
        section.setExpectedTimeComplexity(expectedComplexityInput.getText() == null
            ? ""
            : expectedComplexityInput.getText().trim());

        Integer maxLines = parseOptionalInt(maxLinesInput.getText());
        if (maxLinesInput.getText() != null && !maxLinesInput.getText().isBlank() && maxLines == null) {
            statusLabel.setText("Max lines must be a whole number.");
            return;
        }

        Integer timeLimit = parseOptionalInt(timeLimitInput.getText());
        if (timeLimitInput.getText() != null && !timeLimitInput.getText().isBlank() && timeLimit == null) {
            statusLabel.setText("Time limit must be a whole number.");
            return;
        }

        section.setMaxLinesOfCode(maxLines);
        section.setTimeLimitSeconds(timeLimit);
        question.addSection(section);

        if (!driver.addQuestion(question)) {
            statusLabel.setText("Only contributors can create questions.");
            return;
        }

        driver.saveAllData();
        statusLabel.setText("Question created successfully.");
        clearForm();
    }

    @FXML
    private void goToDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    private void goBack() throws IOException {
        // Back button navigates to dashboard
        goToDashboard();
    }

    private void clearForm() {
        titleInput.clear();
        descriptionInput.clear();
        constraintsInput.clear();
        examplesInput.clear();
        expectedComplexityInput.clear();
        maxLinesInput.clear();
        timeLimitInput.clear();
        difficultyInput.setValue(Difficulty.EASY);
        categoryInput.setValue(Category.ARRAY);
        typeInput.setValue(QuestionType.SHORT_ANSWER);
    }

    private Integer parseOptionalInt(String input) {
        if (input == null) {
            return null;
        }

        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private ArrayList<String> parseLines(String rawText) {
        ArrayList<String> lines = new ArrayList<>();

        if (rawText == null || rawText.isBlank()) {
            return lines;
        }

        String[] splitLines = rawText.split("\\r?\\n");
        for (String line : splitLines) {
            if (line != null && !line.isBlank()) {
                lines.add(line.trim());
            }
        }

        return lines;
    }
}
