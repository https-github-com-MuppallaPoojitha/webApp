package com.cloud.assignment2.service;

import  com.cloud.assignment2.Repository.UserDao;
import com.cloud.assignment2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public User findByUsernameIgnoreCase(String username) {
        return userDao.findByUsernameIgnoreCase(username);
    }
}
