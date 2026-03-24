package com.model;

import java.lang.reflect.Field;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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

    // Tests for Bug #63

    @Test
    public void userList_freshInstance_doesNotContainPreviouslyAddedUser() throws Exception {
        UserList first = UserList.getInstance();
        first.addUser(new User("carry@test.com", "Password1!", "First", "Last"));

        Field f = UserList.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);

        UserList second = UserList.getInstance();
        assertNull(second.getUserByEmail("carry@test.com"));
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
        assertNull(second.getQuestion(q.getQuestionId()));
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
        assertSame(a, b);
    }

    @Test
    public void questionList_getInstance_returnsSameReference() {
        QuestionList a = QuestionList.getInstance();
        QuestionList b = QuestionList.getInstance();
        assertSame(a, b);
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

    // Tests for Bug #66

    @Test
    public void systemFacade_login_withValidEmail_returnsUser() {
        UserList.getInstance().addUser(new User("email@test.com", "Password1!", "First", "Last"));
        User result = SystemFacade.getInstance().login("email@test.com", "Password1!");
        assertNotNull(result);
    }

    @Test
    public void systemFacade_login_withWrongPassword_returnsNull() {
        UserList.getInstance().addUser(new User("email@test.com", "Password1!", "First", "Last"));
        User result = SystemFacade.getInstance().login("email@test.com", "WrongPass1!");
        assertNull(result);
    }

    @Test
    public void systemFacade_login_withValidUsername_returnsNull() {
        UserList.getInstance().addUser(new User("email@test.com", "Password1!", "First", "Last"));
        User result = SystemFacade.getInstance().login("First", "Password1!");
        assertNull(result);
    }

    @Test
    public void systemFacade_login_withNullIdentifier_returnsNull() {
        User result = SystemFacade.getInstance().login(null, "Password1!");
        assertNull(result);
    }

    @Test
    public void systemFacade_login_setsCurrentUser_onSuccess() {
        UserList.getInstance().addUser(new User("email@test.com", "Password1!", "First", "Last"));
        SystemFacade.getInstance().login("email@test.com", "Password1!");
        assertNotNull(SystemFacade.getInstance().getCurrentUser());
    }

    @Test
    public void systemFacade_login_clearsCurrentUser_onFailure() {
        SystemFacade.getInstance().login("nobody@test.com", "Password1!");
        assertNull(SystemFacade.getInstance().getCurrentUser());
    }

    // Tests for Bug #67

    @Test
    public void addQuestion_withNoCurrentUser_returnsFalse() {
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        assertFalse(SystemFacade.getInstance().addQuestion(q));
    }

    @Test
    public void addQuestion_withNonContributorUser_returnsFalse() {
        User user = new User("a@test.com", "Password1!", "First", "Last");
        user.setContributor(false);
        UserList.getInstance().addUser(user);
        SystemFacade.getInstance().login("a@test.com", "Password1!");
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        assertFalse(SystemFacade.getInstance().addQuestion(q));
    }

    @Test
    public void addQuestion_withContributorUser_returnsTrue() {
        User user = new User("a@test.com", "Password1!", "First", "Last");
        user.setContributor(true);
        UserList.getInstance().addUser(user);
        SystemFacade.getInstance().login("a@test.com", "Password1!");
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        assertTrue(SystemFacade.getInstance().addQuestion(q));
    }

    @Test
    public void addQuestion_withNullQuestion_returnsFalse() {
        User user = new User("a@test.com", "Password1!", "First", "Last");
        user.setContributor(true);
        UserList.getInstance().addUser(user);
        SystemFacade.getInstance().login("a@test.com", "Password1!");
        assertFalse(SystemFacade.getInstance().addQuestion(null));
    }

    @Test
    public void addQuestion_nullUserAndNullQuestion_returnsFalse() {
        assertFalse("Bug #67: cannot distinguish null user from null question, both return false",
                SystemFacade.getInstance().addQuestion(null));
    }

    @Test
    public void addQuestion_nonContributorAndNullQuestion_returnsFalse() {
        User user = new User("a@test.com", "Password1!", "First", "Last");
        user.setContributor(false);
        UserList.getInstance().addUser(user);
        SystemFacade.getInstance().login("a@test.com", "Password1!");
        assertFalse("Bug #67: cannot distinguish non-contributor from null question, both return false",
                SystemFacade.getInstance().addQuestion(null));
    }

    // Tests for Bug #68

    @Test
    public void comment_edit_withValidText_updatesText() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("Updated text");
        assertEquals("Updated text", c.getText());
    }

    @Test
    public void comment_edit_withValidText_setsIsEdited() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("Updated text");
        assertTrue(c.isEdited());
    }

    @Test
    public void comment_edit_withNullText_storesEmptyString() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit(null);
        assertEquals("", c.getText());
    }

    @Test
    public void comment_edit_withBlankText_storesBlankText() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("   ");
        assertEquals("Bug #68: blank whitespace should not be accepted as valid edit text",
                "   ", c.getText());
    }

    @Test
    public void comment_edit_withBlankText_setsIsEdited() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("   ");
        assertTrue("Bug #68: isEdited is set to true even for blank text",
                c.isEdited());
    }

    @Test
    public void comment_edit_updatesTimestamp() throws InterruptedException {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        java.time.LocalDateTime before = c.getTimestamp();
        Thread.sleep(10);
        c.edit("Updated text");
        assertTrue(c.getTimestamp().isAfter(before));
    }
}
