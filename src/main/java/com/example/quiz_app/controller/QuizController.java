package com.example.quiz_app.controller;

import com.example.quiz_app.dto.QuizStartDTO;
import com.example.quiz_app.service.QuizInviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin("http://localhost:5173")
public class QuizController {

    private final QuizInviteService quizInviteService;

    public QuizController(QuizInviteService quizInviteService) {
        this.quizInviteService = quizInviteService;
    }

    @GetMapping("/start")
    public ResponseEntity<QuizStartDTO> startQuiz(@RequestParam String token) throws Exception {
        return ResponseEntity.ok(quizInviteService.startQuiz(token));
    }
}
