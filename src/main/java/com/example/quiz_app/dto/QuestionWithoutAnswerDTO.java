package com.example.quiz_app.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionWithoutAnswerDTO {
    private String questionId;
    private String questionText;
    private List<String> options;
}