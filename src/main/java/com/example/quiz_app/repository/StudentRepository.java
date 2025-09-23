package com.example.quiz_app.repository;

import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    Page<Student> findByNameContainingIgnoreCase(String searchValue, Pageable pageable);

    Optional<Student> findByEmail(String searchValue);

    Optional<Student> findByPhone(String searchValue);

    Page<Student> findAllBySubject(Subject subject, Pageable pageable);
}
