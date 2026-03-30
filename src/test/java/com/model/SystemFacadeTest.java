package com.model;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SystemFacadeTest {

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

    // Bug #63 — singletons cannot be reset between tests

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
        assertSame(UserList.getInstance(), UserList.getInstance());
    }

    @Test
    public void questionList_getInstance_returnsSameReference() {
        assertSame(QuestionList.getInstance(), QuestionList.getInstance());
    }

    // Bug #66 — login() only authenticates by email, not username

    @Test
    public void login_withValidEmail_returnsUser() {
        UserList.getInstance().addUser(new User("email@test.com", "Password1!", "First", "Last"));
        User result = SystemFacade.getInstance().login("email@test.com", "Password1!");
        assertNotNull(result);
    }

    @Test
    public void login_withWrongPassword_returnsNull() {
        UserList.getInstance().addUser(new User("email@test.com", "Password1!", "First", "Last"));
        User result = SystemFacade.getInstance().login("email@test.com", "WrongPass1!");
        assertNull(result);
    }

    @Test
    public void login_withValidUsername_returnsNull() {
        UserList.getInstance().addUser(new User("email@test.com", "Password1!", "First", "Last"));
        User result = SystemFacade.getInstance().login("First", "Password1!");
        assertNull(result);
    }

    @Test
    public void login_withNullIdentifier_returnsNull() {
        assertNull(SystemFacade.getInstance().login(null, "Password1!"));
    }

    @Test
    public void login_setsCurrentUser_onSuccess() {
        UserList.getInstance().addUser(new User("email@test.com", "Password1!", "First", "Last"));
        SystemFacade.getInstance().login("email@test.com", "Password1!");
        assertNotNull(SystemFacade.getInstance().getCurrentUser());
    }

    @Test
    public void login_clearsCurrentUser_onFailure() {
        SystemFacade.getInstance().login("nobody@test.com", "Password1!");
        assertNull(SystemFacade.getInstance().getCurrentUser());
    }

    @Test
    public void login_setsCurrentQuestion_toFirstQuestion() {
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        InterviewQuestion first = new InterviewQuestion(
                "First Q", "d", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        ql.addQuestion(first);
        UserList.getInstance().addUser(new User("cq@test.com", "Password1!", "F", "L"));
        SystemFacade.getInstance().login("cq@test.com", "Password1!");
        assertSame(first, SystemFacade.getInstance().getCurrentQuestion());
    }

    // Bug #67 — addQuestion() returns false for both null user and non-contributor

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
        assertFalse(SystemFacade.getInstance().addQuestion(null));
    }

    @Test
    public void addQuestion_nonContributorAndNullQuestion_returnsFalse() {
        User user = new User("a@test.com", "Password1!", "First", "Last");
        user.setContributor(false);
        UserList.getInstance().addUser(user);
        SystemFacade.getInstance().login("a@test.com", "Password1!");
        assertFalse(SystemFacade.getInstance().addQuestion(null));
    }

    // logout

    @Test
    public void logout_clearsCurrentUser_returnsTrue() {
        User user = new User("sf@test.com", "Password1!", "First", "Last");
        UserList.getInstance().addUser(user);
        SystemFacade sf = SystemFacade.getInstance();
        sf.login("sf@test.com", "Password1!");
        assertNotNull(sf.getCurrentUser());
        assertTrue(sf.logout());
        assertNull(sf.getCurrentUser());
    }

    @Test
    public void logout_clearsCurrentQuestion() {
        User user = new User("sf@test.com", "Password1!", "First", "Last");
        UserList.getInstance().addUser(user);
        SystemFacade sf = SystemFacade.getInstance();
        sf.login("sf@test.com", "Password1!");
        sf.logout();
        assertNull(sf.getCurrentQuestion());
    }

    // selectQuestion

    @Test
    public void selectQuestion_validId_setsCurrentQuestion() {
        SystemFacade sf = SystemFacade.getInstance();
        QuestionList ql = QuestionList.getInstance();
        InterviewQuestion q = new InterviewQuestion(
                "T", "d", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        ql.addQuestion(q);
        sf.selectQuestion(q.getQuestionId());
        assertSame(q, sf.getCurrentQuestion());
    }

    @Test
    public void selectQuestion_nullId_setsCurrentQuestionNull() {
        SystemFacade sf = SystemFacade.getInstance();
        sf.selectQuestion(null);
        assertNull(sf.getCurrentQuestion());
    }

    @Test
    public void selectQuestion_nonExistentId_setsCurrentQuestionNull() {
        SystemFacade sf = SystemFacade.getInstance();
        sf.selectQuestion(java.util.UUID.randomUUID());
        assertNull(sf.getCurrentQuestion());
    }

    // getQuestionsByCategory

    @Test
    public void getQuestionsByCategory_returnsMatches() {
        SystemFacade sf = SystemFacade.getInstance();
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        ql.addQuestion(new InterviewQuestion(
                "LL Q", "d", Difficulty.EASY, Category.LINKED_LIST, QuestionType.CODING, null));
        ql.addQuestion(new InterviewQuestion(
                "Arr Q", "d", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null));
        assertEquals(1, sf.getQuestionsByCategory(Category.LINKED_LIST).size());
    }

    @Test
    public void getQuestionsByCategory_noMatch_returnsEmptyList() {
        SystemFacade sf = SystemFacade.getInstance();
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        assertTrue(sf.getQuestionsByCategory(Category.BIG_O).isEmpty());
    }

    // getQuestionsByDifficulty

    @Test
    public void getQuestionsByDifficulty_returnsMatches() {
        SystemFacade sf = SystemFacade.getInstance();
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        ql.addQuestion(new InterviewQuestion(
                "Easy Q", "d", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null));
        ql.addQuestion(new InterviewQuestion(
                "Hard Q", "d", Difficulty.HARD, Category.ARRAY, QuestionType.CODING, null));
        assertEquals(1, sf.getQuestionsByDifficulty(Difficulty.HARD).size());
    }

    @Test
    public void getQuestionsByDifficulty_noMatch_returnsEmptyList() {
        SystemFacade sf = SystemFacade.getInstance();
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        assertTrue(sf.getQuestionsByDifficulty(Difficulty.MEDIUM).isEmpty());
    }

    // addUser / deleteUser / getUsers

    @Test
    public void addUser_valid_returnsTrue() {
        assertTrue(SystemFacade.getInstance().addUser(
                new User("newuser@test.com", "Password1!", "New", "User")));
    }

    @Test
    public void addUser_duplicate_returnsFalse() {
        SystemFacade sf = SystemFacade.getInstance();
        sf.addUser(new User("dupuser@test.com", "Password1!", "Dup", "User"));
        assertFalse(sf.addUser(new User("dupuser@test.com", "Password1!", "Dup2", "User2")));
    }

    @Test
    public void deleteUser_existing_returnsTrue() {
        SystemFacade sf = SystemFacade.getInstance();
        User user = new User("deluser@test.com", "Password1!", "Del", "User");
        sf.addUser(user);
        assertTrue(sf.deleteUser(user.getUserId()));
    }

    @Test
    public void deleteUser_nonExistent_returnsFalse() {
        assertFalse(SystemFacade.getInstance().deleteUser(java.util.UUID.randomUUID()));
    }

    @Test
    public void getUsers_returnsNonEmptyListAfterAdd() {
        SystemFacade sf = SystemFacade.getInstance();
        sf.addUser(new User("getusers@test.com", "Password1!", "Get", "Users"));
        assertFalse(sf.getUsers().isEmpty());
    }
}
