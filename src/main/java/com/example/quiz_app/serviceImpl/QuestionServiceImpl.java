package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.dao.QuestionDao;
import com.example.quiz_app.dto.AddQuestionDTO;
import com.example.quiz_app.dto.CodeSubmissionDTO;
import com.example.quiz_app.dto.CodeValidationResultDTO;
import com.example.quiz_app.enums.QuestionType;
import com.example.quiz_app.models.Question;
import com.example.quiz_app.service.CodeExecutionService;
import com.example.quiz_app.service.QuestionService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;
    private final CodeExecutionService codeExecutionService;

    public QuestionServiceImpl(QuestionDao questionDao, CodeExecutionService codeExecutionService) {
        this.questionDao = questionDao;
        this.codeExecutionService = codeExecutionService;
    }

    @Override
    public ResponseEntity<String> addAllQuestions(List<AddQuestionDTO> questions) {
        List<Question> questionsToSave = new ArrayList<>();

        for (AddQuestionDTO questionDTO : questions) {
            // Validate the question before adding
            ResponseEntity<String> validationResponse = validateQuestion(questionDTO);
            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                return validationResponse;
            }
            questionsToSave.add(questionDTO.toQuestion());
        }

        try {
            questionDao.saveAllQuestions(questionsToSave);
            return ResponseEntity.ok("Questions added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving questions: " + e.getMessage());
        }
    }


    @Override
    public ResponseEntity<CodeValidationResultDTO> validateCandidateCode(CodeSubmissionDTO submission) {
        try {
            CodeValidationResultDTO result = codeExecutionService.validateCode(submission);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            CodeValidationResultDTO errorResult = new CodeValidationResultDTO();
            errorResult.setCorrect(false);
            errorResult.setMessage("Error validating code: " + e.getMessage());
            errorResult.setErrorDetails(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
    }

    private ResponseEntity<String> validateQuestion(AddQuestionDTO questionDTO) {
        if (questionDTO.getQuestionText() == null || questionDTO.getQuestionText().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Question text cannot be empty");
        }

        if (questionDTO.getQuestionType() == null) {
            return ResponseEntity.badRequest().body("Question type is required");
        }

        if (questionDTO.getSubject() == null) {
            return ResponseEntity.badRequest().body("Subject is required");
        }

        if (questionDTO.getQuestionType() == QuestionType.MCQ) {
            if (questionDTO.getOptions() == null || questionDTO.getOptions().size() < 2) {
                return ResponseEntity.badRequest().body("At least 2 options are required for MCQ");
            }
            if (questionDTO.getCorrectOptionIndex() < 0 ||
                questionDTO.getCorrectOptionIndex() >= questionDTO.getOptions().size()) {
                return ResponseEntity.badRequest().body("Invalid correct option index");
            }
        } else if (questionDTO.getQuestionType() == QuestionType.CODING) {
            if (questionDTO.getTestCases() == null || questionDTO.getTestCases().isEmpty()) {
                return ResponseEntity.badRequest().body("At least one test case is required for coding questions");
            }
            if (questionDTO.getLanguage() == null || questionDTO.getLanguage().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Programming language is required for coding questions");
            }
        }

        return ResponseEntity.ok("Validation successful");
    }
}
