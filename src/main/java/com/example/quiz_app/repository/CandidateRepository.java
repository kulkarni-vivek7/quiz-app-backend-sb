package com.example.quiz_app.repository;

import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Page<Candidate> findByNameContainingIgnoreCase(String searchValue, Pageable pageable);

    Optional<Candidate> findByEmail(String searchValue);

    Optional<Candidate> findByPhone(String searchValue);

    Page<Candidate> findAllBySubject(Subject subject, Pageable pageable);
}
