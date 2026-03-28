package com.model;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class DataLoaderTest {

    private Path usersPath;
    private Path questionsPath;
    private String originalUsersJson;
    private String originalQuestionsJson;

    @Before
    public void setUp() throws Exception {
        // Find the test JSON files.
        usersPath = toPath("/json/users.json");
        questionsPath = toPath("/json/questions.json");

        // Save original file text so we can put it back after each test.
        originalUsersJson = Files.readString(usersPath, StandardCharsets.UTF_8);
        originalQuestionsJson = Files.readString(questionsPath, StandardCharsets.UTF_8);
    }

    @After
    public void tearDown() throws Exception {
        // Put files back the way they were before the test.
        Files.writeString(usersPath, originalUsersJson, StandardCharsets.UTF_8);
        Files.writeString(questionsPath, originalQuestionsJson, StandardCharsets.UTF_8);
    }

    // This test checks normal input: one good user JSON should load correctly.
    @Test
    public void loadUsers_withValidJson_mapsAllExpectedFields() throws Exception {
        UUID userId = UUID.randomUUID();
        String createdAt = "2025-03-01T10:15:30";
        String lastLogin = "2025-03-02T11:22:33";

        String usersJson = "[" +
                "{" +
                "\"userId\":\"" + userId + "\"," +
                "\"email\":\"alice@example.com\"," +
                "\"passwordHash\":\"$2a$10$abcdefghijklmnopqrstuv\"," +
                "\"firstName\":\"Alice\"," +
                "\"lastName\":\"Ng\"," +
                "\"createdAt\":\"" + createdAt + "\"," +
                "\"lastLogin\":\"" + lastLogin + "\"," +
                "\"isAdmin\":true," +
                "\"isContributor\":false," +
                "\"profile\":{" +
                "\"school\":\"USC\"," +
                "\"major\":\"CS\"," +
                "\"gradYear\":2027," +
                "\"totalUpvotes\":12," +
                "\"resumeURL\":\"resume.pdf\"" +
                "}" +
                "}" +
                "]";

        Files.writeString(usersPath, usersJson, StandardCharsets.UTF_8);

        ArrayList<User> users = DataLoader.loadUsers();

        assertEquals(1, users.size());
        User user = users.get(0);

        assertEquals(userId, user.getUserId());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("$2a$10$abcdefghijklmnopqrstuv", user.getPasswordHash());
        assertEquals("Alice", user.getFirstName());
        assertEquals("Ng", user.getLastName());
        assertEquals(LocalDateTime.parse(createdAt), user.getCreatedAt());
        assertEquals(LocalDateTime.parse(lastLogin), user.getLastLogin());
        assertTrue(user.isAdmin());
        assertFalse(user.isContributor());

        Profile profile = user.getProfile();
        assertEquals("USC", profile.getSchool());
        assertEquals("CS", profile.getMajor());
        assertEquals(2027, profile.getGradYear());
        assertEquals(12, profile.getTotalUpvotes());
        assertEquals("resume.pdf", profile.getResumeURL());
    }

    // This test checks messy input: missing or bad values should not break loading.
    @Test
    public void loadUsers_withInvalidAndMissingFields_usesSafeDefaults() throws Exception {
        String usersJson = "[" +
                "{" +
                "\"userId\":\"not-a-uuid\"," +
                "\"email\":null," +
                "\"passwordHash\":123," +
                "\"firstName\":null," +
                "\"lastName\":null," +
                "\"createdAt\":\"not-a-date\"," +
                "\"lastLogin\":\"invalid-date\"," +
                "\"isAdmin\":\"true\"," +
                "\"isContributor\":\"false\"," +
                "\"profile\":null" +
                "}" +
                "]";

        Files.writeString(usersPath, usersJson, StandardCharsets.UTF_8);

        ArrayList<User> users = DataLoader.loadUsers();

        assertEquals(1, users.size());
        User user = users.get(0);

        assertNotNull(user.getUserId());
        assertEquals("", user.getEmail());
        assertEquals("", user.getPasswordHash());
        assertEquals("", user.getFirstName());
        assertEquals("", user.getLastName());
        assertNotNull(user.getCreatedAt());
        assertNull(user.getLastLogin());
        assertTrue(user.isAdmin());
        assertFalse(user.isContributor());

        Profile profile = user.getProfile();
        assertEquals("", profile.getSchool());
        assertEquals("", profile.getMajor());
        assertEquals(0, profile.getGradYear());
        assertEquals(0, profile.getTotalUpvotes());
        assertEquals("", profile.getResumeURL());
    }

    // This test checks broken JSON text: loader should return an empty list.
    @Test
    public void loadUsers_withMalformedJson_returnsEmptyList() throws Exception {
        Files.writeString(usersPath, "[{bad json", StandardCharsets.UTF_8);

        ArrayList<User> users = DataLoader.loadUsers();

        assertTrue(users.isEmpty());
    }

    // This test checks a full question with nested data (comments, replies, sections, answers).
    @Test
    public void loadQuestions_withValidNestedJson_mapsQuestionSectionsAnswersAndComments() throws Exception {
        UUID questionId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();
        UUID replyId = UUID.randomUUID();
        UUID sectionCommentId = UUID.randomUUID();
        UUID answerId = UUID.randomUUID();

        String createdAt = "2025-01-01T00:00:00";
        String updatedAt = "2025-01-02T00:00:00";

        String questionsJson = "[" +
                "{" +
                "\"questionId\":\"" + questionId + "\"," +
                "\"title\":\"Two Sum\"," +
                "\"description\":\"Find indices\"," +
                "\"difficulty\":\"MEDIUM\"," +
                "\"type\":\"CODING\"," +
                "\"category\":\"ARRAY\"," +
                "\"imageURL\":\"img.png\"," +
                "\"authorId\":\"" + authorId + "\"," +
                "\"totalAttempts\":10," +
                "\"totalSuccesses\":4," +
                "\"createdAt\":\"" + createdAt + "\"," +
                "\"lastUpdated\":\"" + updatedAt + "\"," +
                "\"comments\":[{" +
                "\"commentId\":\"" + commentId + "\"," +
                "\"text\":\"Top-level\"," +
                "\"authorId\":\"" + authorId + "\"," +
                "\"timestamp\":\"2025-01-03T10:00:00\"," +
                "\"isEdited\":true," +
                "\"upvoteCount\":3," +
                "\"downvoteCount\":1," +
                "\"replies\":[{" +
                "\"commentId\":\"" + replyId + "\"," +
                "\"text\":\"Reply\"," +
                "\"authorId\":\"" + authorId + "\"," +
                "\"timestamp\":\"2025-01-03T10:01:00\"," +
                "\"isEdited\":false," +
                "\"upvoteCount\":0," +
                "\"downvoteCount\":0," +
                "\"replies\":[]" +
                "}]" +
                "}]," +
                "\"sections\":[{" +
                "\"title\":\"Constraints\"," +
                "\"content\":\"n up to 10^4\"," +
                "\"type\":\"CONSTRAINT\"," +
                "\"dataType\":\"STRING\"," +
                "\"imageURL\":\"sec.png\"," +
                "\"expectedTimeComplexity\":\"O(n)\"," +
                "\"maxLinesOfCode\":30," +
                "\"timeLimitSeconds\":2," +
                "\"constraints\":[\"n >= 2\"]," +
                "\"examples\":[\"[2,7,11,15], target=9\"]," +
                "\"answers\":[{" +
                "\"answerId\":\"" + answerId + "\"," +
                "\"codeSnippet\":\"return map;\"," +
                "\"explanation\":\"Use hash map\"," +
                "\"upvoteCount\":5," +
                "\"downvoteCount\":1," +
                "\"authorId\":\"" + authorId + "\"," +
                "\"createdAt\":\"2025-01-04T00:00:00\"," +
                "\"comments\":[]" +
                "}]," +
                "\"comments\":[{" +
                "\"commentId\":\"" + sectionCommentId + "\"," +
                "\"text\":\"Section comment\"," +
                "\"authorId\":\"" + authorId + "\"," +
                "\"timestamp\":\"2025-01-05T00:00:00\"," +
                "\"isEdited\":false," +
                "\"upvoteCount\":0," +
                "\"downvoteCount\":0," +
                "\"replies\":[]" +
                "}]" +
                "}]" +
                "}" +
                "]";

        Files.writeString(questionsPath, questionsJson, StandardCharsets.UTF_8);

        // Load questions from file after we write our test JSON.
        ArrayList<InterviewQuestion> questions = DataLoader.loadQuestions();

        assertEquals(1, questions.size());

        InterviewQuestion question = questions.get(0);
        assertEquals(questionId, question.getQuestionId());
        assertEquals("Two Sum", question.getTitle());
        assertEquals("Find indices", question.getDescription());
        assertEquals(Difficulty.MEDIUM, question.getDifficulty());
        assertEquals(QuestionType.CODING, question.getType());
        assertEquals(Category.ARRAY, question.getCategory());
        assertEquals("img.png", question.getImageURL());
        assertEquals(authorId, question.getAuthorId());
        assertEquals(10, question.getTotalAttempts());
        assertEquals(4, question.getTotalSuccesses());
        assertEquals(LocalDateTime.parse(createdAt), question.getCreatedAt());
        assertEquals(LocalDateTime.parse(updatedAt), question.getLastUpdated());

        // Check top-level question comment and its reply.
        assertEquals(1, question.getComments().size());
        Comment topComment = question.getComments().get(0);
        assertEquals(commentId, topComment.getCommentId());
        assertEquals("Top-level", topComment.getText());
        assertTrue(topComment.isEdited());
        assertEquals(3, topComment.getUpvoteCount());
        assertEquals(1, topComment.getDownvoteCount());
        assertEquals(1, topComment.getReplies().size());
        assertEquals(replyId, topComment.getReplies().get(0).getCommentId());

        // Check section info.
        assertEquals(1, question.getSections().size());
        Section section = question.getSections().get(0);
        assertEquals("Constraints", section.getTitle());
        assertEquals("n up to 10^4", section.getBody());
        assertEquals(SectionType.CONSTRAINT, section.getSectionType());
        assertEquals(DataType.STRING, section.getDataType());
        assertEquals("sec.png", section.getImageURL());
        assertEquals("O(n)", section.getExpectedTimeComplexity());
        assertEquals(Integer.valueOf(30), section.getMaxLinesOfCode());
        assertEquals(Integer.valueOf(2), section.getTimeLimitSeconds());
        assertEquals(1, section.getConstraints().size());
        assertEquals(1, section.getExamples().size());

        // Check answer inside section.
        assertEquals(1, section.getAnswers().size());
        Answer answer = section.getAnswers().get(0);
        assertEquals(answerId, answer.getAnswerId());
        assertEquals("return map;", answer.getCodeSnippet());
        assertEquals("Use hash map", answer.getExplanation());
        assertEquals(5, answer.getUpvoteCount());
        assertEquals(1, answer.getDownvoteCount());

        // Check section comment.
        assertEquals(1, section.getComments().size());
        assertEquals(sectionCommentId, section.getComments().get(0).getCommentId());
    }

    // This test checks bad enum and date values: loader should use default values.
    @Test
    public void loadQuestions_withInvalidEnumsAndInvalidDates_usesFallbackDefaults() throws Exception {
        UUID validQuestionId = UUID.randomUUID();

        String questionsJson = "[" +
                "{" +
                "\"questionId\":\"" + validQuestionId + "\"," +
                "\"title\":\"Q\"," +
                "\"description\":\"D\"," +
                "\"difficulty\":\"NOT_REAL\"," +
                "\"type\":\"NOT_REAL\"," +
                "\"category\":\"NOT_REAL\"," +
                "\"authorId\":\"not-a-uuid\"," +
                "\"createdAt\":\"invalid-date\"," +
                "\"lastUpdated\":\"invalid-date\"," +
                "\"sections\":[{" +
                "\"title\":\"S\"," +
                "\"content\":\"B\"," +
                "\"type\":\"NOT_REAL\"," +
                "\"dataType\":\"NOT_REAL\"," +
                "\"constraints\":[null,\"\",\"  \",\"ok\"]," +
                "\"examples\":[null,\"ok-example\"]" +
                "}]" +
                "}" +
                "]";

        Files.writeString(questionsPath, questionsJson, StandardCharsets.UTF_8);

        ArrayList<InterviewQuestion> questions = DataLoader.loadQuestions();

        assertEquals(1, questions.size());
        InterviewQuestion question = questions.get(0);

        assertEquals(Difficulty.EASY, question.getDifficulty());
        assertEquals(QuestionType.SHORT_ANSWER, question.getType());
        assertEquals(Category.ARRAY, question.getCategory());
        assertNull(question.getAuthorId());
        assertNotNull(question.getCreatedAt());
        assertNotNull(question.getLastUpdated());

        assertEquals(1, question.getSections().size());
        Section section = question.getSections().get(0);
        assertEquals(SectionType.DESCRIPTION, section.getSectionType());
        assertNull(section.getDataType());
        assertEquals(1, section.getConstraints().size());
        assertEquals("ok", section.getConstraints().get(0));
        assertEquals(1, section.getExamples().size());
        assertEquals("ok-example", section.getExamples().get(0));
    }

    // This test checks broken question JSON text: loader should return an empty list.
    @Test
    public void loadQuestions_withMalformedJson_returnsEmptyList() throws Exception {
        Files.writeString(questionsPath, "[{broken", StandardCharsets.UTF_8);

        ArrayList<InterviewQuestion> questions = DataLoader.loadQuestions();

        assertTrue(questions.isEmpty());
    }

    private Path toPath(String resourcePath) throws Exception {
        // Convert classpath resource into a Path we can read/write in tests.
        URL resource = DataLoader.class.getResource(resourcePath);
        assertNotNull("Missing test resource: " + resourcePath, resource);
        return Paths.get(resource.toURI());
    }
}
