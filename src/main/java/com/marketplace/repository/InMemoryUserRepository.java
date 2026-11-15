package com.marketplace.repository;
import com.marketplace.model.User;
import java.util.concurrent.ConcurrentHashMap;

/** Usernames are normalized to lowercase to enforce case-insensitive uniqueness. */
public final class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();
    public boolean add(User user) { return users.putIfAbsent(key(user.getUsername()), user) == null; }
    public boolean exists(String username) { return users.containsKey(key(username)); }
    private String key(String username) { return username.toLowerCase(java.util.Locale.ROOT); }
}
