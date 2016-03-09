package com.example.danielwinther.androidroomreservations;

import java.io.Serializable;

public class User implements Serializable {
    private int userId;
    private String name;
    private String username;
    private String password;

    public User(int userId, String name, String username, String password) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return name;
    }
}
