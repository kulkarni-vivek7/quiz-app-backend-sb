package com.example.quiz_app.daoImpl;

import com.example.quiz_app.dao.QuestionDao;
import com.example.quiz_app.enums.QuestionType;
import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Question;
import com.example.quiz_app.repository.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class QuestionDaoImpl implements QuestionDao {

    private final QuestionRepository repository;

    public QuestionDaoImpl(QuestionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveAllQuestions(List<Question> questions1) {
        repository.saveAll(questions1);
    }

    @Override
    public List<Question> findAllQuestionsBySubjectName(Subject subject) {
        return repository.findAllBySubject(subject);
    }

    @Override
    public Optional<Question> findQuestionById(String questionId) {
        return repository.findById(questionId);
    }

    @Override
    public Page<Question> findAllQuestionsBySubject(Subject subject, Pageable pageable) {
        return repository.findAllBySubject(subject, pageable);
    }

    @Override
    public Page<Question> findAllQuestions(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Question> findByQuestionId(String questionId) {
        return repository.findByQuestionId(questionId);
    }

    @Override
    public Page<Question> findByQuestionType(QuestionType questionType, Pageable pageable) {
        return repository.findByQuestionType(questionType, pageable);
    }

    @Override
    public Page<Question> findAllQuestionsByQuestionText(String searchValue, Pageable pageable) {
        return repository.findByQuestionTextContainingIgnoreCase(searchValue, pageable);
    }
}
