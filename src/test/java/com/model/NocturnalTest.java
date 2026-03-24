package com.model;

import java.lang.reflect.Field;

import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Bug #63
 */
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
}
