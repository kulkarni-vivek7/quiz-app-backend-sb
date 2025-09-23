package com.example.quiz_app.service;

import com.example.quiz_app.dto.AddQuestionDTO;
import com.example.quiz_app.response.ResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {
    ResponseEntity<String> addAllQuestions(List<AddQuestionDTO> questions);

    ResponseEntity<ResponseStructure<?>> findAllQuestions(String searchParam, String searchValue, int page, int limit);
}
