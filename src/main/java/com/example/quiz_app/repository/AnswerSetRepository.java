package com.example.quiz_app.repository;

import com.example.quiz_app.models.AnswerSet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AnswerSetRepository extends MongoRepository<AnswerSet, String> {
    Optional<AnswerSet> findByCandidateId(String studentId);
}
