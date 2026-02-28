package com.model;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Very simple data loader modeled after the teacher's example.
 *
 * Only the user-reading method is implemented; it reads the hard‑coded
 * "json/users.json" file and builds User objects.  The goal is to keep the
 * code rudimentary so students can understand every line.
 */
public class DataLoader {

    private static final String USER_FILE = "json/users.json";

    /**
     * Read all users from the JSON file and return them in a list.
     *
     * This mirrors the teacher example: open the file, parse an array, walk
     * each object, pull out a few fields and call the User constructor.
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
                // additional fields can be read here if desired

                // The User constructor must match whatever your User class uses.
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
     * This method is deliberately as basic as the users loader; adapt the
     * constructor calls to match your actual InterviewQuestion/Section classes.
     */
    public static ArrayList<InterviewQuestion> loadQuestions() {
        ArrayList<InterviewQuestion> questions = new ArrayList<>();

        try {
            FileReader reader = new FileReader("json/questions.json");
            JSONArray questionsJSON = (JSONArray) new JSONParser().parse(reader);

            for (int i = 0; i < questionsJSON.size(); i++) {
                JSONObject qJSON = (JSONObject) questionsJSON.get(i);
                String qId = (String) qJSON.get("questionId");
                String title = (String) qJSON.get("title");
                String difficulty = (String) qJSON.get("difficulty");
                String type = (String) qJSON.get("type");
                String category = (String) qJSON.get("category");
                String imageURL = (String) qJSON.get("imageURL");
                String authorId = (String) qJSON.get("authorId");
                long totalAttempts = (Long) qJSON.get("totalAttempts");
                long totalSuccesses = (Long) qJSON.get("totalSuccesses");
                String createdAt = (String) qJSON.get("createdAt");
                String lastUpdated = (String) qJSON.get("lastUpdated");

                // create question object (constructor signature may differ)
                InterviewQuestion q = new InterviewQuestion(
                        qId, title, difficulty, type, category,
                        imageURL, authorId,
                        (int) totalAttempts, (int) totalSuccesses,
                        createdAt, lastUpdated);

                // load sections
                ArrayList<Section> secs = new ArrayList<>();
                JSONArray secArray = (JSONArray) qJSON.get("sections");
                for (int j = 0; secArray != null && j < secArray.size(); j++) {
                    JSONObject sJSON = (JSONObject) secArray.get(j);
                    String stitle = (String) sJSON.get("title");
                    String scontent = (String) sJSON.get("content");
                    String stype = (String) sJSON.get("type");

                    // section constructor may differ in your code
                    secs.add(new Section(stitle, scontent, stype));
                }
                q.setSections(secs); // or q.addSection(...) depending on API
                questions.add(q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    /**
     * Simple main method for manual testing.
     */
    public static void main(String[] args) {
        ArrayList<User> users = DataLoader.loadUsers();
        for (User u : users) {
            System.out.println(u);
        }
    }
}