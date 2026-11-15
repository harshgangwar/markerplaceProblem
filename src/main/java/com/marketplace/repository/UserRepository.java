package com.marketplace.repository;
import com.marketplace.model.User;

public interface UserRepository {
    boolean add(User user); // false when duplicate
    boolean exists(String username);
}
