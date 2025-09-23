package com.example.quiz_app.dao;

import com.example.quiz_app.models.AnswerSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerSetDao {
    void saveAnswerSet(AnswerSet answerSet);

    Optional<AnswerSet> findAnswerSetByStudentId(String studentId);

    Page<AnswerSet> findAllAnswerSets(Pageable pageable);
}
