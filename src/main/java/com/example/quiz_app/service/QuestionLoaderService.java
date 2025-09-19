package com.example.quiz_app.service;

import com.example.quiz_app.models.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionLoaderService {
    List<Question> getQuestionsBySubject(String subjectName, int count) throws Exception ;
}
