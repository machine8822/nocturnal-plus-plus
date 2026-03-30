package com.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DataWriterTest {

    @Before
    public void resetSingletons() throws Exception {
        Field userListField = UserList.class.getDeclaredField("instance");
        userListField.setAccessible(true);
        userListField.set(null, null);

        Field questionListField = QuestionList.class.getDeclaredField("instance");
        questionListField.setAccessible(true);
        questionListField.set(null, null);
    }

    @After
    public void resetSingletonsAfter() throws Exception {
        resetSingletons();
    }

    // saveUsers

    @Test
    public void saveUsers_emptyList_returnsTrue() {
        assertTrue(DataWriter.saveUsers(new ArrayList<>()));
    }

    @Test
    public void saveUsers_singleUser_returnsTrue() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("save@test.com", "Password1!", "Save", "User"));
        assertTrue(DataWriter.saveUsers(users));
    }

    @Test
    public void saveUsers_multipleUsers_returnsTrue() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("one@test.com", "Password1!", "One", "User"));
        users.add(new User("two@test.com", "Password1!", "Two", "User"));
        users.add(new User("three@test.com", "Password1!", "Three", "User"));
        assertTrue(DataWriter.saveUsers(users));
    }

    @Test
    public void saveUsers_thenLoadUsers_roundTripsCorrectly() {
        ArrayList<User> users = new ArrayList<>();
        User user = new User("round@test.com", "Password1!", "Round", "Trip");
        user.setAdmin(true);
        user.setContributor(true);
        users.add(user);

        DataWriter.saveUsers(users);
        ArrayList<User> loaded = DataLoader.loadUsers();

        assertEquals(1, loaded.size());
        assertEquals("round@test.com", loaded.get(0).getEmail());
        assertEquals("Round", loaded.get(0).getFirstName());
        assertEquals("Trip", loaded.get(0).getLastName());
        assertTrue(loaded.get(0).isAdmin());
        assertTrue(loaded.get(0).isContributor());
    }

    @Test
    public void saveUsers_preservesProfile() {
        ArrayList<User> users = new ArrayList<>();
        User user = new User("prof@test.com", "Password1!", "Prof", "User");
        user.setProfile(new Profile("USC", "CS", 2026, 10, "resume.pdf"));
        users.add(user);

        DataWriter.saveUsers(users);
        ArrayList<User> loaded = DataLoader.loadUsers();

        Profile p = loaded.get(0).getProfile();
        assertEquals("USC", p.getSchool());
        assertEquals("CS", p.getMajor());
        assertEquals(2026, p.getGradYear());
        assertEquals(10, p.getTotalUpvotes());
        assertEquals("resume.pdf", p.getResumeURL());
    }

    // saveQuestions

    @Test
    public void saveQuestions_emptyList_returnsTrue() {
        assertTrue(DataWriter.saveQuestions(new ArrayList<>()));
    }

    @Test
    public void saveQuestions_singleQuestion_returnsTrue() {
        ArrayList<InterviewQuestion> questions = new ArrayList<>();
        questions.add(new InterviewQuestion(
                "Title", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null));
        assertTrue(DataWriter.saveQuestions(questions));
    }

    @Test
    public void saveQuestions_thenLoadQuestions_roundTripsCorrectly() {
        ArrayList<InterviewQuestion> questions = new ArrayList<>();
        UUID authorId = UUID.randomUUID();
        InterviewQuestion q = new InterviewQuestion(
                "Two Sum", "Find indices", Difficulty.MEDIUM, Category.ARRAY, QuestionType.CODING, authorId);
        q.recordAttempt(true);
        q.recordAttempt(false);
        questions.add(q);

        DataWriter.saveQuestions(questions);
        ArrayList<InterviewQuestion> loaded = DataLoader.loadQuestions();

        assertEquals(1, loaded.size());
        assertEquals("Two Sum", loaded.get(0).getTitle());
        assertEquals("Find indices", loaded.get(0).getDescription());
        assertEquals(Difficulty.MEDIUM, loaded.get(0).getDifficulty());
        assertEquals(Category.ARRAY, loaded.get(0).getCategory());
        assertEquals(2, loaded.get(0).getTotalAttempts());
        assertEquals(1, loaded.get(0).getTotalSuccesses());
    }

    @Test
    public void saveQuestions_preservesSections() {
        ArrayList<InterviewQuestion> questions = new ArrayList<>();
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        Section s = new Section("Constraints", "n up to 10^4", DataType.STRING, SectionType.CONSTRAINT);
        s.addConstraint("n >= 2");
        s.addExample("[2,7,11,15], target=9");
        s.setExpectedTimeComplexity("O(n)");
        s.setMaxLinesOfCode(30);
        s.setTimeLimitSeconds(2);
        q.addSection(s);
        questions.add(q);

        DataWriter.saveQuestions(questions);
        ArrayList<InterviewQuestion> loaded = DataLoader.loadQuestions();

        assertEquals(1, loaded.get(0).getSections().size());
        Section loadedSection = loaded.get(0).getSections().get(0);
        assertEquals("Constraints", loadedSection.getTitle());
        assertEquals(SectionType.CONSTRAINT, loadedSection.getSectionType());
        assertEquals(1, loadedSection.getConstraints().size());
        assertEquals(1, loadedSection.getExamples().size());
        assertEquals("O(n)", loadedSection.getExpectedTimeComplexity());
        assertEquals(Integer.valueOf(30), loadedSection.getMaxLinesOfCode());
        assertEquals(Integer.valueOf(2), loadedSection.getTimeLimitSeconds());
    }

    @Test
    public void saveQuestions_preservesComments() {
        ArrayList<InterviewQuestion> questions = new ArrayList<>();
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        UUID commentAuthor = UUID.randomUUID();
        Comment c = new Comment("Great question", commentAuthor);
        Comment reply = new Comment("Thanks", commentAuthor);
        c.addReply(reply);
        q.addComment(c);
        questions.add(q);

        DataWriter.saveQuestions(questions);
        ArrayList<InterviewQuestion> loaded = DataLoader.loadQuestions();

        assertEquals(1, loaded.get(0).getComments().size());
        Comment loadedComment = loaded.get(0).getComments().get(0);
        assertEquals("Great question", loadedComment.getText());
        assertEquals(1, loadedComment.getReplies().size());
        assertEquals("Thanks", loadedComment.getReplies().get(0).getText());
    }

    @Test
    public void saveQuestions_preservesAnswers() {
        ArrayList<InterviewQuestion> questions = new ArrayList<>();
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        Section s = new Section("Desc", "body", DataType.STRING, SectionType.DESCRIPTION);
        Answer a = new Answer("int x = 1;", "Simple assignment", UUID.randomUUID());
        a.upvote();
        a.upvote();
        a.downvote();
        s.addAnswer(a);
        q.addSection(s);
        questions.add(q);

        DataWriter.saveQuestions(questions);
        ArrayList<InterviewQuestion> loaded = DataLoader.loadQuestions();

        Answer loadedAnswer = loaded.get(0).getSections().get(0).getAnswers().get(0);
        assertEquals("int x = 1;", loadedAnswer.getCodeSnippet());
        assertEquals("Simple assignment", loadedAnswer.getExplanation());
        assertEquals(2, loadedAnswer.getUpvoteCount());
        assertEquals(1, loadedAnswer.getDownvoteCount());
    }
}
