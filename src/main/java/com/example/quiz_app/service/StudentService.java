package com.example.quiz_app.service;

import com.example.quiz_app.dto.QuestionWithoutAnswerDTO;
import com.example.quiz_app.dto.StudentDTO;
import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.response.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {
    ResponseEntity<List<QuestionWithoutAnswerDTO>> enrollStudent(@Valid StudentDTO studentDTO) throws Exception;

    ResponseEntity<ResponseStructure<AnswerSet>> submitAnswers(AnswerSet answerSet) throws Exception ;

    ResponseEntity<ResponseStructure<?>> getAllStudents(String searchParam, String searchValue, int page, int limit);

    ResponseEntity<ResponseStructure<?>> getAllAnswerSets(String searchParam, String searchValue, int page, int limit);
}
