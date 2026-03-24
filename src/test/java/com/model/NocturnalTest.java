package com.model;

import java.lang.reflect.Field;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/* Tests for Bug #63 */
public class NocturnalTest {

    @Before
    public void resetSingletons() throws Exception {
        Field userListField = UserList.class.getDeclaredField("instance");
        userListField.setAccessible(true);
        userListField.set(null, null);

        Field questionListField = QuestionList.class.getDeclaredField("instance");
        questionListField.setAccessible(true);
        questionListField.set(null, null);

        Field facadeField = SystemFacade.class.getDeclaredField("instance");
        facadeField.setAccessible(true);
        facadeField.set(null, null);
    }

    @After
    public void resetSingletonsAfter() throws Exception {
        resetSingletons();
    }

    @Test
    public void userList_freshInstance_doesNotContainPreviouslyAddedUser() throws Exception {
        UserList first = UserList.getInstance();
        first.addUser(new User("carry@test.com", "Password1!", "First", "Last"));

        Field f = UserList.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);

        UserList second = UserList.getInstance();
        assertNull("Bug #63: user from previous instance should not carry over",
                second.getUserByEmail("carry@test.com"));
    }

    @Test
    public void questionList_freshInstance_doesNotContainPreviouslyAddedQuestion() throws Exception {
        QuestionList first = QuestionList.getInstance();
        InterviewQuestion q = new InterviewQuestion(
                "Test Q", "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        first.addQuestion(q);

        Field f = QuestionList.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);

        QuestionList second = QuestionList.getInstance();
        assertNull("Bug #63: question from previous instance should not carry over",
                second.getQuestion(q.getQuestionId()));
    }

    @Test
    public void userList_sameInstance_returnsAddedUser() {
        UserList ul = UserList.getInstance();
        ul.addUser(new User("same@test.com", "Password1!", "First", "Last"));
        assertNotNull(ul.getUserByEmail("same@test.com"));
    }

    @Test
    public void userList_getInstance_returnsSameReference() {
        UserList a = UserList.getInstance();
        UserList b = UserList.getInstance();
        assertSame("getInstance should return the same singleton object", a, b);
    }

    @Test
    public void questionList_getInstance_returnsSameReference() {
        QuestionList a = QuestionList.getInstance();
        QuestionList b = QuestionList.getInstance();
        assertSame("getInstance should return the same singleton object", a, b);
    }

    // Tests for Bug #64

    @Test
    public void login_withCorrectPassword_returnsTrue() {
        User user = new User("a@test.com", "Password1!", "First", "Last");
        assertTrue(user.login("Password1!"));
    }

    @Test
    public void login_withWrongPassword_returnsFalse() {
        User user = new User("a@test.com", "Password1!", "First", "Last");
        assertFalse(user.login("WrongPass1!"));
    }

    @Test
    public void login_withNullPassword_returnsFalse() {
        User user = new User("a@test.com", "Password1!", "First", "Last");
        assertFalse(user.login(null));
    }

    @Test
    public void login_nullPasswordAtConstruction_cannotAuthenticate() {
        User user = new User(java.util.UUID.randomUUID(), "b@test.com", null, "First", "Last");
        assertFalse(user.login("anyPassword"));
    }

    @Test
    public void login_updatesLastLogin_onSuccess() {
        User user = new User("a@test.com", "Password1!", "First", "Last");
        assertNull(user.getLastLogin());
        user.login("Password1!");
        assertNotNull(user.getLastLogin());
    }

    @Test
    public void login_doesNotUpdateLastLogin_onFailure() {
        User user = new User("a@test.com", "Password1!", "First", "Last");
        user.login("WrongPass1!");
        assertNull(user.getLastLogin());
    }

    // Tests for Bug #65

    @Test
    public void updateContent_withValidTitle_updatesTitle() {
        InterviewQuestion q = new InterviewQuestion(
                "Old Title", "Old Desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        q.updateContent("New Title", "New Desc");
        assertEquals("New Title", q.getTitle());
    }

    @Test
    public void updateContent_withNullTitle_doesNotChangeTitle() {
        InterviewQuestion q = new InterviewQuestion(
                "Original", "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        q.updateContent(null, "New Desc");
        assertEquals("Original", q.getTitle());
    }

    @Test
    public void updateContent_withBlankTitle_doesNotChangeTitle() {
        InterviewQuestion q = new InterviewQuestion(
                "Original", "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        q.updateContent("   ", "New Desc");
        assertEquals("Original", q.getTitle());
    }

    @Test
    public void interviewQuestion_constructedWithNullTitle_titleIsNull() {
        InterviewQuestion q = new InterviewQuestion(
                null, "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        assertNull(q.getTitle());
    }

    @Test
    public void updateContent_onNullTitleQuestion_setsNewTitle() {
        InterviewQuestion q = new InterviewQuestion(
                null, "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        q.updateContent("Fixed Title", "desc");
        assertEquals("Fixed Title", q.getTitle());
    }

    @Test
    public void updateContent_updatesLastUpdatedTimestamp() throws InterruptedException {
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        java.time.LocalDateTime before = q.getLastUpdated();
        Thread.sleep(10);
        q.updateContent("New Title", "New Desc");
        assertTrue(q.getLastUpdated().isAfter(before));
    }
}
