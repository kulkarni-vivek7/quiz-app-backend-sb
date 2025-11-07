package com.example.quiz_app.service;

import com.example.quiz_app.dto.CodeSubmissionDTO;
import com.example.quiz_app.dto.CodeValidationResultDTO;

public interface CodeExecutionService {
    CodeValidationResultDTO validateCode(CodeSubmissionDTO submission);
}