package com.example.quiz_app.dao;

import com.example.quiz_app.enums.UserRole;
import com.example.quiz_app.models.LoginDetails;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginDao {
    Optional<LoginDetails> findByEmail(String email);

    boolean existsByRoleHr(UserRole role);

    void saveLoginDetails(LoginDetails loginDetails);

    void updateLoginDetails(LoginDetails loginDetails);

    Optional<LoginDetails> findByPhone(@Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits") String phone);
}
