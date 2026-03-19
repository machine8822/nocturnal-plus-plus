package com.model;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

                LocalDateTime createdAt = safeParseDate((String) personJSON.get("createdAt"));
                LocalDateTime lastLogin = safeParseDate((String) personJSON.get("lastLogin"));
                boolean isAdmin = personJSON.get("isAdmin") instanceof Boolean ? (Boolean) personJSON.get("isAdmin") : false;
                boolean isContributor = personJSON.get("isContributor") instanceof Boolean ? (Boolean) personJSON.get("isContributor") : false;

                Profile profile = loadProfile((JSONObject) personJSON.get("profile"));

                users.add(new User(id, email, passwordHash, firstName, lastName,
                        createdAt, lastLogin, isAdmin, isContributor, profile, new ArrayList<>()));
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

                // question-level comments
                JSONArray qComments = (JSONArray) qJSON.get("comments");
                if (qComments != null) {
                    for (int j = 0; j < qComments.size(); j++) {
                        JSONObject cJSON = (JSONObject) qComments.get(j);
                        q.addComment(loadComment(cJSON));
                    }
                }

                // load sections
                JSONArray secArray = (JSONArray) qJSON.get("sections");
                for (int j = 0; secArray != null && j < secArray.size(); j++) {
                    JSONObject sJSON = (JSONObject) secArray.get(j);
                    String stitle = (String) sJSON.get("title");
                    String scontent = (String) sJSON.get("content");

                    SectionType sectionType = SectionType.valueOf((String) sJSON.get("type"));
                    DataType dataType = null;
                    if (sJSON.get("dataType") != null) {
                        dataType = DataType.valueOf((String) sJSON.get("dataType"));
                    }

                    Section sec = new Section(stitle, scontent, dataType, sectionType);

                    sec.setImageURL((String) sJSON.get("imageURL"));
                    sec.setExpectedTimeComplexity((String) sJSON.get("expectedTimeComplexity"));

                    if (sJSON.get("maxLinesOfCode") instanceof Long) {
                        sec.setMaxLinesOfCode(((Long) sJSON.get("maxLinesOfCode")).intValue());
                    }

                    if (sJSON.get("timeLimitSeconds") instanceof Long) {
                        sec.setTimeLimitSeconds(((Long) sJSON.get("timeLimitSeconds")).intValue());
                    }

                    JSONArray constraintsJSON = (JSONArray) sJSON.get("constraints");
                    if (constraintsJSON != null) {
                        for (int c = 0; c < constraintsJSON.size(); c++) {
                            Object constraintValue = constraintsJSON.get(c);
                            if (constraintValue instanceof String) {
                                sec.addConstraint((String) constraintValue);
                            }
                        }
                    }

                    JSONArray examplesJSON = (JSONArray) sJSON.get("examples");
                    if (examplesJSON != null) {
                        for (int ex = 0; ex < examplesJSON.size(); ex++) {
                            Object exampleValue = examplesJSON.get(ex);
                            if (exampleValue instanceof String) {
                                sec.addExample((String) exampleValue);
                            }
                        }
                    }

                    // answers
                    JSONArray answersJSON = (JSONArray) sJSON.get("answers");
                    if (answersJSON != null) {
                        for (int a = 0; a < answersJSON.size(); a++) {
                            sec.addAnswer(loadAnswer((JSONObject) answersJSON.get(a)));
                        }
                    }

                    // comments
                    JSONArray commentsJSON = (JSONArray) sJSON.get("comments");
                    if (commentsJSON != null) {
                        for (int c = 0; c < commentsJSON.size(); c++) {
                            sec.addComment(loadComment((JSONObject) commentsJSON.get(c)));
                        }
                    }

                    q.addSection(sec);
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

        ArrayList<InterviewQuestion> questions = DataLoader.loadQuestions();
        for (InterviewQuestion q : questions) {
            System.out.println(q);
        }
    }

    private static LocalDateTime safeParseDate(String raw) {
        if (raw == null || raw.isBlank()) return null;
        try {
            return LocalDateTime.parse(raw);
        } catch (Exception e) {
            return null;
        }
    }

    private static Profile loadProfile(JSONObject profileJSON) {
        if (profileJSON == null) return new Profile();

        String school = (String) profileJSON.get("school");
        String major = (String) profileJSON.get("major");

        int gradYear = 0;
        if (profileJSON.get("gradYear") instanceof Long) {
            gradYear = ((Long) profileJSON.get("gradYear")).intValue();
        }

        int totalUpvotes = 0;
        if (profileJSON.get("totalUpvotes") instanceof Long) {
            totalUpvotes = ((Long) profileJSON.get("totalUpvotes")).intValue();
        }

        String resumeURL = (String) profileJSON.get("resumeURL");
        return new Profile(school, major, gradYear, totalUpvotes, resumeURL);
    }

    private static Answer loadAnswer(JSONObject answerJSON) {
        if (answerJSON == null) return null;

        String codeSnippet = (String) answerJSON.get("codeSnippet");
        String explanation = (String) answerJSON.get("explanation");
        UUID authorId = answerJSON.get("authorId") == null ? null : UUID.fromString((String) answerJSON.get("authorId"));

        Answer answer = new Answer(codeSnippet, explanation, authorId);

        if (answerJSON.get("createdAt") != null) {
            answer.setCreatedAt(safeParseDate((String) answerJSON.get("createdAt")));
        }

        if (answerJSON.get("upvoteCount") instanceof Long) {
            answer.setUpvoteCount(((Long) answerJSON.get("upvoteCount")).intValue());
        }

        if (answerJSON.get("downvoteCount") instanceof Long) {
            answer.setDownvoteCount(((Long) answerJSON.get("downvoteCount")).intValue());
        }

        JSONArray commentsJSON = (JSONArray) answerJSON.get("comments");
        List<Comment> comments = new ArrayList<>();
        if (commentsJSON != null) {
            for (int i = 0; i < commentsJSON.size(); i++) {
                comments.add(loadComment((JSONObject) commentsJSON.get(i)));
            }
        }
        answer.setComments(comments);

        return answer;
    }

    private static Comment loadComment(JSONObject commentJSON) {
        if (commentJSON == null) return null;

        String text = (String) commentJSON.get("text");
        UUID authorId = commentJSON.get("authorId") == null ? null : UUID.fromString((String) commentJSON.get("authorId"));

        Comment comment = new Comment(text, authorId);

        comment.setTimestamp(safeParseDate((String) commentJSON.get("timestamp")));

        if (commentJSON.get("isEdited") instanceof Boolean) {
            comment.setEdited((Boolean) commentJSON.get("isEdited"));
        }

        if (commentJSON.get("upvoteCount") instanceof Long) {
            comment.setUpvoteCount(((Long) commentJSON.get("upvoteCount")).intValue());
        }

        if (commentJSON.get("downvoteCount") instanceof Long) {
            comment.setDownvoteCount(((Long) commentJSON.get("downvoteCount")).intValue());
        }

        JSONArray repliesJSON = (JSONArray) commentJSON.get("replies");
        List<Comment> replies = new ArrayList<>();
        if (repliesJSON != null) {
            for (int i = 0; i < repliesJSON.size(); i++) {
                replies.add(loadComment((JSONObject) repliesJSON.get(i)));
            }
        }
        comment.setReplies(replies);

        return comment;
    }
}
