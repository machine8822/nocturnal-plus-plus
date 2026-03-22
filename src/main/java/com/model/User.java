package com.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

public class User {
    private final UUID userId;
    private final String email;
    private String passwordHash;
    private String firstName;
    private String lastName;

    private final LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    private boolean isAdmin;
    private boolean isContributor;

    private Profile profile;
    private final List<UUID> bookmarkedQuestionIds;

    public User(String email, String password, String firstName, String lastName) {
        this(UUID.randomUUID(), email, hashPassword(password), firstName, lastName,
                LocalDateTime.now(), null, false, false, new Profile(), new ArrayList<>());
    }

    public User(UUID userId, String email, String passwordHash, String firstName, String lastName) {
        this(userId, email, passwordHash, firstName, lastName,
                LocalDateTime.now(), null, false, false, new Profile(), new ArrayList<>());
    }

    public User(UUID userId,
                String email,
                String passwordHash,
                String firstName,
                String lastName,
                LocalDateTime createdAt,
                LocalDateTime lastLogin,
                boolean isAdmin,
                boolean isContributor,
                Profile profile,
                List<UUID> bookmarkedQuestionIds) {

        this.userId = userId == null ? UUID.randomUUID() : userId;
        this.email = (email == null) ? "" : email.trim();
        this.passwordHash = passwordHash == null ? "" : passwordHash;
        this.firstName = firstName == null ? "" : firstName;
        this.lastName = lastName == null ? "" : lastName;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.lastLogin = lastLogin;
        this.isAdmin = isAdmin;
        this.isContributor = isContributor;
        this.profile = profile == null ? new Profile() : profile;
        this.bookmarkedQuestionIds = (bookmarkedQuestionIds == null)
            ? new ArrayList<>()
            : new ArrayList<>(bookmarkedQuestionIds);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isContributor() {
        return isContributor;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setContributor(boolean contributor) {
        isContributor = contributor;
    }

    public void setFirstName(String firstName) {
        if (firstName != null) this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName != null) this.lastName = lastName;
    }

    public void setProfile(Profile profile) {
        if (profile != null) this.profile = profile;
    }

    public boolean login(String password) {
        if (password == null) return false;
        if (passwordHash == null || passwordHash.isBlank()) return false;

        boolean ok = false;

        if (passwordHash.startsWith("$2")) {
            ok = BCrypt.checkpw(password, passwordHash);
        }

        if (ok) {
            lastLogin = LocalDateTime.now();
        }

        return ok;
    }

    public boolean changePassword(String oldPass, String newPass) {
        if (!login(oldPass)) return false;
        if (!isValidPassword(newPass)) return false;
        this.passwordHash = hashPassword(newPass);
        return true;
    }

    public boolean addBookmark(UUID questionId) {
        if (questionId == null) return false;
        if (bookmarkedQuestionIds.contains(questionId)) return false;
        bookmarkedQuestionIds.add(questionId);
        return true;
    }

    public boolean removeBookmark(UUID questionId) {
        if (questionId == null) return false;
        return bookmarkedQuestionIds.remove(questionId);
    }

    public List<UUID> getBookmarkedQuestionIds() {
        return Collections.unmodifiableList(bookmarkedQuestionIds);
    }

    public int getBookmarkCount() {
        return bookmarkedQuestionIds.size();
    }

    public boolean hasBookmarked(UUID questionId) {
        if (questionId == null) return false;
        return bookmarkedQuestionIds.contains(questionId);
    }

    public String getFullName() {
        String first = firstName == null ? "" : firstName.trim();
        String last = lastName == null ? "" : lastName.trim();
        String fullName = (first + " " + last).trim();
        return fullName.isEmpty() ? email : fullName;
    }

    private boolean isValidPassword(String password) {
        if (password == null) return false;
        if (password.length() < 8 || password.length() > 24) return false;
        return hasUpperCase(password) && hasLowerCase(password) && hasDigit(password) && hasSpecialChar(password);
    }

    private boolean hasUpperCase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) return true;
        }
        return false;
    }

    private boolean hasLowerCase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) return true;
        }
        return false;
    }

    private boolean hasDigit(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) return true;
        }
        return false;
    }

    private boolean hasSpecialChar(String password) {
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (!Character.isLetterOrDigit(c)) return true;
        }
        return false;
    }

    public static String hashPassword(String password) {
        if (password == null) return "";
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isAdmin=" + isAdmin +
                ", isContributor=" + isContributor +
                "}";
    }
}
