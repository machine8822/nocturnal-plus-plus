package com.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class UserBugTest {

    // Bug #64 — user created with null password can never authenticate

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

    // Bug #69 — login() silently fails for non-BCrypt password hashes

    @Test
    public void login_nonBcryptHash_alwaysReturnsFalse() {
        User user = new User(java.util.UUID.randomUUID(), "sparrow@test.com",
                "hashed-password-456", "Sullivan", "Sparrow");
        assertFalse(user.login("hashed-password-456"));
    }

    @Test
    public void login_nonBcryptHash_neverUpdatesLastLogin() {
        User user = new User(java.util.UUID.randomUUID(), "sparrow@test.com",
                "hashed-password-456", "Sullivan", "Sparrow");
        user.login("hashed-password-456");
        assertNull(user.getLastLogin());
    }

    @Test
    public void login_bcryptHash_succeeds() {
        User user = new User("valid@test.com", "Password1!", "Valid", "User");
        assertTrue(user.login("Password1!"));
    }

    // Bug #70 — changePassword() updates lastLogin even when password change fails

    @Test
    public void changePassword_invalidNewPassword_stillUpdatesLastLogin() {
        User user = new User("cp@test.com", "Password1!", "First", "Last");
        assertNull(user.getLastLogin());
        boolean changed = user.changePassword("Password1!", "weak");
        assertFalse(changed);
        assertNotNull(user.getLastLogin());
    }

    @Test
    public void changePassword_validNewPassword_updatesHash() {
        User user = new User("cp@test.com", "Password1!", "First", "Last");
        assertTrue(user.changePassword("Password1!", "NewSecure2@"));
        assertTrue(user.login("NewSecure2@"));
        assertFalse(user.login("Password1!"));
    }

    @Test
    public void changePassword_wrongOldPassword_returnsFalse() {
        User user = new User("cp@test.com", "Password1!", "First", "Last");
        assertFalse(user.changePassword("WrongOld1!", "NewSecure2@"));
    }

    @Test
    public void changePassword_nullOldPassword_returnsFalse() {
        User user = new User("cp@test.com", "Password1!", "First", "Last");
        assertFalse(user.changePassword(null, "NewSecure2@"));
    }

    @Test
    public void changePassword_nullNewPassword_returnsFalse() {
        User user = new User("cp@test.com", "Password1!", "First", "Last");
        assertFalse(user.changePassword("Password1!", null));
    }
}
