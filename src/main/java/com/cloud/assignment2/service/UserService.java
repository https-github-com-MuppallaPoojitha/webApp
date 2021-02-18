package com.cloud.assignment2.service;

import com.cloud.assignment2.model.User;

public interface UserService {
    User save(User user);
    User findByUsernameIgnoreCase(String username);
}
