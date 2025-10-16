package com.example.quiz_app.repository;

import com.example.quiz_app.enums.UserRole;
import com.example.quiz_app.models.LoginDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LoginRepository extends MongoRepository<LoginDetails, String> {
    Optional<LoginDetails> findFirstByEmail(String email);

    boolean existsByRole(UserRole role);

    Optional<LoginDetails> findFirstByPhone(String phone);
}
