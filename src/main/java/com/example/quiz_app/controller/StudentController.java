package com.example.quiz_app.controller;

import com.example.quiz_app.dto.QuestionWithoutAnswerDTO;
import com.example.quiz_app.dto.StudentDTO;
import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@CrossOrigin("http://localhost:5173")
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
    public ResponseEntity<ResponseStructure<AnswerSet>> submitAnswers(@RequestBody AnswerSet answerSet)
        throws Exception {

        return studentService.submitAnswers(answerSet);
    }

//    GET Mappings-----------------------------------------------------------------------
    @GetMapping("/getStudents")
    public ResponseEntity<ResponseStructure<?>> getAllStudents(
            @RequestParam String searchParam,
            @RequestParam String searchValue,
            @RequestParam int page,
            @RequestParam int limit
    )
    {
        return studentService.getAllStudents(searchParam, searchValue, page, limit);
    }

    @GetMapping("/getAnswerSets")
    public ResponseEntity<ResponseStructure<?>> getAllAnswerSets(
            @RequestParam String searchParam,
            @RequestParam String searchValue,
            @RequestParam int page,
            @RequestParam int limit
    )
    {
        return studentService.getAllAnswerSets(searchParam, searchValue, page, limit);
    }
}
