package com.example.quiz_app.repository;

import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findAllBySubject(Subject subject);

    Page<Question> findAllBySubject(Subject subject, Pageable pageable);
}
