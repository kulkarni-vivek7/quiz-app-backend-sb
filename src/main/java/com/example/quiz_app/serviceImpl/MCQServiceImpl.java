package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Question;
import com.example.quiz_app.service.MCQService;
import com.example.quiz_app.service.QuestionLoaderService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MCQServiceImpl implements MCQService {

    private final QuestionLoaderService questionLoaderService;

    public MCQServiceImpl(QuestionLoaderService questionLoaderService) {
        this.questionLoaderService = questionLoaderService;
    }

    @Override
    public List<Question> getQuestionsForSubject(Subject subjectName, int count) throws Exception {
        return questionLoaderService.getQuestionsBySubject(subjectName, count);
    }
}
