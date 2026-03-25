package com.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User("test@example.com", "ValidPass1!", "John", "Doe");
    }

    @Test
    public void testPasswordLength8Valid() {
        boolean result = user.changePassword("ValidPass1!", "Pass123!");
        assertTrue("8-char password with all requirements should be valid", result);
    }

    @Test
    public void testPasswordTooShort() {
        boolean result = user.changePassword("ValidPass1!", "Pass1!");
        assertFalse("Password shorter than 8 chars should be invalid", result);
    }

    @Test
    public void testPasswordTooLong() {
        boolean result = user.changePassword("ValidPass1!", "Pass123!Pass123!Pass123!A");
        assertFalse("Password longer than 24 chars should be invalid", result);
    }

    @Test
    public void testValidPassword() {
        boolean result = user.changePassword("ValidPass1!", "NewPass123!");
        assertTrue("Valid password should be accepted", result);
    }

    @Test
    public void testLoginSuccess() {
        assertTrue("Login with correct password should succeed", user.login("ValidPass1!"));
    }

    @Test
    public void testLoginFail() {
        assertFalse("Login with wrong password should fail", user.login("WrongPass1!"));
    }
}
