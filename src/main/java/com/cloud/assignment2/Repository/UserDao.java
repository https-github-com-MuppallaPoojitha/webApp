package com.cloud.assignment2.Repository;

import com.cloud.assignment2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User save(User user);
    User findByUsernameIgnoreCase(String username);
}
