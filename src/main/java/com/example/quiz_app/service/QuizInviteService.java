package com.example.quiz_app.service;

import com.example.quiz_app.dto.QuizStartDTO;
import org.springframework.stereotype.Service;

@Service
public interface QuizInviteService {
    QuizStartDTO startQuiz(String token) throws Exception;
}