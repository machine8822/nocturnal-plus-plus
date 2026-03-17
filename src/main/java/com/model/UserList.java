package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Singleton that stores and authenticates Users.
 *
 * @author Jonah Mosquera
 */

public class UserList {
    private static UserList instance;

    private final HashMap<String, User> usersByEmail;
    private final HashMap<UUID, User> usersById;

    private UserList() {
        usersByEmail = new HashMap<>();
        usersById = new HashMap<>();

        // Initialize from persisted data (if file exists).
        ArrayList<User> loaded = DataLoader.loadUsers();
        for (User u : loaded) {
            addUser(u);
        }
    }

    public static UserList getInstance() {
        if (instance == null) {
            instance = new UserList();
        }
        return instance;
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(usersById.values());
    }

    public User getUserByEmail(String email) {
        if (email == null) return null;
        return usersByEmail.get(email.trim().toLowerCase());
    }

    public User getUserById(UUID id) {
        if (id == null) return null;
        return usersById.get(id);
    }

    public boolean addUser(User user) {
        if (user == null) return false;
        if (user.getEmail() == null || user.getEmail().isBlank()) return false;

        String key = user.getEmail().trim().toLowerCase();
        if (usersByEmail.containsKey(key)) return false;
        usersByEmail.put(key, user);
        usersById.put(user.getUserId(), user);
        return true;
    }

    public boolean removeUser(UUID userId) {
        if (userId == null) return false;
        User removed = usersById.remove(userId);
        if (removed == null) return false;
        usersByEmail.remove(removed.getEmail().trim().toLowerCase());
        return true;
    }

    /**
     * Login flow (matches the sequence diagram): find user by email, then call User.login().
     *
     * @return the User if authenticated, otherwise null.
     */
    public User authenticate(String user, String pass) {
        User found = getUserByEmail(user);
        if (found == null) return null;
        return found.login(pass) ? found : null;
    }

    public boolean isValidUser(String user, String pass) {
        return authenticate(user, pass) != null;
    }
}
