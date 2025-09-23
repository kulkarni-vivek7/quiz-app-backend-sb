package com.example.quiz_app.dao;

import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionDao {

    void saveAllQuestions(List<Question> questions1);

    List<Question> findAllQuestionsBySubjectName(Subject subject);

    Optional<Question> findQuestionById(String questionId);

    Page<Question> findAllQuestionsBySubject(Subject subject, Pageable pageable);

    Page<Question> findAllQuestions(Pageable pageable);
}
