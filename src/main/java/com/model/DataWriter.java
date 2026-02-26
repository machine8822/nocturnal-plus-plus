package model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataWriter {

    private String basePath;

    public DataWriter(String basePath) {
        this.basePath = basePath;
    }

    public boolean saveUsers(ArrayList<User> users) {
        try (FileWriter writer = new FileWriter(basePath + "/users.json")) {
            writer.write(users.toString()); // placeholder
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveQuestions(ArrayList<InterviewQuestion> questions) {
        try (FileWriter writer = new FileWriter(basePath + "/questions.json")) {
            writer.write(questions.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveSubmissions(ArrayList<QuestionSubmission> subs) {
        try (FileWriter writer = new FileWriter(basePath + "/submissions.json")) {
            writer.write(subs.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}