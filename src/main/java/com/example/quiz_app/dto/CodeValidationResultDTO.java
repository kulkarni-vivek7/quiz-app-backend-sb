package com.example.quiz_app.dto;

import java.util.List;

import lombok.Data;

@Data
public class CodeValidationResultDTO {
    private boolean isCorrect;
    private String message;
    private int passedTestCases;
    private int totalTestCases;
    private String errorDetails;
    private List<String> testResults;  // Added to store the execution output
}
