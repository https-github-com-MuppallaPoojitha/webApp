package com.assingment1.cloud.Repository;


import com.assingment1.cloud.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    public Optional<User> findUserByEmailaddress(String email);
}
