package com.example.quiz_app.dto;

import lombok.Data;

@Data
public class CodeSubmissionDTO {
    private String questionId;
    private String candidateCode;
    private String language;
}
