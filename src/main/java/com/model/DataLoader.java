package com.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Loads Users and InterviewQuestions from JSON files.
 * Extends DataConstants so it can detect JUnit and switch file paths.
 */
public class DataLoader extends DataConstants {

    /**
     * Read all users from the JSON file and return them in a list.
     *
     * @return ArrayList of User instances (empty on error)
     */
    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();

        try {
            BufferedReader reader = getReaderFromFile(USER_FILE, USER_FILE_JUNIT);
            if (reader == null) return users;

            JSONArray peopleJSON = (JSONArray) new JSONParser().parse(reader);

            for (Object person : peopleJSON) {
                if (!(person instanceof JSONObject personJSON)) {
                    continue;
                }

                UUID id = parseUUID(personJSON.get("userId"), UUID.randomUUID());
                String email = asString(personJSON.get("email"));
                String passwordHash = asString(personJSON.get("passwordHash"));
                String firstName = asString(personJSON.get("firstName"));
                String lastName = asString(personJSON.get("lastName"));
                LocalDateTime createdAt = parseLocalDateTime(personJSON.get("createdAt"), LocalDateTime.now());
                LocalDateTime lastLogin = parseLocalDateTime(personJSON.get("lastLogin"), null);
                boolean isAdmin = asBoolean(personJSON.get("isAdmin"));
                boolean isContributor = asBoolean(personJSON.get("isContributor"));
                Profile profile = loadProfile((JSONObject) personJSON.get("profile"));

                users.add(new User(id, email, passwordHash, firstName, lastName, createdAt,
                        lastLogin, isAdmin, isContributor, profile, new ArrayList<>()));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Read interview questions (including their sections) from JSON.
     */
    public static ArrayList<InterviewQuestion> loadQuestions() {
        ArrayList<InterviewQuestion> questions = new ArrayList<>();

        try {
            BufferedReader reader = getReaderFromFile(QUESTION_FILE, QUESTION_FILE_JUNIT);
            if (reader == null) return questions;

            JSONArray questionsJSON = (JSONArray) new JSONParser().parse(reader);

            for (Object question : questionsJSON) {
                if (!(question instanceof JSONObject qJSON)) {
                    continue;
                }

                UUID qId = parseUUID(qJSON.get("questionId"), UUID.randomUUID());
                String title = asString(qJSON.get("title"));
                String description = asString(qJSON.get("description"));
                Difficulty difficulty = parseEnum(Difficulty.class, qJSON.get("difficulty"), Difficulty.EASY);
                QuestionType type = parseEnum(QuestionType.class, qJSON.get("type"), QuestionType.SHORT_ANSWER);
                Category category = parseEnum(Category.class, qJSON.get("category"), Category.ARRAY);
                String imageURL = asString(qJSON.get("imageURL"));
                UUID authorId = parseUUID(qJSON.get("authorId"), null);
                int totalAttempts = asInt(qJSON.get("totalAttempts"));
                int totalSuccesses = asInt(qJSON.get("totalSuccesses"));
                LocalDateTime createdAt = parseLocalDateTime(qJSON.get("createdAt"), LocalDateTime.now());
                LocalDateTime lastUpdated = parseLocalDateTime(qJSON.get("lastUpdated"), createdAt);
                List<Comment> comments = loadComments((JSONArray) qJSON.get("comments"));
                List<Section> sections = loadSections((JSONArray) qJSON.get("sections"));

                questions.add(new InterviewQuestion(
                        qId,
                        title,
                        description,
                        difficulty,
                        category,
                        type,
                        authorId,
                        createdAt,
                        lastUpdated,
                        totalAttempts,
                        totalSuccesses,
                        imageURL,
                        comments,
                        sections));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    /**
     * Returns a BufferedReader that reads from the correct file depending
     * on whether the code is running under JUnit or not.
     *
     * Normal run:  reads from the file system (e.g. "json/users.json")
     * JUnit test:  reads from classpath resources (e.g. "/json/users.json" in src/test/resources)
     */
    private static BufferedReader getReaderFromFile(String fileName, String junitFileName) {
        try {
            if (isJUnitTest()) {
                InputStream inputStream = DataLoader.class.getResourceAsStream(junitFileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                return new BufferedReader(inputStreamReader);
            } else {
                FileReader reader = new FileReader(fileName);
                return new BufferedReader(reader);
            }
        } catch (Exception e) {
            System.out.println("Can't load: " + fileName);
            return null;
        }
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

    private static Profile loadProfile(JSONObject profileJSON) {
        if (profileJSON == null) {
            return new Profile();
        }

        return new Profile(
                asString(profileJSON.get("school")),
                asString(profileJSON.get("major")),
                asInt(profileJSON.get("gradYear")),
                asInt(profileJSON.get("totalUpvotes")),
                asString(profileJSON.get("resumeURL")));
    }

    private static List<Section> loadSections(JSONArray sectionArray) {
        List<Section> sections = new ArrayList<>();
        if (sectionArray == null) {
            return sections;
        }

        for (Object sectionValue : sectionArray) {
            if (!(sectionValue instanceof JSONObject sectionJSON)) {
                continue;
            }

            SectionType sectionType = parseEnum(SectionType.class, sectionJSON.get("type"), SectionType.DESCRIPTION);
            DataType dataType = parseEnum(DataType.class, sectionJSON.get("dataType"), null);
            Section section = new Section(
                    asString(sectionJSON.get("title")),
                    asString(sectionJSON.get("content")),
                    dataType,
                    sectionType);

            section.setImageURL(asString(sectionJSON.get("imageURL")));
            section.setExpectedTimeComplexity(asString(sectionJSON.get("expectedTimeComplexity")));

            Integer maxLinesOfCode = asNullableInt(sectionJSON.get("maxLinesOfCode"));
            if (maxLinesOfCode != null) {
                section.setMaxLinesOfCode(maxLinesOfCode);
            }

            Integer timeLimitSeconds = asNullableInt(sectionJSON.get("timeLimitSeconds"));
            if (timeLimitSeconds != null) {
                section.setTimeLimitSeconds(timeLimitSeconds);
            }

            section.setConstraints(loadStringList((JSONArray) sectionJSON.get("constraints")));
            section.setExamples(loadStringList((JSONArray) sectionJSON.get("examples")));

            JSONArray answersJSON = (JSONArray) sectionJSON.get("answers");
            if (answersJSON != null) {
                for (Object answerValue : answersJSON) {
                    if (answerValue instanceof JSONObject answerJSON) {
                        section.addAnswer(loadAnswer(answerJSON));
                    }
                }
            }

            for (Comment comment : loadComments((JSONArray) sectionJSON.get("comments"))) {
                section.addComment(comment);
            }

            sections.add(section);
        }

        return sections;
    }

    private static Answer loadAnswer(JSONObject answerJSON) {
        return new Answer(
                parseUUID(answerJSON.get("answerId"), UUID.randomUUID()),
                asString(answerJSON.get("codeSnippet")),
                asString(answerJSON.get("explanation")),
                asInt(answerJSON.get("upvoteCount")),
                asInt(answerJSON.get("downvoteCount")),
                parseUUID(answerJSON.get("authorId"), null),
                parseLocalDateTime(answerJSON.get("createdAt"), LocalDateTime.now()),
                loadComments((JSONArray) answerJSON.get("comments")));
    }

    private static List<Comment> loadComments(JSONArray commentsJSON) {
        List<Comment> comments = new ArrayList<>();
        if (commentsJSON == null) {
            return comments;
        }

        for (Object commentValue : commentsJSON) {
            if (commentValue instanceof JSONObject commentJSON) {
                comments.add(loadComment(commentJSON));
            }
        }

        return comments;
    }

    private static Comment loadComment(JSONObject commentJSON) {
        return new Comment(
                parseUUID(commentJSON.get("commentId"), UUID.randomUUID()),
                asString(commentJSON.get("text")),
                parseUUID(commentJSON.get("authorId"), null),
                parseLocalDateTime(commentJSON.get("timestamp"), LocalDateTime.now()),
                asBoolean(commentJSON.get("isEdited")),
                asInt(commentJSON.get("upvoteCount")),
                asInt(commentJSON.get("downvoteCount")),
                loadComments((JSONArray) commentJSON.get("replies")));
    }

    private static List<String> loadStringList(JSONArray values) {
        List<String> items = new ArrayList<>();
        if (values == null) {
            return items;
        }

        for (Object value : values) {
            if (value instanceof String stringValue && !stringValue.isBlank()) {
                items.add(stringValue);
            }
        }

        return items;
    }

    private static String asString(Object value) {
        return asString(value, "");
    }

    private static String asString(Object value, String defaultValue) {
        if (value instanceof String stringValue) {
            return stringValue;
        }
        return defaultValue;
    }

    private static int asInt(Object value) {
        if (value instanceof Number numberValue) {
            return numberValue.intValue();
        }

        if (value instanceof String stringValue) {
            try {
                return Integer.parseInt(stringValue);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    private static Integer asNullableInt(Object value) {
        if (value == null) {
            return null;
        }

        return asInt(value);
    }

    private static boolean asBoolean(Object value) {
        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }

        if (value instanceof String stringValue) {
            return Boolean.parseBoolean(stringValue);
        }

        return false;
    }

    private static UUID parseUUID(Object value, UUID defaultValue) {
        if (value instanceof UUID uuidValue) {
            return uuidValue;
        }

        if (value instanceof String stringValue && !stringValue.isBlank()) {
            try {
                return UUID.fromString(stringValue);
            } catch (IllegalArgumentException e) {
                return defaultValue;
            }
        }

        return defaultValue;
    }

    private static LocalDateTime parseLocalDateTime(Object value, LocalDateTime defaultValue) {
        if (value instanceof String stringValue && !stringValue.isBlank()) {
            try {
                return LocalDateTime.parse(stringValue);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        return defaultValue;
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> enumType, Object value, E defaultValue) {
        if (value instanceof String enumName && !enumName.isBlank()) {
            try {
                return Enum.valueOf(enumType, enumName);
            } catch (IllegalArgumentException e) {
                return defaultValue;
            }
        }

        return defaultValue;
    }
}
