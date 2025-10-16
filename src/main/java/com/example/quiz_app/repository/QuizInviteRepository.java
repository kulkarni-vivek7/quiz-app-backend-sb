package com.example.quiz_app.repository;

import com.example.quiz_app.models.QuizInvite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuizInviteRepository extends MongoRepository<QuizInvite, String> {
    Optional<QuizInvite> findByToken(String token);

    Optional<QuizInvite> findByCandidateId(String candidateId);

    Page<QuizInvite> findByUsedTrue(Pageable pageable);
}