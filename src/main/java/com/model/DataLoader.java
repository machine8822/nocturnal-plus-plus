package com.model;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * Only the user-reading method is implemented; it reads the hard‑coded
 * users.json file and builds User objects. 
 */
public class DataLoader {

    private static final String USER_FILE = "json/users.json";

    /**
     * Read all users from the JSON file and return them in a list.
     *
     * @return ArrayList of User instances (empty on error)
     */
    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();

        try {
            FileReader reader = new FileReader(USER_FILE);
            JSONArray peopleJSON = (JSONArray) new JSONParser().parse(reader);

            for (int i = 0; i < peopleJSON.size(); i++) {
                JSONObject personJSON = (JSONObject) peopleJSON.get(i);
                UUID id = UUID.fromString((String) personJSON.get("userId"));
                String email = (String) personJSON.get("email");
                String passwordHash = (String) personJSON.get("passwordHash");
                String firstName = (String) personJSON.get("firstName");
                String lastName = (String) personJSON.get("lastName");
                // additional fields can be read here if you so desire...

                // The User constructor must match whateve User class uses.
                users.add(new User(id, email, passwordHash, firstName, lastName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Read interview questions (including their sections) from JSON.
     *
     * The structure matches what DataWriter writes.  We simply walk the array
     * and extract the same fields; sections are treated as nested arrays.
     *
     * This method is deliberately as basic as the users loader adapts the
     * constructor calls to match your actual InterviewQuestion/Section classes.
     */
    public static ArrayList<InterviewQuestion> loadQuestions() {
        ArrayList<InterviewQuestion> questions = new ArrayList<>();

        try {
            FileReader reader = new FileReader("json/questions.json");
            JSONArray questionsJSON = (JSONArray) new JSONParser().parse(reader);

            for (int i = 0; i < questionsJSON.size(); i++) {
                JSONObject qJSON = (JSONObject) questionsJSON.get(i);
                UUID qId = UUID.fromString((String) qJSON.get("questionId"));
                String title = (String) qJSON.get("title");
                String description = (String) qJSON.get("description");
                Difficulty difficulty = Difficulty.valueOf((String) qJSON.get("difficulty"));
                QuestionType type = QuestionType.valueOf((String) qJSON.get("type"));
                Category category = Category.valueOf((String) qJSON.get("category"));
                String imageURL = (String) qJSON.get("imageURL");
                UUID authorId = UUID.fromString((String) qJSON.get("authorId"));
                long totalAttempts = (Long) qJSON.get("totalAttempts");
                long totalSuccesses = (Long) qJSON.get("totalSuccesses");
                LocalDateTime createdAt = LocalDateTime.parse((String) qJSON.get("createdAt"));
                LocalDateTime lastUpdated = LocalDateTime.parse((String) qJSON.get("lastUpdated"));

                // create question object (constructor signature may differ)
                InterviewQuestion q = new InterviewQuestion(
                    qId,
                    title,
                    description,
                    difficulty,
                    category,
                    type,
                    authorId,
                    createdAt,
                    lastUpdated,
                    (int) totalAttempts,
                    (int) totalSuccesses,
                    imageURL);

                // load sections
                JSONArray secArray = (JSONArray) qJSON.get("sections");
                for (int j = 0; secArray != null && j < secArray.size(); j++) {
                    JSONObject sJSON = (JSONObject) secArray.get(j);
                    String stitle = (String) sJSON.get("title");
                    String scontent = (String) sJSON.get("content");
                    String stype = (String) sJSON.get("type");

                    q.addSection(new Section(stitle, scontent, stype));
                }
                questions.add(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    /**
     * Simple main method for manual testing
     */
    public static void main(String[] args) {
        ArrayList<User> users = DataLoader.loadUsers();
        for (User u : users) {
            System.out.println(u);
        }
    }
}
