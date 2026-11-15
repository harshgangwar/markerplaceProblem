package com.marketplace.model;

/** Immutable registered marketplace user. */
public final class User {
    private final String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }
}
