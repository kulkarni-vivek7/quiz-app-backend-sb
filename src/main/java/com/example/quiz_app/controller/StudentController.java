package com.example.quiz_app.controller;

import com.example.quiz_app.dto.QuestionWithoutAnswerDTO;
import com.example.quiz_app.dto.StudentDTO;
import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/enroll")
    public ResponseEntity<List<QuestionWithoutAnswerDTO>> enrollStudent(
            @Valid
            @RequestBody StudentDTO studentDTO) throws Exception
    {
        return studentService.enrollStudent(studentDTO);
    }

    @PostMapping("/answer")
    public ResponseEntity<String> submitAnswers(@RequestBody AnswerSet answerSet)
        throws Exception {

        return studentService.submitAnswers(answerSet);
    }
}
