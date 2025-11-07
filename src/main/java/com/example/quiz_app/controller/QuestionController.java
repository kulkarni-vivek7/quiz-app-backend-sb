package com.example.quiz_app.controller;

import com.example.quiz_app.dto.AddQuestionDTO;
import com.example.quiz_app.dto.CodeSubmissionDTO;
import com.example.quiz_app.dto.CodeValidationResultDTO;
import com.example.quiz_app.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin("http://localhost:5173")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<String> addAllQuestions(@RequestBody List<AddQuestionDTO> questions) {
        return questionService.addAllQuestions(questions);
    }

    @PostMapping("/validate")
    public ResponseEntity<CodeValidationResultDTO> validateCandidateCode(@RequestBody CodeSubmissionDTO submission) {
        return questionService.validateCandidateCode(submission);
    }
}
