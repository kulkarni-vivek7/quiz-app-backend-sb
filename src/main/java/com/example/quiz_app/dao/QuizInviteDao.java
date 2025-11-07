package com.example.quiz_app.dao;

import com.example.quiz_app.models.QuizInvite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizInviteDao {
    void save(QuizInvite invite);

    Optional<QuizInvite> findByToken(String token);

    Optional<QuizInvite> findByCandidateId(String candidateId);

    void deleteQuizInvite(QuizInvite invite);

    Page<QuizInvite> findQuizInvitesByUsedTrue(Pageable pageable);

    Page<QuizInvite> findQuizInvitesByUsedFalse(Pageable pageable);
}
