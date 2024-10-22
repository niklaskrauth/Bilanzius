package org.bilanzius.persistence.models;

public class User {

    public static User createUser(String username, String password) {
        return new User(0, username, password);
    }

    private final int id;
    private final String username;
    private String hashedPassword;

    public User(int id, String username, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public int getId() {
        return id;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() {
        return username;
    }
}
