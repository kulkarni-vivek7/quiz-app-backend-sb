package com.example.quiz_app.controller;

import com.example.quiz_app.dto.AddQuestionDTO;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<String> addAllQuestions(@RequestBody List<AddQuestionDTO> questions)
    {
        return questionService.addAllQuestions(questions);
    }

//    GET Methods

    @GetMapping
    public ResponseEntity<ResponseStructure<?>> getAllQuestions(
            @RequestParam String searchParam,
            @RequestParam String searchValue,
            @RequestParam int page,
            @RequestParam int limit
    )
    {
        return questionService.findAllQuestions(searchParam, searchValue, page, limit);
    }
}
