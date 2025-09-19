package com.example.quiz_app.service;

import com.example.quiz_app.models.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MCQService {
    List<Question> getQuestionsForSubject(String subjectName, int count) throws Exception ;
}
