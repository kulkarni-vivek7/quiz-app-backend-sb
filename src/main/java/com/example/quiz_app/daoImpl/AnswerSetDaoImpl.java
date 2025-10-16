package com.example.quiz_app.daoImpl;

import com.example.quiz_app.dao.AnswerSetDao;
import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.repository.AnswerSetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AnswerSetDaoImpl implements AnswerSetDao {

    private final AnswerSetRepository answerSetRepository;

    public AnswerSetDaoImpl(AnswerSetRepository answerSetRepository) {
        this.answerSetRepository = answerSetRepository;
    }

    @Override
    public void saveAnswerSet(AnswerSet answerSet) {
        answerSetRepository.save(answerSet);
    }

    @Override
    public Optional<AnswerSet> findAnswerSetByCandidateId(String candidateId) {
        return answerSetRepository.findByCandidateId(candidateId);
    }

    @Override
    public Page<AnswerSet> findAllAnswerSets(Pageable pageable) {
        return answerSetRepository.findAll(pageable);
    }

    @Override
    public void deleteAnswerSet(AnswerSet answerSet) {
        answerSetRepository.delete(answerSet);
    }
}
