package com.example.quiz_app.dao;

import com.example.quiz_app.models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao {
    User saveUser(User user);

    Optional<User> getUserByEmail(String email);
}
