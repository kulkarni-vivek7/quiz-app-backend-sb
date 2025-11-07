package com.example.quiz_app.daoImpl;

import com.example.quiz_app.dao.QuizInviteDao;
import com.example.quiz_app.models.QuizInvite;
import com.example.quiz_app.repository.QuizInviteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class QuizInviteDaoImpl implements QuizInviteDao {

    private final QuizInviteRepository repository;

    public QuizInviteDaoImpl(QuizInviteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(QuizInvite invite) {
        repository.save(invite);
    }

    @Override
    public Optional<QuizInvite> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public Optional<QuizInvite> findByCandidateId(String candidateId) {
        return repository.findByCandidateId(candidateId);
    }

    @Override
    public void deleteQuizInvite(QuizInvite invite) {
        repository.delete(invite);
    }

    @Override
    public Page<QuizInvite> findQuizInvitesByUsedTrue(Pageable pageable) {
        return repository.findByUsedTrue(pageable);
    }

    @Override
    public Page<QuizInvite> findQuizInvitesByUsedFalse(Pageable pageable) {
        return repository.findByUsedFalse(pageable);
    }
}
