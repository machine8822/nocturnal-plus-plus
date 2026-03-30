package com.model;

import java.lang.reflect.Field;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Coverage tests for UserList class
 */
public class UserListTest {

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

    // --- addUser ---

    @Test
    public void addUser_valid_returnsTrue() {
        UserList ul = UserList.getInstance();
        assertTrue(ul.addUser(new User("test@test.com", "Password1!", "First", "Last")));
    }

    @Test
    public void addUser_null_returnsFalse() {
        assertFalse(UserList.getInstance().addUser(null));
    }

    @Test
    public void addUser_blankEmail_returnsFalse() {
        User user = new User(java.util.UUID.randomUUID(), "   ", "hash", "First", "Last");
        assertFalse(UserList.getInstance().addUser(user));
    }

    @Test
    public void addUser_duplicateEmail_returnsFalse() {
        UserList ul = UserList.getInstance();
        ul.addUser(new User("dup@test.com", "Password1!", "First", "Last"));
        assertFalse(ul.addUser(new User("dup@test.com", "Password1!", "Another", "User")));
    }

    @Test
    public void addUser_caseInsensitiveEmail_rejectsDuplicate() {
        UserList ul = UserList.getInstance();
        ul.addUser(new User("Case@Test.com", "Password1!", "First", "Last"));
        assertFalse(ul.addUser(new User("case@test.com", "Password1!", "Another", "User")));
    }

    // --- getUserByEmail ---

    @Test
    public void getUserByEmail_existing_returnsUser() {
        UserList ul = UserList.getInstance();
        User user = new User("find@test.com", "Password1!", "First", "Last");
        ul.addUser(user);
        assertSame(user, ul.getUserByEmail("find@test.com"));
    }

    @Test
    public void getUserByEmail_null_returnsNull() {
        assertNull(UserList.getInstance().getUserByEmail(null));
    }

    @Test
    public void getUserByEmail_nonExistent_returnsNull() {
        assertNull(UserList.getInstance().getUserByEmail("nobody@test.com"));
    }

    @Test
    public void getUserByEmail_caseInsensitive_returnsUser() {
        UserList ul = UserList.getInstance();
        ul.addUser(new User("Mixed@Test.com", "Password1!", "First", "Last"));
        assertNotNull(ul.getUserByEmail("mixed@test.com"));
    }

    // --- getUserById ---

    @Test
    public void getUserById_existing_returnsUser() {
        UserList ul = UserList.getInstance();
        User user = new User("byid@test.com", "Password1!", "First", "Last");
        ul.addUser(user);
        assertSame(user, ul.getUserById(user.getUserId()));
    }

    @Test
    public void getUserById_null_returnsNull() {
        assertNull(UserList.getInstance().getUserById(null));
    }

    @Test
    public void getUserById_nonExistent_returnsNull() {
        assertNull(UserList.getInstance().getUserById(java.util.UUID.randomUUID()));
    }

    // --- removeUser ---

    @Test
    public void removeUser_existing_returnsTrue() {
        UserList ul = UserList.getInstance();
        User user = new User("remove@test.com", "Password1!", "First", "Last");
        ul.addUser(user);
        assertTrue(ul.removeUser(user.getUserId()));
    }

    @Test
    public void removeUser_existing_removesFromBothMaps() {
        UserList ul = UserList.getInstance();
        User user = new User("remove@test.com", "Password1!", "First", "Last");
        ul.addUser(user);
        ul.removeUser(user.getUserId());
        assertNull(ul.getUserByEmail("remove@test.com"));
        assertNull(ul.getUserById(user.getUserId()));
    }

    @Test
    public void removeUser_null_returnsFalse() {
        assertFalse(UserList.getInstance().removeUser(null));
    }

    @Test
    public void removeUser_nonExistent_returnsFalse() {
        assertFalse(UserList.getInstance().removeUser(java.util.UUID.randomUUID()));
    }

    // --- authenticate ---

    @Test
    public void authenticate_validCredentials_returnsUser() {
        UserList ul = UserList.getInstance();
        ul.addUser(new User("auth@test.com", "Password1!", "First", "Last"));
        assertNotNull(ul.authenticate("auth@test.com", "Password1!"));
    }

    @Test
    public void authenticate_wrongPassword_returnsNull() {
        UserList ul = UserList.getInstance();
        ul.addUser(new User("auth@test.com", "Password1!", "First", "Last"));
        assertNull(ul.authenticate("auth@test.com", "WrongPass1!"));
    }

    @Test
    public void authenticate_nonExistentEmail_returnsNull() {
        assertNull(UserList.getInstance().authenticate("nobody@test.com", "Password1!"));
    }

    @Test
    public void authenticate_nullEmail_returnsNull() {
        assertNull(UserList.getInstance().authenticate(null, "Password1!"));
    }

    // --- isValidUser ---

    @Test
    public void isValidUser_valid_returnsTrue() {
        UserList ul = UserList.getInstance();
        ul.addUser(new User("valid@test.com", "Password1!", "First", "Last"));
        assertTrue(ul.isValidUser("valid@test.com", "Password1!"));
    }

    @Test
    public void isValidUser_invalid_returnsFalse() {
        assertFalse(UserList.getInstance().isValidUser("no@test.com", "Password1!"));
    }

    // --- getUsers ---

    @Test
    public void getUsers_returnsNewListEachTime() {
        UserList ul = UserList.getInstance();
        ul.addUser(new User("list@test.com", "Password1!", "First", "Last"));
        assertNotSame(ul.getUsers(), ul.getUsers());
    }

    @Test
    public void getUsers_containsAddedUser() {
        UserList ul = UserList.getInstance();
        User user = new User("contains@test.com", "Password1!", "First", "Last");
        ul.addUser(user);
        assertTrue(ul.getUsers().contains(user));
    }

    @Test
    public void getUsers_doesNotContainRemovedUser() {
        UserList ul = UserList.getInstance();
        User user = new User("gone@test.com", "Password1!", "First", "Last");
        ul.addUser(user);
        ul.removeUser(user.getUserId());
        assertFalse(ul.getUsers().contains(user));
    }
}
