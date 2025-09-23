package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.dao.QuestionDao;
import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Question;
import com.example.quiz_app.service.QuestionLoaderService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QuestionLoaderServiceImpl implements QuestionLoaderService {

    private final QuestionDao questionDao;

    public QuestionLoaderServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<Question> getQuestionsBySubject(Subject subject, int count) throws Exception {
        List<Question> allQuestions = questionDao.findAllQuestionsBySubjectName(subject);

        // Shuffle the list to randomize questions order
        Collections.shuffle(allQuestions);

        if(count > allQuestions.size()) {
            count = allQuestions.size();
        }

        return allQuestions.subList(0, count);
    }
}
